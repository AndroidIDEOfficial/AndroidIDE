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

package com.itsaky.androidide.xml.internal.widgets

import com.itsaky.androidide.xml.internal.widgets.util.DefaultWidget
import com.itsaky.androidide.xml.internal.widgets.util.WidgetParser
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import org.slf4j.LoggerFactory

/**
 * Default implementation of [WidgetTable].
 *
 * @author Akash Yadav
 */
class DefaultWidgetTable : WidgetTable {

  private val root: WidgetNode = WidgetNode(name = "", isWidget = false)

  companion object {

    private val log = LoggerFactory.getLogger(DefaultWidgetTable::class.java)
  }

  override fun getWidget(name: String): Widget? {
    val node = getNode(name)!!
    if (!node.isWidget) {
      return null
    }

    return node.widget
  }

  override fun findWidgetWithSimpleName(name: String): Widget? {
    return findWidgetWithSimpleName(name, root)
  }

  override fun getAllWidgets(): Set<Widget> {
    val result = mutableSetOf<Widget>()
    root.children.values.forEach { result.addAll(collectWidgets(it)) }
    return result
  }

  private fun collectWidgets(node: WidgetNode): Set<Widget> {
    val result = mutableSetOf<Widget>()
    if (node.widget != null) {
      result.add(node.widget)
    }

    node.children.values.forEach { result.addAll(collectWidgets(it)) }

    return result
  }

  private fun findWidgetWithSimpleName(name: String, root: WidgetNode): Widget? {
    for (child in root.children.values) {
      if (child.widget?.simpleName == name) {
        return child.widget
      }

      if (child.children.isEmpty()) {
        continue
      }

      val inner = findWidgetWithSimpleName(name, child)
      if (inner != null) {
        return inner
      }
    }

    return null
  }

  internal fun putWidget(line: String) {
    val widget =
      WidgetParser.parse(line)
        ?: run {
          log.debug("Cannot parse widget from line: {}", line)
          return
        }

    putWidget(widget)
  }

  internal fun putWidget(widget: DefaultWidget) {
    val name = widget.qualifiedName.substringBeforeLast('.')
    val node = getNode(name)!!
    val existing = node.children[widget.simpleName]
    val newNode = WidgetNode(name = widget.qualifiedName, isWidget = true, widget = widget)

    if (existing == null) {
      node.children[widget.simpleName] = newNode
      return
    }

    // Happens when the inner classes are defined earlier than the parent class
    // For example, when FrameLayout.LayoutParams is defined earlier than FrameLayout

    newNode.children.putAll(existing.children)
    node.children[widget.simpleName] = newNode
  }

  @JvmOverloads
  fun getNode(name: String, createIfNotPresent: Boolean = true): WidgetNode? {
    if (name.isBlank()) {
      return root
    }

    if (!name.contains('.')) {
      return if (createIfNotPresent) {
        root.children.computeIfAbsent(name) { WidgetNode(name = name, isWidget = false) }
      } else {
        root.children[name]
      }
    }

    val segments = name.split('.')
    var node: WidgetNode? = root
    for (segment in segments) {
      if (node == null) {
        break
      }

      node =
        if (createIfNotPresent) {
          node.children.computeIfAbsent(segment) { WidgetNode(name = segment, isWidget = false) }
        } else {
          node.children[segment]
        }
    }

    return node
  }

  inner class WidgetNode(
    val name: String,
    val isWidget: Boolean,
    val widget: DefaultWidget? = null,
    val children: MutableMap<String, WidgetNode> = mutableMapOf()
  ) {

    override fun toString(): String {
      return "WidgetNode(name='$name', isWidget=$isWidget, widget=$widget, children=$children)"
    }

    override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other !is WidgetNode) return false

      if (name != other.name) return false
      if (isWidget != other.isWidget) return false
      if (widget != other.widget) return false
      if (children != other.children) return false

      return true
    }

    override fun hashCode(): Int {
      var result = name.hashCode()
      result = 31 * result + isWidget.hashCode()
      result = 31 * result + (widget?.hashCode() ?: 0)
      result = 31 * result + children.hashCode()
      return result
    }
  }
}
