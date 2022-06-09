package com.darkshandev.freshcam.data.datasources

import android.content.Context
import android.util.Log
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.ClassifierResult
import com.darkshandev.freshcam.data.models.LatestLabelResponse
import com.darkshandev.freshcam.data.networks.ClassifierService
import com.darkshandev.freshcam.utils.ErrorUtils
import com.darkshandev.freshcam.utils.asTensorInput
import com.darkshandev.freshcam.utils.getIndexOfMax
import com.darkshandev.freshcam.utils.getMax
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.inject.Inject
import kotlin.math.absoluteValue


class ClassifierDatasource @Inject constructor(
    private val retrofit: Retrofit,
    private val classifierService: ClassifierService,
    private val modelDownloader: FirebaseModelDownloader,
    private val remoteConfig: FirebaseRemoteConfig,
    @ApplicationContext private val context: Context
) {

    private var interpreterSingleClassifier: Interpreter? = null
    private var interpreterFruitsClassifier: Interpreter? = null
    private var interpreterFreshnessClassifier: Interpreter? = null

    suspend fun classifyImage(image: File): AppState<ClassifierResult> {
        return try {
            return if (remoteConfig.getBoolean("isSingleModelClassifier")) {
                classifyWithSingleModel(image)
            } else {
                classifyWithMultiModel(image)
            }
        } catch (e: IOException) {
            AppState.Error(e.message ?: "error")
        }
    }

    private suspend fun classifyWithSingleModel(image: File): AppState<ClassifierResult> {
        return if (interpreterSingleClassifier != null) {
            val input = image.asTensorInput()
            val tensorBuffer = TensorBuffer.createFixedSize(intArrayOf(4, 32), DataType.FLOAT32)
            val modelOutput = tensorBuffer.buffer.order(ByteOrder.nativeOrder())
            interpreterSingleClassifier?.run(input, modelOutput)
            modelOutput.rewind()
            val probabilities = modelOutput.asFloatBuffer()
            val index = probabilities.getIndexOfMax()
            val result = ClassifierResult(index, (probabilities.get(index) * 100))
            AppState.Success(result)
        } else {
            AppState.Error("wait a moment model is not completely downloaded")
        }
    }

    private suspend fun classifyWithMultiModel(image: File): AppState<ClassifierResult> {
        suspend fun classifyFruits(input: ByteBuffer): FloatBuffer {
            val tensorFruitsBuffer =
                TensorBuffer.createFixedSize(intArrayOf(4, 32), DataType.FLOAT32)
            val modelFruitsOutput = tensorFruitsBuffer.buffer.order(ByteOrder.nativeOrder())
            interpreterFruitsClassifier?.run(input, modelFruitsOutput)
            modelFruitsOutput.rewind()
            return modelFruitsOutput.asFloatBuffer()
        }

        suspend fun classifyFreshness(input: ByteBuffer): FloatBuffer {
            val tensorFreshnessBuffer =
                TensorBuffer.createFixedSize(intArrayOf(4, 32), DataType.FLOAT32)
            val modelFreshnessOutput = tensorFreshnessBuffer.buffer.order(ByteOrder.nativeOrder())
            interpreterFreshnessClassifier?.run(input, modelFreshnessOutput)
            modelFreshnessOutput.rewind()
            return modelFreshnessOutput.asFloatBuffer()
        }

        return if (interpreterFruitsClassifier != null && interpreterFreshnessClassifier != null) {
            val input = image.asTensorInput()
            val fruitsProbabilities = classifyFruits(input)
            val freshnessProbabilities = classifyFreshness(input)
            val freshConfidence = ((freshnessProbabilities.getMax() - 0.5) / 5) * 1000

            val index = fruitsProbabilities.getIndexOfMax()
            val result = ClassifierResult(
                index,
                freshConfidence.absoluteValue.toFloat(),
                freshConfidence < 0
            )
            AppState.Success(result)
        } else {
            AppState.Error("wait a moment model is not completely downloaded")
        }
    }

    suspend fun getLatestLabel(): AppState<LatestLabelResponse> =
        getResponse(context.getString(R.string.cannot_get_labels)) {
            classifierService.getLatestLabels()
        }


    private val _downloadStatus = MutableStateFlow<AppState<String>>(AppState.Initial())
    val downloadStatus = _downloadStatus.asStateFlow()

    suspend fun getLatestModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()
        val isSingle = remoteConfig.getBoolean("isSingleModelClassifier")
        if (isSingle) {
            _downloadStatus.value = AppState.Loading()
            modelDownloader.run {

                getModel(
                    "converted_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                    conditions
                )
                    .addOnSuccessListener { model: CustomModel? ->
                        val modelFile = model?.file
                        if (modelFile != null) {
                            Log.d("model", "model downloaded ${modelFile.absolutePath}")
                            interpreterSingleClassifier = Interpreter(modelFile)
                            _downloadStatus.value = AppState.Success("model downloaded")
                            deleteDownloadedModel("fruits-classifier")
                            deleteDownloadedModel("freshness-classifier")
                        }
                    }.addOnFailureListener {
                        _downloadStatus.value = AppState.Error(it.message ?: "unknown error")
                    }
            }
        } else {
            modelDownloader.run {
                _downloadStatus.value = AppState.Loading()

                getModel(
                    "fruits-classifier", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                    conditions
                )
                    .addOnSuccessListener { model: CustomModel? ->
                        val modelFile = model?.file
                        if (modelFile != null) {
                            Log.d("model", "model downloaded ${modelFile.absolutePath}")
                            interpreterFruitsClassifier = Interpreter(modelFile)
                            _downloadStatus.value = AppState.Success("model downloaded")
                        }
                    }.addOnFailureListener {
                        _downloadStatus.value = AppState.Error(it.message ?: "unknown error")
                    }
                _downloadStatus.value = AppState.Loading()
                getModel(
                    "freshness-classifier", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
                    conditions
                )
                    .addOnSuccessListener { model: CustomModel? ->
                        val modelFile = model?.file
                        if (modelFile != null) {
                            Log.d("model", "model downloaded ${modelFile.absolutePath}")
                            interpreterFreshnessClassifier = Interpreter(modelFile)
                            _downloadStatus.value = AppState.Success("model downloaded")
                            deleteDownloadedModel("converted_model")
                        }
                    }.addOnFailureListener {
                        _downloadStatus.value = AppState.Error(it.message ?: "unknown error")
                    }
            }
        }
    }


    private suspend fun <T> getResponse(
        defaultErrorMessage: String,
        request: suspend () -> Response<T>

    ): AppState<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                val body = result.body()
                body?.let {
                    return AppState.Success(it)
                } ?: run {
                    return error("${result.code()} ${result.message()}")
                }
            } else {

                val errorResponse = ErrorUtils.parseError(result, retrofit)
                error(errorResponse?.localizedMessage ?: defaultErrorMessage)

            }
        } catch (e: Throwable) {
            AppState.Error(context.getString(R.string.error_request), null)
        }
    }

    private fun <T> error(errorMessage: String): AppState<T> =
        AppState.Error(errorMessage)
}