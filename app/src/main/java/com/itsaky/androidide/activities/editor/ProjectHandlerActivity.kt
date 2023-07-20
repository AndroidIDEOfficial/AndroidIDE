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

package com.itsaky.androidide.activities.editor

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.MarginLayoutParams
import android.widget.CheckBox
import androidx.annotation.GravityInt
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.R.string
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding
import com.itsaky.androidide.fragments.sheets.ProgressSheet
import com.itsaky.androidide.handlers.EditorBuildEventListener
import com.itsaky.androidide.handlers.LspHandler.connectClient
import com.itsaky.androidide.handlers.LspHandler.destroyLanguageServers
import com.itsaky.androidide.interfaces.IProjectHandler
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.IDELanguageClientImpl
import com.itsaky.androidide.preferences.internal.NO_OPENED_PROJECT
import com.itsaky.androidide.preferences.internal.lastOpenedProject
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.projects.ProjectManager.cachedInitResult
import com.itsaky.androidide.projects.ProjectManager.getProjectDirPath
import com.itsaky.androidide.projects.ProjectManager.notifyProjectUpdate
import com.itsaky.androidide.projects.ProjectManager.projectInitialized
import com.itsaky.androidide.projects.ProjectManager.projectPath
import com.itsaky.androidide.projects.ProjectManager.rootProject
import com.itsaky.androidide.projects.ProjectManager.setupProject
import com.itsaky.androidide.projects.api.Project
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.services.builder.GradleBuildService
import com.itsaky.androidide.services.builder.GradleBuildServiceConnnection
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.utils.DialogUtils.newMaterialDialogBuilder
import com.itsaky.androidide.utils.RecursiveFileSearcher
import com.itsaky.androidide.utils.flashError
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern
import java.util.stream.Collectors

/** @author Akash Yadav */
@Suppress("MemberVisibilityCanBePrivate")
abstract class ProjectHandlerActivity : BaseEditorActivity(), IProjectHandler {

  protected var mSearchingProgress: ProgressSheet? = null
  protected var mFindInProjectDialog: AlertDialog? = null

  protected var isFromSavedInstance = false
  protected var shouldInitialize = false

  protected var initializingFuture: CompletableFuture<out InitializeResult?>? = null

  val findInProjectDialog: AlertDialog
    get() {
      if (mFindInProjectDialog == null) {
        createFindInProjectDialog()
      }
      return mFindInProjectDialog!!
    }

  protected val mBuildEventListener = EditorBuildEventListener()

  companion object {
    const val STATE_KEY_FROM_SAVED_INSTANACE = "ide.editor.isFromSavedInstance"
    const val STATE_KEY_SHOULD_INITIALIZE = "ide.editor.isInitializing"
    @JvmStatic private val buildServiceConnection = GradleBuildServiceConnnection()
  }

  abstract fun doCloseAll(runAfter: () -> Unit)

  abstract fun saveOpenedFiles()

  override fun doDismissSearchProgress() {
    if (mSearchingProgress?.isShowing == true) {
      mSearchingProgress!!.dismiss()
    }
  }

  override fun doConfirmProjectClose() {
    confirmProjectClose()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    savedInstanceState?.let {
      this.shouldInitialize = it.getBoolean(STATE_KEY_SHOULD_INITIALIZE, true)
      this.isFromSavedInstance = it.getBoolean(STATE_KEY_FROM_SAVED_INSTANACE, false)
    }
      ?: run {
        this.shouldInitialize = true
        this.isFromSavedInstance = false
      }

    startServices()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.apply {
      putBoolean(STATE_KEY_SHOULD_INITIALIZE, !viewModel.isInitializing)
      putBoolean(STATE_KEY_FROM_SAVED_INSTANACE, true)
    }
  }

  override fun onPause() {
    super.onPause()
    if (isDestroying) {
      // reset these values here
      // sometimes, when the IDE closed and reopened instantly, these values prevent initialization
      // of the project
      ProjectManager.destroy()

      viewModel.isInitializing = false
      viewModel.isBuildInProgress = false
    }
  }

