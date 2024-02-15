package com.teovladusic.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CameraState
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.plugin.attribution.generated.AttributionSettings
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import com.mapbox.maps.plugin.logo.generated.LogoSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import com.teovladusic.core.designsystem.theme.title1Medium

@Composable
internal fun HomeRoute(viewModel: HomeViewModel = hiltViewModel()) {
    HomeScreen(viewModel)
}

@Composable
internal fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (state.friendsViewType) {
            HomeFriendsViewType.Map -> FriendsMapView(
                cameraState = viewModel.mapCameraState,
                onCameraStateChanged = viewModel::onCameraStateChanged
            )

            HomeFriendsViewType.List -> FriendsListView()
        }

        TabsSwitcher(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            selectedViewType = state.friendsViewType,
            onViewTypeClick = viewModel::changeViewType
        )

        FriendsCount(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            count = state.friendsCount
        )
    }
}

@OptIn(MapboxExperimental::class)
@Composable
private fun FriendsMapView(cameraState: CameraState, onCameraStateChanged: (CameraState) -> Unit) {
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapInitOptionsFactory = {
            MapInitOptions(
                context = it,
                styleUri = Style.MAPBOX_STREETS,
                cameraOptions = CameraOptions.Builder()
                    .center(cameraState.center)
                    .padding(cameraState.padding)
                    .zoom(cameraState.zoom)
                    .bearing(cameraState.bearing)
                    .pitch(cameraState.pitch)
                    .build()
            )
        },
        logoSettings = LogoSettings { enabled = false },
        attributionSettings = AttributionSettings { enabled = false },
        scaleBarSettings = ScaleBarSettings { enabled = false },
        compassSettings = CompassSettings { enabled = false },
        mapEvents = MapEvents(
            onCameraChanged = { onCameraStateChanged(it.cameraState) },
        )
    ) {

    }
}

@Composable
private fun FriendsListView() = Unit

@Composable
private fun TabsSwitcher(
    modifier: Modifier,
    selectedViewType: HomeFriendsViewType,
    onViewTypeClick: (HomeFriendsViewType) -> Unit
) {
    val viewTypes = HomeFriendsViewType.entries

    TabRow(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                spotColor = Color(color = 0x26000000),
                ambientColor = Color(color = 0x26000000)
            )
            .size(
                width = 138.dp, // Hardcode the width (if not set, it's fillMaxWidth)
                height = 48.dp
            )
            .clip(RoundedCornerShape(100.dp)),
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
                    .padding(4.dp)
                    .clip(RoundedCornerShape(100.dp))
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
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 5.dp),
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
                elevation = 8.dp,
                spotColor = Color(color = 0x26000000),
                ambientColor = Color(color = 0x26000000)
            )
            .size(48.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(size = 100.dp)
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