package com.darkshandev.freshcam.presentation.classifier.viewmodels

import androidx.lifecycle.ViewModel
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.ScanResult
import com.darkshandev.freshcam.data.repositories.ClassifierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ClassifierViewmodel @Inject constructor(private val repository: ClassifierRepository) :
    ViewModel() {
    private val _image = MutableStateFlow<File?>(null)
    val image = _image.asStateFlow()
    fun setImage(image: File) {
        _image.value?.delete()
        _image.value = image
    }

    private val _result = MutableStateFlow<AppState<ScanResult>>(AppState.Initial())
    val result = _result.asStateFlow()
    fun classifyImage() {
        _result.value = AppState.Loading()
        repository.classifyImage(image.value!!, object : ClassifierRepository.ClassifierCallback {
            override fun onSuccess(result: ScanResult) {
                _result.value = AppState.Success(result)
            }

            override fun onError(error: String) {
                _result.value = AppState.Error(error)
            }
        })
    }

}