package com.teovladusic.skratchassignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.teovladusic.feature.home.navigation.HOME_ROUTE
import com.teovladusic.feature.home.navigation.homeScreen

@Composable
fun SkratchAssignmentNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
    ) {
        homeScreen()
    }
}