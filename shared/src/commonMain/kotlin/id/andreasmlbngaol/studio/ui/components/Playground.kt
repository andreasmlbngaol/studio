package id.andreasmlbngaol.studio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.RoundedCorner
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.DEFAULT_SHAPE_SCALE
import id.andreasmlbngaol.studio.data.Strings
import id.andreasmlbngaol.studio.data.ThemeMode
import id.andreasmlbngaol.studio.scaledShape
import id.andreasmlbngaol.studio.scaledTopShape
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.util.parseHexColor
import id.andreasmlbngaol.studio.util.toHexString
import kotlin.random.Random

/** The designed Studio blue, shown in the picker even while a custom seed is active. */
private val StudioSeed = Color(0xFF415F91)

/** HSV value used for generated seeds; M3 only cares about hue and chroma anyway. */
private const val SEED_VALUE = 0.8f

/** Track height of the M3 Expressive slider. */
private val TrackHeight = 16.dp

/** Half the handle plus the gap M3 leaves between the handle and the track. */
private val TrackEdgeInset = 8.dp

private val themeOptions = listOf(ThemeMode.Light, ThemeMode.Dark, ThemeMode.System)

private data class SeedPreset(val name: String, val color: Color?)

private val seedPresets = listOf(
    SeedPreset("Studio", null),
    SeedPreset("Teal", Color(0xFF00696D)),
    SeedPreset("Sage", Color(0xFF3F6B3F)),
    SeedPreset("Amber", Color(0xFF7A5900)),
    SeedPreset("Clay", Color(0xFF8B4A3B)),
    SeedPreset("Rose", Color(0xFF9C4055)),
    SeedPreset("Plum", Color(0xFF7B4E8C)),
    SeedPreset("Slate", Color(0xFF4A5568)),
)

/**
 * In-canvas overlay: scrim plus a bottom-docked panel. Kept inside the app's own
 * composition so it needs no platform dialog support.
 */
@Composable
fun AppearanceOverlay(visible: Boolean, compact: Boolean, onDismiss: () -> Unit) {
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val maxPanelHeight = maxHeight * 0.92f
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(180)),
            exit = fadeOut(tween(140)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.24f))
                    // The scrim is the topmost hit target, so the page behind cannot be
                    // scrolled or clicked; no extra pointerInput is needed here.
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onDismiss,
                    ),
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(tween(280, easing = FastOutSlowInEasing)) { it } +
                fadeIn(tween(180)),
            exit = slideOutVertically(tween(200, easing = FastOutSlowInEasing)) { it } +
                fadeOut(tween(140)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (compact) 0.dp else 24.dp),
        ) {
            AppearancePanel(
                compact = compact,
                maxPanelHeight = maxPanelHeight,
                onClose = onDismiss,
            )
        }
    }
}

