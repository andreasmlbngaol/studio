package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.data.Language
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.state.LocalAppConfig

@Composable
fun groupToggleColors() = ToggleButtonDefaults.colors(
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    checkedContainerColor = MaterialTheme.colorScheme.primary,
    checkedContentColor = MaterialTheme.colorScheme.onPrimary,
)

@Composable
fun navLabel(item: TopLevel): String {
    val strings = LocalAppConfig.current.strings
    return when (item) {
        TopLevel.Home -> strings.navHome
        TopLevel.Work -> strings.navWork
        TopLevel.About -> strings.navAbout
        TopLevel.Contact -> strings.navContact
    }
}

@Composable
fun ConnectedNavGroup(
    selected: TopLevel?,
    onNavigate: (TopLevel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = TopLevel.entries
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        items.forEachIndexed { index, item ->
            ToggleButton(
                checked = item == selected,
                onCheckedChange = { checked -> if (checked) onNavigate(item) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    items.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                colors = groupToggleColors(),
                modifier = Modifier.height(40.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
            ) {
                Text(navLabel(item), style = MaterialTheme.typography.labelLarge, maxLines = 1)
            }
        }
    }
}

@Composable
fun LanguageToggle(modifier: Modifier = Modifier) {
    val config = LocalAppConfig.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        Language.entries.forEachIndexed { index, language ->
            ToggleButton(
                checked = config.language == language,
                onCheckedChange = { checked -> if (checked) config.onSetLanguage(language) },
                shapes = if (index == 0) {
                    ButtonGroupDefaults.connectedLeadingButtonShapes()
                } else {
                    ButtonGroupDefaults.connectedTrailingButtonShapes()
                },
                colors = groupToggleColors(),
                modifier = Modifier.size(width = 44.dp, height = 40.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(language.name, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
