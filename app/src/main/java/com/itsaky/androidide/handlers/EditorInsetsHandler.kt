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

package com.itsaky.androidide.handlers

import android.graphics.Color
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.R
import com.itsaky.androidide.utils.doOnApplyWindowInsets
import com.itsaky.androidide.utils.updateSystemBarColors

/** @author Akash Yadav */
object EditorInsetsHandler {

  @JvmStatic
  fun setupInsets(activity: EditorActivity) {
    activity.window.updateSystemBarColors(
      statusBarColor = Color.TRANSPARENT,
      navigationBarColor = ContextCompat.getColor(activity, R.color.primaryDarkColor)
    )

    val binding = activity.binding
    binding.root.doOnApplyWindowInsets { view, insets, _, _ ->
      val appBarHeight = binding.editorAppBarLayout.height
      insets.getInsets(WindowInsetsCompat.Type.statusBars()).apply {
        binding.editorAppBarLayout.updatePadding(top = top)
        binding.endNav.getChildAt(0).updatePadding(top = top + SizeUtils.dp2px(16f))
        val behaviour = BottomSheetBehavior.from(binding.bottomSheet)
        behaviour.isFitToContents = false
        behaviour.expandedOffset = appBarHeight + SizeUtils.dp2px(1f)
      }
      insets.getInsets(WindowInsetsCompat.Type.navigationBars()).apply {
        binding.root.updatePadding(bottom = bottom)
        binding.bottomSheet.binding.spaceBottom.updateLayoutParams<LayoutParams> {
          height = bottom + appBarHeight + SizeUtils.dp2px(1f)
        }
      }
      (view as? ViewGroup)?.forEach {
        ViewCompat.dispatchApplyWindowInsets(it, insets)
      }
    }
  }
}
