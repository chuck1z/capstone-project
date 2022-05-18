package com.darkshandev.freshcam.data.networks

import com.darkshandev.freshcam.data.models.FruitsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StoryService {
    @GET("fruits")
    suspend fun getFruits(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        ): Response<List<FruitsModel>>
}