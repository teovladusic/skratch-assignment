package com.teovladusic.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.teovladusic.core.domain.model.Friend
import com.teovladusic.core.common.R
import com.teovladusic.core.designsystem.theme.calloutBook
import com.teovladusic.core.designsystem.theme.largeTitleBlack

@Composable
internal fun FriendsListView(
    friends: List<Friend>,
    nestedScrollConnection: NestedScrollConnection,
    listState: LazyListState
) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary)
            .windowInsetsPadding(insets = WindowInsets.statusBars),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        item {
            Text(
                text = stringResource(id = R.string.my_friends),
                style = MaterialTheme.typography.largeTitleBlack,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 32.dp, bottom = 20.dp)
            )
        }

        items(
            items = friends,
            key = { it.id }
        ) {
            FriendListItem(friend = it)
        }
    }
}

@Composable
private fun FriendListItem(friend: Friend) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = friend.pictureUrl,
            contentDescription = "friend",
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${friend.name} ${friend.lastname}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = friend.username,
                style = MaterialTheme.typography.calloutBook,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}