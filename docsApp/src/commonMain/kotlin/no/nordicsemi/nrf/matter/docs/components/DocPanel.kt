package no.nordicsemi.nrf.matter.docs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.docs.docs.DocAnchor
import no.nordicsemi.nrf.matter.docs.docs.DocSection
import no.nordicsemi.nrf.matter.docs.docs.DocStepPage
import no.nordicsemi.nrf.matter.docs.docs.DocsRepository
import no.nordicsemi.nrf.matter.docs.docs.LinkTarget
import no.nordicsemi.nrf.matter.docs.docs.buildStepPages
import no.nordicsemi.nrf.matter.docs.markdown.MarkdownContent

@Composable
fun DocPanel(
    anchor: DocAnchor,
    onLink: (LinkTarget) -> Unit,
    onDismiss: () -> Unit,
    onOpenFullPage: (DocAnchor) -> Unit,
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
    showFullPage: Boolean = false,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
) {
    var section by remember { mutableStateOf<DocSection?>(null) }
    var stepPages by remember { mutableStateOf<List<DocStepPage>>(emptyList()) }
    var pageIndex by remember { mutableStateOf(0) }
    LaunchedEffect(anchor, showFullPage) {
        pageIndex = 0
        if (showFullPage) {
            stepPages = buildStepPages(DocsRepository.blocksOf(anchor.page), anchor.page.displayTitle)
        } else {
            section = DocsRepository.section(anchor)
        }
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "From the docs",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    val title = if (showFullPage) {
                        stepPages.getOrNull(pageIndex)?.title ?: anchor.page.displayTitle
                    } else {
                        section?.title ?: anchor.page.displayTitle
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                if (dismissible) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                } else if (showFullPage && primaryActionLabel != null && onPrimaryAction != null) {
                    TextButton(onClick = onPrimaryAction) {
                        Text("Skip")
                    }
                }
            }
            HorizontalDivider()

            Box(modifier = Modifier.weight(1f)) {
                val blocks = if (showFullPage) stepPages.getOrNull(pageIndex)?.blocks else section?.blocks
                if (blocks != null) {
                    key(if (showFullPage) pageIndex else section?.id) {
                        MarkdownContent(
                            blocks = blocks,
                            onLinkClick = { target -> onLink(DocsRepository.resolveLink(target, anchor.page)) },
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                        )
                    }
                }
            }

            HorizontalDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (showFullPage) {
                    if (stepPages.size > 1) {
                        Text(
                            text = "${pageIndex + 1} / ${stepPages.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (pageIndex > 0) {
                        OutlinedButton(onClick = { pageIndex-- }) {
                            Text("Back")
                        }
                    }
                    val isLastPage = stepPages.isEmpty() || pageIndex >= stepPages.lastIndex
                    if (!isLastPage) {
                        Button(onClick = { pageIndex++ }) {
                            Text("Next")
                        }
                    } else if (primaryActionLabel != null && onPrimaryAction != null) {
                        Button(onClick = onPrimaryAction) {
                            Text(primaryActionLabel)
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                    if (primaryActionLabel != null && onPrimaryAction != null) {
                        OutlinedButton(onClick = { onOpenFullPage(anchor) }) {
                            Text("View full page: ${anchor.page.displayTitle}")
                        }
                    } else {
                        Button(onClick = { onOpenFullPage(anchor) }) {
                            Text("View full page: ${anchor.page.displayTitle}")
                        }
                    }
                    if (primaryActionLabel != null && onPrimaryAction != null) {
                        Button(onClick = onPrimaryAction) {
                            Text(primaryActionLabel)
                        }
                    }
                }
            }
        }
    }
}
