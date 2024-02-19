package com.teovladusic.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkratchSheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(width = 56.dp, height = 5.dp)
            .background(
                MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(100.dp)
            )
    )
}