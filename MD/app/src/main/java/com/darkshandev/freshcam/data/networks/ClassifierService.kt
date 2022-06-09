package com.darkshandev.freshcam.data.networks

import com.darkshandev.freshcam.data.models.LatestLabelResponse
import retrofit2.Response
import retrofit2.http.GET

interface ClassifierService {
    @GET("models/label")
    suspend fun getLatestLabels(
    ): Response<LatestLabelResponse>
}