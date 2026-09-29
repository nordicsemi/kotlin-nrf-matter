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
import no.nordicsemi.nrf.matter.docs.docs.DocStepPage
import no.nordicsemi.nrf.matter.docs.docs.DocsRepository
import no.nordicsemi.nrf.matter.docs.docs.LinkTarget
import no.nordicsemi.nrf.matter.docs.docs.buildStepPages
import no.nordicsemi.nrf.matter.docs.markdown.MarkdownContent
import no.nordicsemi.nrf.matter.docs.markdown.MdBlock
import no.nordicsemi.nrf.matter.docs.markdown.slugify

@Composable
fun DocPanel(
    anchor: DocAnchor,
    onLink: (LinkTarget) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissible: Boolean = true,
    primaryActionLabel: String? = null,
    onPrimaryAction: (() -> Unit)? = null,
) {
    var stepPages by remember { mutableStateOf<List<DocStepPage>>(emptyList()) }
    var pageIndex by remember { mutableStateOf(0) }
    LaunchedEffect(anchor) {
        val pages = buildStepPages(DocsRepository.blocksOf(anchor.page), anchor.page.displayTitle)
        stepPages = pages
        pageIndex = pages.indexOfSection(anchor.sectionId)
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
                    Text(
                        text = stepPages.getOrNull(pageIndex)?.title ?: anchor.page.displayTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                if (dismissible) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                } else if (primaryActionLabel != null && onPrimaryAction != null) {
                    TextButton(onClick = onPrimaryAction) {
                        Text("Skip")
                    }
                }
            }
            HorizontalDivider()

            Box(modifier = Modifier.weight(1f)) {
                val blocks = stepPages.getOrNull(pageIndex)?.blocks
                if (blocks != null) {
                    key(pageIndex) {
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
            }
        }
    }
}

private fun List<DocStepPage>.indexOfSection(sectionId: String?): Int {
    if (sectionId == null) return 0
    val index = indexOfFirst { page ->
        slugify(page.title) == sectionId ||
            page.blocks.any { it is MdBlock.Heading && it.slug == sectionId }
    }
    return index.coerceAtLeast(0)
}
