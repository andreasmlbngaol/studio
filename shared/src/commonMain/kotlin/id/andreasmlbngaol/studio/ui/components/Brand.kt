package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig

/**
 * Placeholder app mark for Studio: a primary rounded square with a four point star.
 * Replaced with the final logo asset later.
 */
@Composable
fun StudioMark(size: Dp, corner: Dp) {
    val container = MaterialTheme.colorScheme.primary
    val star = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(container),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.size(size * 0.54f)) {
            val width = this.size.width
            val height = this.size.height
            val centerX = width / 2f
            val centerY = height / 2f
            val pinch = width * 0.1f
            val path = Path().apply {
                moveTo(centerX, 0f)
                quadraticTo(centerX + pinch, centerY - pinch, width, centerY)
                quadraticTo(centerX + pinch, centerY + pinch, centerX, height)
                quadraticTo(centerX - pinch, centerY + pinch, 0f, centerY)
                quadraticTo(centerX - pinch, centerY - pinch, centerX, 0f)
                close()
            }
            drawPath(path, star)
        }
    }
}

@Composable
fun StudioBrand(compact: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 10.dp else 12.dp),
    ) {
        StudioMark(
            size = if (compact) 44.dp else 48.dp,
            corner = if (compact) 12.dp else 14.dp,
        )
        Text(
            text = LocalAppConfig.current.appName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
