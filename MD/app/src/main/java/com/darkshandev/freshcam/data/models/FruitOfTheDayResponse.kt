package com.darkshandev.freshcam.data.models

data class FruitOfTheDayResponse(
    val `data`: FruitOfTheDay,
    val error: Boolean,
    val message: String
)