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
import com.itsaky.androidide.inflater.IView.AttributeChangeListener
import com.itsaky.androidide.inflater.IViewGroup
import com.itsaky.androidide.inflater.internal.utils.simpleName
import com.itsaky.androidide.inflater.internal.utils.tagName
import com.itsaky.androidide.inflater.utils.newAttribute
import com.itsaky.androidide.inflater.viewAdapter
import com.itsaky.androidide.resources.R.drawable
import org.slf4j.LoggerFactory

open class ViewImpl
@JvmOverloads
constructor(
  var file: LayoutFile,
  override val name: String,
  override val view: View,
  override val simpleName: String = name.simpleName(),
  override val tag: String = name.tagName()
) : IView {

  private var fg: Drawable? = null
  private var touched: Drawable? = null

  private val attrChangeListeners = mutableListOf<AttributeChangeListener>()
  private val _attributes = mutableListOf<IAttribute>()
  internal val namespaces = mutableMapOf<String, INamespace>()

  override var parent: IViewGroup? = null
  override val namespaceDecls: Collection<INamespace>
    get() = this.namespaces.values
  override val attributes: List<IAttribute>
    get() = this._attributes

  companion object {

    private val log = LoggerFactory.getLogger(ViewImpl::class.java)
  }

  override fun addAttribute(attribute: IAttribute, apply: Boolean, update: Boolean) {
    if (hasAttribute(attribute)) {
      if (!update) {
        return
      }
      updateAttribute(attribute)
    } else {
      this._attributes.add(attribute)

      if (!apply) {
        // attribute should not be applied
        return
      }

      applyAttribute(attribute)
      notifyAttrAdded(attribute)
    }
  }

  override fun removeAttribute(attribute: IAttribute) {

    val exising = findAttribute(attribute) ?: return
    val value = exising.value

    // reset the value of the attribute to its default value
    applyAttribute(attribute = newAttribute(view = this, attribute = exising, value = ""))

    if (this._attributes.remove(exising)) {
      notifyAttrRemoved(newAttribute(view = this, attribute = exising, value = value))
    }
  }

  override fun updateAttribute(attribute: IAttribute) {
    updateAttributeInternal(attribute)
  }

  override fun findAttribute(name: String, namespaceUri: String?): IAttribute? {
    return this.attributes.find { it.namespace?.uri == namespaceUri && it.name == name }
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

  override fun registerAttributeChangeListener(listener: AttributeChangeListener) {
    if (this.attrChangeListeners.contains(listener)) {
      log.warn("Attempt to register an already-registered AttributeChangeListener")
      return
    }
    this.attrChangeListeners.add(listener)
  }

  override fun unregisterAttributeChangeListener(listener: AttributeChangeListener) {
    this.attrChangeListeners.remove(listener)
  }

  override fun applyAttribute(attribute: IAttribute) {
    val adapter = viewAdapter
    if (adapter == null) {
      log.warn("No attribute adapter found for view {}", name)
      return
    }
    adapter.apply(this, attribute)
  }

  fun findNamespaces(): Set<INamespace> {
    return hashSetOf<INamespace>().apply {
      addAll(namespaces.values)
      if (parent is ViewImpl) {
        (parent as? ViewImpl)?.findNamespaces()?.let { addAll(it) }
      }
    }
  }

  fun findNamespaceByUri(uri: String): INamespace? {
    return this.namespaces[uri] ?: (parent as? ViewImpl)?.findNamespaceByUri(uri)
  }

  open fun immutable(): IView {
    return ImmutableViewImpl(this)
  }

  protected open fun hasAttribute(attribute: IAttribute): Boolean {
    return hasAttribute(attribute.name, attribute.namespace?.uri)
  }

  protected open fun findAttribute(attribute: IAttribute): IAttribute? {
    return findAttribute(attribute.name, attribute.namespace?.uri)
  }

  internal open fun printHierarchy(): String {
    return StringBuilder().apply { printHierarchy(this, 0) }.toString()
  }

  internal open fun printHierarchy(builder: StringBuilder, indent: Int) {
    builder.append(" ".repeat(indent * 4))
    builder.append(name)
    builder.append("\n")
  }

  private fun updateAttributeInternal(attribute: IAttribute, notify: Boolean = true) {
    val existing =
      findAttribute(attribute)
        ?: throw IllegalArgumentException("Attribute '${attribute.name}' not found")

    val oldVal = existing.value
    existing.value = attribute.value
    applyAttribute(existing)

    if (notify) {
      notifyAttrUpdated(newAttribute(view = this, attribute = attribute), oldVal)
    }
  }

  private fun notifyAttrAdded(attribute: IAttribute) {
    for (listener in this.attrChangeListeners) {
      listener.onAttributeAdded(this, attribute)
    }
  }

  private fun notifyAttrRemoved(attribute: IAttribute) {
    for (listener in this.attrChangeListeners) {
      listener.onAttributeRemoved(this, attribute)
    }
  }

  private fun notifyAttrUpdated(attribute: IAttribute, oldValue: String) {
    for (listener in this.attrChangeListeners) {
      listener.onAttributeUpdated(this, attribute, oldValue)
    }
  }
}
