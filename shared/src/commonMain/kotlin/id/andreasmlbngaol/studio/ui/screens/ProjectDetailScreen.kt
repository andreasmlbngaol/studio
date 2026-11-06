package id.andreasmlbngaol.studio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.data.GalleryIcon
import id.andreasmlbngaol.studio.data.Project
import id.andreasmlbngaol.studio.data.ProjectDetail
import id.andreasmlbngaol.studio.data.ResultStat
import id.andreasmlbngaol.studio.navigation.TopLevel
import id.andreasmlbngaol.studio.scaledShape
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodySmallTextStyle
import id.andreasmlbngaol.studio.theme.bodyTextStyle
import id.andreasmlbngaol.studio.theme.captionTextStyle
import id.andreasmlbngaol.studio.theme.sectionTitleTextStyle
import id.andreasmlbngaol.studio.ui.components.CtaButton
import id.andreasmlbngaol.studio.ui.components.SiteFooter
import id.andreasmlbngaol.studio.ui.components.TagChip
import id.andreasmlbngaol.studio.ui.components.projectIcon
import id.andreasmlbngaol.studio.ui.layout.Page
import id.andreasmlbngaol.studio.ui.layout.PortfolioPage
import id.andreasmlbngaol.studio.util.openUrl

@Composable
fun ProjectDetailScreen(
    slug: String,
    onNavigate: (TopLevel) -> Unit,
    onBack: () -> Unit,
    onOpenProject: (String) -> Unit,
) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val project = config.content.project(slug)
    val detail = project?.detail

    if (project == null || detail == null) {
        PortfolioPage { _ ->
            Page {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = strings.backToWork,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable(onClick = onBack),
                    )
                }
            }
        }
        return
    }

    PortfolioPage { compact ->
        Page {
            if (compact) {
                CompactProjectContent(
                    project = project,
                    detail = detail,
                    onOpenUrl = ::openUrl,
                    onOpenProject = onOpenProject,
                )
            } else {
                ExpandedProjectContent(
                    project = project,
                    detail = detail,
                    onOpenUrl = ::openUrl,
                    onOpenProject = onOpenProject,
                )
            }
            SiteFooter(compact)
        }
    }
}

private fun primaryAction(project: Project): Triple<String, ImageVector, String?> {
    return when {
        project.apkUrl != null -> Triple("apk", Icons.Filled.Download, project.apkUrl)
        project.liveUrl != null -> Triple("live", Icons.AutoMirrored.Filled.OpenInNew, project.liveUrl)
        else -> Triple("code", Icons.Filled.Code, project.githubUrl)
    }
}

@Composable
private fun ExpandedProjectContent(
    project: Project,
    detail: ProjectDetail,
    onOpenUrl: (String) -> Unit,
    onOpenProject: (String) -> Unit,
) {
    val strings = LocalAppConfig.current.strings
    Column(Modifier.fillMaxWidth()) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 64.dp, end = 64.dp, top = 40.dp, bottom = 40.dp),
        ) {
            val titleSize = if (maxWidth < 1000.dp) 96 else 140
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    detail.tags.forEach { TagChip(it) }
                }
                Text(
                    text = project.name,
                    fontSize = titleSize.sp,
                    lineHeight = (titleSize * 0.98f).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = project.lead,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(760.dp),
                )
                ProjectActions(project = project, detail = detail, onOpenUrl = onOpenUrl)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 16.dp)
                .height(560.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, scaledShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = projectIcon(project.slug),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(160.dp),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            MetaItem(strings.role, detail.role, Modifier.weight(1f))
            MetaItem(strings.timeline, detail.timeline, Modifier.weight(1f))
            MetaItem(strings.stack, detail.stack, Modifier.weight(1f))
            MetaItem(strings.platform, detail.platform, Modifier.weight(1f))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 56.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = strings.overview,
                    style = sectionTitleTextStyle(false),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                detail.body.forEach { paragraph ->
                    Text(
                        text = paragraph,
                        style = bodySmallTextStyle(false),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Surface(
                modifier = Modifier.weight(1.03f),
                shape = MaterialTheme.shapes.extraLargeIncreased,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DetailRow(
                        icon = Icons.Filled.FolderOff,
                        title = strings.problem,
                        supporting = detail.problemTitle,
                        trailing = detail.problemCount,
                    )
                    DetailRow(
                        icon = Icons.Filled.AccountTree,
                        title = strings.approach,
                        supporting = detail.approachTitle,
                        trailing = detail.approachCount,
                    )
                    DetailRow(
                        icon = Icons.Filled.Verified,
                        title = strings.outcome,
                        supporting = detail.outcomeTitle,
                        trailing = detail.outcomeCount,
                    )
                }
            }
        }

        ResultsSection(detail = detail, compact = false)

        GallerySection(detail = detail, compact = false)

        NextProjectSection(detail = detail, compact = false, onOpenProject = onOpenProject)
    }
}

