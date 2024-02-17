package com.teovladusic.skratchassignment.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.teovladusic.feature.home.navigation.HOME_ROUTE
import com.teovladusic.feature.home.navigation.homeScreen
import com.teovladusic.friend_details.navigation.friendDetailsSheet
import com.teovladusic.friend_details.navigation.navigateToFriendDetails

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun SkratchAssignmentNavHost() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.secondary,
        scrimColor = Color.Black.copy(alpha = .3f)
    ) {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
        ) {
            homeScreen(
                onFriendClick = { navController.navigateToFriendDetails(it) }
            )
            friendDetailsSheet()
        }
    }
}