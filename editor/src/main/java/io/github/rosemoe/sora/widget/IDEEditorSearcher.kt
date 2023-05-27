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

package io.github.rosemoe.sora.widget

import com.itsaky.androidide.editor.ui.IDEEditor

/**
 * Search text in editor. As the constructor of [EditorSearcher] is package private, we cannot
 * extend it in another package. So we put this class in the same package.
 *
 * @author Akash Yadav
 */
open class IDEEditorSearcher(editor: IDEEditor) : EditorSearcher(editor) {

  var isSearching = false
    private set

  protected fun getEditor(): CodeEditor {
    try {
      val field = EditorSearcher::class.java.getDeclaredField("editor")
      field.isAccessible = true
      return field.get(this) as CodeEditor
    } catch (error: Throwable) {
      throw RuntimeException("Unable get instance of editor", error)
    }
  }

  fun updateSearchOptions(searchOptions: SearchOptions) {
    this.searchOptions = searchOptions
  }

  override fun replaceAll(replacement: String, whenFinished: Runnable?) {
    markSearching()
    super.replaceAll(replacement, whenFinished)
  }

  override fun replaceThis(replacement: String) {
    markSearching()
    super.replaceThis(replacement)
  }

  override fun gotoNext(): Boolean {
    markSearching()
    return super.gotoNext()
  }

  override fun gotoPrevious(): Boolean {
    markSearching()
    return super.gotoPrevious()
  }

  override fun stopSearch() {
    isSearching = false
    super.stopSearch()
  }

  fun onClose() {
    isSearching = false
  }

  private fun markSearching() {
    isSearching = true
  }
}
