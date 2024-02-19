package com.teovladusic.core.data.repository

import com.teovladusic.core.data.model.mapApiResult
import com.teovladusic.core.data.model.toDomain
import com.teovladusic.core.data.retrofit.RetrofitNetworkUserApi
import com.teovladusic.core.domain.dispatchers.DispatcherProvider
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.domain.model.FriendsData
import com.teovladusic.core.domain.model.Result
import com.teovladusic.core.domain.repository.FriendRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class FriendRepositoryImpl(
    private val api: RetrofitNetworkUserApi,
    private val dispatcherProvider: DispatcherProvider
) : FriendRepository {

    // Cached friends list
    private val _friendsDataFlow = MutableStateFlow(FriendsData(emptyList(), 0))

    override val friendsDataFlow: StateFlow<FriendsData>
        get() = _friendsDataFlow

    override suspend fun getFriends(resultsCount: Int): Result<List<Friend>> =
        withContext(dispatcherProvider.io) {
            _friendsDataFlow.update { it.copy(friendsCount = resultsCount) }

            val friendsResult = api.getUsers(resultsCount).mapApiResult {
                it.data?.results?.map { user -> user.toDomain() }.orEmpty()
            }

            val friendsList = (friendsResult as? Result.Success)?.value.orEmpty()

            _friendsDataFlow.update { FriendsData(friendsList, resultsCount) }

            friendsResult
        }

    override suspend fun getFriend(id: String): Friend? = withContext(dispatcherProvider.io) {
        _friendsDataFlow.value.friends.find { it.id == id }
    }
}