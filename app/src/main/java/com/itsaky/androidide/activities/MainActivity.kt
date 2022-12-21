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

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.text.HtmlCompat
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.activities.editor.EditorActivityKt
import com.itsaky.androidide.R.id
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.IDEActivity
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.databinding.ActivityMainBinding
import com.itsaky.androidide.fragments.MainFragment
import com.itsaky.androidide.preferences.internal.NO_OPENED_PROJECT
import com.itsaky.androidide.preferences.internal.autoOpenProjects
import com.itsaky.androidide.preferences.internal.confirmProjectOpen
import com.itsaky.androidide.preferences.internal.lastOpenedProject
import com.itsaky.androidide.projects.ProjectManager.projectPath
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.utils.DialogUtils
import com.itsaky.androidide.utils.Environment
import com.itsaky.toaster.Toaster.Type.ERROR
import com.itsaky.toaster.Toaster.Type.INFO
import com.itsaky.toaster.toast
import java.io.File

class MainActivity : IDEActivity() {
  private var binding: ActivityMainBinding? = null
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
    supportFragmentManager
      .beginTransaction()
      .replace(id.container, MainFragment(), MainFragment.TAG)
      .commit()
  }

  override fun onStorageGranted() {}
  override fun onStorageDenied() {
    toast(string.msg_storage_denied, ERROR)
    finishAffinity()
  }

  override fun preSetContentLayout() {
    installSplashScreen()
  }

  override fun bindLayout(): View {
    binding = ActivityMainBinding.inflate(layoutInflater)
    return binding!!.root
  }

  private fun showDialogInstallJdkSdk() {
    val dp24 = SizeUtils.dp2px(24f)
    val builder = DialogUtils.newMaterialDialogBuilder(this)
    builder.setTitle(string.title_warning)
    val view = TextView(this)
    view.setPaddingRelative(dp24, dp24, dp24, dp24)
    view.text =
      HtmlCompat.fromHtml(
        getString(string.msg_require_install_jdk_and_android_sdk),
        HtmlCompat.FROM_HTML_MODE_COMPACT
      )
    view.movementMethod = LinkMovementMethod.getInstance()
    builder.setView(view)
    builder.setCancelable(false)
    builder.setPositiveButton(R.string.ok) { _, _ -> openTerminal() }
    builder.setNegativeButton(R.string.cancel) { _, _ -> finishAffinity() }
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
    builder.setPositiveButton(R.string.ok) { _, _ -> finishAffinity() }
    builder.create().show()
  }

  private fun openLastProject() {
    binding!!.root.post { tryOpenLastProject() }
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
      toast(string.msg_opened_project_does_not_exist, INFO)
      return
    }
    val project = File(openedProject)
    if (!project.exists()) {
      toast(string.msg_opened_project_does_not_exist, INFO)
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
    builder.setMessage(getString(string.msg_confirm_open_project, root.absolutePath))
    builder.setCancelable(false)
    builder.setPositiveButton(string.yes) { _, _ -> openProject(root) }
    builder.setNegativeButton(string.no, null)
    builder.show()
  }

  private fun openProject(root: File) {
    projectPath = root.absolutePath
    startActivity(Intent(this, EditorActivityKt::class.java))
  }

  private fun checkToolsIsInstalled(): Boolean {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists()
  }

  override fun onDestroy() {
    super.onDestroy()
    binding = null
  }
}
