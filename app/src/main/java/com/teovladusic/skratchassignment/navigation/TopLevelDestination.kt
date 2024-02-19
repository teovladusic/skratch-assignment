package com.teovladusic.skratchassignment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons

enum class TopLevelDestination {
    FriendsMap,
    FriendsList;

    val icon: Painter
        @Composable get() = when (this) {
            FriendsMap -> SkratchAssignmentIcons.map
            FriendsList -> SkratchAssignmentIcons.list
        }
}