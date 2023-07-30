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
import android.content.pm.PackageInstaller.SessionCallback
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.StrictMode
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.annotation.GravityInt
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ThreadUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.itsaky.androidide.BuildConfig
import com.itsaky.androidide.R.attr
import com.itsaky.androidide.R.drawable
import com.itsaky.androidide.R.id
import com.itsaky.androidide.R.string
import com.itsaky.androidide.actions.ActionItem.Location.EDITOR_FILE_TABS
import com.itsaky.androidide.activities.PreferencesActivity
import com.itsaky.androidide.activities.TerminalActivity
import com.itsaky.androidide.adapters.DiagnosticsAdapter
import com.itsaky.androidide.adapters.SearchListAdapter
import com.itsaky.androidide.app.IDEActivity
import com.itsaky.androidide.databinding.ActivityEditorBinding
import com.itsaky.androidide.databinding.LayoutDiagnosticInfoBinding
import com.itsaky.androidide.events.InstallationResultEvent
import com.itsaky.androidide.fragments.FileTreeFragment
import com.itsaky.androidide.fragments.SearchResultFragment
import com.itsaky.androidide.handlers.EditorActivityLifecyclerObserver
import com.itsaky.androidide.handlers.LspHandler.registerLanguageServers
import com.itsaky.androidide.interfaces.DiagnosticClickListener
import com.itsaky.androidide.logsender.LogSender
import com.itsaky.androidide.lookup.Lookup
import com.itsaky.androidide.lsp.models.DiagnosticItem
import com.itsaky.androidide.models.DiagnosticGroup
import com.itsaky.androidide.models.LogLine
import com.itsaky.androidide.models.OpenedFile
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.models.SearchResult
import com.itsaky.androidide.projects.ProjectManager.getProjectDirPath
import com.itsaky.androidide.projects.ProjectManager.projectPath
import com.itsaky.androidide.projects.builder.BuildService
import com.itsaky.androidide.services.log.LogReceiverService
import com.itsaky.androidide.services.log.LogReceiverServiceConnection
import com.itsaky.androidide.services.log.lookupLogService
import com.itsaky.androidide.ui.editor.CodeEditorView
import com.itsaky.androidide.uidesigner.UIDesignerActivity
import com.itsaky.androidide.utils.ActionMenuUtils.createMenu
import com.itsaky.androidide.utils.ApkInstallationSessionCallback
import com.itsaky.androidide.utils.DURATION_INDEFINITE
import com.itsaky.androidide.utils.DialogUtils.newMaterialDialogBuilder
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.InstallationResultHandler.onResult
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashbarBuilder
import com.itsaky.androidide.utils.resolveAttr
import com.itsaky.androidide.utils.showOnUiThread
import com.itsaky.androidide.utils.withIcon
import com.itsaky.androidide.viewmodel.EditorViewModel
import com.itsaky.androidide.xml.resources.ResourceTableRegistry
import com.itsaky.androidide.xml.versions.ApiVersionsRegistry
import com.itsaky.androidide.xml.widgets.WidgetTableRegistry
import java.io.File
import java.util.Objects
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN

