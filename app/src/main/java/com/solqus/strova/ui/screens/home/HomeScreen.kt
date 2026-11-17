
package com.solqus.strova.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    link: String,
    queuedCount: Int,
    onLinkChange: (String) -> Unit,
    onAdd: () -> Unit,
    onOpenQueue: () -> Unit
) {
    var revealStage by remember {
        mutableStateOf(0)
    }

    LaunchedEffect(Unit) {
        revealStage = 1
        delay(90)
        revealStage = 2
        delay(120)
        revealStage = 3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        AnimatedVisibility(
            visible = revealStage >= 1,
            enter = fadeIn(tween(400)) + slideInVertically(
                initialOffsetY = { it / 8 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            BrandHeader()
        }

        AnimatedVisibility(
            visible = revealStage >= 2,
            enter = fadeIn(tween(450)) +
                    slideInVertically(
                        initialOffsetY = { it / 10 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ) +
                    scaleIn(
                        initialScale = 0.97f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    )
        ) {
            ImportCard(
                link = link,
                onLinkChange = onLinkChange,
                onAdd = onAdd
            )
        }

        AnimatedVisibility(
            visible = revealStage >= 3,
            enter = fadeIn(tween(450)) + slideInVertically(
                initialOffsetY = { it / 8 },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QueueSummary(
                    count = queuedCount,
                    onClick = onOpenQueue
                )

                RecentActivity()
            }
        }
    }
}

@Composable
private fun BrandHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = RoundedCornerShape(19.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "STROVA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Music, kept simple.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ImportCard(
    link: String,
    onLinkChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    val foreground = MaterialTheme.colorScheme.onSecondaryContainer

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 32.dp,
            topEnd = 32.dp,
            bottomEnd = 32.dp,
            bottomStart = 12.dp
        ),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f)
                ) {
                    Text(
                        text = "FIRST PROVIDER",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = foreground,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = "TIDAL",
                    style = MaterialTheme.typography.labelLarge,
                    color = foreground,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Bring the music\ninto Strova.",
                    style = MaterialTheme.typography.displaySmall,
                    color = foreground
                )

                Text(
                    text = "Start with a link. We'll take it from there.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = foreground.copy(alpha = 0.82f)
                )
            }

            EqualizerGraphic(
                color = foreground.copy(alpha = 0.55f)
            )

            OutlinedTextField(
                value = link,
                onValueChange = onLinkChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                label = {
                    Text("Tidal link")
                },
                placeholder = {
                    Text("Paste a music URL")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Link,
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        if (link.isNotBlank()) onAdd()
                    }
                )
            )

            FilledTonalButton(
                onClick = onAdd,
                enabled = link.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                AnimatedContent(
                    targetState = link.isBlank(),
                    transitionSpec = {
                        fadeIn(tween(160)) togetherWith
                                fadeOut(tween(100))
                    },
                    label = "import_button_label"
                ) { isEmpty ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEmpty) {
                                "Paste a link to continue"
                            } else {
                                "Add to queue"
                            }
                        )

                        Spacer(Modifier.width(10.dp))

                        Icon(
                            imageVector = if (isEmpty) {
                                Icons.Filled.Link
                            } else {
                                Icons.Filled.ArrowForward
                            },
                            contentDescription = null
                        )
                    }
                }
            }

            Text(
                text = "Tidal is the first provider we're building around.",
                style = MaterialTheme.typography.labelMedium,
                color = foreground.copy(alpha = 0.75f)
            )
        }
    }
}

@Composable
private fun EqualizerGraphic(
    color: Color
) {
    val heights = listOf(
        0.30f, 0.55f, 0.82f, 0.48f,
        0.95f, 0.65f, 0.38f, 0.72f,
        0.45f, 0.88f, 0.58f, 0.35f,
        0.68f, 0.92f, 0.42f, 0.62f,
        0.32f, 0.78f, 0.50f, 0.36f
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
    ) {
        drawEqualizer(
            heights = heights,
            color = color
        )
    }
}

private fun DrawScope.drawEqualizer(
    heights: List<Float>,
    color: Color
) {
    val barWidth = 4.dp.toPx()
    val gap = 5.dp.toPx()
    val totalWidth =
        heights.size * barWidth + (heights.size - 1) * gap

    val startX = size.width - totalWidth
    val centerY = size.height / 2f

    heights.forEachIndexed { index, factor ->
        val barHeight = size.height * factor

        drawRoundRect(
            color = color,
            topLeft = Offset(
                x = startX + index * (barWidth + gap),
                y = centerY - barHeight / 2f
            ),
            size = Size(
                width = barWidth,
                height = barHeight
            ),
            cornerRadius = CornerRadius(
                x = barWidth,
                y = barWidth
            )
        )
    }
}

@Composable
private fun QueueSummary(
    count: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Your queue",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "View pending items",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    (
                            fadeIn(tween(180)) +
                                    slideInVertically { it / 2 }
                            ) togetherWith (
                            fadeOut(tween(120)) +
                                    slideOutVertically { -it / 2 }
                            )
                },
                label = "queue_count"
            ) { value ->
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun RecentActivity() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "RECENT ACTIVITY",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.LibraryMusic,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Your story starts here",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = "Your recent activity will appear here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