@Composable
private fun AppearancePanel(
    compact: Boolean,
    maxPanelHeight: Dp,
    onClose: () -> Unit,
) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val seed = config.accentSeed ?: StudioSeed

    // Hue and saturation are cached here instead of being read back from the seed on every
    // frame: at zero saturation the colour is grey and the hue is no longer recoverable.
    var hue by remember { mutableFloatStateOf(seed.toHsv().first) }
    var saturation by remember { mutableFloatStateOf(seed.toHsv().second) }
    var lastSeed by remember { mutableStateOf(seed) }
    // Bumped by every control except the hex field, so the field knows to drop focus and
    // follow the new value instead of keeping stale text.
    var hexRevision by remember { mutableIntStateOf(0) }

    // A seed that did not come from these controls (the hex field, or a value restored from
    // a previous session) wins over the cached hue and saturation.
    LaunchedEffect(seed) {
        if (seed != lastSeed) {
            val (h, s, _) = seed.toHsv()
            hue = h
            saturation = s
            lastSeed = seed
        }
    }

    fun commit(color: Color) {
        lastSeed = color
        hexRevision++
        config.onSetAccentSeed(color)
    }

    fun commitPreset(color: Color?) {
        val resolved = color ?: StudioSeed
        val (h, s, _) = resolved.toHsv()
        hue = h
        saturation = s
        lastSeed = resolved
        hexRevision++
        config.onSetAccentSeed(color)
    }

    Surface(
        modifier = Modifier
            // widthIn must come before fillMaxWidth, otherwise fillMaxWidth locks the
            // constraints and the cap is silently ignored.
            .widthIn(max = 720.dp)
            .fillMaxWidth()
            .heightIn(max = maxPanelHeight)
            // Swallows taps on empty panel space so they do not reach the scrim.
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {},
            ),
        shape = if (compact) scaledTopShape(28.dp) else MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = if (compact) 20.dp else 32.dp)
                .padding(top = 10.dp),
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(width = 32.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = strings.appearanceTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                IconButton(
                    onClick = onClose,
                    shapes = IconButtonDefaults.shapes(),
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = strings.appearanceClose,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            // Short viewports (landscape phones, small laptops) scroll the controls while
            // the title stays put. fill = false keeps the panel compact when it all fits.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = if (compact) 20.dp else 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                BoxWithConstraints(Modifier.fillMaxWidth()) {
                    // Two columns only once there is room: below this the colour card gets
                    // too narrow for eight swatches and the sliders become unusable.
                    val twoColumns = maxWidth >= 600.dp
                    val colorCard: @Composable (Modifier) -> Unit = { modifier ->
                        ColorCard(
                            modifier = modifier,
                            hue = hue,
                            saturation = saturation,
                            seed = seed,
                            selectedSeed = config.accentSeed,
                            hexRevision = hexRevision,
                            onPreset = ::commitPreset,
                            onHueChange = { next ->
                                hue = next
                                commit(Color.hsv(next, saturation, SEED_VALUE))
                            },
                            onSaturationChange = { next ->
                                saturation = next
                                commit(Color.hsv(hue, next, SEED_VALUE))
                            },
                        )
                    }
                    if (twoColumns) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(
                                modifier = Modifier.weight(0.75f),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                ThemeCard()
                                CornerCard()
                                // Actions sit under the short column so the sheet does not
                                // grow a row taller than the cards need.
                                AppearanceActions(
                                    modifier = Modifier.padding(top = 8.dp),
                                    onSurprise = { commitPreset(randomSeed()) },
                                    onReset = {
                                        commitPreset(null)
                                        config.onSetShapeScale(DEFAULT_SHAPE_SCALE)
                                    },
                                )
                            }
                            colorCard(Modifier.weight(1f))
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            ThemeCard()
                            colorCard(Modifier.fillMaxWidth())
                            CornerCard()
                            AppearanceActions(
                                modifier = Modifier.padding(top = 8.dp),
                                onSurprise = { commitPreset(randomSeed()) },
                                onReset = {
                                    commitPreset(null)
                                    config.onSetShapeScale(DEFAULT_SHAPE_SCALE)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

/** One self-contained block of the panel. Cards replace dividers as the separator. */
@Composable
private fun SectionCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                trailing?.invoke()
            }
            content()
        }
    }
}

