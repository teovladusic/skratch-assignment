package com.teovladusic.core.designsystem.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import com.teovladusic.core.designsystem.theme.title1Medium
import com.teovladusic.core.designsystem.theme.title2Book

@Composable
fun SkratchNumberField(
    modifier: Modifier,
    value: TextFieldValue,
    keyboardType: KeyboardType = KeyboardType.Number,
    placeholder: String? = null,
    onDone: () -> Unit = {},
    onFocus: (Boolean) -> Unit = {},
    onValueChange: (TextFieldValue) -> Unit
) {
    TextField(
        modifier = modifier
            .onFocusChanged { onFocus(it.hasFocus) },
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        placeholder = {
            placeholder?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.title2Book,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }
        },
        textStyle = MaterialTheme.typography.title1Medium.copy(textAlign = TextAlign.End),
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            errorContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        singleLine = true
    )
}