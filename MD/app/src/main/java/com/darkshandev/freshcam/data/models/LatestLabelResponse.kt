package com.darkshandev.freshcam.data.models

data class LatestLabelResponse(
    val `data`: List<Label>,
    val error: Boolean,
    val message: String
)