package no.nordicsemi.nrf.matter.docs.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import no.nordicsemi.nrf.matter.docs.docs.DocGroup
import no.nordicsemi.nrf.matter.docs.docs.DocPage
import no.nordicsemi.nrf.matter.docs.docs.DocsRepository
import no.nordicsemi.nrf.matter.docs.docs.LinkTarget
import no.nordicsemi.nrf.matter.docs.markdown.MdBlock
import no.nordicsemi.nrf.matter.docs.markdown.MdBlockView

@Composable
fun DocsBrowserScreen(
    initialAnchor: DocAnchor,
    onLink: (LinkTarget) -> Unit,
    onClose: () -> Unit,
) {
    var currentPage by remember(initialAnchor) { mutableStateOf(initialAnchor.page) }
    var sectionId by remember(initialAnchor) { mutableStateOf(initialAnchor.sectionId) }
    var navigation by remember(initialAnchor) { mutableStateOf(0) }
    var loaded by remember { mutableStateOf<Pair<DocPage, List<MdBlock>>?>(null) }
    val blocks = loaded?.takeIf { it.first == currentPage }?.second

    LaunchedEffect(currentPage) { loaded = currentPage to DocsRepository.blocksOf(currentPage) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.width(260.dp).fillMaxHeight().verticalScroll(rememberScrollState())) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("All docs", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = "Close") }
                }
                DocGroup.entries.forEach { group ->
                    Text(
                        text = group.displayName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                    )
                    DocPage.entries.filter { it.group == group }.forEach { page ->
                        Text(
                            text = page.displayTitle,
                            fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                            color = if (page == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { currentPage = page; sectionId = null; navigation++ }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    }
                }
            }
            VerticalDivider()
            if (blocks == null) {
                Spacer(modifier = Modifier.weight(1f).fillMaxHeight())
            } else {
                key(currentPage, navigation) {
                    val initialIndex = sectionId
                        ?.let { id -> blocks.indexOfFirst { it is MdBlock.Heading && it.slug == id } }
                        ?.coerceAtLeast(0)
                        ?: 0
                    LazyColumn(
                        state = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex),
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        itemsIndexed(blocks) { _, block ->
                            MdBlockView(
                                block = block,
                                onLinkClick = { target ->
                                    when (val resolved = DocsRepository.resolveLink(target, currentPage)) {
                                        is LinkTarget.Internal -> {
                                            currentPage = resolved.anchor.page
                                            sectionId = resolved.anchor.sectionId
                                            navigation++
                                        }
                                        is LinkTarget.External -> onLink(resolved)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
