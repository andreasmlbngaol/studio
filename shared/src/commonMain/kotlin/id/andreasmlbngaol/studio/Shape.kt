package id.andreasmlbngaol.studio

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Token source: Figma collection "M3 Shape".
// extraSmall 4, small 8, medium 12, large 16, largeIncreased 20,
// extraLarge 28, extraLargeIncreased 32, extraExtraLarge 48.

/**
 * Playground slider value for the shape scale: 0 is fully sharp, [DEFAULT_SHAPE_SCALE]
 * is the designed radius, 1 is the softest.
 */
const val DEFAULT_SHAPE_SCALE = 0.5f

/** Maps the playground slider to a radius multiplier (0x at sharp, 1x at default, 1.75x at soft). */
fun shapeMultiplier(scale: Float): Float {
    val value = scale.coerceIn(0f, 1f)
    return if (value <= DEFAULT_SHAPE_SCALE) {
        value / DEFAULT_SHAPE_SCALE
    } else {
        1f + (value - DEFAULT_SHAPE_SCALE) / (1f - DEFAULT_SHAPE_SCALE) * 0.75f
    }
}

/** Builds the Material shape tokens for a radius multiplier. */
fun portfolioShapes(multiplier: Float = 1f): Shapes {
    fun radius(base: Int): RoundedCornerShape = RoundedCornerShape(base.dp * multiplier)
    return Shapes(
        extraSmall = radius(4),
        small = radius(8),
        medium = radius(12),
        large = radius(16),
        largeIncreased = radius(20),
        extraLarge = radius(28),
        extraLargeIncreased = radius(32),
        extraExtraLarge = radius(48),
    )
}

/** Radius multiplier currently in effect, so one-off radii can follow the shape scale too. */
val LocalShapeMultiplier = compositionLocalOf { 1f }

/** A radius that is not a Material token but still follows the shape scale. */
@Composable
fun scaledShape(radius: Dp): RoundedCornerShape =
    RoundedCornerShape(radius * LocalShapeMultiplier.current)

/** Top-only rounded corners that follow the shape scale. */
@Composable
fun scaledTopShape(radius: Dp): RoundedCornerShape {
    val scaled = radius * LocalShapeMultiplier.current
    return RoundedCornerShape(topStart = scaled, topEnd = scaled)
}
