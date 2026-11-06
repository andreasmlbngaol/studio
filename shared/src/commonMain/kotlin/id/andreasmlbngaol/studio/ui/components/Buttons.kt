package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig

@Composable
fun HireMeButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shapes = ButtonDefaults.shapes(),
        modifier = modifier.height(40.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(),
    ) {
        Icon(Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text = LocalAppConfig.current.strings.hireMe,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
        )
    }
}

@Composable
fun CtaButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    filled: Boolean,
    modifier: Modifier = Modifier,
    height: Dp = 56.dp,
    dense: Boolean = false,
) {
    val content: @Composable RowScope.() -> Unit = {
        Icon(icon, contentDescription = null, modifier = Modifier.size(if (dense) 20.dp else 24.dp))
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = if (dense) MaterialTheme.typography.labelLarge else MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
    val padding = PaddingValues(horizontal = if (dense) 16.dp else 24.dp)
    if (filled) {
        Button(
            onClick = onClick,
            shapes = ButtonDefaults.shapes(),
            modifier = modifier.height(height),
            contentPadding = padding,
            content = content,
        )
    } else {
        FilledTonalButton(
            onClick = onClick,
            shapes = ButtonDefaults.shapes(),
            modifier = modifier.height(height),
            contentPadding = padding,
            content = content,
        )
    }
}
