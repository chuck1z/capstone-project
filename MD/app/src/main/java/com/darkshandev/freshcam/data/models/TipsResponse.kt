package com.darkshandev.freshcam.data.models

data class TipsResponse(
    val `data`: List<Tips>,
    val error: Boolean,
    val message: String
)