package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.theme.captionTextStyle

private fun iconFor(item: TopLevel, selected: Boolean): ImageVector = when (item) {
    TopLevel.Home -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
    TopLevel.Work -> if (selected) Icons.Filled.Work else Icons.Outlined.WorkOutline
    TopLevel.About -> if (selected) Icons.Filled.Person else Icons.Outlined.Person
    TopLevel.Contact -> if (selected) Icons.Filled.Mail else Icons.Outlined.Mail
}

@Composable
fun BottomNav(selected: TopLevel, onNavigate: (TopLevel) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
            ) {
                TopLevel.entries.forEach { item ->
                    val isSelected = item == selected
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavigate(item) },
                        icon = {
                            Icon(
                                imageVector = iconFor(item, isSelected),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                            )
                        },
                        label = { Text(navLabel(item), style = captionTextStyle()) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
    }
}
