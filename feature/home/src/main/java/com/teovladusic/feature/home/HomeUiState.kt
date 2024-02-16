package com.teovladusic.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.teovladusic.core.common.UiText
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons
import com.teovladusic.core.domain.model.Friend

data class HomeUiState(
    val friends: List<Friend> = emptyList(),
    val isLoading: Boolean = false,
    val friendsCount: Int = 100,
    val friendsViewType: HomeFriendsViewType = HomeFriendsViewType.Map,
    val errorMessage: UiText? = null
)

enum class HomeFriendsViewType {
    Map, List;

    val painter: Painter
        @Composable get() = when (this) {
            Map -> SkratchAssignmentIcons.map
            List -> SkratchAssignmentIcons.list
        }
}