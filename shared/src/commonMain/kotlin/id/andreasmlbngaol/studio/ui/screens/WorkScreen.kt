package id.andreasmlbngaol.studio.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.data.Project
import id.andreasmlbngaol.studio.data.ProjectCategory
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodySmallTextStyle
import id.andreasmlbngaol.studio.theme.captionTextStyle
import id.andreasmlbngaol.studio.ui.components.FilterPill
import id.andreasmlbngaol.studio.ui.components.PageHeader
import id.andreasmlbngaol.studio.ui.components.ProjectCard
import id.andreasmlbngaol.studio.ui.components.SectionHeader
import id.andreasmlbngaol.studio.ui.components.SiteFooter
import id.andreasmlbngaol.studio.ui.layout.Page
import id.andreasmlbngaol.studio.ui.layout.PortfolioPage
import id.andreasmlbngaol.studio.util.openUrl
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.FlowRow

@Composable
fun WorkScreen(onOpenProject: (String) -> Unit) {
    val config = LocalAppConfig.current
    val strings = config.strings
    var filter by remember { mutableStateOf<ProjectCategory?>(null) }
    val moreProjects = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()

    PortfolioPage { isCompact ->
        Page {
            if (!isCompact) {
                PageHeader(title = strings.workTitle, subtitle = strings.workSubtitle)
            }
            HighlightsSection(
                compact = isCompact,
                projects = config.content.highlights,
                onOpenProject = onOpenProject,
                onViewAll = { scope.launch { moreProjects.bringIntoView() } },
            )
            MoreProjectsSection(
                compact = isCompact,
                projects = config.content.moreProjects.filter { project ->
                    filter == null || project.categories.contains(filter)
                },
                filter = filter,
                onFilter = { filter = it },
                onOpen = { project -> project.githubUrl?.let(::openUrl) },
                modifier = Modifier.bringIntoViewRequester(moreProjects),
            )
            SiteFooter(isCompact)
        }
    }
}

@Composable
private fun FilterRow(
    selected: ProjectCategory?,
    onSelect: (ProjectCategory?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppConfig.current.strings
    val options = listOf(
        null to strings.filterAll,
        ProjectCategory.Android to strings.filterAndroid,
        ProjectCategory.Backend to strings.filterBackend,
        ProjectCategory.Ai to strings.filterAi,
        ProjectCategory.Web to strings.filterWeb,
        ProjectCategory.FullStack to strings.filterFullStack,
    )
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { (category, label) ->
            FilterPill(
                text = label,
                selected = selected == category,
                onClick = { onSelect(category) },
            )
        }
    }
}

@Composable
private fun HighlightsSection(
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
                vertical = if (compact) 16.dp else 64.dp,
            ),
    ) {
        val threeAcross = !compact && maxWidth >= 960.dp
        Column(verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 32.dp)) {
            SectionHeader(
                title = strings.highlights,
                compact = compact,
                action = if (!compact) {
                    {
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
                    }
                } else {
                    null
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
                            showMedia = true,
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
private fun MoreProjectsSection(
    compact: Boolean,
    projects: List<Project>,
    filter: ProjectCategory?,
    onFilter: (ProjectCategory?) -> Unit,
    onOpen: (Project) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalAppConfig.current.strings
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (compact) 16.dp else 64.dp,
                end = if (compact) 16.dp else 64.dp,
                top = if (compact) 16.dp else 32.dp,
                bottom = if (compact) 24.dp else 96.dp,
            ),
    ) {
        val narrow = compact || maxWidth < 960.dp
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 24.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = strings.moreProjects,
                    style = id.andreasmlbngaol.studio.theme.sectionTitleTextStyle(compact),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "${projects.size} " + strings.projectsCount.substringAfter(' '),
                    style = if (compact) captionTextStyle() else bodySmallTextStyle(false),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (compact) {
                FilterRow(selected = filter, onSelect = onFilter)
            }
            Column(Modifier.fillMaxWidth()) {
                if (!narrow) {
                    TableColumns()
                }
                projects.forEachIndexed { index, project ->
                    ProjectTableRow(
                        number = index + 4,
                        project = project,
                        compact = narrow,
                        onClick = { onOpen(project) },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun TableColumns() {
    val strings = LocalAppConfig.current.strings
    val style = captionTextStyle()
    val color = MaterialTheme.colorScheme.outline
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(strings.tableNumber, style = style, color = color, modifier = Modifier.width(40.dp))
        Text(strings.tableProject, style = style, color = color, modifier = Modifier.width(240.dp))
        Spacer(Modifier.weight(1f))
        Text(strings.tablePlatform, style = style, color = color, modifier = Modifier.width(170.dp))
        Text(strings.tableStack, style = style, color = color, modifier = Modifier.width(300.dp))
        Text(strings.tableYear, style = style, color = color, modifier = Modifier.width(56.dp))
        Spacer(Modifier.width(20.dp))
    }
}

@Composable
private fun ProjectTableRow(
    number: Int,
    project: Project,
    compact: Boolean,
    onClick: () -> Unit,
) {
    if (compact) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = number.toString().padStart(2, '0'),
                style = captionTextStyle(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(Modifier.weight(1f)) {
                Text(
                    text = project.name,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${project.platform} · ${project.stack} · ${project.year}",
                    style = captionTextStyle(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(
                Icons.Filled.ArrowOutward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = number.toString().padStart(2, '0'),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(40.dp),
            )
            Text(
                text = project.name,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(240.dp),
            )
            Text(
                text = project.tableDescription,
                style = bodySmallTextStyle(false),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = project.platform,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(170.dp),
            )
            Text(
                text = project.stack,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(300.dp),
            )
            Text(
                text = project.year,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.width(56.dp),
            )
            Icon(
                Icons.Filled.ArrowOutward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
