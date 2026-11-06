package id.andreasmlbngaol.studio.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun heroTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.displayLarge.copy(
    fontSize = if (compact) 63.sp else 132.sp,
    lineHeight = if (compact) 68.sp else 140.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = if (compact) (-1.5).sp else (-3).sp,
)

@Composable
fun pageTitleTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.displayLarge.copy(
    fontSize = if (compact) 40.sp else 72.sp,
    lineHeight = if (compact) 48.sp else 80.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = if (compact) (-1).sp else (-2).sp,
)

@Composable
fun sectionTitleTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.headlineLarge.copy(
    fontSize = if (compact) 28.sp else 36.sp,
    lineHeight = if (compact) 36.sp else 44.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = if (compact) (-0.5).sp else (-1).sp,
)

@Composable
fun cardTitleTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.titleLarge.copy(
    fontSize = if (compact) 20.sp else 22.sp,
    lineHeight = if (compact) 28.sp else 28.sp,
    fontWeight = if (compact) FontWeight.Medium else FontWeight.Normal,
)

@Composable
fun subtitleTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.bodyLarge.copy(
    fontSize = if (compact) 16.sp else 20.sp,
    lineHeight = if (compact) 24.sp else 28.sp,
    fontWeight = FontWeight.Normal,
)

@Composable
fun bodyTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.bodyLarge.copy(
    fontSize = if (compact) 16.sp else 18.sp,
    lineHeight = if (compact) 24.sp else 28.sp,
    fontWeight = FontWeight.Normal,
)

@Composable
fun bodySmallTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.bodyMedium.copy(
    fontSize = if (compact) 14.sp else 16.sp,
    lineHeight = if (compact) 20.sp else 24.sp,
    fontWeight = FontWeight.Normal,
)

@Composable
fun labelTextStyle(compact: Boolean): TextStyle = MaterialTheme.typography.labelLarge.copy(
    fontSize = if (compact) 12.sp else 14.sp,
    lineHeight = if (compact) 16.sp else 20.sp,
    fontWeight = FontWeight.Medium,
)

@Composable
fun captionTextStyle(): TextStyle = MaterialTheme.typography.labelMedium.copy(
    fontSize = 11.sp,
    lineHeight = 16.sp,
    fontWeight = FontWeight.Medium,
)
