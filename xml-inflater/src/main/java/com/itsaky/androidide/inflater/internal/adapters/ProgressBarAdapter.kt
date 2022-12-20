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

import android.widget.ProgressBar
import com.itsaky.androidide.inflater.AttributeHandlerScope
import com.itsaky.androidide.inflater.models.UiWidget
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string

/**
 * Attribute adapter for [ProgressBar].
 *
 * @author Akash Yadav
 */
@com.itsaky.androidide.annotations.inflater.ViewAdapter(ProgressBar::class)
open class ProgressBarAdapter<T : ProgressBar> : ViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("indeterminate") { view.isIndeterminate = parseBoolean(value) }
    create("indeterminateDrawable") { view.indeterminateDrawable = parseDrawable(context, value) }
    create("indeterminateTint") { view.indeterminateTintList = parseColorStateList(context, value) }
    create("indeterminateTintMode") { view.indeterminateTintMode = parsePorterDuffMode(value) }
    create("max") { view.max = parseInteger(value, 100) }
    create("maxHeight") {
      if (isApi29()) {
        view.maxHeight = parseDimension(context, value, Int.MAX_VALUE)
      }
    }
    create("maxWidth") {
      if (isApi29()) {
        view.maxWidth = parseDimension(context, value, Int.MAX_VALUE)
      }
    }
    create("min") { view.min = parseInteger(value, 0) }
    create("minHeight") {
      if (isApi29()) {
        view.minHeight = parseDimension(context, value, 0)
      }
    }
    create("minWidth") {
      if (isApi29()) {
        view.minWidth = parseDimension(context, value, 0)
      }
    }
    create("progress") { view.progress = parseInteger(value, 50) }
    create("progressBackgroundTint") {
      view.progressBackgroundTintList = parseColorStateList(context, value)
    }
    create("progressBackgroundTintMode") {
      view.progressBackgroundTintMode = parsePorterDuffMode(value)
    }
    create("progressDrawable") { view.progressDrawable = parseDrawable(context, value) }
    create("progressTint") { view.progressTintList = parseColorStateList(context, value) }
    create("progressTintMode") { view.progressTintMode = parsePorterDuffMode(value) }
    create("secondaryProgress") { view.secondaryProgress = parseInteger(value, 0) }
    create("secondaryProgressTint") {
      view.secondaryProgressTintList = parseColorStateList(context, value)
    }
    create("secondaryProgressTintMode") {
      view.secondaryProgressTintMode = parsePorterDuffMode(value)
    }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(ProgressBar::class.java, string.widget_progressbar, drawable.ic_widget_progress_bar)
    )
  }
}
