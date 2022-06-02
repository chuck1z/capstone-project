package com.darkshandev.freshcam.presentation.fruits.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.FruitsDetail
import com.darkshandev.freshcam.data.models.FruitsTips
import com.darkshandev.freshcam.data.repositories.FruitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FruitsViewmodel @Inject constructor(private val repo: FruitsRepository) : ViewModel() {
//    private val _fruits = MutableLiveData<List<FruitsTips>>()
//    val fruits: LiveData<List<FruitsTips>> = _fruits
//    private val _isLoading = MutableLiveData<Boolean>()
    private val _fruits = MutableStateFlow<AppState<List<FruitsTips>>>(AppState.Initial())
    val fruits = _fruits.asStateFlow()
    private val _selectedFruitsId = MutableStateFlow<String>("")
    fun setSelectedFruitsId(id: String) {
        _selectedFruitsId.value = id
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    val fruitsDetail:StateFlow<AppState<FruitsDetail>> =_selectedFruitsId
        .distinctUntilChanged { old, new -> old == new && new == "" }
        .transformLatest { id ->
            repo.getFruitsDetail(id)
                .collect { result ->
                    emit(result)
                }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            AppState.Loading()
        )

}
