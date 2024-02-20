package com.teovladusic.skratchassignment

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teovladusic.core.common.R
import com.teovladusic.core.common.UiText
import com.teovladusic.core.domain.model.Result
import com.teovladusic.core.domain.repository.FriendRepository
import com.teovladusic.core.ui.extension.getUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityUiState())
    val state = _state.asStateFlow()

    val friendsData = friendRepository.friendsDataFlow

    var friendsCountTextFieldState by mutableStateOf(TextFieldValue(DEFAULT_FRIENDS_COUNT.toString()))
        private set

    fun onFriendsCountTextChanged(value: TextFieldValue) {
        friendsCountTextFieldState = value
    }

    init {
        fetchFriends(DEFAULT_FRIENDS_COUNT)
    }

    private fun fetchFriends(count: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val friendsResult = friendRepository.getFriends(count)

            if (friendsResult is Result.Failure) {
                val networkErrorType = friendsResult.networkErrorType
                val uiText: UiText = when {
                    networkErrorType != null -> networkErrorType.getUiText()
                    else -> UiText.StringResource(R.string.unknown_error)
                }

                _state.update { it.copy(errorMessage = uiText) }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }

    fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun confirmNumberOfUsers() {
        val count = friendsCountTextFieldState.text.toIntOrNull() ?: DEFAULT_FRIENDS_COUNT
        fetchFriends(count)
    }

    companion object {
        const val DEFAULT_FRIENDS_COUNT = 5
    }
}