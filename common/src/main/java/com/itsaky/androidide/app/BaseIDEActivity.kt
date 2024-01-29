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

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import androidx.fragment.app.Fragment
import com.itsaky.androidide.common.R
import com.itsaky.androidide.tasks.cancelIfActive
import com.itsaky.androidide.ui.themes.IThemeManager
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.resolveAttr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.greenrobot.eventbus.EventBus

abstract class BaseIDEActivity : AppCompatActivity() {

  open val subscribeToEvents: Boolean = false

  open val navigationBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  open val statusBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  /**
   * [CoroutineScope] for executing tasks with the [Default][Dispatchers.Default] dispatcher.
   */
  val activityScope = CoroutineScope(Dispatchers.Default)

  override fun onCreate(savedInstanceState: Bundle?) {
    window?.apply {
      navigationBarColor = this@BaseIDEActivity.navigationBarColor
      statusBarColor = this@BaseIDEActivity.statusBarColor
    }
    IThemeManager.getInstance().applyTheme(this)
    super.onCreate(savedInstanceState)
    preSetContentLayout()
    setContentView(bindLayout())

    hideSystemBarsIfPossible(resources.configuration)
  }

  override fun onDestroy() {
    super.onDestroy()
    activityScope.cancelIfActive("Activity is being destroyed")
  }

  override fun onStart() {
    super.onStart()
    if (!EventBus.getDefault().isRegistered(this) && subscribeToEvents) {
      EventBus.getDefault().register(this)
    }
  }

  override fun onStop() {
    super.onStop()
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this)
    }
  }

  fun loadFragment(fragment: Fragment, id: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    hideSystemBarsIfPossible(newConfig)
  }

  private fun hideSystemBarsIfPossible(configuration: Configuration) {
    val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView).also {
      it.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    if (configuration.orientation == ORIENTATION_LANDSCAPE) {
      windowInsetsController.hide(systemBars())
    } else windowInsetsController.show(systemBars())
  }

  protected open fun preSetContentLayout() {}

  protected abstract fun bindLayout(): View

  companion object {

    const val REQCODE_STORAGE = 1009
    protected var LOG = ILogger.newInstance("StudioActivity")
  }
}
