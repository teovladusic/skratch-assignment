package com.teovladusic.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun FloatingContentAnimatedVisibility(
    modifier: Modifier,
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> fullHeight }),
        content = content
    )
}