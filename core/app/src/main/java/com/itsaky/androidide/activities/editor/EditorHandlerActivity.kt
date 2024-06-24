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

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams
import androidx.annotation.DrawableRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.collection.MutableIntObjectMap
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.blankj.utilcode.util.ImageUtils
import com.itsaky.androidide.R.string
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem.Location.EDITOR_TOOLBAR
import com.itsaky.androidide.actions.ActionsRegistry.Companion.getInstance
import com.itsaky.androidide.actions.FillMenuParams
import com.itsaky.androidide.editor.language.treesitter.JavaLanguage
import com.itsaky.androidide.editor.language.treesitter.JsonLanguage
import com.itsaky.androidide.editor.language.treesitter.KotlinLanguage
import com.itsaky.androidide.editor.language.treesitter.LogLanguage
import com.itsaky.androidide.editor.language.treesitter.TSLanguageRegistry
import com.itsaky.androidide.editor.language.treesitter.XMLLanguage
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.editor.ui.IDEEditor
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.interfaces.IEditorHandler
import com.itsaky.androidide.models.FileExtension
import com.itsaky.androidide.models.OpenedFile
import com.itsaky.androidide.models.OpenedFilesCache
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.models.SaveResult
import com.itsaky.androidide.projects.internal.ProjectManagerImpl
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.ui.CodeEditorView
import com.itsaky.androidide.utils.DialogUtils.newYesNoDialog
import com.itsaky.androidide.utils.IntentUtils.openImage
import com.itsaky.androidide.utils.UniqueNameBuilder
import com.itsaky.androidide.utils.flashSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.set

/**
 * Base class for EditorActivity. Handles logic for working with file editors.
 *
 * @author Akash Yadav
 */
open class EditorHandlerActivity : ProjectHandlerActivity(), IEditorHandler {

  protected val isOpenedFilesSaved = AtomicBoolean(false)

  override fun doOpenFile(file: File, selection: Range?) {
    openFileAndSelect(file, selection)
  }

  override fun doCloseAll(runAfter: () -> Unit) {
    closeAll(runAfter)
  }

  override fun provideCurrentEditor(): CodeEditorView? {
    return getCurrentEditor()
  }

  override fun provideEditorAt(index: Int): CodeEditorView? {
    return getEditorAtIndex(index)
  }

  override fun preDestroy() {
    super.preDestroy()
    TSLanguageRegistry.instance.destroy()
    editorViewModel.removeAllFiles()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    mBuildEventListener.setActivity(this)
    super.onCreate(savedInstanceState)

    editorViewModel._displayedFile.observe(
      this) { this.content.editorContainer.displayedChild = it }
    editorViewModel._startDrawerOpened.observe(this) { opened ->
      this.binding.editorDrawerLayout.apply {
        if (opened) openDrawer(GravityCompat.START) else closeDrawer(GravityCompat.START)
      }
    }

    editorViewModel._filesModified.observe(this) { invalidateOptionsMenu() }
    editorViewModel._filesSaving.observe(this) { invalidateOptionsMenu() }

    editorViewModel.observeFiles(this) {
      // rewrite the cached files index if there are any opened files
      val currentFile =
        getCurrentEditor()?.editor?.file?.absolutePath
          ?: run {
            editorViewModel.writeOpenedFiles(null)
            editorViewModel.openedFilesCache = null
            return@observeFiles
          }
      getOpenedFiles().also {
        val cache = OpenedFilesCache(currentFile, it)
        editorViewModel.writeOpenedFiles(cache)
        editorViewModel.openedFilesCache = cache
      }
    }

    executeAsync {
      TSLanguageRegistry.instance.register(JavaLanguage.TS_TYPE, JavaLanguage.FACTORY)
      TSLanguageRegistry.instance.register(KotlinLanguage.TS_TYPE_KT, KotlinLanguage.FACTORY)
      TSLanguageRegistry.instance.register(KotlinLanguage.TS_TYPE_KTS, KotlinLanguage.FACTORY)
      TSLanguageRegistry.instance.register(LogLanguage.TS_TYPE, LogLanguage.FACTORY)
      TSLanguageRegistry.instance.register(JsonLanguage.TS_TYPE, JsonLanguage.FACTORY)
      TSLanguageRegistry.instance.register(XMLLanguage.TS_TYPE, XMLLanguage.FACTORY)
      IDEColorSchemeProvider.initIfNeeded()
    }
  }

