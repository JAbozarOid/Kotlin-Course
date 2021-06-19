package com.github.recipes.util

/**
 * A sealed class is abstract by itself, it cannot be instantiated directly and can have abstract members.
 * Sealed classes are not allowed to have non-private constructors (their constructors are private by default).
 */
/**
 * Kotlin operators and symbols
 * 3- ? : marks a type as nullable
 */
sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}