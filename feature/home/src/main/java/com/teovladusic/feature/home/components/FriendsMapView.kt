package com.teovladusic.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
import com.teovladusic.core.designsystem.theme.footnoteBook
import com.teovladusic.core.domain.model.Friend

@OptIn(MapboxExperimental::class)
@Composable
internal fun FriendsMapView(
    cameraState: CameraState,
    onCameraStateChanged: (CameraState) -> Unit,
    friends: List<Friend>,
    onFriendClick: (Friend) -> Unit
) {
    val mapViewportState = rememberMapViewportState {
        setCameraOptions(cameraState.toCameraOptions())
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        mapInitOptionsFactory = {
            MapInitOptions(
                context = it,
                styleUri = Style.OUTDOORS
            )
        },
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

        MapEffect(key1 = friends) {

        }

        friends.forEach { friend ->
            ViewAnnotation(
                options = viewAnnotationOptions {
                    geometry(Point.fromLngLat(friend.longitude, friend.latitude))
                },
            ) {
                FriendMapCircle(friend = friend, onFriendClick = onFriendClick)
            }
        }
    }
}

@Composable
private fun FriendMapCircle(friend: Friend, onFriendClick: (Friend) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onFriendClick(friend) }
    ) {
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
