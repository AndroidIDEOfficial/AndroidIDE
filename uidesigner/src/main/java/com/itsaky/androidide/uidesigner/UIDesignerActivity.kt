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

package com.itsaky.androidide.uidesigner

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.itsaky.androidide.app.BaseIDEActivity
import com.itsaky.androidide.uidesigner.databinding.ActivityUiDesignerBinding
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment

/**
 * The UI Designer activity allows the user to design XML layouts with a drag-n-drop interface.
 *
 * @author Akash Yadav
 */
class UIDesignerActivity : BaseIDEActivity() {

  private var binding: ActivityUiDesignerBinding? = null
  private val workspace: DesignerWorkspaceFragment?
    get() = this.binding?.workspace?.getFragment()

  companion object {
    const val EXTRA_FILE = "layout_file"
  }

  override fun bindLayout(): View {
    this.binding = ActivityUiDesignerBinding.inflate(layoutInflater)
    this.binding!!.root.childId = this.binding!!.container.id
    return this.binding!!.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    workspace?.setupFromBundle(intent.extras)

    setSupportActionBar(this.binding!!.toolbar)
    supportActionBar?.title = workspace?.viewModel?.file?.nameWithoutExtension

    ActionBarDrawerToggle(
        this,
        binding!!.root,
        binding!!.toolbar,
        R.string.app_name,
        R.string.app_name
      )
      .apply {
        binding!!.root.addDrawerListener(this)
        syncState()
      }
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
  }
}
