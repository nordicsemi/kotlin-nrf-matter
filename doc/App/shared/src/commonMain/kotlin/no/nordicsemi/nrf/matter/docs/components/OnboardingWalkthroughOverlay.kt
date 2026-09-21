package no.nordicsemi.nrf.matter.docs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import no.nordicsemi.nrf.matter.docs.docs.DocPage
import no.nordicsemi.nrf.matter.docs.docs.DocsRepository
import no.nordicsemi.nrf.matter.docs.docs.LinkTarget
import no.nordicsemi.nrf.matter.docs.markdown.MdBlock
import no.nordicsemi.nrf.matter.docs.markdown.MdBlockView

@Composable
fun OnboardingWalkthroughOverlay(
    onLink: (LinkTarget) -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var blocks by remember { mutableStateOf<List<MdBlock>>(emptyList()) }
    LaunchedEffect(Unit) { blocks = DocsRepository.blocksOf(DocPage.ONBOARDING) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "Adding a device",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = DocPage.ONBOARDING.displayTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            HorizontalDivider()

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(blocks) { _, block ->
                    MdBlockView(block = block, onLinkClick = { target -> onLink(DocsRepository.resolveLink(target, DocPage.ONBOARDING)) })
                }
            }

            HorizontalDivider()
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.End) {
                Button(onClick = onDone) {
                    Text("Done, add the device")
                }
            }
        }
    }
}
