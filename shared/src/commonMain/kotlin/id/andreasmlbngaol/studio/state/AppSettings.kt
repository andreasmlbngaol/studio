package id.andreasmlbngaol.studio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.russhwolf.settings.Settings
import id.andreasmlbngaol.studio.data.Language
import id.andreasmlbngaol.studio.data.ThemeMode

class AppSettings(
    private val settings: Settings = Settings(),
) {
    var themeMode by mutableStateOf(
        ThemeMode.entries.firstOrNull { it.name == settings.getStringOrNull(KEY_THEME) } ?: ThemeMode.System,
    )
        private set

    var language by mutableStateOf(
        Language.entries.firstOrNull { it.name == settings.getStringOrNull(KEY_LANGUAGE) } ?: Language.EN,
    )
        private set

    fun setThemeMode(mode: ThemeMode) {
        themeMode = mode
        settings.putString(KEY_THEME, mode.name)
    }

    fun setLanguage(language: Language) {
        this.language = language
        settings.putString(KEY_LANGUAGE, language.name)
    }

    fun toggleTheme(currentlyDark: Boolean) {
        setThemeMode(if (currentlyDark) ThemeMode.Light else ThemeMode.Dark)
    }

    fun toggleLanguage() {
        setLanguage(if (language == Language.EN) Language.ID else Language.EN)
    }

    private companion object {
        const val KEY_THEME = "studio.theme"
        const val KEY_LANGUAGE = "studio.language"
    }
}

@Composable
fun rememberAppSettings(): AppSettings = remember { AppSettings() }
