package com.teovladusic.friends_map_view

import androidx.compose.runtime.Immutable
import com.mapbox.geojson.Feature
import com.teovladusic.core.domain.model.Friend

@Immutable
data class FriendsMapUiState(
    val friends: List<Friend> = emptyList(),
    val features: List<Feature> = emptyList()
)