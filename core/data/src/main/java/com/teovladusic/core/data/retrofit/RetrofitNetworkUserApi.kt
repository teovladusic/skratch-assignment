package com.teovladusic.core.data.retrofit

import com.teovladusic.core.data.model.NetworkResult
import com.teovladusic.core.data.model.NetworkUser
import com.teovladusic.core.network.api_result.ApiResult
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitNetworkUserApi {
    @GET("api")
    suspend fun getUsers(
        @Query("results") resultsCount: Int
    ): ApiResult<NetworkResult<List<NetworkUser>>>
}