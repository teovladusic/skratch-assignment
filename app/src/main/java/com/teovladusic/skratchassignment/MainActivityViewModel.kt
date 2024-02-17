package com.teovladusic.skratchassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.core.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    val friendsData = friendRepository.friendsDataFlow

    init {
        fetchFriends()
    }

    private fun fetchFriends() {
        viewModelScope.launch {
            friendRepository.getFriends(DEFAULT_FRIENDS_COUNT)
        }
    }

    fun onFriendsCountChange(value: String) {
        val count = value.toIntOrNull() ?: DEFAULT_FRIENDS_COUNT

        viewModelScope.launch {
            friendRepository.getFriends(count)
        }
    }

    companion object {
        const val DEFAULT_FRIENDS_COUNT = 5
    }
}