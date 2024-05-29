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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.models.Checkable
import com.itsaky.androidide.tooling.api.models.GradleTask

/** @author Akash Yadav */
class RunTasksViewModel : ViewModel() {
  
  private val _tasks = MutableLiveData(listOf<Checkable<GradleTask>>())
  private val _selected = MutableLiveData(mutableSetOf<String>())
  private val _displayedChild = MutableLiveData(0)
  private val _query = MutableLiveData("")

  var tasks: List<Checkable<GradleTask>>
    get() = _tasks.value!!
    set(value) {
      _tasks.value = value
    }

  var displayedChild: Int
    get() = this._displayedChild.value!!
    set(value) {
      this._displayedChild.value = value
    }

  var query: String
    get() = _query.value!!
    set(value) {
      _query.value = value
    }

  val selected: Set<String>
    get() = _selected.value!!

  fun observeDisplayedChild(owner: LifecycleOwner, observer: Observer<Int>) {
    _displayedChild.observe(owner, observer)
  }

  fun observeQuery(owner: LifecycleOwner, observer: Observer<String>) {
    _query.observe(owner, observer)
  }

  fun select(item: String) {
    this._selected.value!!.add(item)
  }

  fun deselect(item: String) {
    this._selected.value!!.remove(item)
  }

  fun getSelectedTaskPaths(): String {
    return selected.joinToString(separator = "\n")
  }
}
