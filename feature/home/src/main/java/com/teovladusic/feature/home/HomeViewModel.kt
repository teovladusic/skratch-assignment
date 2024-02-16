package com.teovladusic.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.teovladusic.core.domain.model.Result
import com.teovladusic.core.domain.usecase.GetFriendsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    // Check if good enough
    var mapCameraState = provideDefaultCameraState()
        private set

    init {
        fetchFriends()
    }

    private fun fetchFriends() {
        val friendsCount = _state.value.friendsCount
        viewModelScope.launch {
            when (val result = getFriendsUseCase(friendsCount)) {
                is Result.Failure -> _state.update {// todo: do this
                    it.copy()
                }

                is Result.Success -> _state.update { state ->
                    state.copy(friends = result.value)
                }
            }
        }
    }

    fun changeViewType(newViewType: HomeFriendsViewType) {
        _state.update { it.copy(friendsViewType = newViewType) }
    }

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
