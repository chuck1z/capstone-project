package com.darkshandev.freshcam.data.models

data class LatestLabelResponse(
    val `data`: List<ClassifierLabel>,
    val error: Boolean,
    val message: String
)