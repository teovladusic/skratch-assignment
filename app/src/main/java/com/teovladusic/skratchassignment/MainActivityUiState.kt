package com.teovladusic.skratchassignment

import com.teovladusic.core.common.UiText

data class MainActivityUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)