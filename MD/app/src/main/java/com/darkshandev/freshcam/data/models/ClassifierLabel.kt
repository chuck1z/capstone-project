package com.darkshandev.freshcam.data.models

import com.google.gson.annotations.SerializedName

data class ClassifierLabel(
val id: Int,
val index: Int,
val label: String,
@SerializedName("short_desc")
val shortDesc: String
)

fun ClassifierLabel.toEntity(): ClassifierLabelEntity {
    return ClassifierLabelEntity(
        id = id,
        index = index,
        label = label,
        shortDesc = shortDesc
    )
}