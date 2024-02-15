package com.teovladusic.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons

data class HomeUiState(
    val friends: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val friendsCount: Int = 5,
    val friendsViewType: HomeFriendsViewType = HomeFriendsViewType.Map
)

enum class HomeFriendsViewType {
    Map, List;


    val painter: Painter
        @Composable get() = when (this) {
            Map -> SkratchAssignmentIcons.map
            List -> SkratchAssignmentIcons.list
        }
}