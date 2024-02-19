package com.teovladusic.core.ui.extension

import com.teovladusic.core.common.R
import com.teovladusic.core.common.UiText
import com.teovladusic.core.domain.model.NetworkErrorType

fun NetworkErrorType.getUiText(): UiText {
    return when (this) {
        NetworkErrorType.BadRequest -> UiText.StringResource(R.string.request_not_valid)
        NetworkErrorType.Unauthorized -> UiText.StringResource(R.string.you_dont_have_permission)
        NetworkErrorType.Forbidden -> UiText.StringResource(R.string.you_dont_have_permission)
        NetworkErrorType.NotFound -> UiText.StringResource(R.string.not_found)
    }
}