package com.teovladusic.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.teovladusic.core.common.UiText
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons
import com.teovladusic.core.domain.model.Friend

data class HomeUiState(
    val friends: List<Friend> = emptyList(),
    val isFriendsLoading: Boolean = false,
    val friendsCount: Int = DEFAULT_FRIENDS_COUNT,
    val friendsViewType: HomeFriendsViewType = HomeFriendsViewType.Map,
    val errorMessage: UiText? = null
) {

    companion object {
        const val DEFAULT_FRIENDS_COUNT = 5
    }
}

enum class HomeFriendsViewType {
    Map, List;

    val painter: Painter
        @Composable get() = when (this) {
            Map -> SkratchAssignmentIcons.map
            List -> SkratchAssignmentIcons.list
        }
}