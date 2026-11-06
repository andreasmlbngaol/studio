package id.andreasmlbngaol.studio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.andreasmlbngaol.studio.data.SkillGroup
import id.andreasmlbngaol.studio.state.LocalAppConfig
import id.andreasmlbngaol.studio.theme.bodyTextStyle
import id.andreasmlbngaol.studio.theme.captionTextStyle
import id.andreasmlbngaol.studio.theme.sectionTitleTextStyle
import id.andreasmlbngaol.studio.theme.subtitleTextStyle
import id.andreasmlbngaol.studio.ui.components.PageHeader
import id.andreasmlbngaol.studio.ui.components.SiteFooter
import id.andreasmlbngaol.studio.ui.components.SkillChip
import id.andreasmlbngaol.studio.ui.components.TimelineList
import id.andreasmlbngaol.studio.ui.layout.Page
import id.andreasmlbngaol.studio.ui.layout.PortfolioPage

@Composable
fun AboutScreen() {
    val config = LocalAppConfig.current
    val strings = config.strings
    var isEducation by rememberSaveable { mutableStateOf(false) }

    PortfolioPage { isCompact ->
        Page {
            if (!isCompact) {
                PageHeader(title = strings.aboutTitle, subtitle = strings.aboutSubtitle)
            }
            AboutBody(
                compact = isCompact,
                isEducation = isEducation,
                onSelectTab = { isEducation = it },
            )
            SkillsSection(compact = isCompact)
            SiteFooter(isCompact)
        }
    }
}

@Composable
private fun AboutBody(
    compact: Boolean,
    isEducation: Boolean,
    onSelectTab: (Boolean) -> Unit,
) {
    val config = LocalAppConfig.current
    val strings = config.strings
    val items = if (isEducation) config.content.education else config.content.experience

    if (compact) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = strings.aboutMe,
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = strings.aboutSubtitle,
                    style = bodyTextStyle(true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = strings.aboutTeaserBody,
                    style = bodyTextStyle(true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            AboutTabs(selected = isEducation, onSelect = onSelectTab)
            TimelineList(items = items, compact = true)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = strings.aboutMe,
                    style = sectionTitleTextStyle(false),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = strings.aboutTeaserBody,
                    style = bodyTextStyle(false),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Column(
                modifier = Modifier.weight(0.89f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AboutTabs(
                    selected = isEducation,
                    onSelect = onSelectTab,
                )
                TimelineList(items = items, compact = false)
            }
        }
    }
}

@Composable
private fun AboutTabs(selected: Boolean, onSelect: (Boolean) -> Unit) {
    val strings = LocalAppConfig.current.strings
    Row(Modifier.width(240.dp).height(48.dp)) {
        TabItem(
            label = strings.experience,
            selected = !selected,
            onClick = { onSelect(false) },
            modifier = Modifier.weight(1f),
        )
        TabItem(
            label = strings.education,
            selected = selected,
            onClick = { onSelect(true) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun TabItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
        ) {
            if (selected) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                )
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .align(Alignment.BottomCenter)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
            }
        }
    }
}

@Composable
private fun SkillsSection(compact: Boolean) {
    val config = LocalAppConfig.current
    val strings = config.strings
    if (compact) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 40.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = strings.skillsTitle,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = strings.skillsSubtitle,
                    style = bodyTextStyle(true),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            SkillGroups(groups = config.content.skills, compact = true)
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp, vertical = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(48.dp),
        ) {
            Column(
                modifier = Modifier.width(320.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = strings.skillsTitle,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = strings.skillsSubtitle,
                    style = subtitleTextStyle(false),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            SkillGroups(
                groups = config.content.skills,
                compact = false,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SkillGroups(
    groups: List<SkillGroup>,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        groups.forEach { group ->
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = group.title,
                    style = captionTextStyle(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    group.skills.forEach { skill -> SkillChip(skill = skill, compact = compact) }
                }
            }
        }
    }
}
