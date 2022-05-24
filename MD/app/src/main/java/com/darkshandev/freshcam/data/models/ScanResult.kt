package com.darkshandev.freshcam.data.models

data class ScanResult(
    val label: String,
    val confidence: Float,
    val description: String
)

fun ScanResult.getName(): String {
    return label.split("_").last()
}

fun ScanResult.getFreshness(): String {
    return label.split("_").first()
}
