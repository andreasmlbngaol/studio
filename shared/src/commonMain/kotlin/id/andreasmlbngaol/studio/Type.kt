package id.andreasmlbngaol.studio

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import studio.shared.generated.resources.GoogleSansFlex_24pt_Bold
import studio.shared.generated.resources.GoogleSansFlex_24pt_Medium
import studio.shared.generated.resources.GoogleSansFlex_24pt_Regular
import studio.shared.generated.resources.Res

// Add Google Sans Flex (OFL) to composeResources/font/ as
// google_sans_flex_regular.ttf, _medium.ttf, _bold.ttf.
@Composable
fun portfolioFontFamily() = FontFamily(
    Font(Res.font.GoogleSansFlex_24pt_Regular, FontWeight.Normal),
    Font(Res.font.GoogleSansFlex_24pt_Medium, FontWeight.Medium),
    Font(Res.font.GoogleSansFlex_24pt_Bold, FontWeight.Bold),
//    Font(Res.font.google_sans_flex_regular, FontWeight.Normal),
//    Font(Res.font.google_sans_flex_medium, FontWeight.Medium),
//    Font(Res.font.google_sans_flex_bold, FontWeight.Bold),
)

private fun ts(f: FontFamily, size: Int, line: Int, track: Double, w: FontWeight) =
    TextStyle(fontFamily = f, fontSize = size.sp, lineHeight = line.sp, letterSpacing = track.sp, fontWeight = w)

// Token source: Figma text styles (Display ... Label). Emphasized variants use Bold.
@Composable
fun portfolioTypography(): Typography {
    val f = portfolioFontFamily()
    return Typography(
        displayLarge = ts(f, 57, 64, -0.25, FontWeight.Normal),
        displayMedium = ts(f, 45, 52, 0.0, FontWeight.Normal),
        displaySmall = ts(f, 36, 44, 0.0, FontWeight.Normal),
        headlineLarge = ts(f, 32, 40, 0.0, FontWeight.Normal),
        headlineMedium = ts(f, 28, 36, 0.0, FontWeight.Normal),
        headlineSmall = ts(f, 24, 32, 0.0, FontWeight.Normal),
        titleLarge = ts(f, 22, 28, 0.0, FontWeight.Normal),
        titleMedium = ts(f, 16, 24, 0.15, FontWeight.Medium),
        titleSmall = ts(f, 14, 20, 0.1, FontWeight.Medium),
        bodyLarge = ts(f, 16, 24, 0.5, FontWeight.Normal),
        bodyMedium = ts(f, 14, 20, 0.25, FontWeight.Normal),
        bodySmall = ts(f, 12, 16, 0.4, FontWeight.Normal),
        labelLarge = ts(f, 14, 20, 0.1, FontWeight.Medium),
        labelMedium = ts(f, 12, 16, 0.5, FontWeight.Medium),
        labelSmall = ts(f, 11, 16, 0.5, FontWeight.Medium),
        displayLargeEmphasized = ts(f, 57, 64, -0.25, FontWeight.Bold),
        displayMediumEmphasized = ts(f, 45, 52, 0.0, FontWeight.Bold),
        displaySmallEmphasized = ts(f, 36, 44, 0.0, FontWeight.Bold),
        headlineLargeEmphasized = ts(f, 32, 40, 0.0, FontWeight.Bold),
        headlineMediumEmphasized = ts(f, 28, 36, 0.0, FontWeight.Bold),
        headlineSmallEmphasized = ts(f, 24, 32, 0.0, FontWeight.Bold),
        titleLargeEmphasized = ts(f, 22, 28, 0.0, FontWeight.Bold),
        titleMediumEmphasized = ts(f, 16, 24, 0.15, FontWeight.Bold),
        titleSmallEmphasized = ts(f, 14, 20, 0.1, FontWeight.Bold),
        labelLargeEmphasized = ts(f, 14, 20, 0.1, FontWeight.Bold),
        labelMediumEmphasized = ts(f, 12, 16, 0.5, FontWeight.Bold),
        labelSmallEmphasized = ts(f, 11, 16, 0.5, FontWeight.Bold),
    )
}
