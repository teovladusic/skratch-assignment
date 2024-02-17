package com.teovladusic.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.teovladusic.core.common.R
import com.teovladusic.core.designsystem.components.SkratchNumberField
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons
import com.teovladusic.core.designsystem.theme.title1Medium
import com.teovladusic.core.ui.modifier.clickableWithBackground
import com.teovladusic.feature.home.HomeUiState

@Composable
internal fun BoxScope.FriendsCountContent(
    floatingContentVisible: Boolean,
    friendsCount: Int,
    showNumberOfUsersField: Boolean,
    onUserCountChange: (String) -> Unit,
    setShowNumberOfUsers: (Boolean) -> Unit
) {
    FloatingContentAnimatedVisibility(
        modifier = Modifier.align(Alignment.BottomEnd),
        visible = floatingContentVisible
    ) {
        FriendsCount(
            count = friendsCount,
            showNumberOfUsersField = showNumberOfUsersField,
            onButtonClick = { value ->
                if (showNumberOfUsersField) {
                    value?.let { onUserCountChange(it) }
                }
                setShowNumberOfUsers(!showNumberOfUsersField)
            },
        )
    }
}

@Composable
private fun FriendsCount(
    count: Int,
    showNumberOfUsersField: Boolean,
    onButtonClick: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (showNumberOfUsersField) {
                    val color = Color.Black.copy(alpha = .3f)
                    Modifier.clickableWithBackground(color, color) { onButtonClick(null) }
                } else Modifier
            ),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        var value by rememberSaveable {
            mutableStateOf(count.toString())
        }

        val resetValue = {
            val int = value.toIntOrNull() ?: HomeUiState.DEFAULT_FRIENDS_COUNT
            value = int.toString()
        }

        CountButton(
            showNumberOfUsersField = showNumberOfUsersField,
            count = count,
            onClick = {
                onButtonClick(value)
                resetValue()
            }
        )

        val focusRequester = remember { FocusRequester() }

        AnimatedVisibility(visible = showNumberOfUsersField) {
            SkratchNumberField(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .background(Color.White)
                    .padding(vertical = 8.dp) // Required padding is 24, native TextField has 16, add 8 to match design
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = stringResource(id = R.string.no_of_users),
                onDone = {
                    onButtonClick(value)
                    resetValue()
                }
            )
        }

        LaunchedEffect(key1 = showNumberOfUsersField) {
            if (showNumberOfUsersField) {
                focusRequester.requestFocus()
            }
        }
    }
}

@Composable
private fun CountButton(showNumberOfUsersField: Boolean, count: Int, onClick: () -> Unit) {
    val buttonColor by animateColorAsState(
        targetValue = if (showNumberOfUsersField) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.secondary,
        label = "box_color_animation"
    )

    ElevatedButton(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 32.dp)
            .size(48.dp),
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = buttonColor,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 12.dp,
            pressedElevation = 20.dp,
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (showNumberOfUsersField) {
            Icon(
                imageVector = SkratchAssignmentIcons.check,
                contentDescription = "confirm",
                tint = MaterialTheme.colorScheme.secondary
            )
        } else {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.title1Medium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}