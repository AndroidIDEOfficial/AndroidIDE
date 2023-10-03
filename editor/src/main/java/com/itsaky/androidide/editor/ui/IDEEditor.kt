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

package com.itsaky.androidide.editor.ui

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import com.blankj.utilcode.util.FileUtils
import com.itsaky.androidide.editor.R.string
import com.itsaky.androidide.editor.adapters.CompletionListAdapter
import com.itsaky.androidide.editor.api.IEditor
import com.itsaky.androidide.editor.api.ILspEditor
import com.itsaky.androidide.editor.language.IDELanguage
import com.itsaky.androidide.editor.language.cpp.CppLanguage
import com.itsaky.androidide.editor.language.groovy.GroovyLanguage
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguage
import com.itsaky.androidide.editor.language.treesitter.TreeSitterLanguageProvider
import com.itsaky.androidide.editor.schemes.IDEColorScheme
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider.readScheme
import com.itsaky.androidide.editor.snippets.AbstractSnippetVariableResolver
import com.itsaky.androidide.editor.snippets.FileVariableResolver
import com.itsaky.androidide.editor.snippets.WorkspaceVariableResolver
import com.itsaky.androidide.eventbus.events.editor.ChangeType
import com.itsaky.androidide.eventbus.events.editor.ColorSchemeInvalidatedEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentCloseEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentOpenEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentSaveEvent
import com.itsaky.androidide.eventbus.events.editor.DocumentSelectedEvent
import com.itsaky.androidide.flashbar.Flashbar
import com.itsaky.androidide.lsp.api.ILanguageClient
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.models.Command
import com.itsaky.androidide.lsp.models.DefinitionParams
import com.itsaky.androidide.lsp.models.DefinitionResult
import com.itsaky.androidide.lsp.models.ExpandSelectionParams
import com.itsaky.androidide.lsp.models.ReferenceParams
import com.itsaky.androidide.lsp.models.ReferenceResult
import com.itsaky.androidide.lsp.models.ShowDocumentParams
import com.itsaky.androidide.lsp.models.SignatureHelp
import com.itsaky.androidide.lsp.models.SignatureHelpParams
import com.itsaky.androidide.models.Position
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.preferences.internal.tabSize
import com.itsaky.androidide.preferences.internal.visiblePasswordFlag
import com.itsaky.androidide.progress.ICancelChecker
import com.itsaky.androidide.projects.FileManager.onDocumentClose
import com.itsaky.androidide.projects.FileManager.onDocumentContentChange
import com.itsaky.androidide.projects.FileManager.onDocumentOpen
import com.itsaky.androidide.syntax.colorschemes.DynamicColorScheme
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.utils.DocumentUtils
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.flashProgress
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.event.SelectionChangeEvent
import io.github.rosemoe.sora.lang.EmptyLanguage
import io.github.rosemoe.sora.lang.Language
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.EditorSearcher
import io.github.rosemoe.sora.widget.IDEEditorSearcher
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.component.EditorBuiltinComponent
import io.github.rosemoe.sora.widget.component.EditorTextActionWindow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.function.Consumer

/**
 * [CodeEditor] implementation for the IDE.
 *
 * @author Akash Yadav
 */
