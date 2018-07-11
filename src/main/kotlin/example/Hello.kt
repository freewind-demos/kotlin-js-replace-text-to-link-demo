package example

import org.w3c.dom.*
import kotlin.browser.document

private val keyword = "Kotlin"
private val newLink = """<a href="https://kotlinlang.org/" target="_blank">Kotlin</a>"""

fun main(args: Array<String>) {
    val main = document.getElementById("main")!!
    walkAll(main) { text ->
        if (text.wholeText.contains("Kotlin")) {
            val parts = text.wholeText.split(keyword, ignoreCase = true)
            val result = mutableListOf<Node>()
            parts.forEachIndexed { index, part ->
                result.add(document.createTextNode(part))
                if (index > 0) {
                    result.add(document.fromHtml(newLink)!!)
                }
            }
            result
        } else {
            null
        }
    }
}

fun walkAll(element: Element, fn: (Text) -> List<Node>?) {
    val children = element.childNodes
    for (i in 0 until children.length) {
        children[i]?.let { child ->
            if (child is Text) {
                val newNodes = fn(child)
                if (newNodes != null) {
                    newNodes.forEach { node ->
                        element.insertBefore(node, child)
                    }
                    element.removeChild(child)
                }
            }
            if (child is Element) {
                walkAll(child, fn)
            }
        }
    }
}


private fun Document.fromHtml(html: String): Node? {
    val template = this.createElement("template") as HTMLTemplateElement
    template.innerHTML = html
    return template.content.firstChild
}