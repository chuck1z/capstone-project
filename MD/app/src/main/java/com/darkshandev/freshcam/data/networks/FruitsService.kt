package com.darkshandev.freshcam.data.networks

import com.darkshandev.freshcam.data.models.FruitDetailResponse
import com.darkshandev.freshcam.data.models.FruitOfTheDayResponse
import com.darkshandev.freshcam.data.models.TipsDetailResponse
import com.darkshandev.freshcam.data.models.TipsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FruitsService {
    @GET("fruits/{id}/detail")
    suspend fun getFruitDetail(
        @Path("id") id: String
    ): Response<FruitDetailResponse>

    @GET("fruits/tips/{id}")
    suspend fun getTipsDetail(
        @Path("id") id: String
    ): Response<TipsDetailResponse>

    @GET("fruits/fotd")
    suspend fun getFOTD(): Response<FruitOfTheDayResponse>

    @GET("fruits/tips")
    suspend fun getTips(): Response<TipsResponse>
}