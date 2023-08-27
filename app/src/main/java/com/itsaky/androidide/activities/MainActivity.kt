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

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import androidx.transition.doOnEnd
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.activities.editor.EditorActivityKt
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.LimitlessIDEActivity
import com.itsaky.androidide.databinding.ActivityMainBinding
import com.itsaky.androidide.preferences.internal.NO_OPENED_PROJECT
import com.itsaky.androidide.preferences.internal.autoOpenProjects
import com.itsaky.androidide.preferences.internal.confirmProjectOpen
import com.itsaky.androidide.preferences.internal.lastOpenedProject
import com.itsaky.androidide.preferences.internal.statConsentDialogShown
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.projects.ProjectManagerImpl
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.templates.ITemplateProvider
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashInfo
import com.itsaky.androidide.viewmodel.MainViewModel
import com.itsaky.androidide.viewmodel.MainViewModel.Companion.SCREEN_MAIN
import com.itsaky.androidide.viewmodel.MainViewModel.Companion.SCREEN_TEMPLATE_DETAILS
import com.itsaky.androidide.viewmodel.MainViewModel.Companion.SCREEN_TEMPLATE_LIST
import java.io.File

class MainActivity : LimitlessIDEActivity() {

  private val viewModel by viewModels<MainViewModel>()
  private var _binding: ActivityMainBinding? = null

