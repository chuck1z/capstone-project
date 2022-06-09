package com.darkshandev.freshcam.data.models


data class FruitsDetail(

    val about: String,
    val binom: String,
    val id: String,
    val image: String,
    val name: String,
    val nutrition: Nutrition,
    val tips: List<String>,
    val vitamin: List<String>

)