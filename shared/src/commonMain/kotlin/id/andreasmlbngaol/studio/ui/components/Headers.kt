package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.state.LocalAppConfig

@Composable
fun DesktopHeader(
    selected: TopLevel?,
    onNavigate: (TopLevel) -> Unit,
    onHire: () -> Unit,
    onBack: (() -> Unit)? = null,
    onHome: () -> Unit = {},
    showNav: Boolean = true,
    fullscreen: Boolean = false,
    fullscreenSupported: Boolean = false,
    onToggleFullscreen: () -> Unit = {},
    onOpenAppearance: () -> Unit = {},
) {
    val strings = LocalAppConfig.current.strings
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .padding(horizontal = if (showNav) 64.dp else 32.dp),
    ) {
        if (onBack != null) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledTonalIconButton(
                    onClick = onBack,
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.size(40.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.backToWork, modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = strings.backToWork,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable(onClick = onBack),
                )
            }
        } else {
            StudioBrand(
                compact = false,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable(onClick = onHome),
            )
            if (showNav) {
                ConnectedNavGroup(
                    selected = selected,
                    onNavigate = onNavigate,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            LanguageToggle()
            if (fullscreenSupported) {
                FullscreenButton(fullscreen = fullscreen, onClick = onToggleFullscreen)
            }
            AppearanceButton(onClick = onOpenAppearance)
            HireMeButton(onClick = onHire)
        }
    }
}

@Composable
fun MobileTopBar(
    title: String?,
    onBack: (() -> Unit)?,
    onHire: () -> Unit,
    onHome: () -> Unit = {},
    fullscreen: Boolean = false,
    fullscreenSupported: Boolean = false,
    onToggleFullscreen: () -> Unit = {},
    onOpenAppearance: () -> Unit = {},
) {
    val strings = LocalAppConfig.current.strings
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                shapes = IconButtonDefaults.shapes(),
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = strings.backToWork,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = title.orEmpty(),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )
            IconButton(
                onClick = onHire,
                shapes = IconButtonDefaults.shapes(),
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    Icons.Filled.Mail,
                    contentDescription = strings.hireMe,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (fullscreenSupported) {
                FullscreenButton(fullscreen = fullscreen, onClick = onToggleFullscreen)
            }
        } else {
            if (title == null) {
                StudioBrand(
                    compact = true,
                    modifier = Modifier.clickable(onClick = onHome),
                )
            } else {
                Text(
                    text = title,
                    fontSize = 28.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                )
            }
            Spacer(Modifier.weight(1f))
            LanguageToggle()
            Spacer(Modifier.width(8.dp))
            AppearanceButton(onClick = onOpenAppearance)
            if (fullscreenSupported) {
                Spacer(Modifier.width(8.dp))
                FullscreenButton(fullscreen = fullscreen, onClick = onToggleFullscreen)
            }
        }
    }
}

@Composable
private fun AppearanceButton(onClick: () -> Unit) {
    val strings = LocalAppConfig.current.strings
    FilledTonalIconButton(
        onClick = onClick,
        shapes = IconButtonDefaults.shapes(),
        modifier = Modifier.size(40.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Icon(
            imageVector = Icons.Filled.Palette,
            contentDescription = strings.appearanceOpen,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun FullscreenButton(fullscreen: Boolean, onClick: () -> Unit) {
    val strings = LocalAppConfig.current.strings
    IconButton(
        onClick = onClick,
        shapes = IconButtonDefaults.shapes(),
        modifier = Modifier.size(40.dp),
    ) {
        Icon(
            imageVector = if (fullscreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
            contentDescription = if (fullscreen) strings.fullscreenExit else strings.fullscreenEnter,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )
    }
}
