package com.teovladusic.friends_list_view.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.friends_list_view.FriendsListViewRoute

const val FRIENDS_LIST_VIEW_ROUTE = "friendsListViewRoute"

fun NavGraphBuilder.friendsListViewScreen(onFriendClick: (Friend) -> Unit) {
    composable(route = FRIENDS_LIST_VIEW_ROUTE) {
        FriendsListViewRoute(onFriendClick = onFriendClick)
    }
}

fun NavController.navigateToFriendsList(options: NavOptions) {
    navigate(FRIENDS_LIST_VIEW_ROUTE, options)
}