package com.teovladusic.core.data.model

import com.teovladusic.core.network.api_result.ApiResult
import com.teovladusic.core.domain.model.Result

inline fun <T, K> ApiResult<K>.mapApiResult(
    crossinline mapSuccess: (ApiResult.Success<K>) -> T
): Result<T> = when (this) {
    is ApiResult.Error -> Result.Failure(networkErrorType = networkErrorType)
    is ApiResult.Exception -> Result.Failure(throwable = throwable)
    is ApiResult.Success -> Result.Success(mapSuccess(this))
}