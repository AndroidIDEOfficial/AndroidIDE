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
package com.itsaky.androidide.activities

import android.os.Bundle
import android.view.View
import androidx.core.graphics.Insets
import androidx.fragment.app.Fragment
import com.itsaky.androidide.R
import com.itsaky.androidide.app.EdgeToEdgeIDEActivity
import com.itsaky.androidide.databinding.ActivityPreferencesBinding
import com.itsaky.androidide.fragments.IDEPreferencesFragment
import com.itsaky.androidide.preferences.addRootPreferences
import com.itsaky.androidide.preferences.IDEPreferences as prefs

class PreferencesActivity : EdgeToEdgeIDEActivity() {

  private var _binding: ActivityPreferencesBinding? = null
  private val binding: ActivityPreferencesBinding
    get() = checkNotNull(_binding) { "Activity has been destroyed" }

  private val rootFragment by lazy {
    IDEPreferencesFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setSupportActionBar(binding.toolbar)
    supportActionBar!!.setTitle(R.string.ide_preferences)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

    if (savedInstanceState != null) {
      return
    }

    (prefs.children as MutableList?)?.clear()

    prefs.addRootPreferences()

    val args = Bundle()
    args.putParcelableArrayList(
      IDEPreferencesFragment.EXTRA_CHILDREN,
      ArrayList(prefs.children)
    )

    rootFragment.arguments = args
    loadFragment(rootFragment)
  }

  override fun onApplySystemBarInsets(insets: Insets) {
    val toolbar: View = binding.toolbar
    toolbar.setPadding(
      toolbar.paddingLeft + insets.left,
      toolbar.paddingTop,
      toolbar.paddingRight + insets.right,
      toolbar.paddingBottom
    )

    val fragmentContainer: View = binding.fragmentContainerParent
    fragmentContainer.setPadding(
      fragmentContainer.paddingLeft + insets.left,
      fragmentContainer.paddingTop,
      fragmentContainer.paddingRight + insets.right,
      fragmentContainer.paddingBottom
    )
  }

  override fun bindLayout(): View {
    _binding = ActivityPreferencesBinding.inflate(
      layoutInflater
    )
    return binding.root
  }

  private fun loadFragment(fragment: Fragment) {
    super.loadFragment(fragment, binding.fragmentContainer.id)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
