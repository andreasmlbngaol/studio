package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig

@Composable
fun ThemeToggle(modifier: Modifier = Modifier) {
    val config = LocalAppConfig.current
    FilledTonalIconButton(
        onClick = config.onToggleTheme,
        shapes = IconButtonDefaults.shapes(),
        modifier = modifier.size(40.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Icon(
            imageVector = if (config.darkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            contentDescription = if (config.darkTheme) config.strings.themeLight else config.strings.themeDark,
            modifier = Modifier.size(24.dp),
        )
    }
}
