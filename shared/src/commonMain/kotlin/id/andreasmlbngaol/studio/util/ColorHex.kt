package id.andreasmlbngaol.studio.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * `#RRGGBB` for opaque colors, `#AARRGGBB` when there is transparency. Settings and the
 * hex field share this so the text the user typed is the text they see again.
 */
fun Color.toHexString(): String {
    val argb = toArgb()
    val alpha = (argb ushr 24) and 0xFF
    val rgb = (argb and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()
    return if (alpha == 0xFF) "#$rgb" else "#" + alpha.toString(16).padStart(2, '0').uppercase() + rgb
}

/**
 * Parses `#RGB`, `#RRGGBB`, or `#AARRGGBB`; the leading `#` is optional.
 * Returns null when the text is not a valid hex color.
 */
fun parseHexColor(value: String): Color? {
    val hex = value.trim().removePrefix("#")
    val expanded = when (hex.length) {
        3 -> hex.map { "$it$it" }.joinToString("")
        6, 8 -> hex
        else -> return null
    }
    if (expanded.any { it.digitToIntOrNull(16) == null }) return null
    val digits = if (expanded.length == 6) "FF$expanded" else expanded
    val argb = digits.toLongOrNull(16) ?: return null
    return Color(argb.toInt())
}
