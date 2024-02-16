package com.teovladusic.core.data.repository

import com.teovladusic.core.data.model.mapApiResult
import com.teovladusic.core.data.model.toDomain
import com.teovladusic.core.data.retrofit.RetrofitNetworkUserApi
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.domain.model.Result
import com.teovladusic.core.domain.repository.FriendRepository

class FriendRepositoryImpl(
    private val api: RetrofitNetworkUserApi
) : FriendRepository {
    override suspend fun getFriends(resultsCount: Int): Result<List<Friend>> =
        api.getUsers(resultsCount).mapApiResult {
            it.data?.results?.map { user -> user.toDomain() }.orEmpty()
        }
}