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
package com.itsaky.androidide.viewmodel

import android.view.Gravity.CENTER
import androidx.annotation.GravityInt
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.blankj.utilcode.util.FileUtils
import com.google.gson.GsonBuilder
import com.itsaky.androidide.models.OpenedFilesCache
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.Environment
import java.io.File

/** ViewModel for data used in [com.itsaky.androidide.activities.editor.EditorActivityKt] */
@Suppress("PropertyName")
class EditorViewModel : ViewModel() {

  internal val _isBuildInProgress = MutableLiveData(false)
  internal val _isInitializing = MutableLiveData(false)
  internal val _statusText = MutableLiveData<Pair<CharSequence, @GravityInt Int>>("" to CENTER)
  internal val _displayedFile = MutableLiveData(-1)
  internal val _fileTreeDrawerOpened = MutableLiveData(false)

  private val _openedFiles = MutableLiveData<OpenedFilesCache>()
  private val _isBoundToBuildService = MutableLiveData(false)
  private val files = MutableLiveData<MutableList<File>>(ArrayList())

  private val fileModified = MutableLiveData(false)

  /**
   * Holds information about the currently selected editor fragment. First value in the pair is the
   * index of the editor opened. Second value is the file that is opened.
   */
  private val mCurrentFile = MutableLiveData<Pair<Int, File?>?>(null)

  var openedFilesCache: OpenedFilesCache?
    get() = _openedFiles.value
    set(value) {
      this._openedFiles.value = value
    }

  var isBoundToBuildSerice: Boolean
    get() = _isBoundToBuildService.value ?: false
    set(value) {
      _isBoundToBuildService.value = value
    }

  var isBuildInProgress: Boolean
    get() = _isBuildInProgress.value ?: false
    set(value) {
      _isBuildInProgress.value = value
    }

  var isInitializing: Boolean
    get() = _isInitializing.value ?: false
    set(value) {
      _isInitializing.value = value
    }

  var statusText: CharSequence
    get() = this._statusText.value?.first ?: ""
    set(value) {
      _statusText.value = value to (_statusText.value?.second ?: 0)
    }

  var statusGravity: Int
    get() = this._statusText.value?.second ?: CENTER
    set(value) {
      _statusText.value = (_statusText.value?.first ?: "") to value
    }

  var displayedFileIndex: Int
    get() = _displayedFile.value!!
    set(value) {
      _displayedFile.value = value
    }

  var fileTreeDrawerOpened: Boolean
    get() = _fileTreeDrawerOpened.value ?: false
    set(value) {
      _fileTreeDrawerOpened.value = value
    }

  /**
   * Add the given file to the list of opened files.
   *
   * @param file The file that has been opened.
   */
  fun addFile(file: File) {
    val files = files.value ?: mutableListOf()
    files.add(file)
    this.files.value = files
  }

  /**
   * Remove the file at the given index from the list of opened files.
   *
   * @param index The index of the closed file.
   */
  fun removeFile(index: Int) {
    val files = files.value ?: mutableListOf()
    files.removeAt(index)
    this.files.value = files

    if (this.files.value?.isEmpty() == true) {
      mCurrentFile.value = null
    }
  }

  fun removeAllFiles() {
    files.value = mutableListOf()
    setCurrentFile(-1, null)
  }

  fun setCurrentFile(index: Int, file: File?) {
    displayedFileIndex = index
    mCurrentFile.value = index to file
  }

  fun updateFile(index: Int, newFile: File) {
    val files = files.value ?: return
    files[index] = newFile
  }

  /**
   * Get the opened file at the given index.
   *
   * @param index The index of the file.
   * @return The file at the given index.
   */
  fun getOpenedFile(index: Int): File {
    return files.value!![index]
  }

  /**
   * Get the number of files opened.
   *
   * @return The number of files opened.
   */
  fun getOpenedFileCount(): Int {
    return files.value!!.size
  }

  /**
   * Get the list of currently opened files.
   *
   * @return The list of opened files.
   */
  fun getOpenedFiles(): List<File> {
    return files.value ?: mutableListOf()
  }

  /**
   * Add an observer to the list of opened files.
   *
   * @param lifecycleOwner The lifecycle owner.
   * @param observer The observer.
   */
  fun observeFiles(lifecycleOwner: LifecycleOwner?, observer: Observer<MutableList<File>?>?) {
    files.observe(lifecycleOwner!!, observer!!)
  }

  fun getCurrentFileIndex(): Int {
    return mCurrentFile.value?.first ?: -1
  }

  fun getCurrentFile(): File? {
    return mCurrentFile.value?.second
  }

  fun setFilesModified(modified: Boolean) {
    fileModified.value = modified
  }

  fun areFilesModified(): Boolean {
    val modified = fileModified.value
    return modified != null && modified
  }

  fun readOpenedFiles(result: (OpenedFilesCache?) -> Unit) {
    executeAsync({
      val file = getOpenedFilesCache(false)
      if (file.length() == 0L) {
        return@executeAsync null
      }
      return@executeAsync file.reader().buffered().use { reader ->
        OpenedFilesCache.parse(reader)
      }
    }) {
      result(it)
    }
  }

  fun writeOpenedFiles(cache: OpenedFilesCache?) {
    executeAsync {
      val file = getOpenedFilesCache()

      if (cache == null) {
        file.delete()
        return@executeAsync
      }

      val gson = GsonBuilder().setPrettyPrinting().create()
      val string = gson.toJson(cache)
      file.writeText(string)
    }
  }

  private fun getOpenedFilesCache(forWrite: Boolean = false): File {
    var file = Environment.getProjectCacheDir(ProjectManager.projectPath)
    file = File(file, "editor/openedFiles.json")
    if (file.exists() && forWrite) {
      FileUtils.rename(file, "${file.name}.bak")
    }

    if (file.parentFile?.exists() == false) {
      file.parentFile?.mkdirs()
    }

    file.createNewFile()

    return file
  }
}
