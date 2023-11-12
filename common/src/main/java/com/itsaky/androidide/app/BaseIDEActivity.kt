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
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.common.R
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent
import com.itsaky.androidide.ui.themes.IThemeManager
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.resolveAttr
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Locale

abstract class BaseIDEActivity : AppCompatActivity() {

  open val navigationBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  open val statusBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  val isStoragePermissionGranted: Boolean
    get() =
      (ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED)

  override fun onCreate(savedInstanceState: Bundle?) {
    window?.apply {
      navigationBarColor = this@BaseIDEActivity.navigationBarColor
      statusBarColor = this@BaseIDEActivity.statusBarColor
    }
    IThemeManager.getInstance().applyTheme(this)
    super.onCreate(savedInstanceState)
    preSetContentLayout()
    setContentView(bindLayout())
    if (!isStoragePermissionGranted) {
      requestStorage()
      return
    }
    if (isStoragePermissionGranted) {
      onStorageAlreadyGranted()
    } else {
      onStorageDenied()
    }
  }

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().apply {
      if (!isRegistered(this@BaseIDEActivity)) {
        register(this)
      }
    }
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().apply {
      if (isRegistered(this@BaseIDEActivity)) {
        unregister(this)
      }
    }
  }

  fun loadFragment(fragment: Fragment, id: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
  }

  protected fun requestStorage() {
    if (isStoragePermissionGranted) {
      onStorageGranted()
      return
    }
    ActivityCompat.requestPermissions(
      this,
      arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
      REQCODE_STORAGE
    )
  }

  protected open fun onStorageGranted() {}
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQCODE_STORAGE) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        onStorageGranted()
      else onStorageDenied()
    }
  }

  protected open fun onStorageAlreadyGranted() {}
  protected open fun onStorageDenied() {}
  protected open fun preSetContentLayout() {}

  protected abstract fun bindLayout(): View

  companion object {
    const val REQCODE_STORAGE = 1009
    protected var LOG = ILogger.newInstance("StudioActivity")
  }
}
