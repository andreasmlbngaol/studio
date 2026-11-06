package id.andreasmlbngaol.studio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import id.andreasmlbngaol.studio.data.ThemeMode
import id.andreasmlbngaol.studio.data.portfolioContent
import id.andreasmlbngaol.studio.navigation.AppNavHost
import id.andreasmlbngaol.studio.navigation.ProjectRoute
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.navigation.backToWork
import id.andreasmlbngaol.studio.navigation.navigateTopLevel
import id.andreasmlbngaol.studio.state.AppConfig
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.state.LocalPageScrollController
import id.andreasmlbngaol.studio.state.PageScrollController
import id.andreasmlbngaol.studio.state.rememberAppSettings
import id.andreasmlbngaol.studio.ui.components.BottomNav
import id.andreasmlbngaol.studio.ui.components.DesktopHeader
import id.andreasmlbngaol.studio.ui.components.MobileTopBar
import id.andreasmlbngaol.studio.util.markAppReady
import id.andreasmlbngaol.studio.util.setDocumentTitle

private const val COMPACT_BREAKPOINT = 600
private const val HEADER_NAV_BREAKPOINT = 960

@Composable
fun App(onNavHostReady: suspend (NavController) -> Unit = {}) {
    val settings = rememberAppSettings()
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (settings.themeMode) {
        ThemeMode.System -> systemDark
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val compact = !windowSizeClass.isWidthAtLeastBreakpoint(COMPACT_BREAKPOINT)
    val headerNav = windowSizeClass.isWidthAtLeastBreakpoint(HEADER_NAV_BREAKPOINT)
    val content = remember(settings.language) { portfolioContent(settings.language) }
    val scrollController = remember { PageScrollController() }
    val density = LocalDensity.current
    var headerVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        withFrameNanos { }
        markAppReady()
    }

    PortfolioTheme(darkTheme = darkTheme) {
        CompositionLocalProvider(
            LocalAppConfig provides AppConfig(
                content = content,
                strings = content.strings,
                language = settings.language,
                themeMode = settings.themeMode,
                darkTheme = darkTheme,
                compact = compact,
                headerNav = headerNav,
                onSetLanguage = settings::setLanguage,
                onToggleTheme = { settings.toggleTheme(darkTheme) },
            ),
            LocalPageScrollController provides scrollController,
        ) {
            val navController = rememberNavController()
            LaunchedEffect(navController) { onNavHostReady(navController) }

            val entry by navController.currentBackStackEntryAsState()
            val segment = entry?.destination?.route?.substringBefore('/')
            val projectSlug = if (segment == "project") {
                entry?.toRoute<ProjectRoute>()?.slug
            } else {
                null
            }
            val project = projectSlug?.let(content::project)
            val selected = when (segment) {
                "home" -> TopLevel.Home
                "work" -> TopLevel.Work
                "about" -> TopLevel.About
                "contact" -> TopLevel.Contact
                else -> null
            }
            val isProject = segment == "project"
            val onHire: () -> Unit = { navController.navigateTopLevel(TopLevel.Contact) }
            val onBack: (() -> Unit)? = if (isProject) {
                { navController.backToWork() }
            } else {
                null
            }
            val title = when {
                project != null -> project.name
                isProject -> projectSlug
                segment == "work" -> content.strings.navWork
                segment == "about" -> content.strings.navAbout
                segment == "contact" -> content.strings.navContact
                else -> null
            }
            val documentTitle = when {
                project != null -> "${project.name} - Studio"
                segment == "work" -> "${content.strings.navWork} - Studio"
                segment == "about" -> "${content.strings.navAbout} - Studio"
                segment == "contact" -> "${content.strings.navContact} - Studio"
                else -> "Studio - Andreas Manatar Lumban Gaol"
            }
            LaunchedEffect(documentTitle) { setDocumentTitle(documentTitle) }

            LaunchedEffect(compact, selected) {
                headerVisible = true
                if (!compact || selected == null) return@LaunchedEffect
                val hideDelta = with(density) { 24.dp.toPx() }
                val showDelta = with(density) { 8.dp.toPx() }
                val topOffset = with(density) { 32.dp.toPx() }
                var lastOffset = scrollController.offset
                snapshotFlow { scrollController.offset to scrollController.isScrolling }
                    .collect { (offset, scrolling) ->
                        if (!scrolling) return@collect
                        val delta = offset - lastOffset
                        lastOffset = offset
                        when {
                            offset <= topOffset -> headerVisible = true
                            delta > hideDelta -> headerVisible = false
                            delta < -showDelta -> headerVisible = true
                        }
                    }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(Modifier.fillMaxSize()) {
                    if (compact) {
                        AnimatedVisibility(
                            visible = headerVisible || selected == null,
                            enter = expandVertically(animationSpec = tween(320), expandFrom = Alignment.Top),
                            exit = shrinkVertically(animationSpec = tween(320), shrinkTowards = Alignment.Top),
                        ) {
                            MobileTopBar(
                                title = title,
                                onBack = onBack,
                                onHire = onHire,
                                onHome = { navController.navigateTopLevel(TopLevel.Home) },
                            )
                        }
                    } else {
                        DesktopHeader(
                            selected = selected,
                            onNavigate = navController::navigateTopLevel,
                            onHire = onHire,
                            onBack = onBack,
                            onHome = { navController.navigateTopLevel(TopLevel.Home) },
                            showNav = headerNav,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) {
                        AppNavHost(navController = navController)
                    }
                    if (!headerNav && selected != null) {
                        BottomNav(
                            selected = selected,
                            onNavigate = navController::navigateTopLevel,
                        )
                    }
                }
            }
        }
    }
}