@Composable
private fun ThemeControls() {
    val config = LocalAppConfig.current
    val strings = config.strings
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        themeOptions.forEachIndexed { index, mode ->
            ToggleButton(
                checked = config.themeMode == mode,
                onCheckedChange = { checked -> if (checked) config.onSetThemeMode(mode) },
                shapes = when (index) {
                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                    themeOptions.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                },
                colors = groupToggleColors(),
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(
                    text = themeOptionLabel(mode, strings),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun ThemeCard() {
    val strings = LocalAppConfig.current.strings
    SectionCard(icon = Icons.Outlined.Contrast, title = strings.themeLabel) {
        ThemeControls()
    }
}

@Composable
private fun ColorCard(
    modifier: Modifier,
    hue: Float,
    saturation: Float,
    seed: Color,
    selectedSeed: Color?,
    hexRevision: Int,
    onPreset: (Color?) -> Unit,
    onHueChange: (Float) -> Unit,
    onSaturationChange: (Float) -> Unit,
) {
    val strings = LocalAppConfig.current.strings

    SectionCard(
        modifier = modifier,
        icon = Icons.Filled.Palette,
        title = strings.playgroundColor,
    ) {
        SwatchRow(selectedSeed = selectedSeed, onPreset = onPreset)
        HexField(seed = seed, resetKey = hexRevision, modifier = Modifier.fillMaxWidth())
        LabeledSlider(
            label = strings.playgroundHue,
            value = hue / 360f,
            colors = hueColors(),
            onValueChange = { fraction -> onHueChange(fraction * 360f) },
        )
        LabeledSlider(
            label = strings.playgroundVividness,
            value = saturation,
            colors = vividnessColors(hue),
            onValueChange = onSaturationChange,
        )
    }
}

@Composable
private fun SwatchRow(
    selectedSeed: Color?,
    onPreset: (Color?) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        seedPresets.forEach { preset ->
            SeedSwatch(
                color = preset.color ?: StudioSeed,
                selected = preset.color == selectedSeed,
                label = preset.name,
                onClick = { onPreset(preset.color) },
            )
        }
    }
}

@Composable
private fun CornerCard() {
    val config = LocalAppConfig.current
    val strings = config.strings

    SectionCard(
        icon = Icons.Outlined.RoundedCorner,
        title = strings.playgroundCorners,
        trailing = {
            // Shows what the slider actually changes, so it cannot be mistaken for a
            // colour control.
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .background(MaterialTheme.colorScheme.primary, scaledShape(8.dp)),
            )
        },
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = strings.playgroundSharp,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = strings.playgroundSoft,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            ExpressiveSlider(
                value = config.shapeScale,
                onValueChange = config.onSetShapeScale,
                label = strings.playgroundCorners,
            )
        }
    }
}

@Composable
private fun AppearanceActions(
    onSurprise: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppConfig.current.strings
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilledTonalButton(onClick = onSurprise, shapes = ButtonDefaults.shapes()) {
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(text = strings.playgroundSurprise, style = MaterialTheme.typography.labelLarge)
        }
        TextButton(onClick = onReset, shapes = ButtonDefaults.shapes()) {
            Text(text = strings.playgroundReset, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun HexField(seed: Color, resetKey: Int, modifier: Modifier = Modifier) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val focusManager = LocalFocusManager.current
    var editing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(seed.toHexString()) }
    val parsed = parseHexColor(text)

    LaunchedEffect(seed, editing) {
        if (!editing) text = seed.toHexString()
    }

    // Another control moved the colour: drop focus and adopt the new value instead of
    // leaving stale text in a focused field.
    LaunchedEffect(resetKey) {
        if (resetKey > 0) {
            editing = false
            text = seed.toHexString()
            focusManager.clearFocus()
        }
    }

    val errorText: (@Composable () -> Unit)? = if (text.isNotBlank() && parsed == null) {
        { Text(strings.playgroundHexError, style = MaterialTheme.typography.labelSmall) }
    } else {
        null
    }

    OutlinedTextField(
        value = text,
        onValueChange = { raw ->
            val cleaned = raw.uppercase()
                .filterIndexed { index, char ->
                    (index == 0 && char == '#') || char in '0'..'9' || char in 'A'..'F'
                }
                .take(9)
            text = cleaned
            editing = true
            parseHexColor(cleaned)?.let(config.onSetAccentSeed)
        },
        label = { Text(strings.playgroundHex) },
        singleLine = true,
        isError = text.isNotBlank() && parsed == null,
        supportingText = errorText,
        shape = MaterialTheme.shapes.extraSmall,
        modifier = modifier.onFocusChanged { focus ->
            if (!focus.isFocused && editing) {
                editing = false
                text = (config.accentSeed ?: StudioSeed).toHexString()
            }
        },
    )
}

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    colors: List<Color>,
    onValueChange: (Float) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        GradientSlider(
            value = value,
            onValueChange = onValueChange,
            colors = colors,
            label = label,
        )
    }
}

