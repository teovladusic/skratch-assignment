package com.teovladusic.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teovladusic.feature.home.components.FloatingContentAnimatedVisibility
import com.teovladusic.feature.home.components.FriendsCountContent
import com.teovladusic.feature.home.components.FriendsListView
import com.teovladusic.feature.home.components.FriendsLoadingIndicator
import com.teovladusic.feature.home.components.FriendsMapView
import kotlin.math.abs

@Composable
internal fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    HomeScreen(viewModel)
}

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var floatingContentVisible by rememberSaveable { mutableStateOf(true) }

    // Make sure floating content is visible after changing view type
    LaunchedEffect(key1 = state.friendsViewType) {
        floatingContentVisible = true
    }

    val listState = rememberLazyListState()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scrollStrength = abs(available.y)

                if (scrollStrength > 2f && listState.canScrollForward) {
                    val isScrollingDown = available.y < 0f
                    floatingContentVisible = !isScrollingDown
                }

                return Offset.Zero
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.friendsViewType) {
            HomeFriendsViewType.Map -> FriendsMapView(
                cameraState = viewModel.mapCameraState,
                onCameraStateChanged = viewModel::onCameraStateChanged,
                friends = state.friends
            )

            HomeFriendsViewType.List -> FriendsListView(
                friends = state.friends,
                nestedScrollConnection = nestedScrollConnection,
                listState = listState
            )
        }

        FloatingContentAnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomStart),
            visible = floatingContentVisible,
        ) {
            TabsSwitcher(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                selectedViewType = state.friendsViewType,
                onViewTypeClick = viewModel::changeViewType
            )
        }

        var showNumberOfUsersField by rememberSaveable {
            mutableStateOf(false)
        }

        FriendsCountContent(
            floatingContentVisible = floatingContentVisible,
            friendsCount = state.friendsCount,
            showNumberOfUsersField = showNumberOfUsersField,
            onUserCountChange = viewModel::onUserCountChange,
            setShowNumberOfUsers = { showNumberOfUsersField = it }
        )

        AnimatedVisibility(
            visible = state.isFriendsLoading,
            enter = slideInVertically(initialOffsetY = { fullHeight -> -fullHeight }),
            exit = slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight }),
            modifier = Modifier.windowInsetsPadding(insets = WindowInsets.statusBars)
        ) {
            FriendsLoadingIndicator()
        }
    }
}


@Composable
private fun TabsSwitcher(
    modifier: Modifier,
    selectedViewType: HomeFriendsViewType,
    onViewTypeClick: (HomeFriendsViewType) -> Unit
) {
    val viewTypes = HomeFriendsViewType.entries
    val shape = RoundedCornerShape(100.dp)

    TabRow(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                shape = shape
            )
            .size(
                width = 138.dp, // Hardcode the width (default is fillMaxWidth)
                height = 48.dp
            )
            .clip(shape),
        selectedTabIndex = viewTypes.indexOf(selectedViewType),
        containerColor = MaterialTheme.colorScheme.secondary,
        indicator = {}, // Remove indicator
        divider = {} // Remove divider,
    ) {
        viewTypes.forEach { viewType ->
            val isSelected = viewType == selectedViewType

            Tab(
                selected = isSelected,
                onClick = { onViewTypeClick(viewType) },
                modifier = Modifier
                    .size(width = 62.dp, height = 34.dp)
                    .padding(horizontal = 7.dp)
                    .clip(shape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else Color.Transparent
                    ),
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary,
            ) {
                Icon(
                    painter = viewType.painter,
                    contentDescription = viewType.name,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}
