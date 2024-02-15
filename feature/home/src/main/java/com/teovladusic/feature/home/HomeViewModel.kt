package com.teovladusic.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel: ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun changeViewType(newViewType: HomeFriendsViewType) {
        _state.update { it.copy(friendsViewType = newViewType) }
    }
}