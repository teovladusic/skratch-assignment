package com.teovladusic.friends_map_view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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

    val coroutineScope = rememberCoroutineScope()

    val mapView = remember {
        FriendsMapView(
            context = context,
            cameraState = viewModel.cameraState,
            clickListener = clickListener,
            coroutineScope = coroutineScope,
            cameraChangedCallback = { viewModel.cameraState = it.cameraState }
        )
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            update = { mapView ->
                mapView.updateFeatures(state.features)
            },
            modifier = Modifier
                .padding(bottom = it.calculateBottomPadding())
                .fillMaxSize()
        )
    }
}