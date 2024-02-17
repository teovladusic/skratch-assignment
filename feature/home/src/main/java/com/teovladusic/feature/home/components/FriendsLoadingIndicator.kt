package com.teovladusic.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.teovladusic.core.common.R
import com.teovladusic.core.designsystem.theme.title2Book

@Composable
internal fun FriendsLoadingIndicator() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.friends_loading_text),
            style = MaterialTheme.typography.title2Book,
            color = MaterialTheme.colorScheme.onSecondary
        )

        val lottieComposition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.loading_lottie)
        )

        val lottieProgress by animateLottieCompositionAsState(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = lottieComposition,
            progress = lottieProgress,
            modifier = Modifier.height(60.dp)
        )
    }
}