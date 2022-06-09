package com.darkshandev.freshcam.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClassifierLabelEntity(
    @PrimaryKey val id: String,
    val index: Int,
    val label: String,
    val shortDesc: String
)
