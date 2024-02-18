package com.teovladusic.friend_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.teovladusic.core.common.extension.format
import com.teovladusic.core.designsystem.R
import com.teovladusic.core.designsystem.components.SkratchSheetDragHandle
import com.teovladusic.core.designsystem.theme.bodyBook
import com.teovladusic.core.designsystem.theme.footnoteBook
import com.teovladusic.core.designsystem.theme.title1Bold
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.ui.AndroidIntentLauncher

@Composable
internal fun FriendDetailsRoute(viewModel: FriendDetailsViewModel = hiltViewModel()) {
    FriendDetailsSheet(viewModel)
}

@Composable
internal fun FriendDetailsSheet(viewModel: FriendDetailsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkratchSheetDragHandle()

        Spacer(modifier = Modifier.height(24.dp))

        val friend = state.friend

        if (friend == null) {
            Text(text = "An error occurred")
        } else {
            FriendDetails(friend)
        }
    }
}

@Composable
private fun FriendDetails(friend: Friend) {
    val context = LocalContext.current

    FriendImage(friend.pictureUrl)

    Spacer(modifier = Modifier.height(16.dp))

    FriendNameUsername(friend = friend)

    Spacer(modifier = Modifier.height(24.dp))

    FriendInfoContainer {
        FriendInformationItem(
            icon = painterResource(id = R.drawable.ic_balloon),
            text = "${friend.gender.replaceFirstChar { it.uppercase() }} ${friend.age}",
            label = friend.birthday?.format()
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    FriendInfoContainer {
        FriendInformationItem(
            icon = painterResource(id = R.drawable.ic_pin),
            text = friend.address,
            label = "${friend.city}, ${friend.state}, ${friend.country}"
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    FriendInfoContainer {
        FriendInformationItem(
            icon = painterResource(id = R.drawable.ic_phone),
            text = friend.phone,
            onClick = { AndroidIntentLauncher.openDialNumber(context, friend.phone) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        FriendInformationItem(
            icon = painterResource(id = R.drawable.ic_email),
            text = friend.email,
            onClick = { AndroidIntentLauncher.openSendEmailForm(context, mailTo = friend.email) }
        )
    }

    Spacer(Modifier.height(32.dp))

    val registeredOnLabel = stringResource(id = com.teovladusic.core.common.R.string.registered_on)
    Text(
        text = "$registeredOnLabel ${friend.registeredDateTime?.format()}",
        style = MaterialTheme.typography.footnoteBook,
        color = MaterialTheme.colorScheme.onTertiary,
        modifier = Modifier
            .padding(horizontal = 8.dp) // Whole content has 16 padding, to match design add 8 more
    )

    Spacer(Modifier.height(32.dp))
}

@Composable
private fun FriendInfoContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceBright, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
private fun FriendInformationItem(
    icon: Painter,
    text: String,
    label: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyBook,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            label?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.footnoteBook,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun FriendNameUsername(friend: Friend) {
    Text(
        text = "${friend.name} ${friend.lastname}",
        style = MaterialTheme.typography.title1Bold,
        color = MaterialTheme.colorScheme.onSecondary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )

    Text(
        text = friend.username,
        style = MaterialTheme.typography.bodyBook,
        color = MaterialTheme.colorScheme.onSecondaryContainer
    )
}

@Composable
private fun FriendImage(pictureUrl: String) {
    AsyncImage(
        model = pictureUrl,
        contentDescription = "friend_image",
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}