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
import android.view.Gravity
import android.view.ViewGroup.MarginLayoutParams
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.annotation.GravityInt
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.databinding.LayoutSearchProjectBinding
import com.itsaky.androidide.flashbar.Flashbar
import com.itsaky.androidide.fragments.sheets.ProgressSheet
import com.itsaky.androidide.handlers.EditorBuildEventListener
import com.itsaky.androidide.handlers.LspHandler.connectClient
import com.itsaky.androidide.handlers.LspHandler.destroyLanguageServers
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.IDELanguageClientImpl
import com.itsaky.androidide.lsp.java.utils.CancelChecker
import com.itsaky.androidide.preferences.internal.GeneralPreferences
import com.itsaky.androidide.projects.GradleProject
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.projects.internal.ProjectManagerImpl
import com.itsaky.androidide.services.builder.GradleBuildService
import com.itsaky.androidide.services.builder.GradleBuildServiceConnnection
import com.itsaky.androidide.services.builder.gradleDistributionParams
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.tasks.executeWithProgress
import com.itsaky.androidide.tooling.api.messages.AndroidInitializationParams
import com.itsaky.androidide.tooling.api.messages.InitializeProjectParams
import com.itsaky.androidide.tooling.api.messages.result.InitializeResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_DIRECTORY_INACCESSIBLE
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_DIRECTORY
import com.itsaky.androidide.tooling.api.messages.result.TaskExecutionResult.Failure.PROJECT_NOT_FOUND
import com.itsaky.androidide.tooling.api.models.BuildVariantInfo
import com.itsaky.androidide.tooling.api.models.mapToSelectedVariants
import com.itsaky.androidide.utils.DURATION_INDEFINITE
import com.itsaky.androidide.utils.DialogUtils.newMaterialDialogBuilder
import com.itsaky.androidide.utils.RecursiveFileSearcher
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashbarBuilder
import com.itsaky.androidide.utils.resolveAttr
import com.itsaky.androidide.utils.showOnUiThread
import com.itsaky.androidide.utils.withIcon
import com.itsaky.androidide.viewmodel.BuildVariantsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern
import java.util.stream.Collectors

/** @author Akash Yadav */
@Suppress("MemberVisibilityCanBePrivate")
abstract class ProjectHandlerActivity : BaseEditorActivity() {

  protected val buildVariantsViewModel by viewModels<BuildVariantsViewModel>()

  protected var mSearchingProgress: ProgressSheet? = null
  protected var mFindInProjectDialog: AlertDialog? = null
  protected var syncNotificationFlashbar: Flashbar? = null

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

  private val buildServiceConnection = GradleBuildServiceConnnection()

  companion object {

    const val STATE_KEY_FROM_SAVED_INSTANACE = "ide.editor.isFromSavedInstance"
    const val STATE_KEY_SHOULD_INITIALIZE = "ide.editor.isInitializing"
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

    editorViewModel._isSyncNeeded.observe(this) { isSyncNeeded ->
      if (!isSyncNeeded) {
        // dismiss if already showing
        syncNotificationFlashbar?.dismiss()
        return@observe
      }

      if (syncNotificationFlashbar?.isShowing() == true) {
        // already shown
        return@observe
      }

      notifySyncNeeded()
    }

    startServices()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.apply {
      putBoolean(STATE_KEY_SHOULD_INITIALIZE, !editorViewModel.isInitializing)
      putBoolean(STATE_KEY_FROM_SAVED_INSTANACE, true)
    }
  }

  override fun onPause() {
    super.onPause()
    if (isDestroying) {
      // reset these values here
      // sometimes, when the IDE closed and reopened instantly, these values prevent initialization
      // of the project
      ProjectManagerImpl.getInstance().destroy()

      editorViewModel.isInitializing = false
      editorViewModel.isBuildInProgress = false
    }
  }

