package com.teovladusic.core.network.api_result

@Suppress("detekt.MagicNumber")
enum class NetworkErrorType {
    BadRequest,
    Unauthorized,
    Forbidden,
    NotFound;

    companion object {
        fun fromStatusCode(code: Int) = when (code) {
            400 -> BadRequest
            401 -> Unauthorized
            403 -> Forbidden
            404 -> NotFound
            else -> null
        }
    }
}