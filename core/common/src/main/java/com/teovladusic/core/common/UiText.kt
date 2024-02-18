package com.teovladusic.core.common

import android.content.Context

sealed class UiText {
    data class StringResource(val resId: Int) : UiText()
    data class Text(val value: String) : UiText()
    fun getString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(resId)
            is Text -> value
        }
    }
}
