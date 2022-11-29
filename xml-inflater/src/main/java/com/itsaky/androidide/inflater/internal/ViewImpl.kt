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

package com.itsaky.androidide.inflater.internal

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat.getDrawable
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.R.drawable
import com.itsaky.androidide.inflater.internal.utils.simpleName
import com.itsaky.androidide.inflater.internal.utils.tagName
import com.itsaky.androidide.utils.ILogger

open class ViewImpl
@JvmOverloads
constructor(
  val file: LayoutFile,
  override val name: String,
  override val view: View,
  override val simpleName: String = name.simpleName(),
  override val tag: String = name.tagName()
) : IView {
  private val log = ILogger.newInstance(javaClass.simpleName)

  private var fg: Drawable? = null
  private var touched: Drawable? = null

  internal val attributes = mutableListOf<IAttribute>()

  override var parent: IViewGroup? = null
  internal val namespaceDecls = mutableMapOf<String, INamespace>()

  override fun addAttribute(attribute: IAttribute, update: Boolean) {
    if (hasAttribute(attribute)) {
      if (!update) {
        return
      }
      updateAttribute(attribute)
    } else {
      this.attributes.add(attribute)
      applyAttribute(attribute)
    }
  }

  override fun removeAttribute(attribute: IAttribute) {
    this.attributes.remove(attribute)
    // TODO(itsaky): Should attribute adapters handle this as well?
  }

  override fun updateAttribute(attribute: IAttribute) {
    val existing =
      findAttribute(attribute)
        ?: throw IllegalArgumentException("Attribute '${attribute.name}' not found")
    existing.value = attribute.value
    applyAttribute(existing)
  }

  override fun findAttribute(namespaceUri: String, name: String): IAttribute? {
    return this.attributes.find { it.namespace.uri == namespaceUri && it.name == name }
  }

  override fun onHighlightStateUpdated(highlight: Boolean) {
    if (highlight) {
      this.fg = view.foreground
      this.touched = this.touched ?: getDrawable(view.context, drawable.bg_designer_view)
      view.foreground = this.touched
    } else {
      view.foreground = this.fg
    }
  }

  protected open fun applyAttribute(attribute: IAttribute) {
    val adapter = ViewAdapterIndex.getAdapter(name)
    if (adapter == null) {
      log.warn("No attribute adapter found for view $name")
      return
    }
    adapter.apply(this, attribute)
  }

  protected open fun hasAttribute(attribute: IAttribute): Boolean {
    return hasAttribute(attribute.namespace.uri, attribute.name)
  }

  protected open fun findAttribute(attribute: IAttribute): IAttribute? {
    return findAttribute(attribute.namespace.uri, attribute.name)
  }

  internal fun findNamespaceByUri(uri: String): INamespace? {
    return this.namespaceDecls[uri] ?: (parent as? ViewImpl)?.findNamespaceByUri(uri)
  }

  internal open fun printHierarchy(): String {
    return StringBuilder().apply { printHierarchy(this, 0) }.toString()
  }

  internal open fun printHierarchy(builder: StringBuilder, indent: Int) {
    builder.append(" ".repeat(indent * 4))
    builder.append(name)
    builder.append("\n")
  }
}
