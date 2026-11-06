package id.andreasmlbngaol.studio.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig

/**
 * Studio mark: a grid of selected work where the chosen tile opens into a
 * bracket pair. Built on a 160 unit grid so it stays crisp at any size.
 */
@Composable
fun StudioMark(size: Dp, corner: Dp) {
    val container = MaterialTheme.colorScheme.primary
    val ink = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .background(container),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val unit = this.size.width / 160f
            val soft = ink.copy(alpha = 0.26f)
            val cell = 56f * unit
            val cellRadius = CornerRadius(16f * unit)
            fun tile(x: Float, y: Float) {
                drawRoundRect(
                    color = soft,
                    topLeft = Offset(x * unit, y * unit),
                    size = Size(cell, cell),
                    cornerRadius = cellRadius,
                )
            }
            tile(18f, 18f)
            tile(86f, 18f)
            tile(18f, 86f)

            val bracket = Stroke(
                width = 17f * unit,
                cap = StrokeCap.Butt,
                join = StrokeJoin.Round,
            )
            val left = Path().apply {
                moveTo(110f * unit, 94.5f * unit)
                lineTo(94.5f * unit, 114f * unit)
                lineTo(110f * unit, 133.5f * unit)
            }
            val right = Path().apply {
                moveTo(118f * unit, 94.5f * unit)
                lineTo(133.5f * unit, 114f * unit)
                lineTo(118f * unit, 133.5f * unit)
            }
            drawPath(left, ink, style = bracket)
            drawPath(right, ink, style = bracket)
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
