package com.darkshandev.freshcam.data.models

data class FruitDetailResponse(
    val `data`: FruitsDetail,
    val error: Boolean,
    val message: String
)