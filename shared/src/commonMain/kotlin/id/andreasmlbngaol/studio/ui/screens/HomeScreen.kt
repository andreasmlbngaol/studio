package id.andreasmlbngaol.studio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.data.Project
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodyTextStyle
import id.andreasmlbngaol.studio.theme.heroTextStyle
import id.andreasmlbngaol.studio.theme.sectionTitleTextStyle
import id.andreasmlbngaol.studio.theme.subtitleTextStyle
import id.andreasmlbngaol.studio.ui.components.CompactHeroCollage
import id.andreasmlbngaol.studio.ui.components.CtaButton
import id.andreasmlbngaol.studio.ui.components.HeroCollage
import id.andreasmlbngaol.studio.ui.components.ProjectCard
import id.andreasmlbngaol.studio.ui.components.SectionHeader
import id.andreasmlbngaol.studio.ui.components.SiteFooter
import id.andreasmlbngaol.studio.ui.layout.Page
import id.andreasmlbngaol.studio.ui.layout.PortfolioPage
import id.andreasmlbngaol.studio.util.openUrl
import kotlinx.coroutines.launch
import studio.shared.generated.resources.Res as Resources

@Composable
fun HomeScreen(
    onNavigate: (TopLevel) -> Unit,
    onOpenProject: (String) -> Unit,
) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val scope = rememberCoroutineScope()

    PortfolioPage { compact ->
        Page {
            HeroSection(
                compact = compact,
                onViewWork = { onNavigate(TopLevel.Work) },
                onDownloadCv = {
                    scope.launch { openUrl(Resources.getUri("files/cv.pdf")) }
                },
            )
            SelectedWorkSection(
                compact = compact,
                projects = config.content.highlights,
                onOpenProject = onOpenProject,
                onViewAll = { onNavigate(TopLevel.Work) },
            )
            AboutTeaser(compact = compact, onReadMore = { onNavigate(TopLevel.About) })
            ContactTeaser(compact = compact, onContact = { onNavigate(TopLevel.Contact) })
            SiteFooter(compact)
        }
    }
}

