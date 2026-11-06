package id.andreasmlbngaol.studio.data

import org.jetbrains.compose.resources.DrawableResource

enum class Language {
    EN,
    ID,
}

enum class ThemeMode {
    System,
    Light,
    Dark,
}

enum class ProjectCategory {
    Android,
    Backend,
    Ai,
    Web,
    FullStack,
    Desktop,
}

enum class TimelineIcon {
    Work,
    School,
    Group,
    Internship,
}

enum class DetailTagIcon {
    Android,
    Code,
    Calendar,
    Ai,
    Web,
    FullStack,
}

enum class GalleryIcon {
    PhotoLibrary,
    Image,
    AutoAwesome,
    Eco,
    Science,
    Dashboard,
    Assignment,
    TableChart,
    Insights,
}

data class DetailTag(
    val icon: DetailTagIcon,
    val label: String,
)

data class ResultStat(
    val value: String,
    val label: String,
)

data class GalleryItem(
    val icon: GalleryIcon,
    val label: String,
)

data class ProjectDetail(
    val tags: List<DetailTag>,
    val role: String,
    val timeline: String,
    val stack: String,
    val platform: String,
    val problemTitle: String,
    val problemCount: String,
    val approachTitle: String,
    val approachCount: String,
    val outcomeTitle: String,
    val outcomeCount: String,
    val body: List<String>,
    val results: List<ResultStat>,
    val gallery: List<GalleryItem>,
    val nextSlug: String?,
)

data class Project(
    val slug: String,
    val name: String,
    val cardDescription: String,
    val tableDescription: String,
    val lead: String,
    val platform: String,
    val stack: String,
    val year: String,
    val categories: List<ProjectCategory>,
    val githubUrl: String? = null,
    val liveUrl: String? = null,
    val apkUrl: String? = null,
    val highlight: Boolean = false,
    val detail: ProjectDetail? = null,
)

data class TimelineItem(
    val title: String,
    val subtitle: String,
    val year: String,
    val icon: TimelineIcon,
)

data class Skill(
    val name: String,
    val logo: DrawableResource,
    val url: String,
    val monochrome: Boolean = false,
)

data class SkillGroup(
    val title: String,
    val skills: List<Skill>,
)

data class Strings(
    val navHome: String,
    val navWork: String,
    val navAbout: String,
    val navContact: String,
    val hireMe: String,
    val themeLabel: String,
    val themeLightOption: String,
    val themeDarkOption: String,
    val themeSystem: String,
    val homeGreeting: String,
    val homeName: String,
    val homeSubtitle: String,
    val viewWork: String,
    val downloadCv: String,
    val selectedWork: String,
    val viewAll: String,
    val details: String,
    val code: String,
    val aboutTeaserTitle: String,
    val aboutTeaserBody: String,
    val readMore: String,
    val contactCtaTitle: String,
    val contactCtaBody: String,
    val contactCtaButton: String,
    val footerCopyright: String,
    val workTitle: String,
    val workSubtitle: String,
    val highlights: String,
    val moreProjects: String,
    val projectsCount: String,
    val tableNumber: String,
    val tableProject: String,
    val tablePlatform: String,
    val tableStack: String,
    val tableYear: String,
    val filterAll: String,
    val filterAndroid: String,
    val filterBackend: String,
    val filterAi: String,
    val filterWeb: String,
    val filterFullStack: String,
    val aboutTitle: String,
    val aboutSubtitle: String,
    val aboutMe: String,
    val experience: String,
    val education: String,
    val skillsTitle: String,
    val skillsSubtitle: String,
    val languages: String,
    val frameworks: String,
    val ai: String,
    val data: String,
    val tools: String,
    val contactTitle: String,
    val contactSubtitle: String,
    val sayHello: String,
    val contactBody: String,
    val formName: String,
    val formNamePlaceholder: String,
    val formEmail: String,
    val formEmailPlaceholder: String,
    val formMessage: String,
    val formMessagePlaceholder: String,
    val formSend: String,
    val formSending: String,
    val formSuccess: String,
    val formError: String,
    val formNameError: String,
    val formEmailError: String,
    val formMessageError: String,
    val backToWork: String,
    val share: String,
    val overview: String,
    val problem: String,
    val approach: String,
    val outcome: String,
    val results: String,
    val gallery: String,
    val nextProject: String,
    val moreWork: String,
    val allProjects: String,
    val role: String,
    val timeline: String,
    val stack: String,
    val platform: String,
    val downloadApk: String,
    val liveDemo: String,
    val liveApp: String,
    val sourceCode: String,
    val appearanceTitle: String,
    val appearanceOpen: String,
    val appearanceClose: String,
    val playgroundColor: String,
    val playgroundCorners: String,
    val playgroundHue: String,
    val playgroundVividness: String,
    val playgroundSurprise: String,
    val playgroundReset: String,
    val playgroundSharp: String,
    val playgroundSoft: String,
    val playgroundHex: String,
    val playgroundHexError: String,
    val fullscreenEnter: String,
    val fullscreenExit: String,
)

data class PortfolioContent(
    val strings: Strings,
    val projects: List<Project>,
    val experience: List<TimelineItem>,
    val education: List<TimelineItem>,
    val skills: List<SkillGroup>,
) {
    val highlights: List<Project> get() = projects.filter { it.highlight }
    val moreProjects: List<Project> get() = projects.filterNot { it.highlight }
    fun project(slug: String): Project? = projects.firstOrNull { it.slug == slug }
}
