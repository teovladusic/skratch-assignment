package com.teovladusic.friends_map_view

import com.teovladusic.core.domain.model.Friend

data class FriendsMapUiState(
    val friends: List<Friend> = emptyList()
)