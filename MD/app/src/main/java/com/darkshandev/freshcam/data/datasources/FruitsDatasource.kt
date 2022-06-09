package com.darkshandev.freshcam.data.datasources

import android.content.Context
import com.darkshandev.freshcam.R
import com.darkshandev.freshcam.data.models.*
import com.darkshandev.freshcam.data.networks.FruitsService
import com.darkshandev.freshcam.utils.ErrorUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class FruitsDatasource @Inject constructor(
    private val retrofit: Retrofit,
    private val fruitsService: FruitsService,
    @ApplicationContext private val context: Context
) {

    suspend fun getFruitsDetail(
        fruitsId: String,
    ): AppState<FruitDetailResponse> =
        getResponse(context.getString(R.string.unable_fetch_stories)) {
            fruitsService.getFruitDetail(fruitsId)
        }

    suspend fun getFruitsOfTheDay(): AppState<FruitOfTheDayResponse> =
        getResponse(context.getString(R.string.unable_fetch_fotd)) {
            fruitsService.getFOTD()
        }

    suspend fun getTips(): AppState<TipsResponse> =
        getResponse(context.getString(R.string.unable_to_fetch_tips)) {
            fruitsService.getTips()
        }

    suspend fun getTipsDetail(tipsId: String): AppState<TipsDetailResponse> =
        getResponse(context.getString(R.string.unable_to_fetch_detail_tips)) {
            fruitsService.getTipsDetail(tipsId)
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