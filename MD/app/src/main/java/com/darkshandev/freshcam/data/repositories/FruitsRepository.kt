package com.darkshandev.freshcam.data.repositories

import com.darkshandev.freshcam.data.datasources.FruitsDatasource
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.FruitsDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FruitsRepository @Inject constructor(private val dataSource: FruitsDatasource) {
    fun getFruitsDetail(fruitsId: String): Flow<AppState<FruitsDetail>> =
        flow<AppState<FruitsDetail>> {
            emit(AppState.Loading())
            val result = dataSource.getFruitsDetail(fruitsId)
            when (result) {
                is AppState.Success -> {
                    result.data?.let {
                        if(it.error) {
                            emit(AppState.Error(it.message))
                        }else{
                            emit(AppState.Success(it.data))
                        }
                    } ?: run {
                        emit(AppState.Error(message = ("No data found")))
                    }
                }
                is AppState.Error -> emit(result.message?.let {
                    AppState.Error(it)
                } ?: AppState.Error(message = "Unknown Error")
                )
                else -> {
                    emit(AppState.Error(message = "Unknown error"))
                }
            }
        }.flowOn(Dispatchers.IO)
}