package no.nordicsemi.nrf.matter.docs.markdown

sealed class InlineSpan {
    data class Text(val text: String) : InlineSpan()
    data class Bold(val text: String) : InlineSpan()
    data class Italic(val text: String) : InlineSpan()
    data class Code(val text: String) : InlineSpan()
    data class Link(val text: String, val target: String) : InlineSpan()
}

data class ListItem(
    val spans: List<InlineSpan>,
    val subItems: List<ListItem> = emptyList(),
)

sealed class MdBlock {
    data class Heading(val level: Int, val text: String, val slug: String) : MdBlock()
    data class Paragraph(val spans: List<InlineSpan>) : MdBlock()
    data class CodeBlock(val lang: String?, val code: String) : MdBlock()
    data class Table(val header: List<List<InlineSpan>>, val rows: List<List<List<InlineSpan>>>) : MdBlock()
    data class ListBlock(val items: List<ListItem>, val ordered: Boolean) : MdBlock()
    data class Admonition(val kind: String, val title: String, val body: List<MdBlock>) : MdBlock()
    data class ImageGallery(val images: List<String>) : MdBlock()
    data class BadgeLinks(val badges: List<InlineSpan.Link>) : MdBlock()
}

fun slugify(text: String): String {
    val cleaned = text.lowercase().map { c -> if (c.isLetterOrDigit()) c else '-' }.joinToString("")
    return cleaned.split('-').filter { it.isNotBlank() }.joinToString("-")
}

