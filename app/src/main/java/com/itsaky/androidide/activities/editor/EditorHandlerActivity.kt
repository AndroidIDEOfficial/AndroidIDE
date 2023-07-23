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
import android.view.ViewGroup.LayoutParams
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.view.GravityCompat
import com.blankj.utilcode.util.ImageUtils
import com.blankj.utilcode.util.ThreadUtils
import com.itsaky.androidide.R.string
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem.Location.EDITOR_TOOLBAR
import com.itsaky.androidide.actions.ActionsRegistry.Companion.getInstance
import com.itsaky.androidide.editor.language.java.JavaLanguage
import com.itsaky.androidide.editor.language.json.JsonLanguage
import com.itsaky.androidide.editor.language.kotlin.KotlinLanguage
import com.itsaky.androidide.editor.language.log.LogLanguage
import com.itsaky.androidide.editor.language.treesitter.TSLanguageRegistry
import com.itsaky.androidide.editor.language.xml.XMLLanguage
import com.itsaky.androidide.editor.schemes.IDEColorSchemeProvider
import com.itsaky.androidide.editor.ui.IDEEditor
import com.itsaky.androidide.eventbus.events.editor.DocumentChangeEvent
import com.itsaky.androidide.eventbus.events.file.FileRenameEvent
import com.itsaky.androidide.interfaces.IEditorHandler
import com.itsaky.androidide.models.OpenedFile
import com.itsaky.androidide.models.OpenedFilesCache
import com.itsaky.androidide.models.Range
import com.itsaky.androidide.models.SaveResult
import com.itsaky.androidide.projects.ProjectManager.generateSources
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.tasks.executeAsyncProvideError
import com.itsaky.androidide.ui.editor.CodeEditorView
import com.itsaky.androidide.utils.DialogUtils.newYesNoDialog
import com.itsaky.androidide.utils.IntentUtils.openImage
import com.itsaky.androidide.utils.UniqueNameBuilder
import com.itsaky.androidide.utils.flashSuccess
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.component1
import kotlin.collections.component2
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

  override fun doSaveAll(): Boolean {
    return saveAll()
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
    viewModel.removeAllFiles()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    mBuildEventListener.setActivity(this)
    super.onCreate(savedInstanceState)

    viewModel._displayedFile.observe(this) { this.binding.editorContainer.displayedChild = it }
    viewModel._fileTreeDrawerOpened.observe(this) { opened ->
      this.binding.editorDrawerLayout.apply {
        if (opened) openDrawer(GravityCompat.END) else closeDrawer(GravityCompat.END)
      }
    }

    viewModel.observeFiles(this) {
      // rewrite the cached files index if there are any opened files
      val currentFile =
        getCurrentEditor()?.editor?.file?.absolutePath
          ?: run {
            viewModel.writeOpenedFiles(null)
            viewModel.openedFilesCache = null
            return@observeFiles
          }
      getOpenedFiles().also {
        val cache = OpenedFilesCache(currentFile, it)
        viewModel.writeOpenedFiles(cache)
        viewModel.openedFilesCache = cache
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

  @SuppressLint("RestrictedApi")
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
    return true
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
      viewModel.writeOpenedFiles(null)
      viewModel.openedFilesCache = null
      log.debug("[onPause]", "No opened files.", "Opened files cache reset to null.")
      isOpenedFilesSaved.set(true)
      return
    }

    val cache = OpenedFilesCache(selectedFile = selectedFile.absolutePath, allFiles = openedFiles)

    viewModel.writeOpenedFiles(cache)
    viewModel.openedFilesCache = if (!isDestroying) cache else null
    log.debug("[onPause]", "Opened files cache reset to ${viewModel.openedFilesCache}")
    isOpenedFilesSaved.set(true)
  }

  override fun onStart() {
    super.onStart()

    try {
      if (viewModel.openedFilesCache != null) {
        viewModel.openedFilesCache?.let { cache ->
          onReadOpenedFilesCache(cache)
        }
      } else {
        viewModel.readOpenedFiles { cache ->
          cache ?: return@readOpenedFiles
          onReadOpenedFilesCache(cache)
        }
      }
      viewModel.openedFilesCache = null
    } catch (err: Throwable) {
      log.error("Failed to reopen recently opened files", err)
    }
  }

  private fun onReadOpenedFilesCache(cache: OpenedFilesCache) {
    cache.allFiles.forEach { file ->
      openFile(File(file.filePath), file.selection)
    }
    openFile(File(cache.selectedFile))
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    ensureToolbarMenu(menu)
    return true
  }

  override fun getCurrentEditor(): CodeEditorView? {
    return if (viewModel.getCurrentFileIndex() != -1) {
      getEditorAtIndex(viewModel.getCurrentFileIndex())
    } else null
  }

  override fun getEditorAtIndex(index: Int): CodeEditorView? {
    return binding.editorContainer.getChildAt(index) as CodeEditorView?
  }

  override fun openFileAndSelect(file: File, selection: Range?) {
    openFile(file, selection)
    val opened = getEditorForFile(file)
    if (opened?.editor != null) {
      val editor = opened.editor
      editor.post {
        if (selection == null) {
          editor.setSelection(0, 0)
          return@post
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
    val tab = binding.tabs.getTabAt(index)
    if (tab != null && index >= 0 && !tab.isSelected) {
      tab.select()
    }

    viewModel.fileTreeDrawerOpened = false
    viewModel.displayedFileIndex = index

    return try {
      getEditorAtIndex(index)
    } catch (th: Throwable) {
      log.error("Unable to get editor fragment at opened file index", index)
      log.error(th)
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

    val position = viewModel.getOpenedFileCount()

    log.info("Opening file at index:", position, "file: ", file)

    val editor = CodeEditorView(this, file, selection!!)
    editor.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    binding.editorContainer.addView(editor)
    binding.tabs.addTab(binding.tabs.newTab())

    viewModel.addFile(file)
    viewModel.setCurrentFile(position, file)

    updateTabs()

    return position
  }

  override fun getEditorForFile(file: File): CodeEditorView? {
    for (i in 0 until viewModel.getOpenedFileCount()) {
      val editor = binding.editorContainer.getChildAt(i) as CodeEditorView
      if (file == editor.file) {
        return editor
      }
    }
    return null
  }

  override fun findIndexOfEditorByFile(file: File?): Int {
    if (file == null) {
      log.error("Cannot find index of a null file.")
      return -1
    }

    for (i in 0 until viewModel.getOpenedFileCount()) {
      val opened: File = viewModel.getOpenedFile(i)
      if (opened == file) {
        return i
      }
    }

    return -1
  }

  override fun saveAll(notify: Boolean, processResources: Boolean): Boolean {
    val result = saveAllResult()

    if (notify) {
      flashSuccess(string.all_saved)
    }

    if (result.gradleSaved) {
      notifySyncNeeded()
    }

    if (processResources) {
      generateSources()
    }

    return result.gradleSaved
  }

  override fun saveAllResult(): SaveResult {
    val result = SaveResult()
    for (i in 0 until viewModel.getOpenedFileCount()) {
      saveResult(i, result)
    }
    return result
  }

  override fun saveResult(index: Int, result: SaveResult) {
    if (index < 0 || index >= viewModel.getOpenedFileCount()) {
      return
    }

    val frag = getEditorAtIndex(index)
    if (frag?.file == null) {
      return
    }

    run {
      // Must be called before frag.save()
      // Otherwise, it'll always return false
      val modified = frag.isModified
      frag.save()

      val fileName = frag.file!!.name
      val isGradle = fileName.endsWith(".gradle") || fileName.endsWith(".gradle.kts")
      val isXml: Boolean = fileName.endsWith(".xml")
      if (!result.gradleSaved) {
        result.gradleSaved = modified && isGradle
      }

      if (!result.xmlSaved) {
        result.xmlSaved = modified && isXml
      }
    }

    var modified = false
    for (file in viewModel.getOpenedFiles()) {
      val editor = getEditorForFile(file) ?: continue
      modified = modified || editor.isModified
    }

    val finalModified = modified
    ThreadUtils.runOnUiThread {
      viewModel.setFilesModified(finalModified)

      // set tab as unmodified
      val tab = binding.tabs.getTabAt(index) ?: return@runOnUiThread
      if (tab.text!!.startsWith('*')) {
        tab.text = tab.text!!.substring(startIndex = 1)
      }
    }
  }

  private fun onEditorContentChanged() {
    viewModel.setFilesModified(true)
    invalidateOptionsMenu()
  }

  override fun areFilesModified(): Boolean {
    return viewModel.areFilesModified()
  }

  override fun closeFile(index: Int) {
    if (index >= 0 && index < viewModel.getOpenedFileCount()) {
      val opened: File = viewModel.getOpenedFile(index)
      log.info("Closing file:", opened)
      val editor = getEditorAtIndex(index)
      if (editor != null && editor.isModified) {
        notifyFilesUnsaved(listOf(editor)) { closeFile(index) }
        return
      }

      if (editor?.editor != null) {
        editor.editor.apply {
          notifyClose()
          release()
        }
      } else {
        log.error("Cannot save file before close. Editor instance is null")
      }

      viewModel.removeFile(index)
      binding.apply {
        tabs.removeTabAt(index)
        editorContainer.removeViewAt(index)
      }

      updateTabs()
    } else {
      log.error("Invalid file index. Cannot close.")
      return
    }
  }

  override fun closeAll() {
    closeAll {}
  }

  override fun closeOthers() {
    val unsavedFiles =
      viewModel.getOpenedFiles().map(::getEditorForFile).filter { it != null && it.isModified }

    if (unsavedFiles.isEmpty()) {
      val file = viewModel.getCurrentFile()
      var index = 0

      // keep closing the file at index 0
      // if openedFiles[0] == file, then keep closing files at index 1
      while (viewModel.getOpenedFileCount() != 1) {
        val editor = getEditorAtIndex(index)

        // Index of files changes as we keep close files
        // So we compare the files instead of index
        if (editor != null) {
          if (file != editor.file) {
            closeFile(index)
          } else {
            index = 1
          }
        } else {
          log.error("Unable to save file at index:", index)
        }
      }
    } else {
      notifyFilesUnsaved(unsavedFiles) { closeOthers() }
    }
  }

  open fun ensureToolbarMenu(menu: Menu) {
    menu.clear()

    val data = ActionData()
    val currentEditor = getCurrentEditor()

    data.put(Context::class.java, this)
    data.put(CodeEditorView::class.java, currentEditor)

    if (currentEditor != null) {
      data.put(IDEEditor::class.java, currentEditor.editor)
      data.put(File::class.java, currentEditor.editor.file)
    }

    getInstance().fillMenu(data, EDITOR_TOOLBAR, menu)
    binding.editorToolbar.updateMenuDisplay()
  }

  private fun closeAll(runAfter: () -> Unit = {}) {
    val count = viewModel.getOpenedFileCount()
    val unsavedFiles =
      viewModel.getOpenedFiles().map(this::getEditorForFile).filter { it != null && it.isModified }

    if (unsavedFiles.isNotEmpty()) {
      // There are unsaved files
      notifyFilesUnsaved(unsavedFiles) { closeAll(runAfter) }
      return
    }

    // Files were already saved, close all files one by one
    for (i in 0 until count) {
      val editor = getEditorAtIndex(i)
      if (editor?.editor != null) {
        editor.editor.notifyClose()
        editor.editor.release()
      } else {
        log.error("Unable to close file at index:", i)
      }
    }

    viewModel.removeAllFiles()
    binding.apply {
      tabs.removeAllTabs()
      tabs.requestLayout()
      editorContainer.removeAllViews()
    }

    runAfter()
  }

  override fun getOpenedFiles() =
    viewModel.getOpenedFiles().mapNotNull {
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
          saveAll(true)
          invokeAfter.run()
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
    if (index < 0 || index >= binding.tabs.tabCount) {
      return
    }

    val editor = getEditorAtIndex(index) ?: return
    viewModel.updateFile(index, event.newFile)
    editor.updateFile(event.newFile)

    updateTabs()
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  fun onDocumentChange(event: DocumentChangeEvent) {
    // update content modification status
    onEditorContentChanged()

    val index = findIndexOfEditorByFile(event.file.toFile())
    if (index == -1) {
      return
    }

    val tab = binding.tabs.getTabAt(index)!!
    if (tab.text?.startsWith('*') == true) {
      return
    }

    // mark as modified
    tab.text = "*${tab.text}"
  }

  private fun updateTabs() {
    executeAsyncProvideError({
      val files = viewModel.getOpenedFiles()
      val dupliCount = mutableMapOf<String, Int>()
      val names = mutableMapOf<Int, String>()
      val nameBuilder = UniqueNameBuilder<File>("", File.separator)

      files.forEach {
        var count = dupliCount[it.name] ?: 0
        dupliCount[it.name] = ++count
        nameBuilder.addPath(it, it.path)
      }

      for (i in 0 until binding.tabs.tabCount) {
        val file = files[i]
        val count = dupliCount[file.name] ?: 0
        val isModified = getEditorAtIndex(i)?.isModified ?: false
        var name = if (count > 1) nameBuilder.getShortPath(file) else file.name
        if (isModified) {
          name = "*${name}"
        }
        names[i] = name
      }

      names
    }) { result, error ->
      if (result == null || error != null) {
        log.error("Failed to compute names for file tabs", error)
        return@executeAsyncProvideError
      }

      ThreadUtils.runOnUiThread {
        result.forEach { (index, name) -> binding.tabs.getTabAt(index)?.text = name }
      }
    }
  }
}
