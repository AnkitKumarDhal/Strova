package com.solqus.strova.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.solqus.strova.ui.components.StrovaNavigationBar
import com.solqus.strova.ui.navigation.StrovaDestination
import com.solqus.strova.ui.screens.home.HomeScreen
import com.solqus.strova.ui.screens.library.LibraryScreen
import com.solqus.strova.ui.screens.queue.QueueScreen
import com.solqus.strova.ui.screens.settings.SettingsScreen
import kotlinx.coroutines.launch

@Composable
fun StrovaApp(viewModel: StrovaViewModel = viewModel()) {
    var selectedName by rememberSaveable {
        mutableStateOf(StrovaDestination.Home.name)
    }

    val linkInput by viewModel.linkInput.collectAsStateWithLifecycle()
    val queue by viewModel.queue.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                UiEvent.OpenQueue -> selectedName = StrovaDestination.Queue.name
                is UiEvent.Message -> launch {
                    snackbarHostState.showSnackbar(event.text)
                }
            }
        }
    }

    val selectedDestination = StrovaDestination.valueOf(selectedName)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            StrovaNavigationBar(
                selectedDestination = selectedDestination,
                onDestinationSelected = { selectedName = it.name }
            )
        }
    ) { innerPadding ->

        AnimatedContent(
            targetState = selectedDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            transitionSpec = {
                val direction =
                    if (targetState.ordinal >= initialState.ordinal) 1 else -1

                (
                        fadeIn(animationSpec = tween(250)) +
                                slideInHorizontally(
                                    animationSpec = tween(250),
                                    initialOffsetX = { direction * it / 10 }
                                )
                        ) togetherWith (
                        fadeOut(animationSpec = tween(180)) +
                                slideOutHorizontally(
                                    animationSpec = tween(180),
                                    targetOffsetX = { -direction * it / 10 }
                                )
                        )
            },
            label = "screen_transition"
        ) { destination ->

            when (destination) {
                StrovaDestination.Home -> HomeScreen(
                    link = linkInput,
                    queuedCount = queue.size,
                    onLinkChange = viewModel::onLinkChange,
                    onAdd = viewModel::addLink,
                    onOpenQueue = { selectedName = StrovaDestination.Queue.name }
                )

                StrovaDestination.Queue -> QueueScreen(
                    queue = queue,
                    onRemove = viewModel::remove,
                    onClearFinished = viewModel::clearFinished
                )

                StrovaDestination.Library -> LibraryScreen()

                StrovaDestination.Settings -> SettingsScreen()
            }
        }
    }
}