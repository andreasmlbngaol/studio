package id.andreasmlbngaol.studio.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
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
        AnimatedContent(
            targetState = config.darkTheme,
            transitionSpec = {
                (fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.6f))
                    .togetherWith(fadeOut(tween(160)) + scaleOut(tween(160), targetScale = 0.6f))
            },
            label = "themeIcon",
        ) { dark ->
            Icon(
                imageVector = if (dark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                contentDescription = if (dark) config.strings.themeLight else config.strings.themeDark,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}
