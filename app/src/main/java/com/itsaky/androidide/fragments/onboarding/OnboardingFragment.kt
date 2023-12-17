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
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

  private var title: String? = null
  private var message: String? = null
  private var hasHtml: Boolean = false

  @RawRes
  private var icon: Int? = null

  var name: String? = null

  private var _binding: FragmentOnboardingBinding? = null
  private val binding get() = checkNotNull(_binding) { "Fragment has been destroyed" }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View {
    _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.apply {
      tvMessage.movementMethod = LinkMovementMethod.getInstance()
      tvTitle.text = title
      tvMessage.text = if (hasHtml) message?.let {
        HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
      } else message
      icon?.let(animationView::setAnimation)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  companion object {

    @JvmStatic
    @JvmOverloads
    fun newInstance(title: String, message: String, @RawRes icon: Int, name: String = "", hasHtml: Boolean = false) =
      OnboardingFragment().apply {
        this.title = title
        this.message = message
        this.icon = icon
        this.hasHtml = hasHtml
        this.name = name
      }
  }
}