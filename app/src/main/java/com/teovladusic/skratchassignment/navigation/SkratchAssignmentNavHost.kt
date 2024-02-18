package com.teovladusic.skratchassignment.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.teovladusic.friend_details.navigation.friendDetailsSheet
import com.teovladusic.friend_details.navigation.navigateToFriendDetails
import com.teovladusic.friends_list_view.navigation.friendsListViewScreen
import com.teovladusic.friends_map_view.navigation.FRIENDS_MAP_VIEW_ROUTE
import com.teovladusic.friends_map_view.navigation.friendsMapViewScreen

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun SkratchAssignmentNavHost(
    bottomSheetNavigator: BottomSheetNavigator,
    navController: NavHostController
) {
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.secondary,
        scrimColor = Color.Black.copy(alpha = .3f)
    ) {
        NavHost(
            navController = navController,
            startDestination = FRIENDS_MAP_VIEW_ROUTE,
        ) {
            friendsMapViewScreen(
                onFriendClick = { navController.navigateToFriendDetails(it) }
            )
            friendsListViewScreen(
                onFriendClick = { navController.navigateToFriendDetails(it) }
            )

            friendDetailsSheet()
        }
    }
}
