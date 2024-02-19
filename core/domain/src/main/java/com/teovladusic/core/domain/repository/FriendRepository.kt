package com.teovladusic.core.domain.repository

import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.domain.model.FriendsData
import com.teovladusic.core.domain.model.Result
import kotlinx.coroutines.flow.StateFlow

interface FriendRepository {
    val friendsDataFlow: StateFlow<FriendsData>
    suspend fun getFriends(resultsCount: Int): Result<List<Friend>>
    suspend fun getFriend(id: String): Friend?
}