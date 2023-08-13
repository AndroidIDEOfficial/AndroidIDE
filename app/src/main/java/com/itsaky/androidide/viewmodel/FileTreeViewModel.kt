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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.tasks.runOnUiThread
import com.unnamed.b.atv.view.AndroidTreeView

/**
 * [ViewModel] for the file tree fragment.
 *
 * @author Akash Yadav
 */
internal class FileTreeViewModel : ViewModel() {

  val treeState = MutableLiveData<String>(null)

  val savedState: String
    get() = treeState.value ?: ""

  fun saveState(treeView: AndroidTreeView?) {
    treeView?.let { tree ->
      executeAsync({
        // if a large number of directories have been expanded in the tree
        // this could block teh UI thread
        return@executeAsync tree.saveState
      }) { result ->
        runOnUiThread {
          treeState.value = result
        }
      }
    }
  }
}