package com.darkshandev.freshcam.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FruitsTips(
    val photoUrl: String,
    val title: String,
    val description: String
): Parcelable
