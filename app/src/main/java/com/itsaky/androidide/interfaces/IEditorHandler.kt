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

package com.itsaky.androidide.interfaces

import com.itsaky.androidide.models.Range
import com.itsaky.androidide.models.SaveResult
import com.itsaky.androidide.ui.editor.CodeEditorView
import java.io.File

/**
 * @author Akash Yadav
 */
interface IEditorHandler {
  
  fun findIndexOfEditorByFile(file: File?) : Int
  
  fun getCurrentEditor(): CodeEditorView?
  fun getEditorAtIndex(index: Int) : CodeEditorView?
  fun getEditorForFile(file: File) : CodeEditorView?
  
  fun openFile(file: File) : CodeEditorView? = openFile(file, null)
  fun openFile(file: File, selection: Range?) : CodeEditorView?
  fun openFileAndSelect(file: File, selection: Range?)
  fun openFileAndGetIndex(file: File, selection: Range?) : Int
  
  fun areFilesModified(): Boolean
  
  fun saveAll() : Boolean = saveAll(true)
  fun saveAll(notify: Boolean) : Boolean = saveAll(notify, false)
  fun saveAll(notify: Boolean, processResources: Boolean) : Boolean
  fun saveAllResult() : SaveResult
  fun saveResult(index: Int, result: SaveResult)
  
  fun closeFile(index: Int)
  fun closeAll()
  fun closeOthers()
}