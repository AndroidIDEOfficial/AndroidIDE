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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.itsaky.androidide.templates.Template
import java.util.concurrent.atomic.AtomicInteger

/**
 * [ViewModel] for main activity.
 *
 * @author Akash Yadav
 */
class MainViewModel : ViewModel() {

  companion object {

    // The values assigned to these variables reflect the order in which the screens are presented
    // to the user. A screen with a lower value is displayed before a screen with a higher value.
    // For example, SCREEN_MAIN is the first screen visible to the user, followed by SCREEN_TEMPLATE_LIST,
    // and then SCREEN_TEMPLATE_DETAILS.
    //
    // These values are used as unique identifiers for the screens as well as for determining whether
    // the screen change transition should be forward or backward.
    const val SCREEN_MAIN = 0
    const val SCREEN_TEMPLATE_LIST = 1
    const val SCREEN_TEMPLATE_DETAILS = 2
  }

  private val _currentScreen = MutableLiveData(-1)
  private val _previousScreen = AtomicInteger(-1)
  private val _isTransitionInProgress = MutableLiveData(false)

  internal val template = MutableLiveData<Template<*>>(null)
  internal val creatingProject = MutableLiveData(false)

  val currentScreen: LiveData<Int> = _currentScreen

  val previousScreen: Int
    get() = _previousScreen.get()

  var isTransitionInProgress: Boolean
    get() = _isTransitionInProgress.value ?: false
    set(value) {
      _isTransitionInProgress.value = value
    }

  fun setScreen(screen: Int) {
    _previousScreen.set(_currentScreen.value ?: SCREEN_MAIN)
    _currentScreen.value = screen
  }

  fun postTransition(owner: LifecycleOwner, action: Runnable) {
    if (isTransitionInProgress) {
      _isTransitionInProgress.observe(owner, object : Observer<Boolean> {
        override fun onChanged(t: Boolean) {
          _isTransitionInProgress.removeObserver(this)
          action.run()
        }
      })
    } else {
      action.run()
    }
  }
}