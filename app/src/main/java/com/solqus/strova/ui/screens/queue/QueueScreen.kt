package com.solqus.strova.ui.screens.queue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.solqus.strova.model.QueueItem
import com.solqus.strova.model.QueueStatus

@Composable
fun QueueScreen(
    queue: List<QueueItem>,
    onRemove: (Long) -> Unit,
    onClearFinished: () -> Unit
) {
    val hasFinished = queue.any {
        it.status == QueueStatus.Done || it.status == QueueStatus.Failed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your queue",
            style = MaterialTheme.typography.displaySmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${queue.size} items",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (hasFinished) {
                TextButton(onClick = onClearFinished) {
                    Text("Clear finished")
                }
            }
        }

        if (queue.isEmpty()) {
            Text(
                text = "Nothing in the queue yet.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(queue, key = { it.id }) { item ->
                    QueueCard(
                        item = item,
                        onRemove = { onRemove(item.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun QueueCard(
    item: QueueItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.source.label,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = item.url,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.status == QueueStatus.InProgress) {
                    LinearProgressIndicator(
                        progress = { item.progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Text(
                    text = statusText(item),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Remove from queue"
                )
            }
        }
    }
}

private fun statusText(item: QueueItem): String = when (item.status) {
    QueueStatus.Waiting -> "Waiting · demo"
    QueueStatus.InProgress -> "Downloading ${(item.progress * 100).toInt()}% · demo"
    QueueStatus.Done -> "Done · demo"
    QueueStatus.Failed -> "Failed · demo"
}