@Composable
private fun CompactProjectContent(
    project: Project,
    detail: ProjectDetail,
    onOpenUrl: (String) -> Unit,
    onOpenProject: (String) -> Unit,
) {
    val strings = LocalAppConfig.current.strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            detail.tags.forEach { TagChip(it) }
        }
        Text(
            text = project.name,
            fontSize = 40.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = project.lead,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        ProjectActions(project = project, detail = detail, onOpenUrl = onOpenUrl, stacked = true)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, scaledShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = projectIcon(project.slug),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(96.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            MetaItem(strings.role, detail.role, Modifier.fillMaxWidth())
            MetaItem(strings.timeline, detail.timeline, Modifier.fillMaxWidth())
            MetaItem(strings.stack, detail.stack, Modifier.fillMaxWidth())
            MetaItem(strings.platform, detail.platform, Modifier.fillMaxWidth())
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = strings.overview,
                style = sectionTitleTextStyle(true),
                color = MaterialTheme.colorScheme.onSurface,
            )
            detail.body.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = bodyTextStyle(true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            DetailRow(
                icon = Icons.Filled.FolderOff,
                title = strings.problem,
                supporting = detail.problemTitle,
                trailing = detail.problemCount,
            )
            DetailRow(
                icon = Icons.Filled.AccountTree,
                title = strings.approach,
                supporting = detail.approachTitle,
                trailing = detail.approachCount,
            )
            DetailRow(
                icon = Icons.Filled.Verified,
                title = strings.outcome,
                supporting = detail.outcomeTitle,
                trailing = detail.outcomeCount,
            )
        }

        ResultsSection(detail = detail, compact = true)

        GallerySection(detail = detail, compact = true)

        NextProjectSection(detail = detail, compact = true, onOpenProject = onOpenProject)
    }
}

@Composable
private fun ProjectActions(
    project: Project,
    detail: ProjectDetail,
    onOpenUrl: (String) -> Unit,
    stacked: Boolean = false,
) {
    val strings = LocalAppConfig.current.strings
    val action = primaryAction(project)
    val label = when (action.first) {
        "apk" -> strings.downloadApk
        "live" -> if (project.slug == "caritas-kopi") strings.liveApp else strings.liveDemo
        else -> strings.sourceCode
    }
    val primary: @Composable (Modifier) -> Unit = { modifier ->
        CtaButton(
            text = label,
            icon = action.second,
            onClick = { action.third?.let(onOpenUrl) },
            filled = true,
            modifier = modifier,
            height = if (stacked) 52.dp else 56.dp,
            dense = stacked,
        )
    }
    val source: @Composable (Modifier) -> Unit = { modifier ->
        CtaButton(
            text = strings.sourceCode,
            icon = Icons.Filled.Code,
            onClick = { project.githubUrl?.let(onOpenUrl) },
            filled = false,
            modifier = modifier,
            height = if (stacked) 52.dp else 56.dp,
            dense = stacked,
        )
    }
    if (stacked) {
        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            primary(Modifier.fillMaxWidth())
            source(Modifier.fillMaxWidth())
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            primary(Modifier)
            source(Modifier)
        }
    }
}

