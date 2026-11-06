package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.theme.sectionTitleTextStyle

@Composable
fun SectionHeader(
    title: String,
    compact: Boolean,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        androidx.compose.material3.Text(
            text = title,
            style = sectionTitleTextStyle(compact),
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.weight(1f))
        action?.invoke()
    }
}
