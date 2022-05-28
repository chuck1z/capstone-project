package com.darkshandev.freshcam.data.models

import com.google.gson.annotations.SerializedName

data class FruitsDetailResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: FruitsDetail,

    )

data class Nutrition(

    @SerializedName("carbs") var carbs: String,
    @SerializedName("protein") var protein: String,
    @SerializedName("calories") var calories: String,
    @SerializedName("fat") var fat: String

)

data class FruitsDetail(

    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("binom") var binom: String,
    @SerializedName("nutrition") var nutrition: Nutrition,
    @SerializedName("vitamin") var vitamin: List<String>,
    @SerializedName("about") var about: String,
    @SerializedName("image") var image: String,
    @SerializedName("tips") var tips: List<String>

)