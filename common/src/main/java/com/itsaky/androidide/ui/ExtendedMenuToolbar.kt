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

package com.itsaky.androidide.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MenuItem
import android.view.ViewTreeObserver
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.appcompat.widget.ActionMenuView
import androidx.core.view.children
import androidx.core.view.iterator
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.floor

/** @author Smooth E */
class ExtendedMenuToolbar : MaterialToolbar {

  private var cachedRequiredSpace: Float = 0f
  private var cachedItemWidth: Int = 0

  constructor(context: Context) : super(context)

  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  override fun onConfigurationChanged(newConfig: Configuration?) {
    super.onConfigurationChanged(newConfig)

    viewTreeObserver.addOnGlobalLayoutListener(
      object: ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          updateMenuDisplay()
          viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
      }
    )
  }

  @SuppressLint("RestrictedApi")
  fun updateMenuDisplay() {
    if (cachedRequiredSpace == 0f || cachedItemWidth == 0) {
      var requiredSpace = 0f
      var actionMenuView: ActionMenuView? = null

      for (child in children) {
        if (child is ActionMenuView) {
          actionMenuView = child
          continue
        }

        requiredSpace = maxOf(child.x + child.width - x, requiredSpace)
      }

      cachedRequiredSpace = requiredSpace

      if (actionMenuView?.getChildAt(0) == null) {
        return
      }

      val itemWidth = actionMenuView.getChildAt(0).width

      if (itemWidth == 0) {
        return
      }

      cachedItemWidth = itemWidth
    }

    val maxItemCount = floor((width - cachedRequiredSpace) / cachedItemWidth).toInt()

    var itemsModified = 0
    for (item in menu.iterator()) {
      val flag = getShowAsActionFlag(item)

      val ignoreItem =
        flag == MenuItemImpl.SHOW_AS_ACTION_NEVER ||
        flag == MenuItemImpl.SHOW_AS_ACTION_WITH_TEXT

      if (ignoreItem) {
        continue
      }

      itemsModified++

      if (itemsModified < maxItemCount) {
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
      } else {
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      }
    }
  }

  // https://stackoverflow.com/a/48392871
  @SuppressLint("RestrictedApi")
  private fun getShowAsActionFlag(item: MenuItem): Int {
    val itemImpl = item as MenuItemImpl
    return if (itemImpl.requiresActionButton()) MenuItemImpl.SHOW_AS_ACTION_ALWAYS
      else if (itemImpl.requestsActionButton()) MenuItemImpl.SHOW_AS_ACTION_IF_ROOM
      else if (itemImpl.showsTextAsAction()) MenuItemImpl.SHOW_AS_ACTION_WITH_TEXT
      else MenuItemImpl.SHOW_AS_ACTION_NEVER
  }

}
