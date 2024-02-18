package com.teovladusic.friend_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.core.domain.repository.FriendRepository
import com.teovladusic.friend_details.navigation.FriendDetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendDetailsViewModel @Inject constructor(
    private val friendRepository: FriendRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(FriendDetailsUiState())
    val state = _state.asStateFlow()

    private val friendDetailsArgs = FriendDetailsArgs(savedStateHandle)

    init {
        getFriend(friendDetailsArgs.friendId)
    }

    private fun getFriend(id: String) {
        viewModelScope.launch {
            friendRepository.getFriend(id)?.let { friend ->
                _state.update { it.copy(friend = friend) }
            }
        }
    }
}