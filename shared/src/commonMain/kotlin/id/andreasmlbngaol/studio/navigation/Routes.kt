package id.andreasmlbngaol.studio.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("home")
data object HomeRoute

@Serializable
@SerialName("work")
data object WorkRoute

@Serializable
@SerialName("about")
data object AboutRoute

@Serializable
@SerialName("contact")
data object ContactRoute

@Serializable
@SerialName("project")
data class ProjectRoute(val slug: String)

enum class TopLevel {
    Home,
    Work,
    About,
    Contact,
}
