package no.nordicsemi.nrf.matter.docs.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import docs.shared.generated.resources.Res
import docs.shared.generated.resources.allDrawableResources
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.layout.ContentScale

@Composable
fun MarkdownContent(
    blocks: List<MdBlock>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        blocks.forEach { block -> MdBlockView(block, onLinkClick) }
    }
}

@Composable
fun MdBlockView(block: MdBlock, onLinkClick: (String) -> Unit) {
    when (block) {
        is MdBlock.Heading -> {
            val style = when (block.level) {
                1 -> MaterialTheme.typography.headlineSmall
                2 -> MaterialTheme.typography.titleLarge
                3 -> MaterialTheme.typography.titleMedium
                else -> MaterialTheme.typography.titleSmall
            }
            Text(text = block.text, style = style, fontWeight = FontWeight.Bold)
        }

        is MdBlock.Paragraph -> InlineText(block.spans, onLinkClick)

        is MdBlock.CodeBlock -> {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = block.code,
                    fontFamily = FontFamily.Monospace,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(12.dp),
                )
            }
        }

        is MdBlock.Table -> TableView(block, onLinkClick)

        is MdBlock.ListBlock -> ListBlockView(block, onLinkClick)

        is MdBlock.Admonition -> AdmonitionView(block, onLinkClick)

        is MdBlock.ImageGallery -> ImageGalleryView(block)

        is MdBlock.BadgeLinks -> {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                block.badges.forEach { badge ->
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    ) {
                        Text(
                            text = badge.text,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .clickable { onLinkClick(badge.target) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InlineText(spans: List<InlineSpan>, onLinkClick: (String) -> Unit, style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium) {
    Text(text = spansToAnnotatedString(spans, onLinkClick), style = style)
}

private fun spansToAnnotatedString(spans: List<InlineSpan>, onLinkClick: (String) -> Unit): AnnotatedString =
    buildAnnotatedString {
        spans.forEach { span ->
            when (span) {
                is InlineSpan.Text -> append(span.text)
                is InlineSpan.Bold -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(span.text) }
                is InlineSpan.Italic -> withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append(span.text) }
                is InlineSpan.Code -> withStyle(
                    SpanStyle(fontFamily = FontFamily.Monospace, background = Color(0x1F000000))
                ) { append(" ${span.text} ") }

                is InlineSpan.Link -> {
                    withLink(
                        LinkAnnotation.Clickable(tag = span.target) { onLinkClick(span.target) }
                    ) {
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF00A9CE),
                                textDecoration = TextDecoration.Underline,
                            )
                        ) { append(span.text) }
                    }
                }
            }
        }
    }

@Composable
private fun TableView(table: MdBlock.Table, onLinkClick: (String) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            Row(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHighest)) {
                table.header.forEach { cell ->
                    Column(modifier = Modifier.width(220.dp).padding(10.dp)) {
                        InlineText(cell, onLinkClick, MaterialTheme.typography.labelLarge)
                    }
                }
            }
            table.rows.forEach { row ->
                Row {
                    row.forEach { cell ->
                        Column(modifier = Modifier.width(220.dp).padding(10.dp)) {
                            InlineText(cell, onLinkClick, MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListBlockView(list: MdBlock.ListBlock, onLinkClick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        list.items.forEachIndexed { index, item ->
            Row {
                Text(
                    text = if (list.ordered) "${index + 1}." else "•",
                    modifier = Modifier.width(24.dp),
                    fontWeight = FontWeight.Bold,
                )
                Column {
                    InlineText(item.spans, onLinkClick)
                    item.subItems.forEach { sub ->
                        Row(modifier = Modifier.padding(top = 4.dp)) {
                            Text(text = "◦", modifier = Modifier.width(24.dp))
                            InlineText(sub.spans, onLinkClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdmonitionView(admonition: MdBlock.Admonition, onLinkClick: (String) -> Unit) {
    val accent = when (admonition.kind) {
        "tip" -> Color(0xFF00A651)
        "caution", "warning" -> Color(0xFFF58220)
        else -> Color(0xFF00A9CE)
    }
    Surface(
        color = accent.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            Icon(
                imageVector = if (admonition.kind == "caution" || admonition.kind == "warning") Icons.Outlined.Warning else Icons.Outlined.Info,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.padding(end = 8.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = admonition.title, fontWeight = FontWeight.Bold, color = accent)
                MarkdownContent(admonition.body, onLinkClick)
            }
        }
    }
}

private val MAX_SCREENSHOT_HEIGHT = 380.dp

@Composable
private fun ImageGalleryView(gallery: MdBlock.ImageGallery) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterHorizontally),
    ) {
        gallery.images.forEach { name ->
            val resource = Res.allDrawableResources[name]
            if (resource != null) {
                Image(
                    painter = painterResource(resource),
                    contentDescription = name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .heightIn(max = MAX_SCREENSHOT_HEIGHT)
                        .clip(RoundedCornerShape(16.dp)),
                )
            }
        }
    }
}
