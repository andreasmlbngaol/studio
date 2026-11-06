package id.andreasmlbngaol.studio

import id.andreasmlbngaol.studio.data.Language
import id.andreasmlbngaol.studio.data.portfolioContent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SharedCommonTest {

    @Test
    fun projectsAreSplitIntoHighlightsAndMore() {
        val content = portfolioContent(Language.EN)
        assertEquals(12, content.projects.size)
        assertEquals(3, content.highlights.size)
        assertEquals(9, content.moreProjects.size)
        assertTrue(content.projects.all { !it.githubUrl.isNullOrBlank() })
        assertTrue(content.highlights.all { it.detail != null })
    }

    @Test
    fun detailChainLinksEveryHighlight() {
        val content = portfolioContent(Language.EN)
        assertEquals("tomavue", content.project("agallery")?.detail?.nextSlug)
        assertEquals("caritas-kopi", content.project("tomavue")?.detail?.nextSlug)
        assertEquals(null, content.project("caritas-kopi")?.detail?.nextSlug)
        assertNotNull(content.project("agallery")?.apkUrl)
        assertNotNull(content.project("tomavue")?.liveUrl)
    }

    @Test
    fun languageSwitchChangesCopy() {
        assertEquals("Home", portfolioContent(Language.EN).strings.navHome)
        assertEquals("Beranda", portfolioContent(Language.ID).strings.navHome)
        assertEquals("All projects", portfolioContent(Language.EN).strings.workTitle)
        assertEquals("Semua proyek", portfolioContent(Language.ID).strings.workTitle)
    }

    @Test
    fun skillsCoverEveryGroup() {
        val content = portfolioContent(Language.EN)
        assertEquals(5, content.skills.size)
        assertEquals(23, content.skills.sumOf { it.skills.size })
    }
}