open class IDEEditor @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0,
  defStyleRes: Int = 0
) : CodeEditor(context, attrs, defStyleAttr, defStyleRes), IEditor, ILspEditor {

  private var _actionsMenu: EditorActionsMenu? = null
  private var _signatureHelpWindow: SignatureHelpWindow? = null
  private var _diagnosticWindow: DiagnosticWindow? = null
  private var file: File? = null
  private var fileVersion = 0
  private var isModified = false

  private val selectionChangeHandler = Handler(Looper.getMainLooper())
  private var selectionChangeRunner: Runnable? = Runnable {
    val languageClient = languageClient ?: return@Runnable
    val cursor = this.cursor ?: return@Runnable

    if (cursor.isSelected || _signatureHelpWindow?.isShowing == true) {
      return@Runnable
    }

    diagnosticWindow.showDiagnostic(
      languageClient.getDiagnosticAt(file, cursor.leftLine, cursor.leftColumn)
    )
  }

  protected val editorScope = CoroutineScope(Dispatchers.Default)

  var languageServer: ILanguageServer? = null
    private set

  var languageClient: ILanguageClient? = null
    private set

  /**
   * Whether the cursor position change animation is enabled for the editor.
   */
  var isEnsurePosAnimEnabled = true

  /**
   * The text searcher for the editor.
   */
  lateinit var searcher: IDEEditorSearcher

  /**
   * The signature help window for the editor.
   */
  val signatureHelpWindow: SignatureHelpWindow
    get() {
      return _signatureHelpWindow ?: SignatureHelpWindow(this)
        .also { _signatureHelpWindow = it }
    }

  /**
   * The diagnostic window for the editor.
   */
  val diagnosticWindow: DiagnosticWindow
    get() {
      return _diagnosticWindow ?: DiagnosticWindow(this)
        .also { _diagnosticWindow = it }
    }

  companion object {

    private const val SELECTION_CHANGE_DELAY = 500L

    private val log = ILogger.newInstance("IDEEditor")

    /**
     * Create input type flags for the editor.
     */
    fun createInputTypeFlags(): Int {
      var flags = EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE or
          EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
      if (visiblePasswordFlag) {
        flags = flags or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
      }
      return flags
    }
  }

  init {
    initEditor()
  }

  override fun getFile(): File? {
    return this.file
  }

  /**
   * Set the file for this editor.
   */
  fun setFile(file: File?) {
    this.file = file
    file?.also {
      dispatchDocumentOpenEvent()
    }
  }

  override fun isModified(): Boolean {
    return this.isModified
  }

  override fun setSelection(position: Position) {
    setSelection(position.line, position.column)
  }

  override fun setSelection(start: Position, end: Position) {
    if (!isValidPosition(start, true) || !isValidPosition(end, true)) {
      log.warn("Invalid selection range: start=$start end=$end")
      return
    }

    setSelectionRegion(start.line, start.column, end.line, end.column)
  }

  override fun getCursorLSPRange(): Range {
    val end = cursor.right().let {
      Position(line = it.line, column = it.column, index = it.index)
    }
    return Range(cursorLSPPosition, end)
  }

  override fun getCursorLSPPosition(): Position {
    return cursor.left().let {
      Position(line = it.line, column = it.column, index = it.index)
    }
  }

  override fun validateRange(range: Range) {
    val start = range.start
    val end = range.end
    val text = text
    val lineCount = text.lineCount

    start.line = 0.coerceAtLeast(start.line).coerceAtMost(lineCount - 1)
    start.column = 0.coerceAtLeast(start.column).coerceAtMost(text.getColumnCount(start.line))

    end.line = 0.coerceAtLeast(end.line).coerceAtMost(lineCount - 1)
    end.column = 0.coerceAtLeast(end.column).coerceAtMost(text.getColumnCount(end.line))
  }

  override fun isValidRange(range: Range?, allowColumnEqual: Boolean): Boolean {
    if (range == null) {
      return false
    }
    val start = range.start
    val end = range.end
    return isValidPosition(start, allowColumnEqual)
        // make sure start position is before end position
        && isValidPosition(end, allowColumnEqual) && start < end
  }

  override fun isValidPosition(position: Position?, allowColumnEqual: Boolean): Boolean {
    return if (position == null) {
      false
    } else isValidLine(position.line) &&
        isValidColumn(position.line, position.column, allowColumnEqual)
  }

  override fun isValidLine(line: Int): Boolean {
    return line >= 0 && line < text.lineCount
  }

  override fun isValidColumn(line: Int, column: Int, allowEqual: Boolean): Boolean {
    val columnCount = text.getColumnCount(line)
    return column >= 0 && (column < columnCount || allowEqual && column == columnCount)
  }

  override fun append(text: CharSequence?): Int {
    val content = getText()
    if (lineCount <= 0) {
      return 0
    }
    val line = lineCount - 1
    var col = content.getColumnCount(line)
    if (col < 0) {
      col = 0
    }
    content.insert(line, col, text)
    return line
  }

  @UiThread
  override fun replaceContent(newContent: CharSequence?) {
    val lastLine = text.lineCount - 1
    val lastColumn = text.getColumnCount(lastLine)
    text.replace(0, 0, lastLine, lastColumn, newContent ?: "")
  }

  override fun goToEnd() {
    val line = text.lineCount - 1
    setSelection(line, 0)
  }

  override fun setLanguageServer(server: ILanguageServer?) {
    this.languageServer = server
    server?.also {
      this.languageClient = it.client
      snippetController.apply {
        fileVariableResolver = FileVariableResolver(this@IDEEditor)
        workspaceVariableResolver = WorkspaceVariableResolver()
      }
    }
  }

  override fun setLanguageClient(client: ILanguageClient?) {
    this.languageClient = client
  }

  override fun executeCommand(command: Command?) {
    if (command == null) {
      log.warn("Cannot execute command in editor. Command is null.")
      return
    }

    log.info(String.format("Executing command '%s' for completion item.", command.title))
    when (command.command) {
      Command.TRIGGER_COMPLETION -> {
        val completion = getComponent(EditorAutoCompletion::class.java)
        completion.requireCompletion()
      }

      Command.TRIGGER_PARAMETER_HINTS -> signatureHelp()
      Command.FORMAT_CODE -> formatCodeAsync()
    }
  }

  override fun signatureHelp() {
    val languageServer = this.languageServer ?: return
    val file = this.file ?: return

    this.languageClient ?: return

    editorScope.launch {
      val params = SignatureHelpParams(file.toPath(), cursorLSPPosition)
      val help = languageServer.signatureHelp(params)

      withContext(Dispatchers.Main) {
        showSignatureHelp(help)
      }
    }
  }

  override fun showSignatureHelp(help: SignatureHelp?) {
    signatureHelpWindow.setupAndDisplay(help)
  }

  override fun findDefinition() {
    val languageServer = this.languageServer ?: return
    val file = file ?: return

    launchAsyncWithProgress(string.msg_finding_definition) { flashbar, cancelChecker ->
      val params = DefinitionParams(file.toPath(), cursorLSPPosition)
      val result = languageServer.findDefinition(params, cancelChecker)
      onFindDefinitionResult(flashbar, result)
    }
  }

  override fun findReferences() {
    val languageServer = this.languageServer ?: return
    val file = file ?: return

    launchAsyncWithProgress(string.msg_finding_references) { flashbar, cancelChecker ->
      delay(3000L)
      val params = ReferenceParams(file.toPath(), cursorLSPPosition, true)
      val result = languageServer.findReferences(params, cancelChecker)
      onFindReferencesResult(flashbar, result)
    }
  }

  override fun expandSelection() {
    val languageServer = this.languageServer ?: return
    val file = file ?: return

    launchAsyncWithProgress(string.please_wait) { flashbar, _ ->
      val params = ExpandSelectionParams(file.toPath(), cursorLSPRange)
      val result = languageServer.expandSelection(params)

      withContext(Dispatchers.Main) {
        flashbar.dismiss()
        setSelection(result)
      }
    }
  }

  override fun ensureWindowsDismissed() {
    if (_diagnosticWindow?.isShowing == true) {
      _diagnosticWindow?.dismiss()
    }

    if (_signatureHelpWindow?.isShowing == true) {
      _signatureHelpWindow?.dismiss()
    }

    if (_actionsMenu?.isShowing == true) {
      _actionsMenu?.dismiss()
    }
  }

  // not overridable!
  final override fun <T : EditorBuiltinComponent?> replaceComponent(
    clazz: Class<T>,
    replacement: T & Any
  ) {
    super.replaceComponent(clazz, replacement)
  }

  // not overridable
  final override fun <T : EditorBuiltinComponent?> getComponent(clazz: Class<T>): T & Any {
    return super.getComponent(clazz)
  }

  override fun release() {
    ensureWindowsDismissed()
    super.release()

    snippetController.apply {
      (fileVariableResolver as? AbstractSnippetVariableResolver?)?.close()
      (workspaceVariableResolver as? AbstractSnippetVariableResolver?)?.close()

      fileVariableResolver = null
      workspaceVariableResolver = null
    }

    _actionsMenu?.destroy()
    _actionsMenu = null
    languageServer = null
    languageClient = null

    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this)
    }

    if (editorScope.isActive) {
      editorScope.cancelIfActive("Editor is releasing resources.")
    }
  }

  override fun getSearcher(): EditorSearcher {
    return this.searcher
  }

  override fun getExtraArguments(): Bundle {
    return super.getExtraArguments().apply {
      putString(IEditor.KEY_FILE, file?.absolutePath)
    }
  }

  override fun ensurePositionVisible(line: Int, column: Int, noAnimation: Boolean) {
    super.ensurePositionVisible(line, column, !isEnsurePosAnimEnabled || noAnimation)
  }

  override fun getTabWidth(): Int {
    return tabSize
  }

  override fun beginSearchMode() {
    throw UnsupportedOperationException(
      "Search ActionMode is not supported. Use CodeEditorView.beginSearch() instead.")
  }

  override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
    super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
    if (!gainFocus) {
      ensureWindowsDismissed()
    }
  }

  /**
   * Analyze the opened file and publish the diagnostics result.
   */
  open fun analyze() {
    if (editorLanguage !is IDELanguage) {
      return
    }

    val languageServer = languageServer ?: return
    val file = file ?: return

    editorScope.launch {
      val result = languageServer.analyze(file.toPath())
      languageClient?.publishDiagnostics(result)
    }
  }

  /**
   * Mark this editor as NOT modified.
   */
  open fun markUnmodified() {
    this.isModified = false
  }

  /**
   * Mark this editor as modified.
   */
  open fun markModified() {
    this.isModified = true
  }

  /**
   * Notify the language server that the file in this editor is about to be closed.
   */
  open fun notifyClose() {
    file ?: run {
      log.info("Cannot notify language server. File is null.")
      return
    }

    dispatchDocumentCloseEvent()

    _actionsMenu?.unsubscribeEvents()
    selectionChangeRunner?.also {
      selectionChangeHandler.removeCallbacks(it)
    }

    selectionChangeRunner = null

    ensureWindowsDismissed()
  }

  /**
   * Called when this editor is selected and visible to the user.
   */
  open fun onEditorSelected() {
    file ?: return
    dispatchDocumentSelectedEvent()
  }

  /**
   * Dispatches the [DocumentSaveEvent] for this editor.
   */
  open fun dispatchDocumentSaveEvent() {
    markUnmodified()
    if (getFile() == null) {
      return
    }
    val saveEvent = DocumentSaveEvent(getFile()!!.toPath())
    EventBus.getDefault().post(saveEvent)
  }

  /**
   * Called when the color scheme has been invalidated. This usually happens when the user reloads
   * the color schemes.
   */
  @Suppress("unused")
  @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
  open fun onColorSchemeInvalidated(event: ColorSchemeInvalidatedEvent?) {
    val file = getFile() ?: return
    setupLanguage(file)
  }

  /**
   * Setup the editor language for the given [file].
   *
   * This applies a proper [Language] and the color scheme to the editor.
   */
  open fun setupLanguage(file: File?) {
    if (file == null) {
      return
    }
    val language = createLanguage(file)
    val extension = file.extension
    if (language is TreeSitterLanguage) {
      readScheme(context, extension,
        Consumer { scheme: SchemeAndroidIDE? ->
          applyTreeSitterLang(language, extension, scheme)
        })
    } else {
      setEditorLanguage(language)
    }
  }

  /**
   * Applies the given [TreeSitterLanguage] and the [color scheme][scheme] for the given [file type][type].
   */
  open fun applyTreeSitterLang(language: TreeSitterLanguage?, type: String,
    scheme: SchemeAndroidIDE?
  ) {
    applyTreeSitterLang(language as Language?, type, scheme)
  }

  private fun applyTreeSitterLang(
    language: Language?,
    type: String,
    scheme: SchemeAndroidIDE?
  ) {
    var finalScheme = if (scheme != null) {
      scheme
    } else {
      log.error("Failed to read current color scheme")
      SchemeAndroidIDE.newInstance(context)
    }

    if (finalScheme is IDEColorScheme &&
      finalScheme.getLanguageScheme(type) == null
    ) {
      log.warn("Color scheme does not support file type '$type'")
      finalScheme = SchemeAndroidIDE.newInstance(context)
    }

    if (finalScheme is DynamicColorScheme) {
      finalScheme.apply(context)
    }

    colorScheme = finalScheme!!
    setEditorLanguage(language)
  }

  private fun createLanguage(file: File): Language {
    if (!file.isFile) {
      return EmptyLanguage()
    }

    val tsLang = TreeSitterLanguageProvider.forFile(file, context)
    if (tsLang != null) {
      return tsLang
    }

    return when (FileUtils.getFileExtension(file)) {
      "gradle" -> GroovyLanguage()
      "c", "h", "cc", "cpp", "cxx" -> CppLanguage()
      else -> EmptyLanguage()
    }
  }

  /**
   * Update the file for this editor. This does not dispatch the [DocumentOpenEvent].
   */
  open fun updateFile(file: File?) {
    this.file = file
  }

  /**
   * Initialize the editor.
   */
  protected open fun initEditor() {
    _actionsMenu = EditorActionsMenu(this).also {
      it.init()
    }

    markUnmodified()

    searcher = IDEEditorSearcher(this)
    colorScheme = SchemeAndroidIDE.newInstance(context)
    inputType = createInputTypeFlags()

    val window = EditorCompletionWindow(this)
    window.setAdapter(CompletionListAdapter())
    replaceComponent(EditorAutoCompletion::class.java, window)

    getComponent(EditorTextActionWindow::class.java).isEnabled = false

    subscribeEvent(ContentChangeEvent::class.java) { event, _ ->
      markModified()
      file ?: return@subscribeEvent

      editorScope.launch {
        dispatchDocumentChangeEvent(event)
        checkForSignatureHelp(event)
      }
    }

    subscribeEvent(SelectionChangeEvent::class.java) { _, _ ->
      if (_diagnosticWindow?.isShowing == true) {
        _diagnosticWindow?.dismiss()
      }

      selectionChangeRunner?.also {
        selectionChangeHandler.removeCallbacks(it)
        selectionChangeHandler.postDelayed(it, SELECTION_CHANGE_DELAY)
      }
    }

    EventBus.getDefault().register(this)
  }

  protected open fun launchAsyncWithProgress(
    message: Int,
    action: suspend CoroutineScope.(flashbar: Flashbar, cancelChecker: ICancelChecker) -> Unit
  ): Job {
    val cancelChecker = object : ICancelChecker.Default() {
      var job: Job? = null

      override fun cancel() {
        job?.cancel("Cancelled by user")
        job = null
        super.cancel()
      }
    }

    return (context as Activity).flashProgress({
      configureFlashbar(this, string.msg_finding_definition, cancelChecker)
    }) { flashbar ->
      return@flashProgress editorScope.launch {
        cancelChecker.job = coroutineContext[Job]
        action(flashbar, cancelChecker)
      }
    }!!
  }

  protected open suspend fun onFindDefinitionResult(
    flashbar: Flashbar,
    result: DefinitionResult?,
  ) = withContext(Dispatchers.Main) {

    flashbar.dismiss()

    val languageClient = languageClient ?: run {
      log.error("No language client found to handle the definitions result")
      return@withContext
    }

    if (result == null) {
      log.error("Invalid definitions result from language server (null)")
      flashError(string.msg_no_definition)
      return@withContext
    }

    val locations = result.locations
    if (locations.isEmpty()) {
      log.error("No definitions found")
      flashError(string.msg_no_definition)
      return@withContext
    }

    if (locations.size != 1) {
      languageClient.showLocations(locations)
      return@withContext
    }

    val (file1, range) = locations[0]
    if (DocumentUtils.isSameFile(file1, getFile()!!.toPath())) {
      setSelection(range)
      return@withContext
    }

    languageClient.showDocument(
      ShowDocumentParams(file1, range)
    )
  }

  protected open suspend fun onFindReferencesResult(
    flashbar: Flashbar,
    result: ReferenceResult?
  ) = withContext(Dispatchers.Main) {
    flashbar.dismiss()

    flashbar.dismiss()

    val languageClient = languageClient ?: run {
      log.error("No language client found to handle the references result")
      return@withContext
    }

    if (result == null) {
      log.error("Invalid references result from language server (null)")
      flashError(string.msg_no_references)
      return@withContext
    }

    val locations = result.locations
    if (locations.isEmpty()) {
      log.error("No references found")
      flashError(string.msg_no_references)
      return@withContext
    }

    if (result.locations.size == 1) {
      val (file, range) = result.locations[0]

      if (DocumentUtils.isSameFile(file, getFile()!!.toPath())) {
        setSelection(range)
        return@withContext
      }
    }

    languageClient.showLocations(result.locations)
  }

  protected open fun dispatchDocumentOpenEvent() {
    val file = this.file ?: return

    this.fileVersion = 0

    val openEvent = DocumentOpenEvent(
      file.toPath(),
      text.toString(),
      fileVersion
    )

    // Notify FileManager first
    onDocumentOpen(openEvent)
    EventBus.getDefault().post(openEvent)
  }

  protected open fun dispatchDocumentChangeEvent(event: ContentChangeEvent) {
    val file = file?.toPath() ?: return
    var type = ChangeType.INSERT
    if (event.action == ContentChangeEvent.ACTION_DELETE) {
      type = ChangeType.DELETE
    } else if (event.action == ContentChangeEvent.ACTION_SET_NEW_TEXT) {
      type = ChangeType.NEW_TEXT
    }
    var changeDelta = if (type == ChangeType.NEW_TEXT) 0 else event.changedText.length
    if (type == ChangeType.DELETE) {
      changeDelta = -changeDelta
    }
    val start = event.changeStart
    val end = event.changeEnd
    val changeRange = Range(Position(start.line, start.column, start.index),
      Position(end.line, end.column, end.index))
    val changedText = event.changedText.toString()
    val changeEvent = DocumentChangeEvent(file, changedText, text.toString(),
      ++fileVersion, type, changeDelta, changeRange)

    // Notify FileManager first
    onDocumentContentChange(changeEvent)
    EventBus.getDefault().post(changeEvent)
  }

  protected open fun dispatchDocumentSelectedEvent() {
    val file = file ?: return
    val selectedEvent = DocumentSelectedEvent(file.toPath())
    EventBus.getDefault().post(selectedEvent)
  }

  protected open fun dispatchDocumentCloseEvent() {
    val file = file ?: return
    val closeEvent = DocumentCloseEvent(file.toPath(), cursorLSPRange)

    // Notify FileManager first
    onDocumentClose(closeEvent)
    EventBus.getDefault().post(closeEvent)
  }

  /**
   * Checks if the content change event should trigger signature help. Signature help trigger
   * characters are :
   *
   *
   *  * `'('` (parentheses)
   *  * `','` (comma)
   *
   *
   * @param event The content change event.
   */
  private fun checkForSignatureHelp(event: ContentChangeEvent) {
    if (languageServer == null) {
      return
    }
    val changeLength = event.changedText.length
    if (event.action != ContentChangeEvent.ACTION_INSERT || changeLength in 1..2) {
      // changeLength will be 2 as '(' and ')' are inserted at the same time
      return
    }

    val ch = event.changedText[0]
    if (ch == '(' || ch == ',') {
      signatureHelp()
    }
  }

  private fun configureFlashbar(
    builder: Flashbar.Builder,
    @StringRes message: Int,
    cancelChecker: ICancelChecker
  ) {
    builder.message(message)
      .negativeActionText(android.R.string.cancel)
      .negativeActionTapListener { bar: Flashbar ->
        cancelChecker.cancel()
        bar.dismiss()
      }
  }

  private suspend fun Flashbar.dismissOnUiThread() {
    withContext(Dispatchers.Main) {
      dismiss()
    }
  }
}