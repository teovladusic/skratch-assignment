package com.teovladusic.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.teovladusic.feature.home.HomeRoute


const val HOME_ROUTE = "home_route"

fun NavGraphBuilder.homeScreen() {
    composable(route = HOME_ROUTE) {
        HomeRoute()
    }
}