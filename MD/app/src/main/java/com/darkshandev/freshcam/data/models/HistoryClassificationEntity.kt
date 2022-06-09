package com.darkshandev.freshcam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryClassificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fruitsName: String,
    val photo: String,
    val freshness: Boolean,
    val confidence: Float
)
