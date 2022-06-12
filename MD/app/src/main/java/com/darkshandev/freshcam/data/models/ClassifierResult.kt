package com.darkshandev.freshcam.data.models


import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class ClassifierResult(
    val classifiedIndex: Int,
    val confidence: Float,
    val freshness: Boolean? = null
) : Parcelable
