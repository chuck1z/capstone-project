package com.darkshandev.freshcam.presentation.classifier.viewmodels

import androidx.lifecycle.ViewModel
import com.darkshandev.freshcam.data.repositories.ClassifierRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ClassifierViewmodel @Inject constructor(private val repository: ClassifierRepository) : ViewModel()