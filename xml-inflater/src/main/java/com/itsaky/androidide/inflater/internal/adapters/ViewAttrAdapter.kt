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

package com.itsaky.androidide.inflater.internal.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.android.SdkConstants
import com.itsaky.androidide.annotations.inflater.AttributeAdapter
import com.itsaky.androidide.inflater.IAttribute
import com.itsaky.androidide.inflater.IAttributeAdapter
import com.itsaky.androidide.inflater.INamespace
import com.itsaky.androidide.inflater.IView
import com.itsaky.androidide.inflater.internal.ViewImpl
import com.itsaky.androidide.inflater.internal.utils.endParse
import com.itsaky.androidide.inflater.internal.utils.startParse
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.api.AndroidModule
import java.io.File

/**
 * Attribute adapter for [View].
 *
 * @author Akash Yadav
 */
@AttributeAdapter(forView = View::class)
open class ViewAttrAdapter : IAttributeAdapter() {

  override fun apply(view: IView, attribute: IAttribute) {
    doApply<View>(view, attribute) { file, context, layoutParams, namespace, name, value ->
      var handled = true
      when (name) {
        "layout_height" -> layoutParams.height = parseDimension(context, value)
        "layout_width" -> layoutParams.width = parseDimension(context, value)
        "alpha" -> alpha = parseFloat(value)
        else -> handled = false
      }
      this.layoutParams = layoutParams
    }
  }
  
  protected fun parseDimension(context: Context, value: String): Int {
    return com.itsaky.androidide.inflater.internal.utils.parseDimension(context, value)
  }
  
  protected fun parseFloat(value: String, def: Float = 1f): Float {
    return com.itsaky.androidide.inflater.internal.utils.parseFloat(value, def)
  }
  
  override fun applyBasic(view: IView) {
    TODO("Not yet implemented")
  }

  protected open fun canHandleNamespace(namespace: INamespace): Boolean {
    return this.canHandleNamespace(namespace.uri)
  }
  protected open fun canHandleNamespace(nsUri: String): Boolean {
    return SdkConstants.ANDROID_URI == nsUri
  }

  /**
   * Provides easy access to various properties related to the view and attribute when applying an
   * attributes.
   */
  protected fun <T : View> doApply(
    view: IView,
    attribute: IAttribute,
    block:
      T.(
        file: File,
        context: Context,
        layoutParams: ViewGroup.LayoutParams,
        namespace: INamespace,
        name: String,
        value: String
      ) -> Unit
  ) {
    if (!canHandleNamespace(attribute.namespace)) {
      return
    }
    (view.view as T).apply {
      val file = (view as ViewImpl).file
      val module =
        ProjectManager.findModuleForFile(file) as? AndroidModule
          ?: throw IllegalStateException("Cannot find module for file: $file")
      startParse(module)
      block(file, context, layoutParams, attribute.namespace, attribute.name, attribute.value)
      endParse()
    }
  }
}
