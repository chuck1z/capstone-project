package com.darkshandev.freshcam.presentation.fruits.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.darkshandev.freshcam.data.FruitsTips
import com.darkshandev.freshcam.data.repositories.FruitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FruitsViewmodel @Inject constructor(private val repo:FruitsRepository) : ViewModel(){
    private val _fruits = MutableLiveData<List<FruitsTips>>()
    val fruits: LiveData<List<FruitsTips>> = _fruits
}
