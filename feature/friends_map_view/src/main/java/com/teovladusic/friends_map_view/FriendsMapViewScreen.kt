package com.teovladusic.friends_map_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun FriendsMapViewRoute(
    viewModel: FriendsMapViewModel = hiltViewModel(),
    onFriendClick: (String) -> Unit
) {
    FriendsMapViewScreen(viewModel = viewModel, onFriendClick = onFriendClick)
}

@Composable
internal fun FriendsMapViewScreen(
    viewModel: FriendsMapViewModel,
    onFriendClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val clickListener = object : FriendsMapViewClickListener {
        override fun onFriendClick(id: String) {
            onFriendClick(id)
        }
    }

    val mapView = remember {
        FriendsMapView(
            context = context,
            cameraState = viewModel.cameraState,
            clickListener = clickListener,
            cameraChangedCallback = { viewModel.cameraState = it.cameraState }
        )
    }

    AndroidView(
        factory = { mapView },
        update = {
            it.updateFeatures(state.features)
        }
    )
}