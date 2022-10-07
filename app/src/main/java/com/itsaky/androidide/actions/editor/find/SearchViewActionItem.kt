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

package com.itsaky.androidide.actions.editor.find

import android.app.SearchManager
import android.content.Context
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.itsaky.androidide.R
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.app.IDEActivity
import com.itsaky.androidide.views.editor.IDEEditor
import io.github.rosemoe.sora.widget.EditorSearcher.SearchOptions

/**
 * The [SearchView] action in the search action mode.
 *
 * @author Akash Yadav
 */
class SearchViewActionItem() : SearchActionModeAction() {
  override val id: String = "editor.search.view"

  constructor(context: Context) : this() {
    label = context.getString(R.string.text_to_search)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_search)
  }

  override fun execAction(data: ActionData): Any {
    return Unit
  }

  override fun createActionView(data: ActionData): View? {
    val editor = getEditor(data) ?: return null
    val searchManager = editor.context.getSystemService(Context.SEARCH_SERVICE) as SearchManager

    val search = SearchView(editor.context)
    search.isIconified = false
    search.queryHint = editor.context.getString(R.string.text_to_search)
    search.setSearchableInfo(
      searchManager.getSearchableInfo((editor.context as IDEActivity).componentName)
    )
    search.setOnQueryTextListener(OnQueryListener(editor))
    search.performClick()
    search.setIconifiedByDefault(false)
    return search
  }

  private class OnQueryListener(val editor: IDEEditor) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
      editor.searcher.gotoNext()
      return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
      if (TextUtils.isEmpty(newText)) {
        editor.searcher.stopSearch()
        return false
      }

      editor.searcher.search(newText!!, SearchOptions(false, false))
      return false
    }
  }
}
