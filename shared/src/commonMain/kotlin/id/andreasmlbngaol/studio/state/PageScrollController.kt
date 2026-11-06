package id.andreasmlbngaol.studio.state

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Reports the active page scroll offset to the app shell. */
class PageScrollController {
    var offset by mutableIntStateOf(0)
        private set
    var isScrolling by mutableStateOf(false)
        private set

    fun update(offset: Int, isScrolling: Boolean) {
        this.offset = offset
        this.isScrolling = isScrolling
    }
}

val LocalPageScrollController = compositionLocalOf<PageScrollController> {
    error("PageScrollController has not been provided")
}
