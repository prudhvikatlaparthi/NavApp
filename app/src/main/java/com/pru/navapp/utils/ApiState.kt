package com.pru.navapp.utils

sealed class ApiState<T>(
    val data: T? = null,
    val errorMessage: String = ""
) {
    class Success<T>(data: T) : ApiState<T>(data)
    class Failure<T>(errorMessage: String) : ApiState<T>(
        errorMessage = errorMessage
    )

    class Loading<T> : ApiState<T>()
//    class Initial<T> : ApiState<T>()
}