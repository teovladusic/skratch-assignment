package com.teovladusic.core.domain.usecase

import com.teovladusic.core.domain.repository.FriendRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {
    suspend operator fun invoke(resultsCount: Int) = friendRepository.getFriends(resultsCount)
}