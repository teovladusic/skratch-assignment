package com.teovladusic.friend_details.navigation

import android.os.Build
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.teovladusic.core.common.extension.navigate
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.friend_details.FriendDetailsRoute

const val FRIEND_DETAILS_ROUTE = "friend_details_route"
internal const val FRIEND_ARG = "friend"

fun NavController.navigateToFriendDetails(friend: Friend) {
    navigate(FRIEND_DETAILS_ROUTE, args = bundleOf(FRIEND_ARG to friend))
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.friendDetailsSheet() {
    bottomSheet(FRIEND_DETAILS_ROUTE) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            FriendDetailsRoute(friend = it.arguments?.getParcelable(FRIEND_ARG, Friend::class.java))
        } else {
            FriendDetailsRoute(friend = it.arguments?.getParcelable(FRIEND_ARG))
        }
    }
}
