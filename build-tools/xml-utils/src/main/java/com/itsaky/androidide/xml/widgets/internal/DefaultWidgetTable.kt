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

package com.itsaky.androidide.xml.widgets.internal

import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.xml.widgets.Widget
import com.itsaky.androidide.xml.widgets.WidgetTable
import com.itsaky.androidide.xml.widgets.internal.util.DefaultWidget
import com.itsaky.androidide.xml.widgets.internal.util.WidgetParser

/**
 * Default implementation of [WidgetTable].
 *
 * @author Akash Yadav
 */
class DefaultWidgetTable : WidgetTable {

  private val log = ILogger.newInstance(javaClass.simpleName)
  private val root: WidgetNode = WidgetNode(name = "", isWidget = false)

  override fun getWidget(name: String): Widget? {
    val node = getNode(name)
    if (!node.isWidget) {
      return null
    }

    return node.widget
  }

  internal fun putWidget(line: String) {
    val widget =
      WidgetParser.parse(line)
        ?: run {
          log.debug("Cannot parse widget from line: $line")
          return
        }
  
    putWidget(widget)
  }

  internal fun putWidget(widget: DefaultWidget) {
    val name = widget.qualifiedName.substringBeforeLast('.')
    val node = getNode(name)
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

  internal fun getNode(name: String): WidgetNode {
    if (name.isBlank()) {
      return root
    }

    if (!name.contains('.')) {
      return root.children.computeIfAbsent(name) { WidgetNode(name = name, isWidget = false) }
    }

    val segments = name.split('.')
    var node: WidgetNode = root
    for (segment in segments) {
      node = node.children.computeIfAbsent(segment) { WidgetNode(name = segment, isWidget = false) }
    }

    return node
  }

  internal inner class WidgetNode(
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
