package com.darkshandev.freshcam.presentation.classifier.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.ScanResult
import com.darkshandev.freshcam.data.repositories.ClassifierRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClassifierViewmodel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: ClassifierRepository
) : ViewModel() {
    val connectivityManager =
        getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager

    val isFirstLaunch =
        repository.isFirstLaunch.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun markAsLaunched() = viewModelScope.launch {
        repository.marksAsLaunched()
    }

    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()
    fun setImage(image: File) {
        _image.value = image
    }

    val downloadStatus = repository.downloadStatus

    fun clearHistory() = viewModelScope.launch {
        repository.clearHistoryClassification()
    }

    fun getLatestModel() {
        _result.value = AppState.Loading()
        viewModelScope.launch {
            repository.getLatestModel()
        }
        _result.value = AppState.Initial()
    }

    fun getLatestLabel() {
        viewModelScope.launch {
            repository.loadLatestLabel()
        }
    }

    private val _result = MutableStateFlow<AppState<ScanResult>>(AppState.Initial())
    val result = _result.asStateFlow()

    fun classifyImage() {
        _result.value = AppState.Loading()
        viewModelScope.launch {
            repository.classifyImage(
                image.value!!,
                object : ClassifierRepository.ClassifierCallback {
                    override fun onSuccess(result: ScanResult) {
                        Firebase.analytics.logEvent(FirebaseAnalytics.Event.UNLOCK_ACHIEVEMENT) {
                            param(FirebaseAnalytics.Param.ACHIEVEMENT_ID, "freshcam_achievement")
                        }
                        _result.value = AppState.Success(result)
                    }

                    override fun onError(error: String) {
                        _result.value = AppState.Error(error)
                    }
                }
            )
        }

    }

    val classificationHistory = repository.histories.stateIn(
        viewModelScope, SharingStarted.Lazily,
        emptyList()
    )
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val _isConnectionAvailable = MutableStateFlow(false)
    val isConnectionAvailable = _isConnectionAvailable.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isConnectionAvailable.value = true
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unMetered =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            val internet =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            _isConnectionAvailable.value = unMetered || internet
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            _isConnectionAvailable.value = false
        }
    }

    fun requestNetwork() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}