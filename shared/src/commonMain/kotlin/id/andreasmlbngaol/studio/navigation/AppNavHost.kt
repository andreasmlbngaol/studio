package id.andreasmlbngaol.studio.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import id.andreasmlbngaol.studio.ui.screens.AboutScreen
import id.andreasmlbngaol.studio.ui.screens.ContactScreen
import id.andreasmlbngaol.studio.ui.screens.HomeScreen
import id.andreasmlbngaol.studio.ui.screens.ProjectDetailScreen
import id.andreasmlbngaol.studio.ui.screens.WorkScreen

private const val TRANSITION_MILLIS = 420

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { fadeIn(tween(TRANSITION_MILLIS)) },
        exitTransition = { fadeOut(tween(TRANSITION_MILLIS)) },
        popEnterTransition = { fadeIn(tween(TRANSITION_MILLIS)) },
        popExitTransition = { fadeOut(tween(TRANSITION_MILLIS)) },
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigate = navController::navigateTopLevel,
                onOpenProject = { navController.navigate(ProjectRoute(it)) },
            )
        }
        composable<WorkRoute> {
            WorkScreen(
                onOpenProject = { navController.navigate(ProjectRoute(it)) },
            )
        }
        composable<AboutRoute> {
            AboutScreen()
        }
        composable<ContactRoute> {
            ContactScreen()
        }
        composable<ProjectRoute> { entry ->
            val route = entry.toRoute<ProjectRoute>()
            ProjectDetailScreen(
                slug = route.slug,
                onNavigate = navController::navigateTopLevel,
                onBack = navController::backToWork,
                onOpenProject = { navController.navigate(ProjectRoute(it)) },
            )
        }
    }
}
