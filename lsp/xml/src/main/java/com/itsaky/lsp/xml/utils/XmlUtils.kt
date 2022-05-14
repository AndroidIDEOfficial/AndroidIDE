/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.lsp.xml.utils

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.itsaky.androidide.lexers.xml.XMLLexer
import com.itsaky.androidide.utils.CharSequenceReader
import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.xml.INamespace.Resolver
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMElement
import org.eclipse.lemminx.dom.DOMNode
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.util.*

/** @author Akash Yadav */
object XmlUtils {

    private val NAMESPACE_RESOLVER_KEY: String = "namespaceResolver"
    private val caches: WeakHashMap<DOMNode, MutableMap<String, Any>> = WeakHashMap()

    private val LOG = ILogger.newInstance("XmlUtils")
    private val parserFactory: XmlPullParserFactory =
        try {
            XmlPullParserFactory.newInstance()
        } catch (e: XmlPullParserException) {
            LOG.error("Unable to create pull parser factory")
            throw RuntimeException(e)
        }

    @Throws(XmlPullParserException::class)
    fun newParser(): XmlPullParser {
        return parserFactory.newPullParser()
    }

    @Throws(XmlPullParserException::class)
    fun newParser(contents: CharSequence?): XmlPullParser {
        val parser = parserFactory.newPullParser()
        parser.setInput(CharSequenceReader(contents))
        return parser
    }

    fun isTag(node: DOMNode, index: Int): Boolean {
        var name = node.nodeName
        if (name == null) {
            name = ""
        }
        return node.start < index && index <= node.start + name.length + 1
    }

    fun isEndTag(node: DOMNode?, index: Int): Boolean {
        if (node !is DOMElement) {
            return false
        }
        val endOpenOffset = node.endTagOpenOffset
        return if (endOpenOffset == -1) {
            false
        } else index >= endOpenOffset
    }

    fun isInAttributeValue(contents: String?, index: Int): Boolean {
        val lexer = XMLLexer(CharStreams.fromString(contents))
        var token: Token
        while (lexer.nextToken().also { token = it } != null) {
            val start = token.startIndex
            val end = token.stopIndex
            if (token.type == Token.EOF) {
                break
            }

            if (index in start..end) {
                return token.type == XMLLexer.STRING
            }
            if (end > index) {
                break
            }
        }
        return false
    }

    fun getPrefix(parsed: DOMDocument, index: Int, type: NodeType?): String? {
        val text = parsed.text
        when (type) {
            TAG -> {
                val nodeAt = parsed.findNodeAt(index) ?: return null
                return text.substring(nodeAt.start, index)
            }
            ATTRIBUTE -> {
                val attr = parsed.findAttrAt(index) ?: return null
                return text.substring(attr.start, index)
            }
            ATTRIBUTE_VALUE -> {
                val attrAt = parsed.findAttrAt(index) ?: return null
                var prefix = text.substring(attrAt.nodeAttrValue.start + 1, index)
                if (prefix.contains("|")) {
                    prefix = prefix.substring(prefix.lastIndexOf('|') + 1)
                }
                return prefix
            }
            else -> return "<this-will-not-match>"
        }
    }

    fun getNodeType(parsed: DOMDocument, cursor: Int): NodeType {
        val nodeAt = parsed.findNodeAt(cursor) ?: return NodeType.UNKNOWN

        if (isTag(nodeAt, cursor) || isEndTag(nodeAt, cursor)) {
            return TAG
        }

        return if (isInAttributeValue(parsed.textDocument.text, cursor)) {
            ATTRIBUTE_VALUE
        } else {
            ATTRIBUTE
        }
    }

    @Nullable
    fun getRootElement(@NonNull document: DOMDocument): DOMElement? {
        val roots = document.roots
        for (root in roots) {
            if (root is DOMElement) {
                return root
            }
        }
        return null
    }

    fun getNamespaceResolver(document: DOMDocument): Resolver {
        val rootElement = getRootElement(document) ?: return Resolver.EMPTY
        val userData: Any? = retrieveCache(rootElement, NAMESPACE_RESOLVER_KEY)
        if (userData is Resolver) {
            return userData
        }

        val resolver: Resolver =
            object : Resolver {
                override fun findPrefix(@NonNull namespaceUri: String?): String? {
                    return rootElement.getPrefix(namespaceUri)
                }

                override fun findUri(@NonNull prefix: String?): String? {
                    val xmlns = rootElement.getAttributeNode("xmlns", prefix)
                    return xmlns?.value
                }
            }

        updateCache(rootElement, NAMESPACE_RESOLVER_KEY, resolver)
        return resolver
    }

    private fun retrieveCache(node: DOMNode?, key: String): Any? {
        val map = caches[node] ?: return null
        return map[key]
    }

    private fun updateCache(@NonNull node: DOMNode?, @NonNull key: String, value: Any) {
        caches.computeIfAbsent(node) { HashMap() }
        val map: MutableMap<String, Any>? = caches[node]
        if (map != null) {
            map[key] = value
        }
    }

    enum class NodeType {
        UNKNOWN,
        TAG,
        ATTRIBUTE,
        ATTRIBUTE_VALUE
    }
}
