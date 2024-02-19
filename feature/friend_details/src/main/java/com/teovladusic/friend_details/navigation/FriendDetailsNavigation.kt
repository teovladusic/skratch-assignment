package com.teovladusic.friend_details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.teovladusic.friend_details.FriendDetailsRoute
import java.net.URLDecoder
import java.net.URLEncoder

const val FRIEND_DETAILS_ROUTE = "friend_details_route"
internal const val FRIEND_ID_ARG = "friend_id"

internal class FriendDetailsArgs(val friendId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[FRIEND_ID_ARG]),
                    Charsets.UTF_8.name()
                )
            )
}

fun NavController.navigateToFriendDetails(friendId: String) {
    val encodedId = URLEncoder.encode(friendId, Charsets.UTF_8.name())
    navigate("$FRIEND_DETAILS_ROUTE/$encodedId")
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.friendDetailsSheet() {
    bottomSheet(
        "$FRIEND_DETAILS_ROUTE/{$FRIEND_ID_ARG}",
        arguments = listOf(
            navArgument(FRIEND_ID_ARG) { type = NavType.StringType }
        )
    ) {
        FriendDetailsRoute()
    }
}
