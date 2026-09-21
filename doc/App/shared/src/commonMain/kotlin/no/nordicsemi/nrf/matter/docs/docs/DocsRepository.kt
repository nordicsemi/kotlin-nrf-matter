package no.nordicsemi.nrf.matter.docs.docs

import docs.shared.generated.resources.Res
import no.nordicsemi.nrf.matter.docs.markdown.MarkdownParser
import no.nordicsemi.nrf.matter.docs.markdown.MdBlock

data class DocSection(val id: String, val title: String, val blocks: List<MdBlock>)

sealed class LinkTarget {
    data class Internal(val anchor: DocAnchor) : LinkTarget()
    data class External(val url: String) : LinkTarget()
}

object DocsRepository {
    private val blocksCache = mutableMapOf<DocPage, List<MdBlock>>()
    private val sectionsCache = mutableMapOf<DocPage, List<DocSection>>()

    suspend fun blocksOf(page: DocPage): List<MdBlock> {
        blocksCache[page]?.let { return it }
        val bytes = Res.readBytes("files/docs/${page.resourceFile}")
        val blocks = MarkdownParser.parse(bytes.decodeToString())
        blocksCache[page] = blocks
        return blocks
    }

    suspend fun sectionsOf(page: DocPage): List<DocSection> {
        sectionsCache[page]?.let { return it }
        val blocks = blocksOf(page)
        val sections = mutableListOf<DocSection>()
        var current = mutableListOf<MdBlock>()
        var currentId = "top"
        var currentTitle = page.displayTitle
        for (block in blocks) {
            if (block is MdBlock.Heading && block.level in 2..3) {
                sections += DocSection(currentId, currentTitle, current)
                current = mutableListOf()
                currentId = block.slug
                currentTitle = block.text
            } else {
                current.add(block)
            }
        }
        sections += DocSection(currentId, currentTitle, current)
        val result = sections.filter { it.blocks.isNotEmpty() }.ifEmpty { sections }
        sectionsCache[page] = result
        return result
    }

    suspend fun section(anchor: DocAnchor): DocSection? {
        val sections = sectionsOf(anchor.page)
        if (anchor.sectionId == null) return sections.firstOrNull()
        return sections.firstOrNull { it.id == anchor.sectionId } ?: sections.firstOrNull()
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
