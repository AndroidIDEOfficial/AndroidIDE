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

package com.itsaky.androidide.inflater

import android.view.View
import com.android.SdkConstants
import com.itsaky.androidide.inflater.internal.AttributeImpl
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.models.UiWidget

/**
 * Handles logic for applying attributes to a view.
 *
 * @author Akash Yadav
 */
abstract class IViewAdapter<T : View> : AbstractParser() {

  /**
   * The package name or namespace of the module/artifact in which the view that this adapter
   * handles is defined. The value is set to "android" by default unless explicitly specified in the
   * [ViewAdapter][com.itsaky.androidide.annotations.inflater.ViewAdapter] annotation.
   *
   * This is used by the UI designer to quickly look for attributes of an inflated view from the
   * resource tables.
   */
  var moduleNamespace: String = ""
    set(value) {
      if (field.isNotEmpty()) {
        throw UnsupportedOperationException()
      }
      field = value
    }

  /** The attributes that are supported by the adapter. The attributes in the list are immutable. */
  val supportedAttributes by lazy {
    attributeHandlers.map {
      AttributeImpl(namespace = defaultNamespace(), name = it.key, value = "").immutable()
    }
  }

  private val attributeHandlers by lazy {
    val handlers = mutableMapOf<String, AttributeHandlerScope<T>.() -> Unit>()
    createAttrHandlers(handlers::put)
    return@lazy handlers
  }

  private val widget by lazy { createUiWidgets() }

  /**
   * Get the [UiWidget] model that can be used to list this adapter's view in the UI designer.
   *
   * @throws UnsupportedOperationException If this view adapter does not adapt a UI designer widget.
   */
  fun getUiWidgets(): List<UiWidget> {
    return widget
  }

  /**
   * Create [UiWidget]s which will be available to the UI Designer.
   *
   * The default implementation throws [UnsupportedOperationException].
   */
  protected open fun createUiWidgets(): List<UiWidget> {
    throw UnsupportedOperationException(
      "${javaClass.simpleName} is not a UI Designer widget adapter"
    )
  }

  /**
   * Apply the given attribute to the given view.
   *
   * @param view The view to which the attribute must be applied.
   * @param attribute The attribute to apply.
   * @return Whether the attribute was applied or not.
   */
  open fun apply(view: IView, attribute: IAttribute): Boolean {
    return doApply(view, attribute) { applyInternal() }
  }

  /**
   * Apply the basic attributes to a view so that it could be rendered.
   *
   * @param view The view to apply basic attributes to.
   */
  abstract fun applyBasic(view: IView)

  /**
   * Called to check whether the given attribute required for the view. A required view cannot be
   * removed by the user.
   *
   * @param attribute The attribute to check.
   * @return `true` if the attribute is required, `false` otherwise.
   */
  abstract fun isRequiredAttribute(attribute: IAttribute): Boolean

  /**
   * The default namespace that will be used by the UI designer to create and apply new attributes.
   */
  protected open fun defaultNamespace(): INamespace {
    return INamespace.ANDROID
  }

  protected open fun canHandleNamespace(namespace: INamespace): Boolean {
    return this.canHandleNamespace(namespace.uri)
  }
  protected open fun canHandleNamespace(nsUri: String): Boolean {
    return SdkConstants.ANDROID_URI == nsUri
  }

  protected open fun AttributeHandlerScope<T>.applyInternal(): Boolean {
    val handler = attributeHandlers[name]
    if (handler != null) {
      handler()
    }
    return handler != null || applyLayoutParams()
  }

  protected open fun AttributeHandlerScope<T>.applyLayoutParams(): Boolean {
    return false
  }

  /**
   * Provides easy access to various properties related to the view and attribute when applying an
   * attributes.
   */
  private fun doApply(
    view: IView,
    attribute: IAttribute,
    apply: AttributeHandlerScope<T>.() -> Boolean,
  ): Boolean {

    if (!canHandleNamespace(attribute.namespace)) {
      return false
    }

    @Suppress("UNCHECKED_CAST")
    return (view.view as T).let {
      val file = (view as ViewImpl).file
      return@let AttributeHandlerScope(
          it,
          file,
          it.context,
          it.layoutParams,
          attribute.namespace,
          attribute.name,
          attribute.value
        )
        .let(apply)
    }
  }

  /**
   * Creates the attribute handlers. Subclasses are expected to use the [create] function to create
   * new handlers.
   *
   * A subclass should call the `super` method and add only the view specific attribute handlers.
   */
  protected open fun createAttrHandlers(
    create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit
  ) {}
}
