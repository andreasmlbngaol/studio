package id.andreasmlbngaol.studio.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.state.LocalPageScrollController
import kotlinx.coroutines.flow.collect

@Composable
fun PortfolioPage(content: @Composable ColumnScope.(compact: Boolean) -> Unit) {
    val compact = LocalAppConfig.current.compact
    val scrollState = rememberScrollState()
    val scrollController = LocalPageScrollController.current
    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.value to scrollState.isScrollInProgress }
            .collect { (offset, scrolling) -> scrollController.update(offset, scrolling) }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        content(compact)
    }
}

/** Caps the page at the 1440 dp Figma frame and centers it on wider screens. */
@Composable
fun Page(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 1440.dp),
            content = content,
        )
    }
}
