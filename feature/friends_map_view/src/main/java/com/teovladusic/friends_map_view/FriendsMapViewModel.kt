package com.teovladusic.friends_map_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.core.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FriendsMapViewModel @Inject constructor(
    friendRepository: FriendRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FriendsMapUiState())

    val state = _state.combine(friendRepository.friendsDataFlow) { state, friendData ->
        state.copy(friends = friendData.friends)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        FriendsMapUiState()
    )
}