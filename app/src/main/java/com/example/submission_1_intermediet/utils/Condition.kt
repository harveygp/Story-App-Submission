package com.example.submission_1_intermediet.utils

sealed class Condition<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Condition<T>(data)
    class Error<T>(message: String, data: T? = null) : Condition<T>(data, message)
    class Loading<T>(data: T? = null) : Condition<T>(data)
}
