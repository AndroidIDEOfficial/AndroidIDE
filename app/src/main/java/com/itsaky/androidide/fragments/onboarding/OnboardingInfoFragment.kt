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

package com.itsaky.androidide.fragments.onboarding

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.widget.ImageViewCompat
import com.itsaky.androidide.databinding.FragmentOnboardingInfoBinding
import com.itsaky.androidide.fragments.FragmentWithBinding

/**
 * @author Akash Yadav
 */
class OnboardingInfoFragment :
  FragmentWithBinding<FragmentOnboardingInfoBinding>(FragmentOnboardingInfoBinding::inflate) {

  companion object {

    const val KEY_INFO_TEXT = "ide.onboarding.infoFragment.info"
    const val KEY_INFO_ICON = "ide.onboarding.infoFragment.icon"
    const val KEY_INFO_ICON_TINT = "ide.onboarding.infoFragment.icon.tint"

    @JvmStatic
    fun newInstance(
      title: CharSequence,
      info: CharSequence,
      @DrawableRes icon: Int,
      @ColorInt iconTint: Int
    ): OnboardingInfoFragment {
      return OnboardingInfoFragment().apply {
        arguments = Bundle().apply {
          putCharSequence(OnboardingFragment.KEY_ONBOARDING_TITLE, title)
          putCharSequence(KEY_INFO_TEXT, info)
          putInt(KEY_INFO_ICON, icon)
          putInt(KEY_INFO_ICON_TINT, iconTint)
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.infoTitle.text = requireArguments().getCharSequence(
      OnboardingFragment.KEY_ONBOARDING_TITLE)

    binding.infoMessage.text = requireArguments().getCharSequence(KEY_INFO_TEXT)
    binding.image.setImageResource(requireArguments().getInt(KEY_INFO_ICON))
    ImageViewCompat.setImageTintList(binding.image,
      ColorStateList.valueOf(requireArguments().getInt(
        KEY_INFO_ICON_TINT)))
  }
}