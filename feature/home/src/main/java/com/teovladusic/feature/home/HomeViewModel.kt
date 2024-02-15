package com.teovladusic.feature.home

import androidx.lifecycle.ViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    fun changeViewType(newViewType: HomeFriendsViewType) {
        _state.update { it.copy(friendsViewType = newViewType) }
    }

    // Check if good enough
    var mapCameraState = provideDefaultCameraState()
        private set

    fun onCameraStateChanged(cameraState: CameraState) {
        mapCameraState = cameraState
    }
}

private fun provideDefaultCameraState(): CameraState = CameraState(
    Point.fromLngLat(0.0, 0.0),
    EdgeInsets(0.0, 0.0, 0.0, 0.0),
    0.0,
    0.0,
    0.0,
)