  override fun preDestroy() {
    if (isDestroying) {
      releaseServerListener()
      this.initializingFuture?.cancel(true)
      this.initializingFuture = null

      closeProject(false)
    }

    if (IDELanguageClientImpl.isInitialized()) {
      IDELanguageClientImpl.shutdown()
    }

    super.preDestroy()

    if (isDestroying) {

      try {
        stopLanguageServers()
      } catch (err: Exception) {
        log.error("Failed to stop editor services.")
      }

      try {
        unbindService(buildServiceConnection)
        buildServiceConnection.onConnected = {}
      } catch (err: Throwable) {
        log.error("Unable to unbind service")
      } finally {
        (Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) as? GradleBuildService?)
          ?.setEventListener(null)
        Lookup.getDefault().unregister(BuildService.KEY_BUILD_SERVICE)
        viewModel.isBoundToBuildSerice = false
      }
    }
  }

  override fun setStatus(status: CharSequence, @GravityInt gravity: Int) {
    doSetStatus(status, gravity)
  }

  override fun appendBuildOutput(str: String) {
    binding.bottomSheet.appendBuildOut(str)
  }

  override fun notifySyncNeeded() {
    notifySyncNeeded { initializeProject() }
  }

  override fun startServices() {
    val service = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) as GradleBuildService?
    if (viewModel.isBoundToBuildSerice && service != null) {
      log.info("Reusing already started Gradle build service")
      onGradleBuildServiceConnected(service)
      return
    } else {
      log.info("Binding to Gradle build service...")
    }

    buildServiceConnection.onConnected = this::onGradleBuildServiceConnected

    if (
      bindService(
        Intent(this, GradleBuildService::class.java),
        buildServiceConnection,
        BIND_AUTO_CREATE or BIND_IMPORTANT
      )
    ) {
      log.info("Bind request for Gradle build service was successful...")
    } else {
      log.error("Gradle build service doesn't exist or the IDE is not allowed to access it.")
    }

    initLspClient()
  }

  override fun initializeProject() {
    val projectDir = File(projectPath)
    if (!projectDir.exists()) {
      log.error("Project directory does not exist. Cannot initialize project")
      return
    }

    val initialized = projectInitialized && cachedInitResult != null
    log.debug("Is project initialized: $initialized")
    // When returning after a configuration change between the initialization process,
    // we do not want to start another project initialization
    if (isFromSavedInstance && initialized && !shouldInitialize) {
      log.debug("Skipping init process because initialized && !wasInitializing")
      return
    }

    //noinspection ConstantConditions
    ThreadUtils.runOnUiThread { preProjectInit() }

    val buildService = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)
    if (buildService == null) {
      log.error("No build service found. Cannot initialize project.")
      return
    }

    if (!buildService.isToolingServerStarted()) {
      flashError(string.msg_tooling_server_unavailable)
      return
    }

    this.initializingFuture =
      if (shouldInitialize || (!isFromSavedInstance && !initialized)) {
        log.debug("Sending init request to tooling server..")
        buildService.initializeProject(projectDir.absolutePath)
      } else {
        // The project initialization was in progress before the configuration change
        // In this case, we should not start another project initialization
        log.debug("Using cached initialize result as the project is already initialized")
        CompletableFuture.supplyAsync {
          log.warn("Project has already been initialized. Skipping initialization process.")
          cachedInitResult
        }
      }

    this.initializingFuture!!.whenCompleteAsync { result, error ->
      releaseServerListener()

      if (result == null || error != null) {
        log.error("An error occurred initializing the project with Tooling API", error)
        setStatus(getString(string.msg_project_initialization_failed))
        return@whenCompleteAsync
      }

      onProjectInitialized(result)
    }
  }

  private fun releaseServerListener() {
    // Release reference to server listener in order to prevent memory leak
    (Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) as? GradleBuildService?)
      ?.setServerListener(null)
  }

  override fun stopLanguageServers() {
    try {
      destroyLanguageServers(isChangingConfigurations)
    } catch (err: Throwable) {
      log.error("Unable to stop editor services. Please report this issue.", err)
    }
  }

  protected fun onGradleBuildServiceConnected(service: GradleBuildService) {
    log.info("Connected to Gradle build service")

    buildServiceConnection.onConnected = null
    viewModel.isBoundToBuildSerice = true
    Lookup.getDefault().update(BuildService.KEY_BUILD_SERVICE, service)
    service.setEventListener(mBuildEventListener)

    if (!service.isToolingServerStarted()) {
      service.startToolingServer { initializeProject() }
    } else {
      initializeProject()
    }
  }

  protected open fun onProjectInitialized(result: InitializeResult) {
    if (isFromSavedInstance && projectInitialized && result == cachedInitResult) {
      log.debug("Not setting up project as this a configuration change")
      return
    }

    cachedInitResult = result
    setupProject()
    notifyProjectUpdate()
    ThreadUtils.runOnUiThread { postProjectInit() }
  }

  protected open fun preProjectInit() {
    setStatus(getString(string.msg_initializing_project))
    viewModel.isInitializing = true
  }

  protected open fun postProjectInit() {
    initialSetup()
    setStatus(getString(string.msg_project_initialized))
    viewModel.isInitializing = false
    projectInitialized = true

    if (mFindInProjectDialog?.isShowing == true) {
      mFindInProjectDialog!!.dismiss()
    }

    mFindInProjectDialog = null // Create the dialog again if needed
  }

  protected open fun createFindInProjectDialog(): AlertDialog? {
    if (rootProject == null) {
      log.warn("No root project model found. Is the project initialized?")
      flashError(getString(string.msg_project_not_initialized))
      return null
    }

    val moduleDirs =
      try {
        rootProject!!.subModules.stream().map(Project::projectDir).collect(Collectors.toList())
      } catch (e: Throwable) {
        flashError(getString(string.msg_no_modules))
        emptyList()
      }

    return createFindInProjectDialog(moduleDirs)
  }

  protected open fun createFindInProjectDialog(moduleDirs: List<File>): AlertDialog? {
    val srcDirs = mutableListOf<File>()
    val binding = LayoutSearchProjectBinding.inflate(layoutInflater)
    binding.modulesContainer.removeAllViews()

    for (i in moduleDirs.indices) {
      val module = moduleDirs[i]
      val src = File(module, "src")

      if (!module.exists() || !module.isDirectory || !src.exists() || !src.isDirectory) {
        continue
      }

      val check = CheckBox(this)
      check.text = module.name
      check.isChecked = true

      val params = MarginLayoutParams(-2, -2)
      params.bottomMargin = SizeUtils.dp2px(4f)
      binding.modulesContainer.addView(check, params)
      srcDirs.add(src)
    }

    val builder = newMaterialDialogBuilder(this)
    builder.setTitle(string.menu_find_project)
    builder.setView(binding.root)
    builder.setCancelable(false)
    builder.setPositiveButton(string.menu_find) { dialog, _ ->
      val text = binding.input.editText!!.text.toString().trim()
      if (text.isEmpty()) {
        flashError(string.msg_empty_search_query)
        return@setPositiveButton
      }

      val searchDirs = mutableListOf<File>()
      for (i in 0 until binding.modulesContainer.childCount) {
        val check = binding.modulesContainer.getChildAt(i) as CheckBox
        if (check.isChecked) {
          searchDirs.add(srcDirs[i])
        }
      }

      val extensions = binding.filter.editText!!.text.toString().trim()
      val extensionList = mutableListOf<String>()
      if (extensions.isNotEmpty()) {
        if (extensions.contains("|")) {
          for (str in
            extensions
              .split(Pattern.quote("|").toRegex())
              .dropLastWhile { it.isEmpty() }
              .toTypedArray()) {
            if (str.trim().isEmpty()) {
              continue
            }
            extensionList.add(str)
          }
        } else {
          extensionList.add(extensions)
        }
      }

      if (searchDirs.isEmpty()) {
        flashError(string.msg_select_search_modules)
      } else {
        dialog.dismiss()

        getProgressSheet(string.msg_searching_project)?.apply {
          show(supportFragmentManager, "search_in_project_progress")
        }

        RecursiveFileSearcher.searchRecursiveAsync(text, extensionList, searchDirs) { results ->
          handleSearchResults(results)
        }
      }
    }

    builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
    mFindInProjectDialog = builder.create()
    return mFindInProjectDialog
  }

  private fun initialSetup() {
    lastOpenedProject = getProjectDirPath()
    try {
      val rootProject = rootProject
      if (rootProject == null) {
        log.warn("Project not initialized. Skipping initial setup...")
        return
      }

      var projectName = rootProject.name
      if (projectName.isEmpty()) {
        projectName = File(getProjectDirPath()).name
      }

      supportActionBar!!.subtitle = projectName
    } catch (th: Throwable) {
      // ignored
    }
  }

  private fun closeProject(manualFinish: Boolean) {
    if (manualFinish) {
      // if the user is manually closing the project,
      // save the opened files cache
      // this is needed because in this case, the opened files cache will be empty
      // when onPause will be called.
      saveOpenedFiles()
    }

    // Make sure we close files
    // This will make sure that file contents are not erased.
    doCloseAll {
      lastOpenedProject = NO_OPENED_PROJECT
      if (manualFinish) {
        finish()
      }
    }
  }

  private fun confirmProjectClose() {
    val builder = newMaterialDialogBuilder(this)
    builder.setTitle(string.title_confirm_project_close)
    builder.setMessage(string.msg_confirm_project_close)
    builder.setNegativeButton(string.no, null)
    builder.setPositiveButton(string.yes) { dialog, _ ->
      dialog.dismiss()
      closeProject(true)
    }
    builder.show()
  }

  private fun initLspClient() {
    if (!IDELanguageClientImpl.isInitialized()) {
      IDELanguageClientImpl.initialize(this as EditorHandlerActivity)
    }
    connectClient(IDELanguageClientImpl.getInstance())
  }

  open fun getProgressSheet(msg: Int): ProgressSheet? {
    doDismissSearchProgress()

    mSearchingProgress =
      ProgressSheet().also {
        it.isCancelable = false
        it.setMessage(getString(msg))
        it.setSubMessageEnabled(false)
      }

    return mSearchingProgress
  }
}
