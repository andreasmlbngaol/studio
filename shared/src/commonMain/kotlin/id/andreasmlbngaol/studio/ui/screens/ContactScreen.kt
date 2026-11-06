package id.andreasmlbngaol.studio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodyTextStyle
import id.andreasmlbngaol.studio.theme.subtitleTextStyle
import id.andreasmlbngaol.studio.ui.components.ContactForm
import id.andreasmlbngaol.studio.ui.components.PageHeader
import id.andreasmlbngaol.studio.ui.components.SiteFooter
import id.andreasmlbngaol.studio.ui.components.SocialRow
import id.andreasmlbngaol.studio.ui.layout.Page
import id.andreasmlbngaol.studio.ui.layout.PortfolioPage
import id.andreasmlbngaol.studio.util.SocialLinks
import id.andreasmlbngaol.studio.util.openUrl

@Composable
fun ContactScreen() {
    val config = LocalAppConfig.current
    val strings = config.strings

    PortfolioPage { isCompact ->
        Page {
            if (!isCompact) {
                PageHeader(title = strings.contactTitle, subtitle = strings.contactSubtitle)
            }
            ContactSection(compact = isCompact)
            SiteFooter(isCompact)
        }
    }
}

@Composable
private fun ContactSection(compact: Boolean) {
    val strings = LocalAppConfig.current.strings
    if (compact) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = strings.contactBody,
                style = bodyTextStyle(true),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            ContactForm(compact = true)
            ContactDetails(compact = true, onPrimary = false)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 48.dp),
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraExtraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Row(
                    modifier = Modifier.padding(56.dp),
                    horizontalArrangement = Arrangement.spacedBy(64.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                    ) {
                        Text(
                            text = strings.sayHello,
                            fontSize = 72.sp,
                            lineHeight = 68.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Text(
                            text = strings.contactBody,
                            style = subtitleTextStyle(false),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        ContactDetails(compact = false, onPrimary = true)
                    }
                    ContactForm(
                        compact = false,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactDetails(compact: Boolean, onPrimary: Boolean) {
    val color = if (onPrimary) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val emphasis = if (onPrimary) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = SocialLinks.name,
            fontSize = 16.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.Medium,
            color = emphasis,
        )
        Text(
            text = SocialLinks.email,
            fontSize = 14.sp,
            lineHeight = 28.sp,
            color = color,
            modifier = Modifier.clickable { openUrl(SocialLinks.mailto()) },
        )
        Text(
            text = SocialLinks.location,
            fontSize = 14.sp,
            lineHeight = 28.sp,
            color = color,
        )
        SocialRow(
            size = if (compact) 40.dp else 40.dp,
            spacing = 8.dp,
            tint = color,
        )
    }
}
