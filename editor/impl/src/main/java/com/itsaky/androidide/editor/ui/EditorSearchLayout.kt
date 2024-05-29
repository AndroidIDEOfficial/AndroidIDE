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

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.PopupMenu
import com.itsaky.androidide.editor.databinding.LayoutFindInFileBinding
import com.itsaky.androidide.editor.ui.ReplaceAction.doReplace
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.utils.SingleTextWatcher
import io.github.rosemoe.sora.widget.EditorSearcher.SearchOptions
import java.util.regex.Pattern

/**
 * The search layout in [IDEEditor].
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor") // Always created dynamically
class EditorSearchLayout(context: Context, val editor: IDEEditor) : FrameLayout(context) {

  private var searchInputTextWatcher: TextWatcher? = null
  private var searchOptions = SearchOptions(true, false)
  private val findInFileBinding: LayoutFindInFileBinding
  private val optionsMenu: PopupMenu

  private var isSearching = false

  init {
    findInFileBinding = LayoutFindInFileBinding.inflate(LayoutInflater.from(context))
    findInFileBinding.prev.setOnClickListener(::onSearchActionClick)
    findInFileBinding.next.setOnClickListener(::onSearchActionClick)
    findInFileBinding.replace.setOnClickListener(::onSearchActionClick)
    findInFileBinding.close.setOnClickListener(::onSearchActionClick)

    optionsMenu = PopupMenu(context, findInFileBinding.moreOptions, Gravity.TOP)
    optionsMenu.menu.add(0, 0, 0, R.string.msg_ignore_case).apply {
      isCheckable = true
      isChecked = true
    }

    optionsMenu.menu.add(0, 1, 0, R.string.msg_use_regex).apply {
      isCheckable = true
      isChecked = false
    }

    optionsMenu.setOnMenuItemClickListener {
      return@setOnMenuItemClickListener if (it.isCheckable) {
        it.isChecked = !it.isChecked

        val caseInsensitive = searchOptions.caseInsensitive
        val regex = searchOptions.type == SearchOptions.TYPE_REGULAR_EXPRESSION
        searchOptions =
          when (it.itemId) {
            0 -> SearchOptions(it.isChecked, regex)
            1 -> SearchOptions(caseInsensitive, it.isChecked)
            else -> searchOptions
          }
        editor.searcher.updateSearchOptions(searchOptions)

        true
      } else false
    }

    findInFileBinding.root.visibility = GONE
    findInFileBinding.moreOptions.setOnClickListener { optionsMenu.show() }

    addView(
      findInFileBinding.root,
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    )
  }

  fun beginSearchMode() {
    searchInputTextWatcher = SearchInputTextChangeListener(editor)
    findInFileBinding.searchInput.addTextChangedListener(searchInputTextWatcher)
    findInFileBinding.searchInput.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_NEXT) {
        onSearchActionClick(findInFileBinding.next)
      }
      false
    }
    findInFileBinding.root.visibility = VISIBLE
  }

  private fun onSearchActionClick(v: View) {
    val searcher = editor.searcher
    if (v.id == findInFileBinding.close.id) {
      if (this.searchInputTextWatcher == null) {
        return
      }
      findInFileBinding.searchInput.removeTextChangedListener(this.searchInputTextWatcher)
      findInFileBinding.root.visibility = GONE
      this.searchInputTextWatcher = null
      searcher.onClose()
    }
    if (!searcher.hasQuery()) {
      return
    }
    if (v.id == findInFileBinding.prev.id) {
      searcher.gotoPrevious()
      return
    }
    if (v.id == findInFileBinding.next.id) {
      searcher.gotoNext()
      return
    }
    if (v.id == findInFileBinding.replace.id) {
      doReplace(editor)
    }
  }

  inner class SearchInputTextChangeListener(val editor: IDEEditor?) : SingleTextWatcher() {

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int,
    ) {
      if (editor == null) {
        return
      }
      if (TextUtils.isEmpty(s)) {
        editor.searcher.stopSearch()
        return
      }

      // Handle bad regexp
      val query =
        s.toString().let {
          if (searchOptions.type == SearchOptions.TYPE_REGULAR_EXPRESSION) {
            try {
              Pattern.compile(it)
              it
            } catch (error: Throwable) {
              ""
            }
          } else {
            it
          }
        }

      if (query.isNotBlank()) {
        editor.searcher.search(query, searchOptions)
      }
    }
  }
}