/**
 * Base class for EditorActivity which handles most of the view related things.
 *
 * @author Akash Yadav
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseEditorActivity :
  IDEActivity(),
  TabLayout.OnTabSelectedListener,
  NavigationView.OnNavigationItemSelectedListener,
  DiagnosticClickListener {

  protected val mLifecycleObserver = EditorActivityLifecyclerObserver()
  protected var diagnosticInfoBinding: LayoutDiagnosticInfoBinding? = null
  protected var filesTreeFragment: FileTreeFragment? = null
  protected var editorBottomSheet: BottomSheetBehavior<out View?>? = null
  protected var isDestroying = false

  protected val log: ILogger = ILogger.newInstance("EditorActivity")
  protected val logServiceConnection = LogReceiverServiceConnection {
    lookupLogService()?.setConsumer(this::appendApkLog)
  }

  internal var installationCallback: ApkInstallationSessionCallback? = null

  var uiDesignerResultLauncher: ActivityResultLauncher<Intent>? = null
  val viewModel by viewModels<EditorViewModel>()
  lateinit var binding: ActivityEditorBinding
    protected set

  private val onBackPressedCallback: OnBackPressedCallback =
    object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        if (binding.root.isDrawerOpen(GravityCompat.END)) {
          binding.root.closeDrawer(GravityCompat.END)
        } else if (binding.root.isDrawerOpen(GravityCompat.START)) {
          binding.root.closeDrawer(GravityCompat.START)
        } else if (editorBottomSheet?.state != BottomSheetBehavior.STATE_COLLAPSED) {
          editorBottomSheet?.setState(BottomSheetBehavior.STATE_COLLAPSED)
        } else {
          doConfirmProjectClose()
        }
      }
    }

  companion object {
    const val EDITOR_CONTAINER_SCALE_FACTOR = 0.87f
    const val KEY_BOTTOM_SHEET_SHOWN = "editor_bottomSheetShown"
    const val KEY_PROJECT_PATH = "saved_projectPath"
  }

  protected abstract fun provideCurrentEditor(): CodeEditorView?

  protected abstract fun provideEditorAt(index: Int): CodeEditorView?

  protected abstract fun doOpenFile(file: File, selection: Range?)

  protected abstract fun doSaveAll(): Boolean

  protected abstract fun doDismissSearchProgress()

  protected abstract fun doConfirmProjectClose()

  protected abstract fun getOpenedFiles(): List<OpenedFile>

  protected open fun preDestroy() {
    installationCallback?.destroy()
    installationCallback = null

    try {
      lookupLogService()?.setConsumer(null)
      logServiceConnection.onConnected = null
      unbindService(logServiceConnection)
    } catch (e: Exception) {
      log.error("Failed to unbind LogReceiver service")
    }
  }

  protected open fun postDestroy() {
    if (isDestroying) {
      Lookup.getDefault().unregisterAll()
      ApiVersionsRegistry.getInstance().clear()
      ResourceTableRegistry.getInstance().clear()
      WidgetTableRegistry.getInstance().clear()
    }
  }

  override fun bindLayout(): View {
    this.binding = ActivityEditorBinding.inflate(layoutInflater)
    this.diagnosticInfoBinding = this.binding.diagnosticInfo
    return this.binding.root
  }

  @Subscribe(threadMode = MAIN)
  open fun onInstallationResult(event: InstallationResultEvent) {
    val intent = event.intent
    if (isDestroying) {
      return
    }

    val packageName = onResult(this, intent)
    if (packageName != null) {
      Snackbar.make(binding.realContainer, string.msg_action_open_application, Snackbar.LENGTH_LONG)
        .setAction(string.yes) { tryLaunchApp(packageName) }
        .show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    registerLanguageServers()

    if (savedInstanceState != null && savedInstanceState.containsKey(KEY_PROJECT_PATH)) {
      projectPath = savedInstanceState.getString(KEY_PROJECT_PATH)!!
    }

    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    lifecycle.addObserver(mLifecycleObserver)

    setSupportActionBar(binding.editorToolbar)

    setupDrawerToggle()
    binding.tabs.addOnTabSelectedListener(this)

    setupViews()

    KeyboardUtils.registerSoftInputChangedListener(this) { onSoftInputChanged() }
    startLogReceiver()
    setupContainers()
    setupDiagnosticInfo()

    uiDesignerResultLauncher =
      registerForActivityResult(StartActivityForResult(), this::handleUiDesignerResult)
  }

  override fun onPause() {
    super.onPause()
    this.isDestroying = isFinishing
    getFileTreeFragment()?.saveTreeState()
  }

  override fun onResume() {
    super.onResume()
    invalidateOptionsMenu()

    try {
      getFileTreeFragment()?.listProjectFiles()
    } catch (th: Throwable) {
      log.error("Failed to update files list", th)
      flashError(string.msg_failed_list_files)
    }
  }

  override fun onStop() {
    super.onStop()

    checkIsDestroying()
  }

  override fun onDestroy() {
    checkIsDestroying()
    preDestroy()
    super.onDestroy()
    postDestroy()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(KEY_PROJECT_PATH, getProjectDirPath())
    super.onSaveInstanceState(outState)
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      id.editornav_discuss -> app.openTelegramGroup()
      id.editornav_channel -> app.openTelegramChannel()
      id.editornav_suggest -> app.openGitHub()
      id.editornav_needHelp -> showNeedHelpDialog()
      id.editornav_settings -> startActivity(Intent(this, PreferencesActivity::class.java))
      id.editornav_share ->
        startActivity(IntentUtils.getShareTextIntent(getString(string.msg_share_app)))
      id.editornav_close_project -> doConfirmProjectClose()
      id.editornav_terminal -> openTerminal()
    }

    binding.root.closeDrawer(GravityCompat.START)
    return false
  }

  override fun onTabSelected(tab: Tab) {
    val position = tab.position
    viewModel.displayedFileIndex = position

    val editorView = provideEditorAt(position)!!
    editorView.onEditorSelected()

    viewModel.setCurrentFile(position, editorView.file)
    refreshSymbolInput(editorView)
    invalidateOptionsMenu()
  }

  override fun onTabUnselected(tab: Tab) {}

  override fun onTabReselected(tab: Tab) {
    createMenu(this, tab.view, EDITOR_FILE_TABS, true).show()
  }

  override fun onGroupClick(group: DiagnosticGroup?) {
    if (group?.file?.exists() == true && FileUtils.isUtf8(group.file)) {
      doOpenFile(group.file, null)
      hideBottomSheet()
    }
  }

  override fun onDiagnosticClick(file: File, diagnostic: DiagnosticItem) {
    doOpenFile(file, diagnostic.range)
    hideBottomSheet()
  }

  open fun appendApkLog(line: LogLine) {
    binding.bottomSheet.appendApkLog(line)
  }

  open fun handleSearchResults(map: Map<File, List<SearchResult>>?) {
    val results = map ?: emptyMap()
    setSearchResultAdapter(
      SearchListAdapter(
        results,
        { file ->
          doOpenFile(file, null)
          hideBottomSheet()
        }
      ) { match ->
        doOpenFile(match.file, match)
        hideBottomSheet()
      }
    )

    showSearchResults()
    doDismissSearchProgress()
  }

  open fun setSearchResultAdapter(adapter: SearchListAdapter) {
    binding.bottomSheet.setSearchResultAdapter(adapter)
  }

  open fun setDiagnosticsAdapter(adapter: DiagnosticsAdapter) {
    binding.bottomSheet.setDiagnosticsAdapter(adapter)
  }

  open fun hideBottomSheet() {
    if (editorBottomSheet?.state != BottomSheetBehavior.STATE_COLLAPSED) {
      editorBottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
    }
  }

  open fun showSearchResults() {
    if (editorBottomSheet?.state != BottomSheetBehavior.STATE_EXPANDED) {
      editorBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    val index =
      binding.bottomSheet.pagerAdapter.findIndexOfFragmentByClass(SearchResultFragment::class.java)

    if (index >= 0 && index < binding.bottomSheet.binding.tabs.tabCount) {
      binding.bottomSheet.binding.tabs.getTabAt(index)?.select()
    }
  }

  open fun handleDiagnosticsResultVisibility(errorVisible: Boolean) {
    binding.bottomSheet.handleDiagnosticsResultVisibility(errorVisible)
  }

  open fun handleSearchResultVisibility(errorVisible: Boolean) {
    binding.bottomSheet.handleSearchResultVisibility(errorVisible)
  }

  open fun showFirstBuildNotice() {
    newMaterialDialogBuilder(this)
      .setPositiveButton(string.ok, null)
      .setTitle(string.title_first_build)
      .setMessage(string.msg_first_build)
      .setCancelable(false)
      .create()
      .show()
  }

  fun notifySyncNeeded(onConfirm: () -> Unit) {
    val buildService = Lookup.getDefault().lookup(BuildService.KEY_BUILD_SERVICE)
    if (buildService == null || buildService.isBuildInProgress) return

    flashbarBuilder(
        duration = DURATION_INDEFINITE,
        backgroundColor = resolveAttr(attr.colorSecondaryContainer),
        messageColor = resolveAttr(attr.colorOnSecondaryContainer)
      )
      .withIcon(drawable.ic_sync, colorFilter = resolveAttr(attr.colorOnSecondaryContainer))
      .message(string.msg_sync_needed)
      .positiveActionText(string.btn_sync)
      .positiveActionTapListener {
        onConfirm()
        it.dismiss()
      }
      .negativeActionText(string.btn_ignore_changes)
      .negativeActionTapListener { it.dismiss() }
      .showOnUiThread()
  }

  open fun getFileTreeFragment(): FileTreeFragment? {
    if (filesTreeFragment == null) {
      filesTreeFragment =
        supportFragmentManager.findFragmentByTag(FileTreeFragment.TAG) as FileTreeFragment?
    }
    return filesTreeFragment
  }

  fun doSetStatus(text: CharSequence, @GravityInt gravity: Int) {
    viewModel.statusText = text
    viewModel.statusGravity = gravity
  }

  private fun tryLaunchApp(packageName: String) {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
    launchIntent?.let {
      try {
        startActivity(it)
      } catch (e: Throwable) {
        flashError(string.msg_app_launch_failed)
        log.error("Failed to launch application with package name '$packageName'", e)
      }
    }
  }

  private fun checkIsDestroying() {
    if (!isDestroying && isFinishing) {
      isDestroying = true
    }
  }

  private fun handleUiDesignerResult(result: ActivityResult) {
    if (result.resultCode != RESULT_OK || result.data == null) {
      log.warn("UI Designer returned invalid result", result.resultCode, result.data)
      return
    }
    val generated = result.data!!.getStringExtra(UIDesignerActivity.RESULT_GENERATED_XML)
    if (TextUtils.isEmpty(generated)) {
      log.warn("UI Designer returned blank generated XML code")
      return
    }
    val view = provideCurrentEditor()
    if (view?.editor == null) {
      log.warn("No file opened to append UI designer result")
      return
    }
    val text = view.editor.text
    val endLine = text.lineCount - 1
    text.replace(0, 0, endLine, text.getColumnCount(endLine), generated)
  }

  private fun setupDrawerToggle() {
    val toggle =
      ActionBarDrawerToggle(
        this,
        binding.editorDrawerLayout,
        binding.editorToolbar,
        string.app_name,
        string.app_name
      )

    binding.editorDrawerLayout.addDrawerListener(toggle)
    binding.startNav.setNavigationItemSelectedListener(this)
    toggle.syncState()
    binding.editorDrawerLayout.childId = binding.realContainer.id
  }

  private fun onBuildStatusChanged() {
    val visible = viewModel.isBuildInProgress || viewModel.isInitializing
    binding.buildProgressIndicator.visibility = if (visible) View.VISIBLE else View.GONE
    invalidateOptionsMenu()
  }

  private fun setupViews() {
    viewModel._isBuildInProgress.observe(this) { onBuildStatusChanged() }
    viewModel._isInitializing.observe(this) { onBuildStatusChanged() }
    viewModel._statusText.observe(this) { binding.bottomSheet.setStatus(it.first, it.second) }

    viewModel.observeFiles(this) { files ->
      binding.apply {
        if (files.isNullOrEmpty()) {
          tabs.visibility = View.GONE
          viewContainer.displayedChild = 1
        } else {
          tabs.visibility = View.VISIBLE
          viewContainer.displayedChild = 0
        }
      }
    }

    setupNoEditorView()
    setupBottomSheet()

    if (
      !app.prefManager.getBoolean(KEY_BOTTOM_SHEET_SHOWN) &&
        editorBottomSheet?.state != BottomSheetBehavior.STATE_EXPANDED
    ) {
      editorBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
      ThreadUtils.runOnUiThreadDelayed(
        {
          editorBottomSheet?.state = BottomSheetBehavior.STATE_COLLAPSED
          app.prefManager.putBoolean(KEY_BOTTOM_SHEET_SHOWN, true)
        },
        1500
      )
    }
  }

  private fun setupNoEditorView() {
    binding.noEditorSummary.movementMethod = LinkMovementMethod()
    val filesSpan: ClickableSpan =
      object : ClickableSpan() {
        override fun onClick(widget: View) {
          binding.root.openDrawer(GravityCompat.END)
        }
      }
    val bottomSheetSpan: ClickableSpan =
      object : ClickableSpan() {
        override fun onClick(widget: View) {
          editorBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }
      }
    val sb = SpannableStringBuilder()
    appendClickableSpan(sb, string.msg_swipe_for_files, filesSpan)
    appendClickableSpan(sb, string.msg_swipe_for_output, bottomSheetSpan)
    binding.noEditorSummary.text = sb
  }

  private fun appendClickableSpan(
    sb: SpannableStringBuilder,
    @StringRes textRes: Int,
    span: ClickableSpan,
  ) {
    val str = getString(textRes)
    val split = str.split("@@", limit = 3)
    if (split.size != 3) {
      // Not a valid format
      sb.append(str)
      sb.append('\n')
      return
    }
    sb.append(split[0])
    sb.append(split[1], span, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    sb.append(split[2])
    sb.append('\n')
  }

  private fun setupBottomSheet() {
    editorBottomSheet = BottomSheetBehavior.from<View>(binding.bottomSheet)
    editorBottomSheet?.addBottomSheetCallback(
      object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
          if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            val editor = provideCurrentEditor()
            if (editor?.editor != null) {
              editor.editor.ensureWindowsDismissed()
            }
          }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
          binding.apply {
            val editorScale = 1 - slideOffset * (1 - EDITOR_CONTAINER_SCALE_FACTOR)
            this.bottomSheet.onSlide(slideOffset)
            this.viewContainer.scaleX = editorScale
            this.viewContainer.scaleY = editorScale
          }
        }
      }
    )

    val observer: OnGlobalLayoutListener =
      object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
          binding.let {
            it.viewContainer.pivotY = 0f
            it.viewContainer.pivotX = it.viewContainer.width / 2f
            it.viewContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
          }
        }
      }

    binding.apply {
      viewContainer.viewTreeObserver.addOnGlobalLayoutListener(observer)
      bottomSheet.setOffsetAnchor(editorToolbar)
    }
  }

  private fun refreshSymbolInput(editor: CodeEditorView) {
    binding.bottomSheet.refreshSymbolInput(editor)
  }

  private fun setupDiagnosticInfo() {
    val gd = GradientDrawable()
    gd.shape = GradientDrawable.RECTANGLE
    gd.setColor(-0xdededf)
    gd.setStroke(1, -0x1)
    gd.cornerRadius = 8f
    diagnosticInfoBinding?.root?.background = gd
    diagnosticInfoBinding?.root?.visibility = View.GONE
  }

  private fun setupContainers() {
    handleDiagnosticsResultVisibility(true)
    handleSearchResultVisibility(true)
  }

  private fun onSoftInputChanged() {
    invalidateOptionsMenu()
    binding.bottomSheet.onSoftInputChanged()
  }

  private fun openTerminal() {
    val intent = Intent(this, TerminalActivity::class.java)
    intent.putExtra(
      TerminalActivity.KEY_WORKING_DIRECTORY,
      Objects.requireNonNull(getProjectDirPath())
    )
    startActivity(intent)
  }

  private fun startLogReceiver() {
    try {
      val intent = Intent(this, LogReceiverService::class.java)
        .setAction(LogSender.SERVICE_ACTION)
      check(bindService(intent, logServiceConnection, BIND_AUTO_CREATE or BIND_IMPORTANT))
      log.info("LogReceiver service is being started")
    } catch (err: Throwable) {
      log.error("Failed to start LogReceiver service", err)
    }
  }

  private fun showNeedHelpDialog() {
    val builder = newMaterialDialogBuilder(this)
    builder.setTitle(string.need_help)
    builder.setMessage(string.msg_need_help)
    builder.setPositiveButton(string.ok, null)
    builder.create().show()
  }

  open fun installationSessionCallback(): SessionCallback {
    return ApkInstallationSessionCallback(this).also { installationCallback = it }
  }
}
