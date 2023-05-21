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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    // main screens
    const val SCREEN_MAIN = 0
    const val SCREEN_TEMPLATE_LIST = 1
    const val SCREEN_TEMPLATE_DETAILS = 2
  }

  private val _currentScreen = MutableLiveData(-1)
  private val _previousScreen = AtomicInteger(-1)

  internal val template = MutableLiveData<Template>(null)

  val currentScreen: LiveData<Int> = _currentScreen
  val previousScreen: Int
    get() = _previousScreen.get()

  fun setScreen(screen: Int) {
    _previousScreen.set(_currentScreen.value ?: SCREEN_MAIN)
    _currentScreen.value = screen
  }
}