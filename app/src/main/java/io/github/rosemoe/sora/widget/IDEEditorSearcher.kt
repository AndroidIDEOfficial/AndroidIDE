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

@file:Suppress("DEPRECATION")

package io.github.rosemoe.sora.widget

import android.app.ProgressDialog
import android.widget.Toast
import com.itsaky.androidide.R.string
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.views.editor.IDEEditor
import io.github.rosemoe.sora.text.TextUtils
import io.github.rosemoe.sora.util.IntPair
import java.util.concurrent.CompletableFuture

/**
 * Search text in editor. As the constructor of [EditorSearcher] is package private, we cannot
 * extend it in another package. So we put this class in the same package.
 *
 * @author Akash Yadav
 */
open class IDEEditorSearcher(editor: IDEEditor) : EditorSearcher(editor) {

  private val log = ILogger.newInstance(javaClass.simpleName)

  var searching = false
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

  fun replaceAllAsync(replacement: String): CompletableFuture<Void> {
    markSearching()
    val dialog =
      ProgressDialog.show(
        getEditor().context,
        getEditor().context.getString(string.replaceAll),
        getEditor().context.getString(string.msg_replacing),
        true,
        false
      )
    return CompletableFuture.runAsync { replaceAll(replacement) }
      .thenAccept { getEditor().post { dialog.dismiss() } }
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

  override fun replaceAll(replacement: String) {
    checkState()
    markSearching()
    if (!isResultValid) {
      Toast.makeText(getEditor().context, "Editor is still preparing", Toast.LENGTH_SHORT).show()
      return
    }

    val res = lastResults
    try {
      val sb = getEditor().text.toStringBuilder()
      val newLength = replacement.length
      if (searchOptions.useRegex) {
        var delta = 0
        for (i in 0 until res.size()) {
          val region = res[i]
          val start = IntPair.getFirst(region)
          val end = IntPair.getSecond(region)
          val oldLength = end - start
          sb.replace(start + delta, end + delta, replacement)
          delta += newLength - oldLength
        }
      } else {
        var fromIndex = 0
        var foundIndex: Int
        while (
          TextUtils.indexOf(sb, currentPattern, searchOptions.ignoreCase, fromIndex).also {
            foundIndex = it
          } != -1
        ) {
          sb.replace(foundIndex, foundIndex + currentPattern.length, replacement)
          fromIndex = foundIndex + newLength
        }
      }
      getEditor().post {
        val pos = getEditor().cursor.left()
        // stopSearch();
        getEditor()
          .text
          .replace(
            0,
            0,
            getEditor().lineCount - 1,
            getEditor().text.getColumnCount(getEditor().lineCount - 1),
            sb
          )
        getEditor().setSelectionAround(pos.line, pos.column)
      }
    } catch (e: Exception) {
      log.error("Failed to replace", e)
    }
  }

  override fun stopSearch() {
    searching = false
    super.stopSearch()
  }

  fun markSearching() {
    searching = true
  }

  private fun checkState() {
    check(hasQuery()) { "pattern not set" }
  }
}
