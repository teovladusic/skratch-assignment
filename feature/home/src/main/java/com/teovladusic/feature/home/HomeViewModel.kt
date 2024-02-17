package com.teovladusic.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.teovladusic.core.domain.model.Friend
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

    private var allFriends = listOf<Friend>()

    // Check if good enough
    var mapCameraState = provideDefaultCameraState()
        private set

    init {
        val friendsCount = _state.value.friendsCount
        fetchFriends(friendsCount)
    }

    private fun fetchFriends(friendsCount: Int) {
        _state.update { it.copy(isFriendsLoading = true) }

        viewModelScope.launch {
            when (val result = getFriendsUseCase(friendsCount)) {
                is Result.Failure -> _state.update {// todo: do this
                    it.copy(isFriendsLoading = false)
                }

                is Result.Success -> _state.update { state ->
                    allFriends = result.value
                    state.copy(friends = result.value, isFriendsLoading = false)
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

    fun onUserCountChange(count: String) {
        val friendsCount = count.toIntOrNull() ?: HomeUiState.DEFAULT_FRIENDS_COUNT
        _state.update { it.copy(friendsCount = friendsCount) }
        fetchFriends(friendsCount)
    }
}

private fun provideDefaultCameraState(): CameraState = CameraState(
    AMSTERDAM,
    EdgeInsets(0.0, 0.0, 0.0, 0.0),
    DEFAULT_ZOOM,
    0.0,
    0.0,
)

private const val AMSTERDAM_LONG = 4.897070
private const val AMSTERDAM_LAT = 52.377956
private const val DEFAULT_ZOOM = 10.0

private val AMSTERDAM = Point.fromLngLat(AMSTERDAM_LONG, AMSTERDAM_LAT)
