package com.teovladusic.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.style.layers.properties.generated.ProjectionName
import com.mapbox.maps.extension.style.projection.generated.Projection
import com.mapbox.maps.extension.style.projection.generated.setProjection
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.logo.generated.LogoSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import com.mapbox.maps.toCameraOptions
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.teovladusic.core.common.R
import com.teovladusic.core.designsystem.theme.calloutBook
import com.teovladusic.core.designsystem.theme.footnoteBook
import com.teovladusic.core.designsystem.theme.largeTitleBlack
import com.teovladusic.core.designsystem.theme.title1Medium
import com.teovladusic.core.domain.model.Friend
import kotlin.math.abs

@Composable
internal fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    HomeScreen(viewModel)
}

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var floatingContentVisible by remember { mutableStateOf(true) }

    // Make sure floating content is visible after changing view type
    LaunchedEffect(key1 = state.friendsViewType) {
        floatingContentVisible = true
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scrollStrength = abs(available.y)

                if (scrollStrength > 2f) {
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
                nestedScrollConnection = nestedScrollConnection
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

        FloatingContentAnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = floatingContentVisible
        ) {
            FriendsCount(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
                count = state.friendsCount
            )
        }
    }
}

@Composable
private fun FloatingContentAnimatedVisibility(
    modifier: Modifier,
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
        content = content
    )
}

@OptIn(MapboxExperimental::class)
@Composable
private fun FriendsMapView(
    cameraState: CameraState,
    onCameraStateChanged: (CameraState) -> Unit,
    friends: List<Friend>
) {
    val mapViewportState = rememberMapViewportState {
        setCameraOptions(cameraState.toCameraOptions())
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        mapInitOptionsFactory = { MapInitOptions(context = it, styleUri = Style.OUTDOORS) },
        logoSettings = LogoSettings { enabled = false },
        attributionSettings = AttributionSettings { enabled = false },
        scaleBarSettings = ScaleBarSettings { enabled = false },
        compassSettings = CompassSettings { enabled = false },
        mapEvents = MapEvents(onCameraChanged = { onCameraStateChanged(it.cameraState) })
    ) {
        MapEffect(key1 = Unit) {
            it.mapboxMap.loadStyle(Style.OUTDOORS) { style ->
                style.setProjection(Projection(ProjectionName.MERCATOR))
            }
        }

        friends.forEach { friend ->
            ViewAnnotation(
                options = viewAnnotationOptions {
                    geometry(Point.fromLngLat(friend.longitude, friend.latitude))
                }
            ) {
                FriendMapCircle(friend = friend)
            }
        }
    }
}

@Composable
private fun FriendMapCircle(friend: Friend) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = friend.name,
            style = MaterialTheme.typography.footnoteBook,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(100.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                .padding(3.dp)
        ) {
            AsyncImage(
                model = friend.pictureUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
private fun FriendsListView(friends: List<Friend>, nestedScrollConnection: NestedScrollConnection) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = stringResource(id = R.string.my_friends),
                style = MaterialTheme.typography.largeTitleBlack,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp, bottom = 20.dp)
            )
        }

        items(
            items = friends,
            key = { it.id }
        ) {
            FriendListItem(friend = it)
        }
    }
}

@Composable
private fun FriendListItem(friend: Friend) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = friend.pictureUrl,
            contentDescription = "friend",
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${friend.name} ${friend.lastname}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = friend.username,
                style = MaterialTheme.typography.calloutBook,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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

@Composable
private fun FriendsCount(modifier: Modifier, count: Int) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                shape = CircleShape
            )
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape
            )
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.title1Medium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}