  private val onBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      viewModel.apply {

        // Ignore back press if project creating is in progress
        if (creatingProject.value == true) {
          return@apply
        }

        val newScreen = when (currentScreen.value) {
          SCREEN_TEMPLATE_DETAILS -> SCREEN_TEMPLATE_LIST
          SCREEN_TEMPLATE_LIST -> SCREEN_MAIN
          else -> SCREEN_MAIN
        }

        if (currentScreen.value != newScreen) {
          setScreen(newScreen)
        }
      }
    }
  }

  private val binding: ActivityMainBinding
    get() = checkNotNull(_binding)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (!BaseApplication.isAbiSupported()) {
      showDeviceNotSupported()
      return
    }

    showStatConsentDialogIfNeeded {

      app.reportStatsIfNecessary()

      if (!checkToolsIsInstalled()) {
        showDialogInstallJdkSdk()
      } else {
        openLastProject()
      }
    }

    viewModel.currentScreen.observe(this) { screen ->
      if (screen == -1) {
        return@observe
      }

      onScreenChanged(screen)
      onBackPressedCallback.isEnabled = screen != SCREEN_MAIN
    }

    // Data in a ViewModel is kept between activity rebuilds on
    // configuration changes (i.e. screen rotation)
    // * previous == -1 and current == -1 -> this is an initial instantiation of the activity
    if (viewModel.currentScreen.value == -1 && viewModel.previousScreen == -1) {
      viewModel.setScreen(SCREEN_MAIN)
    } else {
      onScreenChanged(viewModel.currentScreen.value)
    }

    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
  }

  private fun showStatConsentDialogIfNeeded(onResult: () -> Unit) {
    if (statConsentDialogShown) {
      onResult()
      return
    }

    DialogUtils.newMaterialDialogBuilder(this).apply {
      setTitle(string.title_androidide_statistics)
      setMessage(string.msg_androidide_statistics)
      setCancelable(false)
      setPositiveButton(string.btn_opt_in) { dialog, _ ->
        dialog.dismiss()
        statOptIn = true
      }
      setNegativeButton(string.btn_no_thanks) { dialog, _ ->
        dialog.dismiss()
        statOptIn = false
      }
      setOnDismissListener {
        statConsentDialogShown = true
        onResult()
      }
      show()
    }
  }

  override fun onInsetsUpdated(insets: Rect) {
    super.onInsetsUpdated(insets)
    binding.fragmentContainersParent.setPadding(insets.left, 0, insets.right, insets.bottom)
  }

  private fun onScreenChanged(screen: Int?) {
    val previous = viewModel.previousScreen
    if (previous != -1) {
      // template list -> template details
      // ------- OR -------
      // template details -> template list
      val setAxisToX =
        (previous == SCREEN_TEMPLATE_LIST || previous == SCREEN_TEMPLATE_DETAILS) &&
            (screen == SCREEN_TEMPLATE_LIST || screen == SCREEN_TEMPLATE_DETAILS)

      val axis = if (setAxisToX) {
        MaterialSharedAxis.X
      } else {
        MaterialSharedAxis.Y
      }

      val isForward = (screen ?: 0) - previous == 1

      val transition = MaterialSharedAxis(axis, isForward)
      transition.doOnEnd {
        viewModel.isTransitionInProgress = false
        onBackPressedCallback.isEnabled = viewModel.currentScreen.value != SCREEN_MAIN
      }

      viewModel.isTransitionInProgress = true
      TransitionManager.beginDelayedTransition(binding.root, transition)
    }

    val currentFragment = when (screen) {
      SCREEN_MAIN -> binding.main
      SCREEN_TEMPLATE_LIST -> binding.templateList
      SCREEN_TEMPLATE_DETAILS -> binding.templateDetails
      else -> throw IllegalArgumentException("Invalid screen id: '$screen'")
    }

    for (fragment in arrayOf(binding.main, binding.templateList, binding.templateDetails)) {
      fragment.isVisible = fragment == currentFragment
    }
  }

  override fun onStorageDenied() {
    flashError(string.msg_storage_denied)
    finishAffinity()
  }

  override fun bindLayout(): View {
    _binding = ActivityMainBinding.inflate(layoutInflater)
    return binding.root
  }

  private fun showDialogInstallJdkSdk() {
    val dp24 = SizeUtils.dp2px(24f)
    val builder = DialogUtils.newMaterialDialogBuilder(this)
    builder.setTitle(string.title_warning)
    val view = TextView(this)
    view.setPaddingRelative(dp24, dp24, dp24, dp24)
    view.text = HtmlCompat.fromHtml(
      getString(string.msg_require_install_jdk_and_android_sdk),
      HtmlCompat.FROM_HTML_MODE_COMPACT)
    view.movementMethod = LinkMovementMethod.getInstance()
    builder.setView(view)
    builder.setCancelable(false)
    builder.setPositiveButton(android.R.string.ok) { _, _ -> openTerminal() }
    builder.setNegativeButton(
      android.R.string.cancel) { _, _ -> finishAffinity() }
    builder.setNeutralButton(string.btn_docs) { _, _ -> app.openDocs(); finishAffinity() }
    builder.show()
  }

  private fun openTerminal() {
    startActivity(Intent(this, TerminalActivity::class.java))
  }

  private fun showDeviceNotSupported() {
    val builder = DialogUtils.newMaterialDialogBuilder(this)
    builder.setTitle(string.title_device_not_supported)
    builder.setMessage(string.msg_device_not_supported)
    builder.setCancelable(false)
    builder.setPositiveButton(android.R.string.ok) { _, _ -> finishAffinity() }
    builder.create().show()
  }

  private fun openLastProject() {
    binding.root.post { tryOpenLastProject() }
  }

  private fun tryOpenLastProject() {
    if (!autoOpenProjects) {
      return
    }

    val openedProject = lastOpenedProject
    if (NO_OPENED_PROJECT == openedProject) {
      return
    }

    if (TextUtils.isEmpty(openedProject)) {
      app
      flashInfo(string.msg_opened_project_does_not_exist)
      return
    }

    val project = File(openedProject)
    if (!project.exists()) {
      flashInfo(string.msg_opened_project_does_not_exist)
      return
    }

    if (confirmProjectOpen) {
      askProjectOpenPermission(project)
      return
    }

    openProject(project)
  }

  private fun askProjectOpenPermission(root: File) {
    val builder = DialogUtils.newMaterialDialogBuilder(this)
    builder.setTitle(string.title_confirm_open_project)
    builder.setMessage(
      getString(string.msg_confirm_open_project, root.absolutePath))
    builder.setCancelable(false)
    builder.setPositiveButton(string.yes) { _, _ -> openProject(root) }
    builder.setNegativeButton(string.no, null)
    builder.show()
  }

  internal fun openProject(root: File) {
    ProjectManagerImpl.getInstance().projectPath = root.absolutePath
    startActivity(Intent(this, EditorActivityKt::class.java))
  }

  private fun checkToolsIsInstalled(): Boolean {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists()
  }

  override fun onDestroy() {
    ITemplateProvider.getInstance().release()
    super.onDestroy()
    _binding = null
  }
}