@Composable
private fun MetaItem(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    title: String,
    supporting: String,
    trailing: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp),
            )
        }
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = supporting,
                style = bodySmallTextStyle(false),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }
        Text(
            text = trailing,
            style = captionTextStyle(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun statColors(index: Int): Pair<Color, Color> = when (index % 3) {
    0 -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
    1 -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    else -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
}

@Composable
private fun ResultsSection(detail: ProjectDetail, compact: Boolean) {
    val strings = LocalAppConfig.current.strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 0.dp else 64.dp,
                vertical = if (compact) 0.dp else 32.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(if (compact) 8.dp else 24.dp),
    ) {
        Text(
            text = strings.results,
            style = sectionTitleTextStyle(compact),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (compact) {
            detail.results.forEachIndexed { index, stat ->
                StatCard(stat = stat, index = index, compact = true, modifier = Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                detail.results.forEachIndexed { index, stat ->
                    StatCard(stat = stat, index = index, compact = false, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun StatCard(stat: ResultStat, index: Int, compact: Boolean, modifier: Modifier = Modifier) {
    val (container, content) = statColors(index)
    Surface(modifier = modifier, shape = MaterialTheme.shapes.large, color = container) {
        Column(
            modifier = Modifier.padding(if (compact) 20.dp else 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stat.value,
                fontSize = if (compact) 48.sp else 96.sp,
                lineHeight = if (compact) 52.sp else 94.sp,
                fontWeight = FontWeight.Bold,
                color = content,
            )
            Text(
                text = stat.label,
                fontSize = if (compact) 14.sp else 16.sp,
                lineHeight = if (compact) 20.sp else 24.sp,
                color = content,
            )
        }
    }
}

private fun galleryIcon(icon: GalleryIcon): ImageVector = when (icon) {
    GalleryIcon.PhotoLibrary -> Icons.Filled.PhotoLibrary
    GalleryIcon.Image -> Icons.Filled.Image
    GalleryIcon.AutoAwesome -> Icons.Filled.AutoAwesome
    GalleryIcon.Eco -> Icons.Filled.Eco
    GalleryIcon.Science -> Icons.Filled.Science
    GalleryIcon.Dashboard -> Icons.Filled.Dashboard
    GalleryIcon.Assignment -> Icons.AutoMirrored.Filled.Assignment
    GalleryIcon.TableChart -> Icons.Filled.TableChart
    GalleryIcon.Insights -> Icons.Filled.Insights
}

@Composable
private fun GallerySection(detail: ProjectDetail, compact: Boolean) {
    val strings = LocalAppConfig.current.strings
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 0.dp else 64.dp,
                vertical = if (compact) 0.dp else 48.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(if (compact) 12.dp else 24.dp),
    ) {
        Text(
            text = strings.gallery,
            style = sectionTitleTextStyle(compact),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (compact) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                detail.gallery.forEach { item ->
                    GalleryCard(item.icon, item.label, Modifier.fillMaxWidth(), 120)
                }
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                detail.gallery.forEach { item ->
                    GalleryCard(item.icon, item.label, Modifier.weight(1f), 220)
                }
            }
        }
    }
}

@Composable
private fun GalleryCard(
    icon: GalleryIcon,
    label: String,
    modifier: Modifier = Modifier,
    height: Int,
) {
    Surface(
        modifier = modifier.height(height.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = galleryIcon(icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun NextProjectSection(
    detail: ProjectDetail,
    compact: Boolean,
    onOpenProject: (String) -> Unit,
) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val next = detail.nextSlug?.let { config.content.project(it) } ?: return
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = if (compact) 0.dp else 64.dp,
                vertical = if (compact) 0.dp else 56.dp,
            ),
        shape = if (compact) scaledShape(24.dp) else MaterialTheme.shapes.extraLargeIncreased,
        color = MaterialTheme.colorScheme.tertiaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (compact) 24.dp else 48.dp,
                vertical = if (compact) 20.dp else 40.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
            ) {
                Text(
                    text = strings.nextProject,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Text(
                    text = next.name,
                    fontSize = if (compact) 28.sp else 72.sp,
                    lineHeight = if (compact) 34.sp else 71.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
            Surface(
                modifier = Modifier
                    .size(if (compact) 56.dp else 96.dp)
                    .clickable { onOpenProject(next.slug) },
                shape = if (compact) scaledShape(18.dp) else MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primary,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = strings.nextProject,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(if (compact) 24.dp else 36.dp),
                    )
                }
            }
        }
    }
}