/**
 * M3 Expressive slider (16dp track, 4x44dp handle) with a gradient track instead of the
 * active/inactive split, because the track itself is the value here.
 */
@Composable
private fun GradientSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    colors: List<Color>,
    label: String,
) {
    val coerced = value.coerceIn(0f, 1f)
    val state = remember { SliderState(value = coerced, steps = 0) }
    state.value = coerced

    Slider(
        state = state,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = label },
        track = { sliderState ->
            Canvas(Modifier.fillMaxWidth().height(TrackHeight)) {
                val inset = TrackEdgeInset.toPx()
                val gap = TrackEdgeInset.toPx()
                val inside = 2.dp.toPx()
                val radius = size.height / 2f
                val center = size.width * sliderState.coercedValueAsFraction
                val brush = Brush.horizontalGradient(
                    colors = colors,
                    startX = inset,
                    endX = size.width - inset,
                )
                drawTrackSegment(brush, inset, center - gap, radius, inside)
                drawTrackSegment(brush, center + gap, size.width - inset, inside, radius)
            }
        },
    )
}

/** Slider with the stock M3 Expressive track, handle and stop indicator. */
@Composable
private fun ExpressiveSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
) {
    val coerced = value.coerceIn(0f, 1f)
    val state = remember { SliderState(value = coerced, steps = 0) }
    state.value = coerced

    Slider(
        state = state,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = label },
    )
}

private fun DrawScope.drawTrackSegment(
    brush: Brush,
    left: Float,
    right: Float,
    leftCorner: Float,
    rightCorner: Float,
) {
    if (right <= left) return
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                left = left,
                top = 0f,
                right = right,
                bottom = size.height,
                topLeftCornerRadius = CornerRadius(leftCorner),
                topRightCornerRadius = CornerRadius(rightCorner),
                bottomRightCornerRadius = CornerRadius(rightCorner),
                bottomLeftCornerRadius = CornerRadius(leftCorner),
            ),
        )
    }
    drawPath(path, brush)
}

@Composable
private fun SeedSwatch(
    color: Color,
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .then(
                if (selected) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                } else {
                    Modifier
                },
            )
            .semantics { contentDescription = label },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(if (selected) 22.dp else 28.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = if (color.luminance() > 0.5f) Color.Black else Color.White,
                    modifier = Modifier.size(13.dp),
                )
            }
        }
    }
}

private fun themeOptionLabel(mode: ThemeMode, strings: Strings): String = when (mode) {
    ThemeMode.Light -> strings.themeLightOption
    ThemeMode.Dark -> strings.themeDarkOption
    ThemeMode.System -> strings.themeSystem
}

private fun hueColors(): List<Color> =
    listOf(0f, 60f, 120f, 180f, 240f, 300f, 360f).map { hue ->
        Color.hsv(hue % 360f, 0.85f, 0.85f)
    }

private fun vividnessColors(hue: Float): List<Color> = listOf(
    Color.hsv(hue, 0f, SEED_VALUE),
    Color.hsv(hue, 0.5f, SEED_VALUE),
    Color.hsv(hue, 1f, SEED_VALUE),
)

private fun randomSeed(): Color {
    val hues = listOf(8f, 32f, 52f, 88f, 120f, 152f, 178f, 202f, 226f, 258f, 288f, 316f, 340f)
    val saturation = Random.nextDouble(0.32, 0.85).toFloat()
    return Color.hsv(hues.random(), saturation, SEED_VALUE)
}

private fun Color.toHsv(): Triple<Float, Float, Float> {
    val max = maxOf(red, green, blue)
    val min = minOf(red, green, blue)
    val delta = max - min
    val hue = when {
        delta < 1e-6f -> 0f
        max == red -> 60f * (((green - blue) / delta) % 6f)
        max == green -> 60f * (((blue - red) / delta) + 2f)
        else -> 60f * (((red - green) / delta) + 4f)
    }
    val saturation = if (max <= 0f) 0f else delta / max
    return Triple((hue + 360f) % 360f, saturation, max)
}
