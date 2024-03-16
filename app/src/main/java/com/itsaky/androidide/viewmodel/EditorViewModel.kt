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
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.FileUtils
import com.google.gson.GsonBuilder
import com.itsaky.androidide.models.OpenedFilesCache
import com.itsaky.androidide.projects.IProjectManager
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.ILogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.Collections

/** ViewModel for data used in [com.itsaky.androidide.activities.editor.EditorActivityKt] */
@Suppress("PropertyName")
class EditorViewModel : ViewModel() {

  internal val _isBuildInProgress = MutableLiveData(false)
  internal val _isInitializing = MutableLiveData(false)
  internal val _statusText = MutableLiveData<Pair<CharSequence, @GravityInt Int>>("" to CENTER)
  internal val _displayedFile = MutableLiveData(-1)
  internal val _startDrawerOpened = MutableLiveData(false)
  internal val _isSyncNeeded = MutableLiveData(false)

  internal val _filesModified = MutableLiveData(false)
  internal val _filesSaving = MutableLiveData(false)

  private val _openedFiles = MutableLiveData<OpenedFilesCache>()
  private val _isBoundToBuildService = MutableLiveData(false)
  private val _files = MutableLiveData<MutableList<File>>(ArrayList())

  /**
   * Holds information about the currently selected editor fragment. First value in the pair is the
   * index of the editor opened. Second value is the file that is opened.
   */
  private val mCurrentFile = MutableLiveData<Pair<Int, File?>?>(null)

  var areFilesModified: Boolean
    get() = _filesModified.value ?: false
    set(value) {
      _filesModified.value = value
    }

  var areFilesSaving: Boolean
    get() = _filesSaving.value ?: false
    set(value) {
      _filesSaving.value = value
    }

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

  var startDrawerOpened: Boolean
    get() = _startDrawerOpened.value ?: false
    set(value) {
      _startDrawerOpened.value = value
    }

  var isSyncNeeded: Boolean
    get() = _isSyncNeeded.value ?: false
    set(value) {
      _isSyncNeeded.value = value
    }

  internal var files: MutableList<File>
    get() = this._files.value ?: Collections.emptyList()
    set(value) {
      this._files.value = value
    }

  private inline fun updateFiles(crossinline action: (files: MutableList<File>) -> Unit) {
    val files = this.files
    action(files)
    this.files = files
  }

  /**
   * Add the given file to the list of opened files.
   *
   * @param file The file that has been opened.
   */
  fun addFile(file: File) = updateFiles { files ->
    files.add(file)
  }

  /**
   * Remove the file at the given index from the list of opened files.
   *
   * @param index The index of the closed file.
   */
  fun removeFile(index: Int) = updateFiles { files ->
    files.removeAt(index)

    if (files.isEmpty()) {
      mCurrentFile.value = null
    }
  }

  fun removeAllFiles() = updateFiles { files ->
    files.clear()
    setCurrentFile(-1, null)
  }

  fun setCurrentFile(index: Int, file: File?) {
    displayedFileIndex = index
    mCurrentFile.value = index to file
  }

  fun updateFile(index: Int, newFile: File) = updateFiles { files ->
    files[index] = newFile
  }

  /**
   * Get the opened file at the given index.
   *
   * @param index The index of the file.
   * @return The file at the given index.
   */
  fun getOpenedFile(index: Int): File {
    return files[index]
  }

  /**
   * Get the number of files opened.
   *
   * @return The number of files opened.
   */
  fun getOpenedFileCount(): Int {
    return files.size
  }

  /**
   * Get the list of currently opened files.
   *
   * @return The list of opened files.
   */
  fun getOpenedFiles(): List<File> {
    return Collections.unmodifiableList(files)
  }

  /**
   * Add an observer to the list of opened files.
   *
   * @param lifecycleOwner The lifecycle owner.
   * @param observer The observer.
   */
  fun observeFiles(lifecycleOwner: LifecycleOwner?, observer: Observer<MutableList<File>?>?) {
    _files.observe(lifecycleOwner!!, observer!!)
  }

  fun getCurrentFileIndex(): Int {
    return mCurrentFile.value?.first ?: -1
  }

  fun getCurrentFile(): File? {
    return mCurrentFile.value?.second
  }

  /**
   * Get the [OpenedFilesCache] if it is already loaded, otherwise read the cache from the file system
   * and invoke the given callback.
   *
   * If the cache is already loaded, [result] is called on the same thread. Otherwise, it is
   * always called on the main/UI thread.
   */
  inline fun getOrReadOpenedFilesCache(crossinline result: (OpenedFilesCache?) -> Unit) {
    return openedFilesCache?.let(result) ?: run {
      viewModelScope.launch(Dispatchers.IO) {
        val cache = try {
          val cacheFile = getOpenedFilesCache(false)
          if (cacheFile.exists() && cacheFile.length() > 0L) {
            cacheFile.bufferedReader().use(OpenedFilesCache::parse)
          } else null
        } catch (err: IOException) {
          // ignore exception
          null
        }

        withContext(Dispatchers.Main) {
          result(cache)
        }
      }.also { job ->
        handleOpenedFilesCacheJobCompletion(job, "read")
      }
      Unit
    }
  }

  fun handleOpenedFilesCacheJobCompletion(it: Job, operation: String) {
    it.invokeOnCompletion { err ->
      if (err != null) {
        ILogger.ROOT.error("[EditorViewModel] Failed to {} opened files cache", operation, err)
      }
    }
  }

  fun writeOpenedFiles(cache: OpenedFilesCache?) {
    viewModelScope.launch(Dispatchers.IO) {
      val file = getOpenedFilesCache(true)

      if (cache == null) {
        file.delete()
        return@launch
      }

      val gson = GsonBuilder().setPrettyPrinting().create()
      val string = gson.toJson(cache)
      file.createNewFile()
      file.writeText(string)
    }.also { job ->
      handleOpenedFilesCacheJobCompletion(job, "write")
    }
  }

  @PublishedApi
  internal fun getOpenedFilesCache(forWrite: Boolean = false): File {
    var file = Environment.getProjectCacheDir(IProjectManager.getInstance().projectDir)
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
