package com.darkshandev.freshcam.data.repositories

import android.os.Bundle
import android.util.Log
import com.darkshandev.freshcam.data.database.ClassifierLabelDao
import com.darkshandev.freshcam.data.database.HistoryClassificationDao
import com.darkshandev.freshcam.data.datasources.ClassifierDatasource
import com.darkshandev.freshcam.data.datasources.PrefService
import com.darkshandev.freshcam.data.models.*
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class ClassifierRepository @Inject constructor(
    private val dataSource: ClassifierDatasource,
    private val dao: ClassifierLabelDao,
    private val historyDao: HistoryClassificationDao,
    private val prefService: PrefService
) {
    interface ClassifierCallback {
        fun onSuccess(result: ScanResult)
        fun onError(error: String)
    }

    val isFirstLaunch = prefService.isFirstLaunch.flowOn(Dispatchers.IO)

    suspend fun marksAsLaunched() = withContext(Dispatchers.IO) {
        prefService.marksAsLaunched()
    }

    val downloadStatus = dataSource.downloadStatus

    suspend fun getLatestModel() {
        dataSource.getLatestModel()
    }

    val histories = historyDao.getHistories().flowOn(Dispatchers.IO)

    suspend fun clearHistoryClassification() = withContext(Dispatchers.IO) {
        historyDao.clearHistories()
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
            Firebase.analytics.logEvent(e.message ?: "unknown exception", null)
        }
    }

    suspend fun classifyImage(image: File, callback: ClassifierCallback) {
        withContext(Dispatchers.IO) {
            when (val result = dataSource.classifyImage(image = image)) {
                is AppState.Success -> {
                    try {
                        result.data?.let {
                            val findLabel = dao.getLabel(it.classifiedIndex)
                            findLabel?.let {label->
                            var labelResult = label.label
                            it.freshness?.let { isFresh ->
                                labelResult = "${if (isFresh) "FRESH_" else "ROTTEN_"}$labelResult"
                            }
                            val scanResult = ScanResult(
                                fruitsId = label.id,
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
                            }?: run{
                                val params = Bundle()
                                params.putString("exception", "exception :"+"label missmatch no label with index ${result.data.classifiedIndex} found ")
                                params.putParcelable("result", result.data)
                                Firebase.analytics.logEvent("classifier_exception", params)
                                callback.onError("missmatch result, please try again")
                            }
                        } ?: callback.onError("No result")
                    } catch (e: Exception) {
                        val params = Bundle()
                        params.putString("exception", "exception :"+e.message)
                        params.putParcelable("result", result.data)
                        Firebase.analytics.logEvent("classifier_exception", params)

                        Log.d("exception", e.message ?: "unknown")
                        Firebase.crashlytics.setCustomKey("exception", e.message ?: "unknown")

                        callback.onError("please try again")
                    }
                }
                is AppState.Error -> callback.onError(result.message ?: "")
                else -> callback.onError("Unknown error")
            }
        }
    }

}