@Composable
private fun HeroSection(
    compact: Boolean,
    onViewWork: () -> Unit,
    onDownloadCv: () -> Unit,
) {
    val strings = LocalAppConfig.current.strings
    if (compact) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column {
                Text(
                    text = strings.homeGreeting,
                    style = heroTextStyle(true),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = strings.homeName,
                    style = heroTextStyle(true),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Text(
                text = strings.homeSubtitle,
                style = subtitleTextStyle(true).copy(fontSize = 22.sp, lineHeight = 28.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            CompactHeroCollage(Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CtaButton(
                    text = strings.viewWork,
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    onClick = onViewWork,
                    filled = true,
                    modifier = Modifier.weight(1f),
                    dense = true,
                )
                CtaButton(
                    text = strings.downloadCv,
                    icon = Icons.Filled.Download,
                    onClick = onDownloadCv,
                    filled = false,
                    modifier = Modifier.weight(1f),
                    dense = true,
                )
            }
        }
    } else {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 56.dp),
        ) {
            val stacked = maxWidth < 900.dp
            if (stacked) {
                Column(verticalArrangement = Arrangement.spacedBy(40.dp)) {
                    HeroText(
                        headlineSize = 96,
                        headlineLineHeight = 100,
                        subtitleSize = 20,
                        onViewWork = onViewWork,
                        onDownloadCv = onDownloadCv,
                    )
                    HeroCollage(
                        Modifier
                            .width(460.dp)
                            .aspectRatio(560f / 580f),
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    HeroText(
                        headlineSize = 132,
                        headlineLineHeight = 125,
                        subtitleSize = 24,
                        onViewWork = onViewWork,
                        onDownloadCv = onDownloadCv,
                        modifier = Modifier.weight(1f),
                    )
                    HeroCollage(
                        Modifier
                            .weight(0.46f)
                            .aspectRatio(560f / 580f),
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroText(
    headlineSize: Int,
    headlineLineHeight: Int,
    subtitleSize: Int,
    onViewWork: () -> Unit,
    onDownloadCv: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppConfig.current.strings
    val headline = heroTextStyle(false).copy(
        fontSize = headlineSize.sp,
        lineHeight = headlineLineHeight.sp,
    )
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(28.dp)) {
        Column {
            Text(strings.homeGreeting, style = headline, color = MaterialTheme.colorScheme.onSurface)
            Text(strings.homeName, style = headline, color = MaterialTheme.colorScheme.primary)
        }
        Text(
            text = strings.homeSubtitle,
            style = subtitleTextStyle(false).copy(fontSize = subtitleSize.sp, lineHeight = (subtitleSize + 8).sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.widthIn(max = 560.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CtaButton(
                text = strings.viewWork,
                icon = Icons.AutoMirrored.Filled.ArrowForward,
                onClick = onViewWork,
                filled = true,
            )
            CtaButton(
                text = strings.downloadCv,
                icon = Icons.Filled.Download,
                onClick = onDownloadCv,
                filled = false,
            )
        }
    }
}

@Composable
private fun SelectedWorkSection(
    compact: Boolean,
    projects: List<Project>,
    onOpenProject: (String) -> Unit,
    onViewAll: () -> Unit,
) {
    val strings = LocalAppConfig.current.strings
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 16.dp else 64.dp,
                vertical = if (compact) 32.dp else 64.dp,
            ),
    ) {
        val threeAcross = !compact && maxWidth >= 960.dp
        Column(verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 32.dp)) {
            SectionHeader(
                title = strings.selectedWork,
                compact = compact,
                action = {
                    TextButton(
                        onClick = onViewAll,
                        shapes = ButtonDefaults.shapes(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        Text(strings.viewAll, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
            )
            if (threeAcross) {
                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    projects.forEachIndexed { index, project ->
                        ProjectCard(
                            project = project,
                            index = index,
                            compact = false,
                            showMedia = true,
                            onDetails = { onOpenProject(project.slug) },
                            onCode = { project.githubUrl?.let(::openUrl) },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    projects.forEachIndexed { index, project ->
                        ProjectCard(
                            project = project,
                            index = index,
                            compact = compact,
                            showMedia = !compact,
                            onDetails = { onOpenProject(project.slug) },
                            onCode = { project.githubUrl?.let(::openUrl) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutTeaser(compact: Boolean, onReadMore: () -> Unit) {
    val strings = LocalAppConfig.current.strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 20.dp else 64.dp,
                vertical = if (compact) 40.dp else 96.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(if (compact) 12.dp else 20.dp),
    ) {
        Text(
            text = strings.aboutTeaserTitle,
            style = sectionTitleTextStyle(compact).copy(
                fontSize = if (compact) 32.sp else 36.sp,
                lineHeight = if (compact) 40.sp else 44.sp,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = strings.aboutTeaserBody,
            style = bodyTextStyle(compact),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = if (compact) Modifier else Modifier.width(760.dp),
        )
        Row(
            modifier = Modifier.clickable(onClick = onReadMore),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = strings.readMore,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ContactTeaser(compact: Boolean, onContact: () -> Unit) {
    val strings = LocalAppConfig.current.strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 20.dp else 64.dp,
                vertical = if (compact) 40.dp else 48.dp,
            ),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLargeIncreased,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            if (compact) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = strings.contactCtaTitle,
                        fontSize = 28.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = strings.contactCtaBody,
                        style = bodyTextStyle(true),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(
                        onClick = onContact,
                        shapes = ButtonDefaults.shapes(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                    ) {
                        Text(strings.contactCtaButton, style = MaterialTheme.typography.labelLarge)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.padding(56.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = strings.contactCtaTitle,
                            fontSize = 48.sp,
                            lineHeight = 54.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = strings.contactCtaBody,
                            style = subtitleTextStyle(false),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    CtaButton(
                        text = strings.contactCtaButton,
                        icon = Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = onContact,
                        filled = true,
                    )
                }
            }
        }
    }
}
