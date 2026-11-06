package id.andreasmlbngaol.studio.state

import androidx.compose.runtime.compositionLocalOf
import id.andreasmlbngaol.studio.data.Language
import id.andreasmlbngaol.studio.data.PortfolioContent
import id.andreasmlbngaol.studio.data.Strings
import id.andreasmlbngaol.studio.data.ThemeMode

data class AppConfig(
    val content: PortfolioContent,
    val strings: Strings,
    val language: Language,
    val themeMode: ThemeMode,
    val darkTheme: Boolean,
    val compact: Boolean,
    val headerNav: Boolean,
    val onSetLanguage: (Language) -> Unit,
    val onToggleTheme: () -> Unit,
) {
    val appName: String get() = "Studio"
}

val LocalAppConfig = compositionLocalOf<AppConfig> {
    error("AppConfig has not been provided")
}
