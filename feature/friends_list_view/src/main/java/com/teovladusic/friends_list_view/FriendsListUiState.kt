package com.teovladusic.friends_list_view

import com.teovladusic.core.domain.model.Friend

data class FriendsListUiState(
    val friends: List<Friend> = emptyList()
)