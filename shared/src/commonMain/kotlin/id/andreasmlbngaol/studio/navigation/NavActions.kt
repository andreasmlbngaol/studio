package id.andreasmlbngaol.studio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.navigateTopLevel(destination: TopLevel) {
    val route: Any = when (destination) {
        TopLevel.Home -> HomeRoute
        TopLevel.Work -> WorkRoute
        TopLevel.About -> AboutRoute
        TopLevel.Contact -> ContactRoute
    }
    val pattern = when (destination) {
        TopLevel.Home -> "home"
        TopLevel.Work -> "work"
        TopLevel.About -> "about"
        TopLevel.Contact -> "contact"
    }
    if (currentDestination?.route == pattern) return
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { inclusive = false }
        launchSingleTop = true
    }
}

fun NavController.backToWork() {
    navigate(WorkRoute) {
        popUpTo(graph.findStartDestination().id) { inclusive = false }
        launchSingleTop = true
    }
}
