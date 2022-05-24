package com.darkshandev.freshcam.data.models

sealed class AppState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : AppState<T>(data)
    class Error<T>(message: String, data: T? = null) : AppState<T>(data, message)
    class Loading<T> : AppState<T>()
    class Initial<T> : AppState<T>()
}