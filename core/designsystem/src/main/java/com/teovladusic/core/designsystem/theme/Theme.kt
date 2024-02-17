package com.teovladusic.core.designsystem.theme

import android.app.Activity
import android.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NewPrimary,
    secondary = SkratchWhite,
    tertiary = SkratchWhite,
    inversePrimary = SkratchPaleBlue,
    onPrimaryContainer = SkratchPaleBlue50,
    onSecondary = SkratchNavy,
    onSecondaryContainer = SkratchText,
    surfaceBright = SkratchUltralight,
    onSurface = SkratchTextDark,
    onTertiary = SkratchNavy50
)

private val LightColorScheme = lightColorScheme(
    primary = NewPrimary,
    secondary = SkratchWhite,
    tertiary = SkratchWhite,
    inversePrimary = SkratchPaleBlue,
    onPrimaryContainer = SkratchPaleBlue50,
    onSecondary = SkratchNavy,
    onSecondaryContainer = SkratchText,
    surfaceBright = SkratchUltralight,
    onSurface = SkratchTextDark,
    onTertiary = SkratchNavy50

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun SkratchAssignmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.TRANSPARENT
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SkratchTypography,
        content = content
    )
}