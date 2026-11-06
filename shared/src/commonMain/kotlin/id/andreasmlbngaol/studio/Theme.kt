package id.andreasmlbngaol.studio

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.dynamicColorScheme

private const val THEME_TRANSITION_MILLIS = 480

// Shorter so dragging the playground hue slider still feels direct.
private const val SEED_TRANSITION_MILLIS = 200

/** Below this channel spread the seed carries no usable hue. */
private const val NEUTRAL_CHROMA_THRESHOLD = 0.02f

private fun Color.isNeutral(): Boolean =
    maxOf(red, green, blue) - minOf(red, green, blue) < NEUTRAL_CHROMA_THRESHOLD

/**
 * @param seedColor null keeps the exact Figma tokens; any color derives all 48 roles
 *   from that seed so contrast stays guaranteed in both light and dark.
 * @param shapeScale 0 = sharp, [DEFAULT_SHAPE_SCALE] = as designed, 1 = soft.
 */
@Composable
fun PortfolioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    seedColor: Color? = null,
    shapeScale: Float = DEFAULT_SHAPE_SCALE,
    content: @Composable () -> Unit,
) {
    val target = remember(seedColor, darkTheme) {
        when {
            seedColor == null && darkTheme -> DarkColors
            seedColor == null -> LightColors
            else -> dynamicColorScheme(
                seedColor = seedColor,
                isDark = darkTheme,
                // A neutral seed has no hue, and the HCT conversion then picks one from
                // rounding noise, which produced a random cyan page. Monochrome keeps a
                // grey seed grey.
                style = if (seedColor.isNeutral()) PaletteStyle.Monochrome else PaletteStyle.TonalSpot,
            )
        }
    }
    val multiplier = shapeMultiplier(shapeScale)
    val transitionMillis = if (seedColor == null) THEME_TRANSITION_MILLIS else SEED_TRANSITION_MILLIS

    CompositionLocalProvider(LocalShapeMultiplier provides multiplier) {
        MaterialExpressiveTheme(
            colorScheme = animatedColorScheme(target, transitionMillis),
            typography = portfolioTypography(),
            shapes = portfolioShapes(multiplier),
            motionScheme = MotionScheme.expressive(),
            content = content,
        )
    }
}

@Composable
private fun animatedColorScheme(target: ColorScheme, durationMillis: Int): ColorScheme {
    val spec = tween<Color>(
        durationMillis = durationMillis,
        easing = FastOutSlowInEasing,
    )

    @Composable
    fun animated(color: Color): Color =
        animateColorAsState(
            targetValue = color,
            animationSpec = spec,
            label = "themeColor",
        ).value

    return target.copy(
        primary = animated(target.primary),
        onPrimary = animated(target.onPrimary),
        primaryContainer = animated(target.primaryContainer),
        onPrimaryContainer = animated(target.onPrimaryContainer),
        inversePrimary = animated(target.inversePrimary),
        secondary = animated(target.secondary),
        onSecondary = animated(target.onSecondary),
        secondaryContainer = animated(target.secondaryContainer),
        onSecondaryContainer = animated(target.onSecondaryContainer),
        tertiary = animated(target.tertiary),
        onTertiary = animated(target.onTertiary),
        tertiaryContainer = animated(target.tertiaryContainer),
        onTertiaryContainer = animated(target.onTertiaryContainer),
        background = animated(target.background),
        onBackground = animated(target.onBackground),
        surface = animated(target.surface),
        onSurface = animated(target.onSurface),
        surfaceVariant = animated(target.surfaceVariant),
        onSurfaceVariant = animated(target.onSurfaceVariant),
        surfaceTint = animated(target.surfaceTint),
        inverseSurface = animated(target.inverseSurface),
        inverseOnSurface = animated(target.inverseOnSurface),
        error = animated(target.error),
        onError = animated(target.onError),
        errorContainer = animated(target.errorContainer),
        onErrorContainer = animated(target.onErrorContainer),
        outline = animated(target.outline),
        outlineVariant = animated(target.outlineVariant),
        scrim = animated(target.scrim),
        surfaceBright = animated(target.surfaceBright),
        surfaceDim = animated(target.surfaceDim),
        surfaceContainer = animated(target.surfaceContainer),
        surfaceContainerHigh = animated(target.surfaceContainerHigh),
        surfaceContainerHighest = animated(target.surfaceContainerHighest),
        surfaceContainerLow = animated(target.surfaceContainerLow),
        surfaceContainerLowest = animated(target.surfaceContainerLowest),
        primaryFixed = animated(target.primaryFixed),
        primaryFixedDim = animated(target.primaryFixedDim),
        onPrimaryFixed = animated(target.onPrimaryFixed),
        onPrimaryFixedVariant = animated(target.onPrimaryFixedVariant),
        secondaryFixed = animated(target.secondaryFixed),
        secondaryFixedDim = animated(target.secondaryFixedDim),
        onSecondaryFixed = animated(target.onSecondaryFixed),
        onSecondaryFixedVariant = animated(target.onSecondaryFixedVariant),
        tertiaryFixed = animated(target.tertiaryFixed),
        tertiaryFixedDim = animated(target.tertiaryFixedDim),
        onTertiaryFixed = animated(target.onTertiaryFixed),
        onTertiaryFixedVariant = animated(target.onTertiaryFixedVariant),
    )
}
