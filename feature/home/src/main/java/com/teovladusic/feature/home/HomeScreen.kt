package com.teovladusic.feature.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapboxMap

@OptIn(MapboxExperimental::class)
@Composable
fun HomeScreen() {
    MapboxMap(
        modifier = Modifier.fillMaxSize()
    )
}
