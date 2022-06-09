package com.darkshandev.freshcam.data.networks

import com.darkshandev.freshcam.data.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FruitsService {
    @GET("fruits/{id}/detail")
    suspend fun getFruitDetail(
        @Path("id") id:String
    ): Response<FruitDetailResponse>
    @GET("fruits/tips/{id}")
    suspend fun getTipsDetail(
        @Path("id") id:String
    ): Response<TipsDetailResponse>
    @GET("fruits/fotd")
    suspend fun getFOTD():Response<FruitOfTheDayResponse>
    @GET("fruits/tips")
    suspend fun getTips():Response<TipsResponse>
}