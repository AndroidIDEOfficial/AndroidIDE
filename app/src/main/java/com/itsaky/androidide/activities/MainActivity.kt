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
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import androidx.transition.doOnEnd
import com.blankj.utilcode.util.SizeUtils
import com.google.android.material.color.DynamicColors
import com.google.android.material.transition.MaterialSharedAxis
import com.itsaky.androidide.activities.editor.EditorActivityKt
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.IDEActivity
import com.itsaky.androidide.databinding.ActivityMainBinding
import com.itsaky.androidide.preferences.internal.NO_OPENED_PROJECT
import com.itsaky.androidide.preferences.internal.autoOpenProjects
import com.itsaky.androidide.preferences.internal.confirmProjectOpen
import com.itsaky.androidide.preferences.internal.lastOpenedProject
import com.itsaky.androidide.projects.ProjectManager.projectPath
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

class MainActivity : IDEActivity() {

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

    if (!checkToolsIsInstalled()) {
      showDialogInstallJdkSdk()
    } else {
      openLastProject()
    }

    viewModel.currentScreen.observe(this) { screen ->
      if (screen == -1) {
        return@observe
      }

      try {
        onScreenChanged(screen)
      } catch (err: Exception) {
        onBackPressedCallback.isEnabled = screen != SCREEN_MAIN
      }
    }

    viewModel.setScreen(SCREEN_MAIN)

    onBackPressedDispatcher.addCallback(/* owner = */
      this, /* onBackPressedCallback = */ onBackPressedCallback)
  }

  override fun onStart() {
    super.onStart()
    ITemplateProvider.getInstance(reload = true)
  }

  override fun onStop() {
    super.onStop()
    if (ITemplateProvider.isLoaded()) {
      ITemplateProvider.getInstance().clear()
    }
  }

  private fun onScreenChanged(screen: Int?) {
    val previous = viewModel.previousScreen
    if (previous != -1) {
      val axis =
        // template list -> template details
        // ------- OR -------
        // template details -> template list
        if ((previous == SCREEN_TEMPLATE_LIST || previous == SCREEN_TEMPLATE_DETAILS) && (screen == SCREEN_TEMPLATE_LIST || screen == SCREEN_TEMPLATE_DETAILS)) {
          MaterialSharedAxis.X
        } else MaterialSharedAxis.Y

      val isForward = when {
        previous == SCREEN_MAIN && screen == SCREEN_TEMPLATE_LIST -> true
        previous == SCREEN_TEMPLATE_LIST && screen == SCREEN_TEMPLATE_DETAILS -> true
        previous == SCREEN_TEMPLATE_DETAILS && screen == SCREEN_TEMPLATE_LIST -> false
        previous == SCREEN_TEMPLATE_DETAILS && screen == SCREEN_MAIN -> false
        previous == SCREEN_TEMPLATE_LIST && screen == SCREEN_MAIN -> false
        else -> throw IllegalStateException("Invalid screen states")
      }

      val transition = MaterialSharedAxis(axis, isForward)
      transition.doOnEnd {
        viewModel.isTransitionInProgress = false
        onBackPressedCallback.isEnabled =
          viewModel.currentScreen.value != SCREEN_MAIN
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

    for (fragment in arrayOf(binding.main, binding.templateList,
      binding.templateDetails)) {
      fragment.isVisible = fragment == currentFragment
    }
  }

  override fun onStorageDenied() {
    flashError(string.msg_storage_denied)
    finishAffinity()
  }

  override fun preSetContentLayout() {
    installSplashScreen().setOnExitAnimationListener {
      it.remove()
      DynamicColors.applyToActivityIfAvailable(this)
    }
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
    builder.setNeutralButton(string.btn_docs) { _, _ -> app.openDocs() }
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
    projectPath = root.absolutePath
    startActivity(Intent(this, EditorActivityKt::class.java))
  }

  private fun checkToolsIsInstalled(): Boolean {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}
