package no.nordicsemi.nrf.matter.docs.docs

import docs.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.docs.markdown.MarkdownParser
import no.nordicsemi.nrf.matter.docs.markdown.MdBlock

data class DocStepPage(val title: String, val blocks: List<MdBlock>)

fun buildStepPages(blocks: List<MdBlock>, fallbackTitle: String): List<DocStepPage> {
    val pages = mutableListOf<DocStepPage>()
    var current = mutableListOf<MdBlock>()
    var currentTitle = fallbackTitle

    fun flush() {
        if (current.isNotEmpty()) pages += DocStepPage(currentTitle, current)
        current = mutableListOf()
    }

    for (block in blocks) {
        if (block is MdBlock.Heading && block.level <= 2) {
            flush()
            currentTitle = block.text
        } else {
            current += block
        }
        if (block is MdBlock.ImageGallery) flush()
    }
    flush()
    return pages
}

sealed class LinkTarget {
    data class Internal(val anchor: DocAnchor) : LinkTarget()
    data class External(val url: String) : LinkTarget()
}

object DocsRepository {
    private val blocksCache = mutableMapOf<DocPage, List<MdBlock>>()

    suspend fun blocksOf(page: DocPage): List<MdBlock> {
        blocksCache[page]?.let { return it }
        val bytes = Res.readBytes("files/docs/${page.resourceFile}")
        val blocks = MarkdownParser.parse(bytes.decodeToString())
        blocksCache[page] = blocks
        return blocks
    }

    fun resolveLink(target: String, currentPage: DocPage): LinkTarget {
        if (target.startsWith("http://") || target.startsWith("https://")) {
            return LinkTarget.External(target)
        }
        if (target.startsWith("#")) {
            return LinkTarget.Internal(DocAnchor(currentPage, target.removePrefix("#")))
        }
        val hashIndex = target.indexOf('#')
        val filePart = if (hashIndex >= 0) target.substring(0, hashIndex) else target
        val sectionPart = if (hashIndex >= 0) target.substring(hashIndex + 1) else null
        val page = DocPage.entries.firstOrNull { it.resourceFile == filePart }
        return if (page != null) {
            LinkTarget.Internal(DocAnchor(page, sectionPart))
        } else {
            LinkTarget.External(target)
        }
    }
}
