package com.darkshandev.freshcam.data.models

data class TipsDetailResponse(
    val `data`: TipsDetail,
    val error: Boolean,
    val message: String
)