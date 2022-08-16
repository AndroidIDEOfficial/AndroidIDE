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

package com.itsaky.androidide.views.editor

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.SearchView
import com.itsaky.androidide.actions.ActionData
import com.itsaky.androidide.actions.ActionItem
import com.itsaky.androidide.actions.ActionsRegistry
import io.github.rosemoe.sora.R
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.EditorSearcher.SearchOptions

/**
 * Action mode for find and replace functions in editor.
 * @author Akash Yadav
 */
class SearchActionMode(val editor: IDEEditor) : ActionMode.Callback {
  
  @SuppressLint("RestrictedApi")
  override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

    if (mode == null) {
      return false
    }

    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }

    val search = SearchView(editor.context)
    search.setOnQueryTextListener(OnQueryListener(editor))
    search.isIconified = false
    search.queryHint = editor.context.getString(R.string.text_to_search)
    search.performClick()
    search.setIconifiedByDefault(false)
    mode.customView = search
    return true
  }

  override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    if (mode == null || menu == null) {
      return false
    }

    menu.clear()
    val registry = ActionsRegistry.getInstance()
    registry.fillMenu(createData(mode), ActionItem.Location.EDITOR_SEARCH_ACTION_MODE, menu)

    return true
  }

  override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    return true
  }

  override fun onDestroyActionMode(mode: ActionMode?) {
    editor.searcher.stopSearch()
  }

  private fun createData(mode: ActionMode): ActionData {
    return ActionData().apply {
      put(Context::class.java, editor.context)
      put(IDEEditor::class.java, editor)
      put(CodeEditor::class.java, editor)
      put(ActionMode::class.java, mode)
    }
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
