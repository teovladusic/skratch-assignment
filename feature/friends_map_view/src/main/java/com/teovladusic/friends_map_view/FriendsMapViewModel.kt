package com.teovladusic.friends_map_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FriendsMapViewModel @Inject constructor(
    friendRepository: FriendRepository
) : ViewModel() {

    var cameraState: CameraState =
        CameraState(Point.fromLngLat(0.0, 0.0), EdgeInsets(0.0, 0.0, 0.0, 0.0), 0.0, 0.0, 0.0)

    private val _state = MutableStateFlow(FriendsMapUiState())

    val state = _state.combine(friendRepository.friendsDataFlow) { state, friendData ->
        val features = withContext(Dispatchers.Default) {
            friendData.friends.map {
                it.toFeature()
            }
        }
        state.copy(friends = friendData.friends, features = features)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        FriendsMapUiState()
    )
}

private fun Friend.toFeature(): Feature {
    return Feature.fromGeometry(
        Point.fromLngLat(longitude, latitude),
        JsonObject().apply {
            addProperty("name", name)
            addProperty("image", pictureUrl)
        },
        id
    )
}