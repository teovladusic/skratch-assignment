package com.teovladusic.core.domain.repository

import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.domain.model.Result

interface FriendRepository {
    suspend fun getFriends(resultsCount: Int): Result<List<Friend>>
}