private val headingRegex = Regex("""^(#{1,6})\s+(.*)$""")
private val admonitionRegex = Regex("""^!!!\s+(\S+)\s+"([^"]*)"\s*$""")
private val tableSeparatorRegex = Regex("""^\s*\|?\s*:?-{2,}:?\s*(\|\s*:?-{2,}:?\s*)*\|?\s*$""")
private val listMarkerRegex = Regex("""^(\s*)([-*+]|\d+\.)\s+(.*)$""")
private val imgSrcRegex = Regex("""<img[^>]*\ssrc="([^"]+)"""")
private val badgeLineRegex = Regex("""^(\s*\[!\[[^]]*]\([^)]*\)]\([^)]+\)\s*)+$""")
private val badgeItemRegex = Regex("""\[!\[([^]]*)]\([^)]*\)]\(([^)]+)\)""")

object MarkdownParser {

    fun parse(source: String): List<MdBlock> {
        val lines = source.replace("\r\n", "\n").split("\n")
        return parseLines(lines)
    }

    private fun parseLines(lines: List<String>): List<MdBlock> {
        val blocks = mutableListOf<MdBlock>()
        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            when {
                line.isBlank() -> i++

                line.trimStart().let { it.startsWith("<div") || it.startsWith("</div>") } -> i++

                line.contains("<img") -> {
                    val images = mutableListOf<String>()
                    while (i < lines.size) {
                        val l = lines[i]
                        if (l.contains("<img")) {
                            val src = imgSrcRegex.find(l)?.groupValues?.get(1)
                            if (src != null && src.startsWith("./screenshots/")) {
                                images += src.removePrefix("./screenshots/").removeSuffix(".png")
                            }
                            i++
                        } else if (l.trimStart().let { it.startsWith("<div") || it.startsWith("</div>") } || l.isBlank()) {
                            i++
                        } else {
                            break
                        }
                    }
                    if (images.isNotEmpty()) blocks += MdBlock.ImageGallery(images)
                }

                headingRegex.matches(line) -> {
                    val m = headingRegex.find(line)!!
                    val level = m.groupValues[1].length
                    val text = m.groupValues[2].trim()
                    blocks += MdBlock.Heading(level, text, slugify(text))
                    i++
                }

                line.trimStart().startsWith("```") -> {
                    val lang = line.trimStart().removePrefix("```").trim().ifBlank { null }
                    i++
                    val code = mutableListOf<String>()
                    while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                        code += lines[i]
                        i++
                    }
                    if (i < lines.size) i++ // skip closing fence
                    blocks += MdBlock.CodeBlock(lang, code.joinToString("\n"))
                }

                admonitionRegex.matches(line) -> {
                    val m = admonitionRegex.find(line)!!
                    val kind = m.groupValues[1].lowercase()
                    val title = m.groupValues[2]
                    i++
                    if (i < lines.size && lines[i].isBlank()) i++
                    val bodyLines = mutableListOf<String>()
                    while (i < lines.size && (lines[i].startsWith("    ") || lines[i].isBlank())) {
                        if (lines[i].isBlank()) {
                            bodyLines += ""
                        } else {
                            bodyLines += lines[i].removePrefix("    ")
                        }
                        i++
                    }
                    // trim trailing blank lines
                    while (bodyLines.isNotEmpty() && bodyLines.last().isBlank()) bodyLines.removeAt(bodyLines.size - 1)
                    if (bodyLines.isEmpty() && i < lines.size && lines[i].isNotBlank()) {
                        bodyLines += lines[i]
                        i++
                    }
                    blocks += MdBlock.Admonition(kind, title, parseLines(bodyLines))
                }

                line.contains("|") && i + 1 < lines.size && tableSeparatorRegex.matches(lines[i + 1]) -> {
                    val header = splitTableRow(line)
                    i += 2
                    val rows = mutableListOf<List<String>>()
                    while (i < lines.size && lines[i].contains("|") && lines[i].isNotBlank()) {
                        rows += splitTableRow(lines[i])
                        i++
                    }
                    blocks += MdBlock.Table(
                        header = header.map { parseInline(it) },
                        rows = rows.map { row -> row.map { parseInline(it) } },
                    )
                }

                badgeLineRegex.matches(line) -> {
                    val badges = badgeItemRegex.findAll(line).map { m ->
                        InlineSpan.Link(m.groupValues[1], m.groupValues[2])
                    }.toList()
                    blocks += MdBlock.BadgeLinks(badges)
                    i++
                }

                listMarkerRegex.matches(line) -> {
                    val (consumed, listBlock) = parseList(lines, i)
                    blocks += listBlock
                    i = consumed
                }

                else -> {
                    val paragraphLines = mutableListOf<String>()
                    while (i < lines.size && lines[i].isNotBlank() &&
                        !headingRegex.matches(lines[i]) &&
                        !admonitionRegex.matches(lines[i]) &&
                        !listMarkerRegex.matches(lines[i]) &&
                        !lines[i].trimStart().startsWith("```") &&
                        !lines[i].contains("<img") &&
                        !(i + 1 < lines.size && lines[i].contains("|") && tableSeparatorRegex.matches(lines[i + 1]))
                    ) {
                        paragraphLines += lines[i].trim()
                        i++
                    }
                    val text = paragraphLines.joinToString(" ")
                    if (text.isNotBlank()) blocks += MdBlock.Paragraph(parseInline(text))
                }
            }
        }
        return blocks
    }

    private fun splitTableRow(line: String): List<String> {
        var trimmed = line.trim()
        if (trimmed.startsWith("|")) trimmed = trimmed.removePrefix("|")
        if (trimmed.endsWith("|")) trimmed = trimmed.removeSuffix("|")
        return trimmed.split("|").map { it.trim() }
    }

    private class RawItem(val text: StringBuilder, val subItems: MutableList<RawItem> = mutableListOf())

    private fun parseList(lines: List<String>, start: Int): Pair<Int, MdBlock.ListBlock> {
        var i = start
        val items = mutableListOf<RawItem>()
        var currentTop: RawItem? = null
        var currentSub: RawItem? = null
        var baseIndent = -1
        var ordered = false

        fun isContinuation(line: String): Boolean =
            line.isNotBlank() && (line.length - line.trimStart().length) > 0 && !listMarkerRegex.matches(line)

        while (i < lines.size) {
            val line = lines[i]
            if (line.isBlank()) {
                val next = lines.getOrNull(i + 1)
                if (next == null || !(listMarkerRegex.matches(next) || isContinuation(next))) break
                i++
                continue
            }
            val m = listMarkerRegex.find(line)
            if (m != null) {
                val indent = m.groupValues[1].length
                val marker = m.groupValues[2]
                val text = m.groupValues[3]
                if (baseIndent == -1) {
                    baseIndent = indent
                    ordered = marker.endsWith(".")
                }
                if (indent <= baseIndent + 1) {
                    val item = RawItem(StringBuilder(text))
                    items += item
                    currentTop = item
                    currentSub = null
                } else {
                    val item = RawItem(StringBuilder(text))
                    currentTop?.subItems?.add(item)
                    currentSub = item
                }
                i++
            } else if (isContinuation(line)) {
                val target = currentSub ?: currentTop
                target?.text?.append(' ')?.append(line.trim())
                i++
            } else {
                break
            }
        }

        val listItems = items.map { top ->
            ListItem(
                spans = parseInline(top.text.toString()),
                subItems = top.subItems.map { sub -> ListItem(parseInline(sub.text.toString())) },
            )
        }
        return i to MdBlock.ListBlock(listItems, ordered)
    }

    private val inlineRegex = Regex("""\*\*(.+?)\*\*|`([^`]+)`|\[([^]]+)]\(([^)]+)\)|\*(.+?)\*""")

    fun parseInline(text: String): List<InlineSpan> {
        val spans = mutableListOf<InlineSpan>()
        var last = 0
        for (m in inlineRegex.findAll(text)) {
            if (m.range.first > last) spans += InlineSpan.Text(text.substring(last, m.range.first))
            val g = m.groupValues
            when {
                g[1].isNotEmpty() -> spans += InlineSpan.Bold(g[1])
                g[2].isNotEmpty() -> spans += InlineSpan.Code(g[2])
                g[3].isNotEmpty() -> spans += InlineSpan.Link(g[3], g[4])
                g[5].isNotEmpty() -> spans += InlineSpan.Italic(g[5])
            }
            last = m.range.last + 1
        }
        if (last < text.length) spans += InlineSpan.Text(text.substring(last))
        return spans
    }
}
