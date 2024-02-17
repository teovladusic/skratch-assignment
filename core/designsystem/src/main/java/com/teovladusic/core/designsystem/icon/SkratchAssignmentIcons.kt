package com.teovladusic.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.teovladusic.core.designsystem.R

object SkratchAssignmentIcons {
    val map: Painter
        @Composable get() = painterResource(id = R.drawable.ic_map)

    val list: Painter
        @Composable get() = painterResource(id = R.drawable.ic_list)

    val check: ImageVector
        @Composable get() = Icons.Default.Check
}