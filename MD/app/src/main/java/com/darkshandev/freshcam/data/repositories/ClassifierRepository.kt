package com.darkshandev.freshcam.data.repositories

import com.darkshandev.freshcam.data.database.ClassifierLabelDao
import com.darkshandev.freshcam.data.database.HistoryClassificationDao
import com.darkshandev.freshcam.data.datasources.ClassifierDatasource
import com.darkshandev.freshcam.data.models.*
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ClassifierRepository @Inject constructor(
    private val dataSource: ClassifierDatasource,
    private val dao: ClassifierLabelDao,
    private val historyDao: HistoryClassificationDao,
    private val analytics: FirebaseAnalytics
) {
    interface ClassifierCallback {
        fun onSuccess(result: ScanResult)
        fun onError(error: String)
    }

    val downloadStatus = dataSource.downloadStatus

    suspend fun getLatestModel() {
        dataSource.getLatestModel()
    }


    suspend fun loadLatestLabel() {
        try {
            val result = dataSource.getLatestLabel()
            if (result is AppState.Success) {
                val rows = result.data?.data ?: emptyList()
                if (rows.isNotEmpty()) {
                    dao.update(rows.map { it.toEntity() })
                }
            }
        } catch (e: Exception) {
            analytics.logEvent(e.message ?: "unknown exception",null)
        }
    }

    suspend fun classifyImage(image: File, callback: ClassifierCallback) {
        withContext(Dispatchers.IO) {
            when (val result = dataSource.classifyImage(image = image)) {
                is AppState.Success -> {
                    try {
                        result.data?.let {
                            val label = dao.getLabel(it.classifiedIndex)
                            var labelResult = label.label
                            it.freshness?.let { isFresh ->
                                labelResult = "${if (isFresh) "FRESH_" else "ROTTEN_"}$labelResult"
                            }
                            val scanResult = ScanResult(
                                label = labelResult,
                                confidence = it.confidence,
                                description = label.shortDesc,
                            )
                            historyDao.addHistory(
                                HistoryClassificationEntity(
                                    fruitsName = scanResult.getName(),
                                    photo = image.path,
                                    freshness = it.freshness ?: false,
                                    confidence = it.confidence
                                )
                            )
                            callback.onSuccess(
                                scanResult
                            )
                        } ?: callback.onError("No result")
                    } catch (e: Exception) {
                        analytics.logEvent(e.message ?: "unknown exception",null)
                        callback.onError("please try again")
                    }
                }
                is AppState.Error -> callback.onError(result.message ?: "")
                else -> callback.onError("Unknown error")
            }
        }
    }

}