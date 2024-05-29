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

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem.Location.UI_DESIGNER_TOOLBAR
import com.itsaky.androidide.actions.ActionsRegistry
import com.itsaky.androidide.actions.FillMenuParams
import com.itsaky.androidide.app.BaseIDEActivity
import com.itsaky.androidide.uidesigner.actions.clearUiDesignerActions
import com.itsaky.androidide.uidesigner.actions.registerUiDesignerActions
import com.itsaky.androidide.uidesigner.databinding.ActivityUiDesignerBinding
import com.itsaky.androidide.uidesigner.fragments.DesignerWorkspaceFragment
import com.itsaky.androidide.uidesigner.utils.ViewToXml
import com.itsaky.androidide.uidesigner.viewmodel.WorkspaceViewModel
import com.itsaky.androidide.utils.flashError
import org.slf4j.LoggerFactory
import java.io.File

/**
 * The UI Designer activity allows the user to design XML layouts with a drag-n-drop interface.
 *
 * @author Akash Yadav
 */
class UIDesignerActivity : BaseIDEActivity() {

  private var binding: ActivityUiDesignerBinding? = null
  private val viewModel by viewModels<WorkspaceViewModel>()

  private val workspace: DesignerWorkspaceFragment?
    get() = this.binding?.workspace?.getFragment<DesignerWorkspaceFragment>()

  private val backPressHandler =
    object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val frag =
          workspace()
            ?: run {
              onFailedToReturnXml("Workspace fragment not found")
              return
            }

        if (viewModel.layoutHasError) {
          onFailedToReturnXml("Inflation failed, layout has errors.")
          return
        }

        if (frag.workspaceView.childCount <= 0) {
          onFailedToReturnXml("No views have been added")
          return
        }

        ViewToXml.generateXml(
          frag.requireContext(),
          frag.workspaceView,
          ::onXmlGenerated
        ) { result, error ->
          if (result != null && error == null) {
            return@generateXml
          }

          // XML generation failed, notify user and exit activity
          runOnUiThread {
            flashError(R.string.msg_generate_xml_failed)
            onFailedToReturnXml(error?.cause?.message ?: error?.message ?: "Unknown error")
          }
        }
      }
    }

  companion object {

    private val log = LoggerFactory.getLogger(UIDesignerActivity::class.java)

    const val EXTRA_FILE = "layout_file"
    const val RESULT_GENERATED_XML = "ide.uidesigner.generatedXml"
  }

  private fun onXmlGenerated(xml: String) {
    setResult(RESULT_OK, Intent().apply { putExtra(RESULT_GENERATED_XML, xml) })
    finish()
  }

  private fun onFailedToReturnXml(reason: String) {
    log.error("Failed to generate XML code because '{}'", reason)
    setResult(RESULT_CANCELED)
    finish()
  }

  override fun bindLayout(): View {
    this.binding = ActivityUiDesignerBinding.inflate(layoutInflater)
    this.binding!!.root.childId = this.binding!!.container.id
    return this.binding!!.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    intent?.extras?.let {
      val path = it.getString(EXTRA_FILE) ?: return
      val file = File(path)
      if (!file.exists()) {
        throw IllegalArgumentException("File does not exist: $file")
      }
      viewModel.file = file
    }

    setSupportActionBar(this.binding!!.toolbar)
    supportActionBar?.title = viewModel.file.nameWithoutExtension

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

    viewModel._drawerOpened.observe(this) { opened ->
      if (binding == null) {
        return@observe
      }

      if (opened) {
        binding!!.root.openDrawer(GravityCompat.START)
      } else {
        binding!!.root.closeDrawer(GravityCompat.START)
      }
    }

    onBackPressedDispatcher.addCallback(backPressHandler)

    registerUiDesignerActions(this)
  }

  override fun onResume() {
    super.onResume()
    registerUiDesignerActions(this)
  }

  override fun onPause() {
    super.onPause()
    clearUiDesignerActions()
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    ensureToolbarMenu(menu)
    return true
  }

  @SuppressLint("RestrictedApi")
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
    return true
  }

  private fun ensureToolbarMenu(menu: Menu) {
    menu.clear()

    val data = ActionData()
    data.put(Context::class.java, this)
    data.put(Fragment::class.java, workspace())

    ActionsRegistry.getInstance().fillMenu(FillMenuParams(data, UI_DESIGNER_TOOLBAR, menu))
  }

  private fun workspace(): DesignerWorkspaceFragment? {
    return workspace
  }

  fun setupHierarchy(view: com.itsaky.androidide.inflater.IView) {
    binding?.hierarchy?.setupWithView(view) { workspace()?.showViewInfo(it) }
  }

  fun openHierarchyView() {
    binding?.root?.openDrawer(GravityCompat.END)
  }
}
