package com.darkshandev.freshcam.data.models

import com.google.gson.annotations.SerializedName

data class ClassifierLabel(
val id: String,
val index: Int,
val name: String,
@SerializedName("short_desc")
val shortDesc: String
)

fun ClassifierLabel.toEntity(): ClassifierLabelEntity {
    return ClassifierLabelEntity(
        id = id,
        index = index,
        label = name,
        shortDesc = shortDesc
    )
}