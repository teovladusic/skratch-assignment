package com.teovladusic.core.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun SimpleErrorDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = text) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier.padding(
                    bottom = 16.dp,
                    end = 16.dp
                )
            ) { Text(text = stringResource(id = android.R.string.ok).uppercase()) }
        }
    )
}