  override fun preDestroy() {

    syncNotificationFlashbar?.dismiss()
    syncNotificationFlashbar = null

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
        Lookup.getDefault().apply {

          (lookup(BuildService.KEY_BUILD_SERVICE) as? GradleBuildService?)
            ?.setEventListener(null)

          unregister(BuildService.KEY_BUILD_SERVICE)
        }

        mBuildEventListener.release()
        editorViewModel.isBoundToBuildSerice = false
      }
    }
  }

  fun setStatus(status: CharSequence) {
    setStatus(status, Gravity.CENTER)
  }

  fun setStatus(status: CharSequence, @GravityInt gravity: Int) {
    doSetStatus(status, gravity)
  }

  fun appendBuildOutput(str: String) {
    content.bottomSheet.appendBuildOut(str)
  }

  fun notifySyncNeeded() {
    notifySyncNeeded { initializeProject() }
  }

  private fun notifySyncNeeded(onConfirm: () -> Unit) {
    val buildService = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)
    if (buildService == null || editorViewModel.isInitializing || buildService.isBuildInProgress) return

    this.syncNotificationFlashbar?.dismiss()

    this.syncNotificationFlashbar = flashbarBuilder(
      duration = DURATION_INDEFINITE,
      backgroundColor = resolveAttr(R.attr.colorSecondaryContainer),
      messageColor = resolveAttr(R.attr.colorOnSecondaryContainer)
    )
      .withIcon(R.drawable.ic_sync, colorFilter = resolveAttr(R.attr.colorOnSecondaryContainer))
      .message(string.msg_sync_needed)
      .positiveActionText(string.btn_sync)
      .positiveActionTapListener {
        onConfirm()
        it.dismiss()
      }
      .negativeActionText(string.btn_ignore_changes)
      .negativeActionTapListener(Flashbar::dismiss)
      .build()

    this.syncNotificationFlashbar?.showOnUiThread()

  }

  fun startServices() {
    val service = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) as GradleBuildService?
    if (editorViewModel.isBoundToBuildSerice && service != null) {
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

  /**
   * Initialize (sync) the project.
   *
   * @param buildVariantsProvider A function which returns the map of project paths to the selected build variants.
   *    This function is called asynchronously.
   */
  fun initializeProject(buildVariantsProvider: () -> Map<String, String>) {
    executeWithProgress { progress ->
      executeAsyncProvideError(buildVariantsProvider::invoke) { result, error ->
        com.itsaky.androidide.tasks.runOnUiThread {
          progress.dismiss()
        }

        if (result == null || error != null) {
          val msg = getString(string.msg_build_variants_fetch_failed)
          flashError(msg)
          log.error(msg, error)
          return@executeAsyncProvideError
        }

        com.itsaky.androidide.tasks.runOnUiThread {
          initializeProject(result)
        }
      }
    }
  }

  fun initializeProject() {
    val currentVariants = buildVariantsViewModel._buildVariants.value

    // no information about the build variants is available
    // use the default variant selections
    if (currentVariants == null) {
      log.debug(
        "No variant selection information available. Default build variants will be selected."
      )
      initializeProject(emptyMap())
      return
    }

    // variant selection information is available
    // but there are updated & unsaved variant selections
    // use the updated variant selections to initialize the project
    if (buildVariantsViewModel.updatedBuildVariants.isNotEmpty()) {
      val newSelections = currentVariants.toMutableMap()
      newSelections.putAll(buildVariantsViewModel.updatedBuildVariants)
      initializeProject {
        newSelections.mapToSelectedVariants().also {
          log.debug("Initializing project with new build variant selections: {}", it)
        }
      }
      return
    }

    // variant selection information is available but no variant selections have been updated
    // the user might be trying to sync the project from options menu
    // initialize the project with the existing selected variants
    initializeProject {
      log.debug("Re-initializing project with existing build variant selections")
      currentVariants.mapToSelectedVariants()
    }
  }

  /**
   * Initialize (sync) the project.
   *
   * @param buildVariants A map of project paths to the selected build variants.
   */
  fun initializeProject(buildVariants: Map<String, String>) {
    val manager = ProjectManagerImpl.getInstance()
    val projectDir = manager.projectDir
    if (!projectDir.exists()) {
      log.error("GradleProject directory does not exist. Cannot initialize project")
      return
    }

    val initialized = manager.projectInitialized && manager.cachedInitResult != null
    log.debug("Is project initialized: {}", initialized)
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
        buildService.initializeProject(createProjectInitParams(projectDir, buildVariants))
      } else {
        // The project initialization was in progress before the configuration change
        // In this case, we should not start another project initialization
        log.debug("Using cached initialize result as the project is already initialized")
        CompletableFuture.supplyAsync {
          log.warn("GradleProject has already been initialized. Skipping initialization process.")
          manager.cachedInitResult
        }
      }

    this.initializingFuture!!.whenCompleteAsync { result, error ->
      releaseServerListener()

      if (result == null || !result.isSuccessful || error != null) {
        if (!CancelChecker.isCancelled(error)) {
          log.error("An error occurred initializing the project with Tooling API", error)
        }

        ThreadUtils.runOnUiThread {
          postProjectInit(false, result?.failure)
        }
        return@whenCompleteAsync
      }

      onProjectInitialized(result)
    }
  }

  private fun createProjectInitParams(
    projectDir: File,
    buildVariants: Map<String, String>
  ): InitializeProjectParams {
    return InitializeProjectParams(
      projectDir.absolutePath,
      gradleDistributionParams,
      createAndroidParams(buildVariants)
    )
  }

  private fun createAndroidParams(buildVariants: Map<String, String>): AndroidInitializationParams {
    if (buildVariants.isEmpty()) {
      return AndroidInitializationParams.DEFAULT
    }

    return AndroidInitializationParams(buildVariants)
  }

  private fun releaseServerListener() {
    // Release reference to server listener in order to prevent memory leak
    (Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE) as? GradleBuildService?)
      ?.setServerListener(null)
  }

  fun stopLanguageServers() {
    try {
      destroyLanguageServers(isChangingConfigurations)
    } catch (err: Throwable) {
      log.error("Unable to stop editor services. Please report this issue.", err)
    }
  }

  protected fun onGradleBuildServiceConnected(service: GradleBuildService) {
    log.info("Connected to Gradle build service")

    buildServiceConnection.onConnected = null
    editorViewModel.isBoundToBuildSerice = true
    Lookup.getDefault().update(BuildService.KEY_BUILD_SERVICE, service)
    service.setEventListener(mBuildEventListener)

    if (!service.isToolingServerStarted()) {
      service.startToolingServer { pid ->
        memoryUsageWatcher.watchProcess(pid, PROC_GRADLE_TOOLING)
        resetMemUsageChart()

        service.metadata().whenComplete { metadata, err ->
          if (metadata == null || err != null) {
            log.error("Failed to get tooling server metadata")
            return@whenComplete
          }

          if (pid != metadata.pid) {
            log.warn(
              "Tooling server pid mismatch. Expected: {}, Actual: {}. Replacing memory watcher...",
              pid, metadata.pid
            )
            memoryUsageWatcher.watchProcess(metadata.pid, PROC_GRADLE_TOOLING)
            resetMemUsageChart()
          }
        }

        initializeProject()
      }
    } else {
      initializeProject()
    }
  }

  protected open fun onProjectInitialized(result: InitializeResult) {
    val manager = ProjectManagerImpl.getInstance()
    if (isFromSavedInstance && manager.projectInitialized && result == manager.cachedInitResult) {
      log.debug("Not setting up project as this a configuration change")
      return
    }

    manager.cachedInitResult = result
    editorActivityScope.launch(Dispatchers.IO) {
      manager.setupProject()
      manager.notifyProjectUpdate()
      updateBuildVariants(manager.requireWorkspace().getAndroidVariantSelections())

      com.itsaky.androidide.tasks.runOnUiThread {
        postProjectInit(true, null)
      }
    }
  }

  protected open fun preProjectInit() {
    setStatus(getString(string.msg_initializing_project))
    editorViewModel.isInitializing = true
  }

  protected open fun postProjectInit(isSuccessful: Boolean, failure: TaskExecutionResult.Failure?) {
    val manager = ProjectManagerImpl.getInstance()
    if (!isSuccessful) {
      val initFailed = getString(string.msg_project_initialization_failed)
      setStatus(initFailed)

      val msg = when (failure) {
        PROJECT_DIRECTORY_INACCESSIBLE -> string.msg_project_dir_inaccessible
        PROJECT_NOT_DIRECTORY -> string.msg_file_is_not_dir
        PROJECT_NOT_FOUND -> string.msg_project_dir_doesnt_exist
        else -> null
      }?.let {
        "$initFailed: ${getString(it)}"
      }

      flashError(msg ?: initFailed)

      editorViewModel.isInitializing = false
      manager.projectInitialized = false
      return
    }

    initialSetup()
    setStatus(getString(string.msg_project_initialized))
    editorViewModel.isInitializing = false
    manager.projectInitialized = true

    if (mFindInProjectDialog?.isShowing == true) {
      mFindInProjectDialog!!.dismiss()
    }

    mFindInProjectDialog = null // Create the dialog again if needed
  }

  private fun updateBuildVariants(buildVariants: Map<String, BuildVariantInfo>) {
    // avoid using the 'runOnUiThread' method defined in the activity
    com.itsaky.androidide.tasks.runOnUiThread {
      buildVariantsViewModel.buildVariants = buildVariants
      buildVariantsViewModel.resetUpdatedSelections()
    }
  }

  protected open fun createFindInProjectDialog(): AlertDialog? {
    val manager = ProjectManagerImpl.getInstance()
    if (manager.getWorkspace() == null) {
      log.warn("No root project model found. Is the project initialized?")
      flashError(getString(string.msg_project_not_initialized))
      return null
    }

    val moduleDirs =
      try {
        manager.getWorkspace()!!.getSubProjects().stream().map(GradleProject::projectDir)
          .collect(Collectors.toList())
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
    val manager = ProjectManagerImpl.getInstance()
    GeneralPreferences.lastOpenedProject = manager.projectDirPath
    try {
      val workspace = manager.getWorkspace()
      if (workspace == null) {
        log.warn("GradleProject not initialized. Skipping initial setup...")
        return
      }

      var projectName = workspace.getRootProject().name
      if (projectName.isEmpty()) {
        projectName = manager.projectDir.name
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

      // reset the lastOpenedProject if the user explicitly chose to close the project
      GeneralPreferences.lastOpenedProject = GeneralPreferences.NO_OPENED_PROJECT
    }

    // Make sure we close files
    // This will make sure that file contents are not erased.
    doCloseAll {
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
