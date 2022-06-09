package com.darkshandev.freshcam.presentation.classifier.viewmodels

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClassifierViewmodel @Inject constructor(
    private val repository: ClassifierRepository
    ) : ViewModel() {
    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()
    fun setImage(image: File) {
        _image.value = image
    }
    val downloadStatus=repository.downloadStatus
    fun getLatestModel() {
        _result.value = AppState.Loading()
        viewModelScope.launch {
            repository.getLatestModel()
        }
        _result.value = AppState.Initial()
    }

    fun getLatestLabel(){
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
                })
        }

    }

}