  override fun onPause() {
    super.onPause()

    // if the user manually closes the project, this will be true
    // in this case, don't overwrite the already saved cache
    if (!isOpenedFilesSaved.get()) {
      saveOpenedFiles()
    }
  }

  override fun onResume() {
    super.onResume()
    isOpenedFilesSaved.set(false)
  }

  override fun saveOpenedFiles() {
    writeOpenedFilesCache(getOpenedFiles(), getCurrentEditor()?.editor?.file)
  }

  private fun writeOpenedFilesCache(openedFiles: List<OpenedFile>, selectedFile: File?) {
    if (selectedFile == null || openedFiles.isEmpty()) {
      editorViewModel.writeOpenedFiles(null)
      editorViewModel.openedFilesCache = null
      log.debug("[onPause] No opened files. Opened files cache reset to null.")
      isOpenedFilesSaved.set(true)
      return
    }

    val cache = OpenedFilesCache(selectedFile = selectedFile.absolutePath, allFiles = openedFiles)

    editorViewModel.writeOpenedFiles(cache)
    editorViewModel.openedFilesCache = if (!isDestroying) cache else null
    log.debug("[onPause] Opened files cache reset to {}", editorViewModel.openedFilesCache)
    isOpenedFilesSaved.set(true)
  }

  override fun onStart() {
    super.onStart()

    try {
      editorViewModel.getOrReadOpenedFilesCache(this::onReadOpenedFilesCache)
      editorViewModel.openedFilesCache = null
    } catch (err: Throwable) {
      log.error("Failed to reopen recently opened files", err)
    }
  }

  private fun onReadOpenedFilesCache(cache: OpenedFilesCache?) {
    cache ?: return
    cache.allFiles.forEach { file ->
      openFile(File(file.filePath), file.selection)
    }
    openFile(File(cache.selectedFile))
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    prepareOptionsMenu(menu)
    return true
  }

