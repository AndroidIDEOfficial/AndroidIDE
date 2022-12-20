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

import android.content.Intent
import android.net.Uri
import android.widget.ListView
import com.itsaky.androidide.lsp.util.DocumentationReferenceProvider
import com.itsaky.androidide.progress.ProgressManager
import com.itsaky.androidide.utils.ILogger
import io.github.rosemoe.sora.lang.completion.CompletionItem
import io.github.rosemoe.sora.lang.completion.CompletionPublisher
import io.github.rosemoe.sora.widget.component.CompletionLayout
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.component.EditorCompletionAdapter
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.min

/**
 * Completion window for the editor.
 *
 * @author Akash Yadav
 */
class EditorCompletionWindow(val editor: IDEEditor) : EditorAutoCompletion(editor) {
  private var mListView: ListView? = null
  private var mAdapter: EditorCompletionAdapter? = null
  private val mItems: MutableList<CompletionItem> = mutableListOf()
  private val log = ILogger.newInstance(javaClass.simpleName)
  
  init {
    mAdapter = super.adapter
  }
  
  override fun setLayout(layout: CompletionLayout) {
    super.setLayout(layout)
    (layout.completionList as? ListView)?.let {
      mListView = it
      it.adapter = mAdapter
      it.setOnItemLongClickListener { _, view, position, _ ->
        val data =
          (mItems[position] as? com.itsaky.androidide.lsp.models.CompletionItem)?.data
            ?: return@setOnItemLongClickListener false
        val url =
          DocumentationReferenceProvider.getUrl(data) ?: return@setOnItemLongClickListener false
        Intent().apply {
          action = Intent.ACTION_VIEW
          setData(Uri.parse(url))
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          view.context.startActivity(this)
        }
        true
      }
    }
  }
  
  override fun setAdapter(adapter: EditorCompletionAdapter) {
    super.setAdapter(adapter)
    mAdapter = adapter
    mAdapter!!.attachValues(this, mItems)
    mAdapter!!.notifyDataSetInvalidated()
    mListView!!.adapter = adapter
  }
  
  override fun select(pos: Int): Boolean {
    if (pos > mAdapter!!.count) {
      return false
    }
    return try {
      super.select(pos)
    } catch (e: Throwable) {
      log.warn("Unable to select completion item at $pos", e)
      false
    }
  }
  
  override fun select(): Boolean {
    return try {
      super.select()
    } catch (e: Throwable) {
      log.warn("Unable to select completion item", e)
      false
    }
  }
  
  override fun cancelCompletion() {
    if (completionThread != null) {
      ProgressManager.instance.cancel(completionThread)
    }
    super.cancelCompletion()
    popup.dismiss()
  }
  
  override fun requireCompletion() {
    if (cancelShowUp || !isEnabled) {
      return
    }
    val text = editor.text
    if (text.cursor.isSelected) {
      hide()
      return
    }
    if (System.nanoTime() - requestTime < editor.props.cancelCompletionNs) {
      hide()
      requestTime = System.nanoTime()
      return
    }
    cancelCompletion()
    requestTime = System.nanoTime()
    super.currentSelection = -1
    val reference = AtomicReference<List<CompletionItem>>()
    val publisher =
      CompletionPublisher(
        editor.handler,
        {
          val newItems = reference.get()
          mItems.clear()
          mItems.addAll(newItems)
          mAdapter!!.notifyDataSetChanged()
          val newHeight = (mAdapter!!.itemHeight * mAdapter!!.count).toFloat()
          setSize(width, min(newHeight, maxHeight.toFloat()).toInt())
          if (!popup.isShowing) {
            dismiss()
            show()
          }
          if (mAdapter!!.count >= 1) {
            super.currentSelection = 0
          }
        },
        editor.editorLanguage.interruptionLevel
      )
    publisher.setUpdateThreshold(1)
    reference.set(publisher.items)
    
    completionThread = CompletionThread(requestTime, publisher)
    completionThread.name = "CompletionThread-$requestTime"
    
    setLoading(true)
    
    completionThread.start()
  }
  
}
