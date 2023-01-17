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
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.itsaky.androidide.common.R
import com.itsaky.androidide.utils.ILogger
import com.itsaky.androidide.utils.isSystemInDarkMode
import com.itsaky.androidide.utils.resolveAttr

abstract class BaseIDEActivity : AppCompatActivity() {

  open val navigationBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  open val statusBarColor: Int
    get() = resolveAttr(R.attr.colorSurface)

  fun loadFragment(fragment: Fragment, id: Int) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(id, fragment)
    transaction.commit()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    window?.apply {
      navigationBarColor = this@BaseIDEActivity.navigationBarColor
      statusBarColor = this@BaseIDEActivity.statusBarColor
    }
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

  val isStoragePermissionGranted: Boolean
    get() =
      (ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) ==
        PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED)

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
