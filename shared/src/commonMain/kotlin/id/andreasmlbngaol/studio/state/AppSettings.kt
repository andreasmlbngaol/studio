package id.andreasmlbngaol.studio.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.russhwolf.settings.Settings
import id.andreasmlbngaol.studio.DEFAULT_SHAPE_SCALE
import id.andreasmlbngaol.studio.data.Language
import id.andreasmlbngaol.studio.data.ThemeMode
import id.andreasmlbngaol.studio.util.parseHexColor
import id.andreasmlbngaol.studio.util.toHexString

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

    /** Playground seed color. null means the designed Studio palette. */
    var accentSeed by mutableStateOf(settings.getStringOrNull(KEY_ACCENT)?.let(::parseHexColor))
        private set

    /** Playground shape scale: 0 = sharp, [DEFAULT_SHAPE_SCALE] = designed, 1 = soft. */
    var shapeScale by mutableStateOf(
        settings.getStringOrNull(KEY_SHAPE)
            ?.toFloatOrNull()
            ?.coerceIn(0f, 1f)
            ?: DEFAULT_SHAPE_SCALE,
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

    fun setAccentSeed(color: Color?) {
        accentSeed = color
        if (color == null) {
            settings.remove(KEY_ACCENT)
        } else {
            settings.putString(KEY_ACCENT, color.toHexString())
        }
    }

    fun setShapeScale(scale: Float) {
        val value = scale.coerceIn(0f, 1f)
        shapeScale = value
        settings.putString(KEY_SHAPE, value.toString())
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
        const val KEY_ACCENT = "studio.accent"
        const val KEY_SHAPE = "studio.shape"
    }
}

@Composable
fun rememberAppSettings(): AppSettings = remember { AppSettings() }
