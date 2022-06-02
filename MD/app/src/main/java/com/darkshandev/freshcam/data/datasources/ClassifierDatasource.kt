package com.darkshandev.freshcam.data.datasources

import android.content.Context
import android.util.Log
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.ClassifierResult
import com.darkshandev.freshcam.utils.ErrorUtils
import com.darkshandev.freshcam.utils.asTensorInput
import com.darkshandev.freshcam.utils.getIndexOfMax
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import dagger.hilt.android.qualifiers.ApplicationContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.IOException
import java.nio.ByteOrder
import javax.inject.Inject


class ClassifierDatasource @Inject constructor(
    private val retrofit: Retrofit,
    private val modelDownloader: FirebaseModelDownloader,
    @ApplicationContext private val context: Context
) {

    private lateinit var interpreter: Interpreter;

    suspend fun classifyImage(image: File): AppState<ClassifierResult> {
        return try {
            val input = image.asTensorInput()
            val tensorBuffer = TensorBuffer.createFixedSize(intArrayOf(4, 32), DataType.FLOAT32)
            val modelOutput = tensorBuffer.buffer.order(ByteOrder.nativeOrder())
            interpreter.run(input, modelOutput)
            modelOutput.rewind()
            val probabilities = modelOutput.asFloatBuffer()
            val index = probabilities.getIndexOfMax()
            val result = ClassifierResult(index, probabilities.get(index))
            AppState.Success(result)
        } catch (e: IOException) {
            AppState.Error(e.message ?: "error")
        }
    }

    suspend fun getLatestModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            // Also possible: .requireCharging() and .requireDeviceIdle()
            .build()

        modelDownloader.getModel(
            "converted_model", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND,
            conditions
        )
            .addOnSuccessListener { model: CustomModel? ->

                val modelFile = model?.file
                if (modelFile != null) {
                    Log.d("model", "model downloaded ${modelFile.absolutePath}")
                    interpreter = Interpreter(modelFile)
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