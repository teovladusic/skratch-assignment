package com.teovladusic.skratchassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.teovladusic.core.common.R
import com.teovladusic.core.designsystem.components.SimpleErrorDialog
import com.teovladusic.core.designsystem.components.SkratchNumberField
import com.teovladusic.core.designsystem.icon.SkratchAssignmentIcons
import com.teovladusic.core.designsystem.theme.SkratchAssignmentTheme
import com.teovladusic.core.designsystem.theme.title1Medium
import com.teovladusic.core.ui.modifier.clickableWithBackground
import com.teovladusic.friends_list_view.navigation.navigateToFriendsList
import com.teovladusic.friends_map_view.navigation.navigateToFriendsMap
import com.teovladusic.skratchassignment.navigation.SkratchAssignmentNavHost
import com.teovladusic.skratchassignment.navigation.TopLevelDestination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkratchAssignmentTheme {
                // A surface container using the 'background' color from the theme
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)
                val backStackEntry = navController.currentBackStackEntryAsState()

                val friendsData by viewModel.friendsData.collectAsStateWithLifecycle()
                val state by viewModel.state.collectAsStateWithLifecycle()

                state.errorMessage?.let {
                    SimpleErrorDialog(
                        title = stringResource(id = R.string.error),
                        text = it.getString(this),
                        onDismissRequest = viewModel::dismissError
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SkratchAssignmentNavHost(bottomSheetNavigator, navController)

                        var showNumberOfUsersField by rememberSaveable {
                            mutableStateOf(false)
                        }

                        if (backStackEntry.value?.destination.isAnyTopLevelDestinationInHierarchy()) {
                            TabsSwitcher(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(horizontal = 24.dp, vertical = 32.dp),
                                currentDestination = backStackEntry.value?.destination,
                                onNavigateToDestination = {
                                    navController.navigateToTopLevelDestination(it)
                                }
                            )

                            FriendsCount(
                                count = friendsData.friendsCount,
                                showNumberOfUsersField = showNumberOfUsersField,
                                onButtonClick = { value ->
                                    if (showNumberOfUsersField) {
                                        value?.let { viewModel.onFriendsCountChange(it) }
                                    }

                                    showNumberOfUsersField = !showNumberOfUsersField
                                },
                            )
                        }

                        LoadingIndicator(isLoading = state.isLoading)
                    }
                }
            }
        }
    }

    private fun NavController.navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to  avoid building up a large stack of destinations
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }

            // Avoid multiple copies of the same destination when reselecting the same item
            launchSingleTop = true

            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.FriendsMap -> navigateToFriendsMap(topLevelNavOptions)
            TopLevelDestination.FriendsList -> navigateToFriendsList(topLevelNavOptions)
        }
    }
}

@Composable
private fun LoadingIndicator(isLoading: Boolean) {
    AnimatedVisibility(
        visible = isLoading,
        enter = slideInVertically(initialOffsetY = { fullHeight -> -fullHeight }),
        exit = slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight })
    ) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_lottie))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
            )
        }
    }
}

@Composable
private fun TabsSwitcher(
    modifier: Modifier,
    currentDestination: NavDestination?,
    onNavigateToDestination: (TopLevelDestination) -> Unit
) {
    val destinations = TopLevelDestination.entries
    val shape = RoundedCornerShape(100.dp)

    TabRow(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black,
                ambientColor = Color.Black,
                shape = shape
            )
            .size(
                width = 138.dp, // Hardcode the width (default is fillMaxWidth)
                height = 48.dp
            )
            .clip(shape),
        selectedTabIndex = -1,
        containerColor = MaterialTheme.colorScheme.secondary,
        indicator = {}, // Remove indicator
        divider = {} // Remove divider,
    ) {
        destinations.forEach { destination ->
            val isSelected = currentDestination.isTopLevelDestinationInHierarchy(destination)

            Tab(
                selected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                modifier = Modifier
                    .size(width = 62.dp, height = 34.dp)
                    .padding(horizontal = 7.dp)
                    .clip(shape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else Color.Transparent
                    ),
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.secondary,
            ) {
                Icon(
                    painter = destination.icon,
                    contentDescription = destination.name,
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

private fun NavDestination?.isAnyTopLevelDestinationInHierarchy(): Boolean {
    val destinations = TopLevelDestination.entries

    return this?.hierarchy?.any {
        destinations.any { topLevelDestination ->
            it.route?.contains(topLevelDestination.name, true) ?: false
        }
    } ?: false
}

@Composable
private fun BoxScope.FriendsCount(
    count: Int,
    showNumberOfUsersField: Boolean,
    onButtonClick: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .align(Alignment.BottomEnd)
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
            val int = value.toIntOrNull() ?: MainActivityViewModel.DEFAULT_FRIENDS_COUNT
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
                color = MaterialTheme.colorScheme.onSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}