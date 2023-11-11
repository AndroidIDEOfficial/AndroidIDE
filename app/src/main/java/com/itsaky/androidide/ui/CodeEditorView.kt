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

package com.itsaky.androidide.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils
import com.itsaky.androidide.activities.editor.BaseEditorActivity
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.editor.api.IEditor
import com.itsaky.androidide.editor.databinding.LayoutCodeEditorBinding
import com.itsaky.androidide.editor.ui.EditorSearchLayout
import com.itsaky.androidide.editor.ui.IDEEditor
import com.itsaky.androidide.editor.ui.IDEEditor.Companion.createInputTypeFlags
import com.itsaky.androidide.editor.utils.ContentReadWrite.readContent
import com.itsaky.androidide.editor.utils.ContentReadWrite.writeTo
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent
import com.itsaky.androidide.lsp.IDELanguageClientImpl
import com.itsaky.androidide.lsp.api.ILanguageServer
import com.itsaky.androidide.lsp.api.ILanguageServerRegistry
import com.itsaky.androidide.lsp.java.JavaLanguageServer
import com.itsaky.androidide.lsp.xml.XMLLanguageServer
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.preferences.internal.DELETE_EMPTY_LINES
import com.itsaky.androidide.preferences.internal.DELETE_TABS_ON_BACKSPACE
import com.itsaky.androidide.preferences.internal.FLAG_LINE_BREAK
import com.itsaky.androidide.preferences.internal.FLAG_PASSWORD
import com.itsaky.androidide.preferences.internal.FLAG_WS_EMPTY_LINE
import com.itsaky.androidide.preferences.internal.FLAG_WS_INNER
import com.itsaky.androidide.preferences.internal.FLAG_WS_LEADING
import com.itsaky.androidide.preferences.internal.FLAG_WS_TRAILING
import com.itsaky.androidide.preferences.internal.FONT_LIGATURES
import com.itsaky.androidide.preferences.internal.FONT_SIZE
import com.itsaky.androidide.preferences.internal.PIN_LINE_NUMBERS
import com.itsaky.androidide.preferences.internal.STICKY_SCROLL_ENABLED
import com.itsaky.androidide.preferences.internal.USE_CUSTOM_FONT
import com.itsaky.androidide.preferences.internal.USE_ICU
import com.itsaky.androidide.preferences.internal.USE_MAGNIFER
import com.itsaky.androidide.preferences.internal.WORD_WRAP
import com.itsaky.androidide.preferences.internal.deleteEmptyLines
import com.itsaky.androidide.preferences.internal.deleteTabsOnBackspace
import com.itsaky.androidide.preferences.internal.drawEmptyLineWs
import com.itsaky.androidide.preferences.internal.drawInnerWs
import com.itsaky.androidide.preferences.internal.drawLeadingWs
import com.itsaky.androidide.preferences.internal.drawLineBreak
import com.itsaky.androidide.preferences.internal.drawTrailingWs
import com.itsaky.androidide.preferences.internal.fontLigatures
import com.itsaky.androidide.preferences.internal.fontSize
import com.itsaky.androidide.preferences.internal.pinLineNumbers
import com.itsaky.androidide.preferences.internal.stickyScrollEnabled
import com.itsaky.androidide.preferences.internal.useCustomFont
import com.itsaky.androidide.preferences.internal.useIcu
import com.itsaky.androidide.preferences.internal.useMagnifier
import com.itsaky.androidide.preferences.internal.wordwrap
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.tasks.runOnUiThread
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.customOrJBMono
import io.github.rosemoe.sora.text.Content
import io.github.rosemoe.sora.text.LineSeparator
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.component.Magnifier
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Closeable
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * A view that handles opened code editor.
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor")
class CodeEditorView(
  context: Context,
  file: File,
  selection: Range
) : LinearLayoutCompat(context), Closeable {

  private var _binding: LayoutCodeEditorBinding? = null
  private var _searchLayout: EditorSearchLayout? = null

  private val codeEditorScope = CoroutineScope(
    Dispatchers.Default + CoroutineName("CodeEditorView"))

  private val binding: LayoutCodeEditorBinding
    get() = checkNotNull(_binding) { "Binding has been destroyed" }

  private val searchLayout: EditorSearchLayout
    get() = checkNotNull(_searchLayout) { "Search layout has been destroyed" }

  /**
   * Get the file of this editor.
   */
  val file: File?
    get() = editor?.file

  /**
   * Get the [IDEEditor] instance of this editor view.
   */
  val editor: IDEEditor?
    get() = _binding?.editor

  /**
   * Returns whether the content of the editor has been modified.
   *
   * @see IDEEditor.isModified
   */
  val isModified: Boolean
    get() = editor?.isModified ?: false

  companion object {

    private val log = ILogger.newInstance("CodeEditorView")
  }

  init {
    _binding = LayoutCodeEditorBinding.inflate(LayoutInflater.from(context))

    binding.editor.apply {
      isHighlightCurrentBlock = true
      props.autoCompletionOnComposing = true
      dividerWidth = SizeUtils.dp2px(2f).toFloat()
      colorScheme = SchemeAndroidIDE.newInstance(context)
      lineSeparator = LineSeparator.LF
    }

    _searchLayout = EditorSearchLayout(context, binding.editor)

    orientation = VERTICAL

    removeAllViews()
    addView(binding.root, LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f))
    addView(searchLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

    readFileAndApplySelection(file, selection)
  }

  /**
   * Get the file of this editor. Throws [IllegalStateException] if no file is available.
   */
  fun requireFile(): File {
    return checkNotNull(file)
  }

  /**
   * Update the file of this editor. This only updates the file reference of the editor and does
   * not resets the content.
   */
  fun updateFile(file: File) {
    val editor = _binding?.editor ?: return
    editor.file = file
    postRead(file)
  }

  /**
   * Called when the editor has been selected and is visible to the user.
   */
  fun onEditorSelected() {
    _binding?.editor?.onEditorSelected() ?: run {
      log.warn("onEditorSelected() called but no editor instance is available")
    }
  }

  /**
   * Begins search mode and shows the [search layout][EditorSearchLayout].
   */
  fun beginSearch() {
    if (_binding == null || _searchLayout == null) {
      log.warn(
        "Editor layout is null content=$binding, searchLayout=$searchLayout")
      return
    }

    searchLayout.beginSearchMode()
  }

  /**
   * Mark this files as saved. Even if it not saved.
   */
  fun markAsSaved() {
    notifySaved()
  }

  /**
   * Saves the content of the editor to the editor's file.
   *
   * @return Whether the save operation was successfully completed or not. If this method returns `false`,
   * it means that there was an error saving the file or the content of the file was not modified and
   * hence the save operation was skipped.
   */
  suspend fun save(): Boolean {
    val file = this.file ?: return false

    if (!isModified && file.exists()) {
      log.info(file.name)
      log.info("File was not modified. Skipping save operation.")
      return false
    }

    val text = _binding?.editor?.text ?: run {
      log.error("Failed to save file. Unable to retrieve the content of editor as it is null.")
      return false
    }

    disableEditingAndRun(Dispatchers.IO) {
      // Do not call suspend functions in this scope
      // the writeTo function acquires lock to the Content object before writing and releases
      // the lock after writing
      // if there are any suspend function calls in between, the lock and unlock calls might not
      // be called on the same thread
      text.writeTo(file, this@CodeEditorView::updateReadWriteProgress)
    }

    withContext(Dispatchers.Main) {
      _binding?.rwProgress?.isVisible = false
    }

    notifySaved()

    return true
  }

  private fun updateReadWriteProgress(progress: Int) {
    val binding = this.binding
    runOnUiThread {
      if (binding.rwProgress.isVisible && (progress < 0 || progress >= 100)) {
        binding.rwProgress.isVisible = false
        return@runOnUiThread
      }

      if (!binding.rwProgress.isVisible) {
        binding.rwProgress.isVisible = true
      }

      binding.rwProgress.progress = progress
    }
  }

  private suspend inline fun <R> disableEditingAndRun(
    context: CoroutineContext = EmptyCoroutineContext,
    crossinline action: CoroutineScope.() -> R
  ): R {
    return withContext(Dispatchers.Main) {
      _binding?.editor?.isEditable = false

      val result = withContext(context) {
        action()
      }

      _binding?.editor?.isEditable = true
      return@withContext result
    }
  }

  private fun readFileAndApplySelection(file: File, selection: Range) {
    updateReadWriteProgress(0)
    codeEditorScope.launch {
      val content = disableEditingAndRun(Dispatchers.IO) {
        selection.validate()
        file.readContent(this@CodeEditorView::updateReadWriteProgress)
      }

      withContext(Dispatchers.Main) {
        initializeContent(content, file, selection)
        _binding?.rwProgress?.isVisible = false
      }
    }
  }

  private fun initializeContent(content: Content, file: File, selection: Range) {
    binding.editor.apply {
      val args = Bundle().apply {
        putString(IEditor.KEY_FILE, file.absolutePath)
      }

      setText(content, args)

      // editor.setText(...) sets the modified flag to true
      // but in this case, file is read from disk and hence the contents are not modified at all
      // so the flag must be changed to unmodified
      // TODO: Find a better way to check content modification status
      markUnmodified()
      postRead(file)

      validateRange(selection)
      setSelection(selection)
    }

    configureEditorIfNeeded()
  }

  private fun postRead(file: File) {
    binding.editor.setupLanguage(file)
    binding.editor.setLanguageServer(createLanguageServer(file))

    if (IDELanguageClientImpl.isInitialized()) {
      binding.editor.setLanguageClient(IDELanguageClientImpl.getInstance())
    }

    // File must be set only after setting the language server
    // This will make sure that textDocument/didOpen is sent
    binding.editor.file = file

    // do not pass this editor instance
    // symbol input must be updated for the current editor
    (context as? BaseEditorActivity?)?.refreshSymbolInput()
    (context as? Activity?)?.invalidateOptionsMenu()
  }

  private fun createLanguageServer(file: File): ILanguageServer? {
    if (!file.isFile) {
      return null
    }

    val serverID: String = when (file.extension) {
      "java" -> JavaLanguageServer.SERVER_ID
      "xml" -> XMLLanguageServer.SERVER_ID
      else -> return null
    }

    return ILanguageServerRegistry.getDefault().getServer(serverID)
  }

  private fun configureEditorIfNeeded() {
    onCustomFontPrefChanged()
    onFontSizePrefChanged()
    onFontLigaturesPrefChanged()
    onPrintingFlagsPrefChanged()
    onInputTypePrefChanged()
    onWordwrapPrefChanged()
    onMagnifierPrefChanged()
    onUseIcuPrefChanged()
    onDeleteEmptyLinesPrefChanged()
    onDeleteTabsPrefChanged()
    onStickyScrollEnabeldPrefChanged()
    onPinLineNumbersPrefChanged()
  }

  private fun onMagnifierPrefChanged() {
    binding.editor.getComponent(Magnifier::class.java).isEnabled = useMagnifier
  }

  private fun onWordwrapPrefChanged() {
    val enabled = wordwrap
    binding.editor.isWordwrap = enabled
  }

  private fun onInputTypePrefChanged() {
    binding.editor.inputType = createInputTypeFlags()
  }

  private fun onPrintingFlagsPrefChanged() {
    var flags = 0
    if (drawLeadingWs) {
      flags = flags or CodeEditor.FLAG_DRAW_WHITESPACE_LEADING
    }
    if (drawTrailingWs) {
      flags = flags or CodeEditor.FLAG_DRAW_WHITESPACE_TRAILING
    }
    if (drawInnerWs) {
      flags = flags or CodeEditor.FLAG_DRAW_WHITESPACE_INNER
    }
    if (drawEmptyLineWs) {
      flags = flags or CodeEditor.FLAG_DRAW_WHITESPACE_FOR_EMPTY_LINE
    }
    if (drawLineBreak) {
      flags = flags or CodeEditor.FLAG_DRAW_LINE_SEPARATOR
    }
    binding.editor.nonPrintablePaintingFlags = flags
  }

  private fun onFontLigaturesPrefChanged() {
    val enabled = fontLigatures
    binding.editor.isLigatureEnabled = enabled
  }

  private fun onFontSizePrefChanged() {
    var textSize = fontSize
    if (textSize < 6 || textSize > 32) {
      textSize = 14f
    }
    binding.editor.setTextSize(textSize)
  }

  private fun onUseIcuPrefChanged() {
    binding.editor.props.useICULibToSelectWords = useIcu
  }

  private fun onCustomFontPrefChanged() {
    val state = useCustomFont
    binding.editor.typefaceText = customOrJBMono(state)
    binding.editor.typefaceLineNumber = customOrJBMono(state)
  }

  private fun onDeleteEmptyLinesPrefChanged() {
    binding.editor.props.deleteEmptyLineFast = deleteEmptyLines
  }

  private fun onDeleteTabsPrefChanged() {
    binding.editor.props.deleteMultiSpaces = if (deleteTabsOnBackspace) -1 else 1
  }

  private fun onStickyScrollEnabeldPrefChanged() {
    binding.editor.props.stickyScroll = stickyScrollEnabled
  }

  private fun onPinLineNumbersPrefChanged() {
    binding.editor.setPinLineNumber(pinLineNumbers)
  }

  /**
   * For internal use only!
   *
   *
   * Marks this editor as unmodified. Used only when the activity is being destroyed.
   */
  internal fun markUnmodified() {
    binding.editor.markUnmodified()
  }

  /**
   * For internal use only!
   *
   *
   * Marks this editor as modified.
   */
  internal fun markModified() {
    binding.editor.markModified()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  @Suppress("unused")
  fun onPreferenceChanged(event: PreferenceChangeEvent) {
    if (_binding == null) {
      return
    }

    BaseApplication.getBaseInstance().prefManager
    when (event.key) {
      FONT_SIZE -> onFontSizePrefChanged()
      FONT_LIGATURES -> onFontLigaturesPrefChanged()

      FLAG_LINE_BREAK,
      FLAG_WS_INNER,
      FLAG_WS_EMPTY_LINE,
      FLAG_WS_LEADING,
      FLAG_WS_TRAILING -> onPrintingFlagsPrefChanged()

      FLAG_PASSWORD -> onInputTypePrefChanged()
      WORD_WRAP -> onWordwrapPrefChanged()
      USE_MAGNIFER -> onMagnifierPrefChanged()
      USE_ICU -> onUseIcuPrefChanged()
      USE_CUSTOM_FONT -> onCustomFontPrefChanged()
      DELETE_EMPTY_LINES -> onDeleteEmptyLinesPrefChanged()
      DELETE_TABS_ON_BACKSPACE -> onDeleteTabsPrefChanged()
      STICKY_SCROLL_ENABLED -> onStickyScrollEnabeldPrefChanged()
      PIN_LINE_NUMBERS -> onPinLineNumbersPrefChanged()
    }
  }

  /**
   * Notifies the editor that its content has been saved.
   */
  private fun notifySaved() {
    binding.editor.dispatchDocumentSaveEvent()
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    EventBus.getDefault().unregister(this)
  }

  override fun close() {
    codeEditorScope.cancelIfActive("Cancellation was requested")
    _binding?.editor?.apply {
      notifyClose()
      release()
    }
  }
}