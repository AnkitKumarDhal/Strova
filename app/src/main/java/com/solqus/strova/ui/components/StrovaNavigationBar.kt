package com.solqus.strova.ui.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.solqus.strova.ui.navigation.StrovaDestination

@Composable
fun StrovaNavigationBar(
    selectedDestination: StrovaDestination,
    onDestinationSelected: (StrovaDestination) -> Unit
) {
    NavigationBar {
        StrovaDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination,
                onClick = {
                    onDestinationSelected(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                label = {
                    Text(destination.label)
                }
            )
        }
    }
}
