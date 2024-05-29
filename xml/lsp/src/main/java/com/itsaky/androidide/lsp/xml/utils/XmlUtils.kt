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
package com.itsaky.androidide.lsp.xml.utils

import com.itsaky.androidide.lexers.xml.XMLLexer
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.ATTRIBUTE_VALUE
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.TAG
import com.itsaky.androidide.lsp.xml.utils.XmlUtils.NodeType.UNKNOWN
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token
import org.eclipse.lemminx.dom.DOMDocument
import org.eclipse.lemminx.dom.DOMElement
import org.eclipse.lemminx.dom.DOMNode
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
object XmlUtils {
  
  private val log = LoggerFactory.getLogger(XmlUtils::class.java)
  
  fun isTag(node: DOMNode, index: Int): Boolean {
    var name = node.nodeName
    if (name.isNullOrBlank()) {
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
    return when (type) {
      TAG -> {
        val nodeAt = parsed.findNodeAt(index) ?: run {
          log.warn("Unable to find node at index {}", index)
          return null
        }
        text.substring(nodeAt.start, index)
      }
      ATTRIBUTE -> {
        val attr = parsed.findAttrAt(index) ?: run {
          log.warn("Unable to find attribute at index {}", index)
          return null
        }
  
        text.substring(attr.start, index)
      }
      ATTRIBUTE_VALUE -> {
        val attrAt = parsed.findAttrAt(index) ?: run {
          log.warn("Unable to find attribute at index {}", index)
          return null
        }
  
        var prefix = text.substring(attrAt.nodeAttrValue.start + 1, index)
        if (prefix.contains("|")) {
          prefix = prefix.substring(prefix.lastIndexOf('|') + 1)
        }
        prefix
      }
      else -> "<this-will-not-match>"
    }
  }

  fun getNodeType(parsed: DOMDocument, cursor: Int): NodeType {
    val nodeAt = parsed.findNodeAt(cursor) ?: run {
      log.warn("Unable to find node at index {}", cursor)
      return UNKNOWN
    }
  
  
    if (isTag(nodeAt, cursor) || isEndTag(nodeAt, cursor)) {
      return TAG
    }

    return if (isInAttributeValue(parsed.textDocument.text, cursor)) {
      ATTRIBUTE_VALUE
    } else {
      ATTRIBUTE
    }
  }
  
  enum class NodeType {
    UNKNOWN,
    TAG,
    ATTRIBUTE,
    ATTRIBUTE_VALUE
  }
}
