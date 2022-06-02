package com.darkshandev.freshcam.data.repositories

import com.darkshandev.freshcam.data.datasources.ClassifierDatasource
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.ScanResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ClassifierRepository @Inject constructor(private val dataSource: ClassifierDatasource) {
    interface ClassifierCallback {
        fun onSuccess(result: ScanResult)
        fun onError(error: String)
    }

    suspend fun getLatestModel() {
        dataSource.getLatestModel()
    }

    fun classifyImage(value: File, callback: ClassifierCallback) {

        callback.onSuccess(
            ScanResult(
                "Orange_Fresh",
                70.0f,
                "lorem ipsum dolor sit amet consectetur adipiscing elit"
            )
        )

    }
}