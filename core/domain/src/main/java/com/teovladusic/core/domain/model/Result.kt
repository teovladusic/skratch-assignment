package com.teovladusic.core.domain.model

sealed interface Result<out T> {
    data class Success<T>(val value: T) : Result<T>
    data class Failure(
        val throwable: Throwable? = null,
        val networkErrorType: NetworkErrorType? = null
    ) : Result<Nothing>
}
