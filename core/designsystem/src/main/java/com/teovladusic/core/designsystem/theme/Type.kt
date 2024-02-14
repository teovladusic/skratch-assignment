package com.teovladusic.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.teovladusic.core.designsystem.R

// Set of Material typography styles to start with
val SkratchTypography = Typography()

val Typography.footnoteBook: TextStyle
    get() = TextStyle(
        fontSize = 12.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Normal,
    )

val Typography.title1Bold: TextStyle
    get() = TextStyle(
        fontSize = 24.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Bold,
    )

val Typography.title1Medium: TextStyle
    get() = TextStyle(
        fontSize = 24.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Normal,
    )

val Typography.title2Book: TextStyle
    get() = TextStyle(
        fontSize = 20.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Normal
    )

val Typography.largeTitleBlack: TextStyle
    get() = TextStyle(
        fontSize = 32.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Black
    )

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
val Typography.bodyMedium: TextStyle
    get() = TextStyle(
        fontSize = 17.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Normal
    )

val Typography.calloutBook: TextStyle
    get() = TextStyle(
        fontSize = 15.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight.Normal
    )

val Typography.bodyBook: TextStyle
    get() = TextStyle(
        fontSize = 17.sp,
        fontFamily = CircularStdFontFamily,
        fontWeight = FontWeight(weight = 450),
    )