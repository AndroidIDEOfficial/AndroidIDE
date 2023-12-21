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

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itsaky.androidide.adapters.onboarding.DefaultOnboardingItemAdapter
import com.itsaky.androidide.databinding.LayoutOnboardingMultiactionBinding
import com.itsaky.androidide.models.OnboardingItem
import com.itsaky.androidide.utils.uncheckedCast

/**
 * @author Akash Yadav
 */
open class OnboardingMultiActionFragment : OnboardingFragment() {

  protected var recyclerView: RecyclerView? = null
    private set

  companion object {

    const val KEY_ACTION_ITEMS = "ide.onboarding.multiActionFragment.items"

    @JvmStatic
    fun newInstance(
      title: CharSequence,
      subtitle: CharSequence,
      items: List<OnboardingItem>
    ): OnboardingFragment {
      return OnboardingMultiActionFragment().apply {
        arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE, title)
          putCharSequence(KEY_ONBOARDING_SUBTITLE, subtitle)

          val arr = items.toTypedArray()
          putParcelableArray(KEY_ACTION_ITEMS, arr)
        }
      }
    }
  }

  override fun createContentView(parent: ViewGroup, attachToParent: Boolean) {
    val binding = LayoutOnboardingMultiactionBinding.inflate(layoutInflater, parent, attachToParent)
    binding.onboardingItems.adapter = createAdapter()
    recyclerView = binding.onboardingItems
  }

  protected inline fun <reified T : OnboardingItem> getItemsFromArgs(): Array<T> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      requireArguments().getParcelableArray(KEY_ACTION_ITEMS, T::class.java)!!
    } else {
      uncheckedCast(
        @Suppress("DEPRECATION") requireArguments().getParcelableArray(KEY_ACTION_ITEMS)!!)
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    recyclerView = null
  }

  protected open fun createAdapter(): RecyclerView.Adapter<*> {
    val items = getItemsFromArgs<OnboardingItem>()
    return DefaultOnboardingItemAdapter(items.toList())
  }
}