package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodySmallTextStyle
import id.andreasmlbngaol.studio.theme.captionTextStyle
import id.andreasmlbngaol.studio.util.SocialLinks
import id.andreasmlbngaol.studio.util.openUrl
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import studio.shared.generated.resources.Res
import studio.shared.generated.resources.icon_gmail
import studio.shared.generated.resources.icon_github
import studio.shared.generated.resources.icon_instagram
import studio.shared.generated.resources.icon_linkedin

@Composable
fun SocialButton(
    icon: DrawableResource,
    label: String,
    url: String,
    size: Dp,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .clickable { openUrl(url) },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier.size(size * 0.5f),
            colorFilter = ColorFilter.tint(tint),
        )
    }
}

@Composable
fun SocialRow(
    size: Dp,
    spacing: Dp,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
        SocialButton(Res.drawable.icon_github, "GitHub", SocialLinks.github, size, tint)
        SocialButton(Res.drawable.icon_linkedin, "LinkedIn", SocialLinks.linkedin, size, tint)
        SocialButton(Res.drawable.icon_instagram, "Instagram", SocialLinks.instagram, size, tint)
        SocialButton(Res.drawable.icon_gmail, "Email", SocialLinks.mailto(), size, tint)
    }
}

@Composable
fun SiteFooter(compact: Boolean) {
    val strings = LocalAppConfig.current.strings
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 20.dp else 64.dp,
                vertical = if (compact) 24.dp else 40.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = strings.footerCopyright,
            style = if (compact) captionTextStyle() else bodySmallTextStyle(false),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.weight(1f))
        SocialRow(
            size = if (compact) 32.dp else 40.dp,
            spacing = if (compact) 8.dp else 32.dp,
        )
    }
}
