package com.teovladusic.friends_map_view.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teovladusic.friends_map_view.FriendsMapViewRoute

const val FRIENDS_MAP_VIEW_ROUTE = "friendsMapViewRoute"

fun NavGraphBuilder.friendsMapViewScreen(onFriendClick: (String) -> Unit) {
    composable(route = FRIENDS_MAP_VIEW_ROUTE) {
        FriendsMapViewRoute(onFriendClick = onFriendClick)
    }
}

fun NavController.navigateToFriendsMap(navOptions: NavOptions) {
    navigate(FRIENDS_MAP_VIEW_ROUTE, navOptions)
}