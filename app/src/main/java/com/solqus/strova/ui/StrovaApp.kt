package com.solqus.strova.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.solqus.strova.ui.components.StrovaNavigationBar
import com.solqus.strova.ui.navigation.StrovaDestination
import com.solqus.strova.ui.screens.home.HomeScreen
import com.solqus.strova.ui.screens.library.LibraryScreen
import com.solqus.strova.ui.screens.queue.QueueScreen
import com.solqus.strova.ui.screens.settings.SettingsScreen

@Composable
fun StrovaApp() {
    var selectedName by rememberSaveable {
        mutableStateOf(StrovaDestination.Home.name)
    }

    var linkInput by rememberSaveable {
        mutableStateOf("")
    }

    var queuedLinks by remember {
        mutableStateOf(emptyList<String>())
    }

    val selectedDestination = StrovaDestination.valueOf(selectedName)

    Scaffold(
        bottomBar = {
            StrovaNavigationBar(
                selectedDestination = selectedDestination,
                onDestinationSelected = {
                    selectedName = it.name
                }
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
                StrovaDestination.Home -> {
                    HomeScreen(
                        link = linkInput,
                        queuedCount = queuedLinks.size,
                        onLinkChange = { linkInput = it },
                        onAdd = {
                            val cleanedLink = linkInput.trim()

                            if (cleanedLink.isNotEmpty()) {
                                queuedLinks = queuedLinks + cleanedLink
                                linkInput = ""
                                selectedName = StrovaDestination.Queue.name
                            }
                        },
                        onOpenQueue = {
                            selectedName = StrovaDestination.Queue.name
                        }
                    )
                }

                StrovaDestination.Queue -> {
                    QueueScreen(
                        queuedLinks = queuedLinks,
                        onRemove = { index ->
                            queuedLinks = queuedLinks.filterIndexed { i, _ ->
                                i != index
                            }
                        }
                    )
                }

                StrovaDestination.Library -> LibraryScreen()

                StrovaDestination.Settings -> SettingsScreen()
            }
        }
    }
}
