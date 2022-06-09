package com.darkshandev.freshcam.data.datasources

import android.content.Context
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.AppState
import com.darkshandev.freshcam.data.models.FruitsDetail
import com.darkshandev.freshcam.data.models.FruitsDetailResponse
import com.darkshandev.freshcam.data.models.Nutrition
import com.darkshandev.freshcam.utils.ErrorUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class FruitsDatasource @Inject constructor(
    private val retrofit: Retrofit,
    @ApplicationContext private val context: Context
) {

    suspend fun getFruitsDetail(
        fruitsId: String,
    ): AppState<FruitsDetailResponse> =
        getResponse(context.getString(R.string.unable_fetch_stories)) {
//            service.getDetail(fruitsId)
            dummyDetail()
        }

    private suspend fun dummyDetail(): Response<FruitsDetailResponse> {
        val fruitsDetailResponseDummy = FruitsDetailResponse(
            error = false, message = "success",
            FruitsDetail(
                id = "1",
                name = "Apple",
                about = "Apple is a fruit",
                image = "test.png",
                vitamin = listOf("vit B"),
                binom = "10",
                tips = listOf(),
                nutrition = Nutrition(
                    calories = "100kkal",
                    fat = "10g",
                    protein = "10g",
                    carbs = "10g"
                ),

                )
        )
        return Response.success(fruitsDetailResponseDummy)
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