  @SuppressLint("RestrictedApi")
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }

    val data = createToolbarActionData()
    getInstance().fillMenu(FillMenuParams(data, EDITOR_TOOLBAR, menu))
    return true
  }

  open fun prepareOptionsMenu(menu: Menu) {
    val data = createToolbarActionData()
    val actions = getInstance().getActions(EDITOR_TOOLBAR)
    actions.forEach { (_, action) ->
      menu.findItem(action.itemId)?.let { item ->
        action.prepare(data)

        item.isVisible = action.visible
        item.isEnabled = action.enabled
        item.title = action.label

        item.icon = action.icon?.apply {
          colorFilter = action.createColorFilter(data)
          alpha = if (action.enabled) 255 else 76
        }

        var showAsAction = action.getShowAsActionFlags(data)
        if (showAsAction == -1) {
          showAsAction = if (action.icon != null) {
            MenuItem.SHOW_AS_ACTION_IF_ROOM
          } else {
            MenuItem.SHOW_AS_ACTION_NEVER
          }
        }

        if (!action.enabled) {
          showAsAction = MenuItem.SHOW_AS_ACTION_NEVER
        }

        item.setShowAsAction(showAsAction)

        action.createActionView(data)?.let { item.actionView = it }
      }
    }
    content.editorToolbar.updateMenuDisplay()
  }

  private fun createToolbarActionData(): ActionData {
    val data = ActionData()
    val currentEditor = getCurrentEditor()

    data.put(Context::class.java, this)
    data.put(CodeEditorView::class.java, currentEditor)

    if (currentEditor != null) {
      data.put(IDEEditor::class.java, currentEditor.editor)
      data.put(File::class.java, currentEditor.file)
    }
    return data
  }

  override fun getCurrentEditor(): CodeEditorView? {
    return if (editorViewModel.getCurrentFileIndex() != -1) {
      getEditorAtIndex(editorViewModel.getCurrentFileIndex())
    } else null
  }

  override fun getEditorAtIndex(index: Int): CodeEditorView? {
    return _binding?.content?.editorContainer?.getChildAt(index) as CodeEditorView?
  }

  override fun openFileAndSelect(file: File, selection: Range?) {
    openFile(file, selection)

    getEditorForFile(file)?.editor?.also { editor ->
      editor.postInLifecycle {
        if (selection == null) {
          editor.setSelection(0, 0)
          return@postInLifecycle
        }

        editor.validateRange(selection)
        editor.setSelection(selection)
      }
    }
  }

  override fun openFile(file: File, selection: Range?): CodeEditorView? {
    val range = selection ?: Range.NONE
    if (ImageUtils.isImage(file)) {
      openImage(this, file)
      return null
    }

    val index = openFileAndGetIndex(file, range)
    val tab = content.tabs.getTabAt(index)
    if (tab != null && index >= 0 && !tab.isSelected) {
      tab.select()
    }

    editorViewModel.startDrawerOpened = false
    editorViewModel.displayedFileIndex = index

    return try {
      getEditorAtIndex(index)
    } catch (th: Throwable) {
      log.error("Unable to get editor fragment at opened file index {}", index, th)
      null
    }
  }

  override fun openFileAndGetIndex(file: File, selection: Range?): Int {
    val openedFileIndex = findIndexOfEditorByFile(file)
    if (openedFileIndex != -1) {
      return openedFileIndex
    }

    if (!file.exists()) {
      return -1
    }

    val position = editorViewModel.getOpenedFileCount()

    log.info("Opening file at index {} file:{}", position, file)

    val editor = CodeEditorView(this, file, selection!!)
    editor.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    content.editorContainer.addView(editor)
    content.tabs.addTab(content.tabs.newTab())

    editorViewModel.addFile(file)
    editorViewModel.setCurrentFile(position, file)

    updateTabs()

    return position
  }

  override fun getEditorForFile(file: File): CodeEditorView? {
    for (i in 0 until editorViewModel.getOpenedFileCount()) {
      val editor = content.editorContainer.getChildAt(i) as? CodeEditorView
      if (file == editor?.file) return editor
    }
    return null
  }

  override fun findIndexOfEditorByFile(file: File?): Int {
    if (file == null) {
      log.error("Cannot find index of a null file.")
      return -1
    }

    for (i in 0 until editorViewModel.getOpenedFileCount()) {
      val opened: File = editorViewModel.getOpenedFile(i)
      if (opened == file) {
        return i
      }
    }

    return -1
  }

  override fun saveAllAsync(
    notify: Boolean,
    requestSync: Boolean,
    processResources: Boolean,
    progressConsumer: ((Int, Int) -> Unit)?,
    runAfter: (() -> Unit)?
  ) {
    editorActivityScope.launch {
      saveAll(notify, requestSync, processResources, progressConsumer)
      runAfter?.invoke()
    }
  }

  override suspend fun saveAll(
    notify: Boolean,
    requestSync: Boolean,
    processResources: Boolean,
    progressConsumer: ((Int, Int) -> Unit)?
  ): Boolean {
    val result = saveAllResult(progressConsumer)

    // don't bother to switch the context if we don't need to
    if (notify || (result.gradleSaved && requestSync)) {
      withContext(Dispatchers.Main) {
        if (notify) {
          flashSuccess(string.all_saved)
        }

        if (result.gradleSaved && requestSync) {
          editorViewModel.isSyncNeeded = true
        }
      }
    }

    if (processResources) {
      ProjectManagerImpl.getInstance().generateSources()
    }

    return result.gradleSaved
  }

  override suspend fun saveAllResult(progressConsumer: ((Int, Int) -> Unit)?): SaveResult {
    return performFileSave {
      val result = SaveResult()
      for (i in 0 until editorViewModel.getOpenedFileCount()) {
        saveResultInternal(i, result)
        progressConsumer?.invoke(i + 1, editorViewModel.getOpenedFileCount())
      }

      return@performFileSave result
    }
  }

  override suspend fun saveResult(index: Int, result: SaveResult) {
    performFileSave {
      saveResultInternal(index, result)
    }
  }

  private suspend fun saveResultInternal(
    index: Int,
    result: SaveResult
  ) : Boolean {
    if (index < 0 || index >= editorViewModel.getOpenedFileCount()) {
      return false
    }

    val frag = getEditorAtIndex(index) ?: return false
    val fileName = frag.file?.name ?: return false

    run {
      // Must be called before frag.save()
      // Otherwise, it'll always return false
      val modified = frag.isModified
      if (!frag.save()) {
        return false
      }

      val isGradle = fileName.endsWith(".gradle") || fileName.endsWith(".gradle.kts")
      val isXml: Boolean = fileName.endsWith(".xml")
      if (!result.gradleSaved) {
        result.gradleSaved = modified && isGradle
      }

      if (!result.xmlSaved) {
        result.xmlSaved = modified && isXml
      }
    }

    val hasUnsaved = hasUnsavedFiles()

    withContext(Dispatchers.Main) {

      editorViewModel.areFilesModified = hasUnsaved

      // set tab as unmodified
      val tab = content.tabs.getTabAt(index) ?: return@withContext
      if (tab.text!!.startsWith('*')) {
        tab.text = tab.text!!.substring(startIndex = 1)
      }
    }

    return true
  }

  private fun hasUnsavedFiles() = editorViewModel.getOpenedFiles().any { file ->
    getEditorForFile(file)?.isModified == true
  }

  private suspend inline fun <T : Any?> performFileSave(crossinline action: suspend () -> T) : T {
    setFilesSaving(true)
    try {
      return action()
    } finally {
      setFilesSaving(false)
    }
  }

  private suspend fun setFilesSaving(saving: Boolean) {
    withContext(Dispatchers.Main.immediate) {
      editorViewModel.areFilesSaving = saving
    }
  }

  override fun areFilesModified(): Boolean {
    return editorViewModel.areFilesModified
  }

  override fun areFilesSaving(): Boolean {
    return editorViewModel.areFilesSaving
  }

  override fun closeFile(index: Int, runAfter: () -> Unit) {
    if (index < 0 || index >= editorViewModel.getOpenedFileCount()) {
      log.error("Invalid file index. Cannot close.")
      return
    }

    val opened = editorViewModel.getOpenedFile(index)
    log.info("Closing file: {}", opened)

    val editor = getEditorAtIndex(index)
    if (editor?.isModified == true) {
      log.info("File has been modified: {}", opened)
      notifyFilesUnsaved(listOf(editor)) {
        closeFile(index, runAfter)
      }
      return
    }

    editor?.close() ?: run {
      log.error("Cannot save file before close. Editor instance is null")
    }

    editorViewModel.removeFile(index)
    content.apply {
      tabs.removeTabAt(index)
      editorContainer.removeViewAt(index)
    }

    editorViewModel.areFilesModified = hasUnsavedFiles()

    updateTabs()
    runAfter()
  }

  override fun closeOthers() {
    if (editorViewModel.getOpenedFileCount() == 0) {
      return
    }

    val unsavedFiles =
      editorViewModel.getOpenedFiles().map(::getEditorForFile)
        .filter { it != null && it.isModified }

    if (unsavedFiles.isNotEmpty()) {
      notifyFilesUnsaved(unsavedFiles) { closeOthers() }
      return
    }

    val file = editorViewModel.getCurrentFile()
    var index = 0

    // keep closing the file at index 0
    // if openedFiles[0] == file, then keep closing files at index 1
    while (editorViewModel.getOpenedFileCount() != 1) {
      val editor = getEditorAtIndex(index)

      if (editor == null) {
        log.error("Unable to save file at index {}", index)
        continue
      }

      // Index of files changes as we keep close files
      // So we compare the files instead of index
      if (file != editor.file) {
        closeFile(index)
      } else {
        index = 1
      }
    }
  }

  override fun closeAll(runAfter: () -> Unit) {
    val count = editorViewModel.getOpenedFileCount()
    val unsavedFiles =
      editorViewModel.getOpenedFiles().map(this::getEditorForFile)
        .filter { it != null && it.isModified }

    if (unsavedFiles.isNotEmpty()) {
      // There are unsaved files
      notifyFilesUnsaved(unsavedFiles) { closeAll(runAfter) }
      return
    }

    // Files were already saved, close all files one by one
    for (i in 0 until count) {
      getEditorAtIndex(i)?.close() ?: run {
        log.error("Unable to close file at index {}", i)
      }
    }

    editorViewModel.removeAllFiles()
    content.apply {
      tabs.removeAllTabs()
      tabs.requestLayout()
      editorContainer.removeAllViews()
    }

    runAfter()
  }

  override fun getOpenedFiles() =
    editorViewModel.getOpenedFiles().mapNotNull {
      val editor = getEditorForFile(it)?.editor ?: return@mapNotNull null
      OpenedFile(it.absolutePath, editor.cursorLSPRange)
    }

  private fun notifyFilesUnsaved(unsavedEditors: List<CodeEditorView?>, invokeAfter: Runnable) {
    if (isDestroying) {
      // Do not show unsaved files dialog if the activity is being destroyed
      // TODO Use a service to save files and to avoid file content loss
      for (editor in unsavedEditors) {
        editor?.markUnmodified()
      }
      invokeAfter.run()
      return
    }

    val mapped = unsavedEditors.mapNotNull { it?.file?.absolutePath }
    val builder =
      newYesNoDialog(
        context = this,
        title = getString(string.title_files_unsaved),
        message = getString(string.msg_files_unsaved, TextUtils.join("\n", mapped)),
        positiveClickListener = { dialog, _ ->
          dialog.dismiss()
          saveAllAsync(notify = true, runAfter = { runOnUiThread(invokeAfter) })
        }
      ) { dialog, _ ->
        dialog.dismiss()
        // Mark all the files as saved, then try to close them all
        for (editor in unsavedEditors) {
          editor?.markAsSaved()
        }
        invokeAfter.run()
      }
    builder.show()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onFileRenamed(event: FileRenameEvent) {
    val index = findIndexOfEditorByFile(event.file)
    if (index < 0 || index >= content.tabs.tabCount) {
      return
    }

    val editor = getEditorAtIndex(index) ?: return
    editorViewModel.updateFile(index, event.newFile)
    editor.updateFile(event.newFile)

    updateTabs()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onDocumentChange(event: DocumentChangeEvent) {
    // update content modification status
    editorViewModel.areFilesModified = true

    val index = findIndexOfEditorByFile(event.file.toFile())
    if (index == -1) {
      return
    }

    val tab = content.tabs.getTabAt(index)!!
    if (tab.text?.startsWith('*') == true) {
      return
    }

    // mark as modified
    tab.text = "*${tab.text}"
  }

  private fun updateTabs() {
    editorActivityScope.launch {
      val files = editorViewModel.getOpenedFiles()
      val dupliCount = mutableMapOf<String, Int>()
      val names = MutableIntObjectMap<Pair<String, @DrawableRes Int>>()
      val nameBuilder = UniqueNameBuilder<File>("", File.separator)

      files.forEach {
        var count = dupliCount[it.name] ?: 0
        dupliCount[it.name] = ++count
        nameBuilder.addPath(it, it.path)
      }

      for (index in 0 until content.tabs.tabCount) {
        val file = files.getOrNull(index) ?: continue
        val count = dupliCount[file.name] ?: 0

        val isModified = getEditorAtIndex(index)?.isModified ?: false
        var name = if (count > 1) nameBuilder.getShortPath(file) else file.name
        if (isModified) {
          name = "*${name}"
        }

        names[index] = name to FileExtension.Factory.forFile(file).icon
      }

      withContext(Dispatchers.Main) {
        names.forEach { index, (name, iconId) ->
          val tab = content.tabs.getTabAt(index) ?: return@forEach
          tab.icon = ResourcesCompat.getDrawable(resources, iconId, theme)
          tab.text = name
        }
      }
    }
  }
}
