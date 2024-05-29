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

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.text.method.LinkMovementMethodCompat
import androidx.core.view.updateLayoutParams
import com.itsaky.androidide.databinding.FragmentOnboardingBinding
import com.itsaky.androidide.fragments.FragmentWithBinding

open class OnboardingFragment :
  FragmentWithBinding<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {

  companion object {

    const val KEY_ONBOARDING_TITLE = "ide.onboarding.fragment.title"
    const val KEY_ONBOARDING_SUBTITLE = "ide.onboarding.fragment.subtitle"
    const val KEY_ONBOARDING_EXTRA_INFO = "ide.onboarding.fragment.extraInfo"

    @JvmStatic
    fun newInstance(title: CharSequence, subtitle: CharSequence,
      extraInfo: CharSequence = ""): OnboardingFragment {
      return OnboardingFragment().apply {
        arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE, title)
          putCharSequence(KEY_ONBOARDING_SUBTITLE, subtitle)
          putCharSequence(KEY_ONBOARDING_EXTRA_INFO, extraInfo)
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.onboardingTitle.text = requireArguments().getCharSequence(KEY_ONBOARDING_TITLE)
    binding.onboardingSubtitle.text = requireArguments().getCharSequence(KEY_ONBOARDING_SUBTITLE)

    val extraInfo = requireArguments().getCharSequence(KEY_ONBOARDING_EXTRA_INFO, "")
    if (extraInfo.isBlank()) {
      binding.onboardingExtraInfo.updateLayoutParams<ViewGroup.LayoutParams> {
        height = 0
      }
    } else {
      binding.onboardingExtraInfo.movementMethod = LinkMovementMethodCompat.getInstance()
      binding.onboardingExtraInfo.text = extraInfo
    }

    binding.contentContainer.removeAllViews()
    createContentView(binding.contentContainer, true)
  }

  protected open fun createContentView(parent: ViewGroup, attachToParent: Boolean) {}
}