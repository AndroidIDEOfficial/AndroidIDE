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
package com.itsaky.androidide.app

import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.common.R
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.resolveAttr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseIDEActivity : AppCompatActivity() {

  open val navigationBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  open val statusBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  /**
   * [CoroutineScope] for executing tasks with the [Default][Dispatchers.Default] dispatcher.
   */
  protected val activityScope = CoroutineScope(Dispatchers.Default)

  override fun onCreate(savedInstanceState: Bundle?) {
    window?.apply {
      navigationBarColor = this@BaseIDEActivity.navigationBarColor
      statusBarColor = this@BaseIDEActivity.statusBarColor
    }
    super.onCreate(savedInstanceState)
    preSetContentLayout()
    setContentView(bindLayout())
  }

  override fun onDestroy() {
    super.onDestroy()
    activityScope.cancelIfActive("Activity is being destroyed")
  }

  fun loadFragment(fragment: Fragment, id: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
  }

  protected open fun preSetContentLayout() {}

  protected abstract fun bindLayout(): View

  companion object {

    const val REQCODE_STORAGE = 1009
    protected var LOG = ILogger.newInstance("StudioActivity")
  }
}
