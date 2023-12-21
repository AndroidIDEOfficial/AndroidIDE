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

package com.itsaky.androidide.fragments.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.appintro.SlidePolicy
import com.itsaky.androidide.R
import com.itsaky.androidide.adapters.onboarding.OnboardingPermissionsAdapter
import com.itsaky.androidide.buildinfo.BuildInfo
import com.itsaky.androidide.models.OnboardingPermissionItem
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.isAtLeastR

/**
 * @author Akash Yadav
 */
class PermissionsFragment : OnboardingMultiActionFragment(), SlidePolicy {

  var adapter: OnboardingPermissionsAdapter? = null

  private val storagePermissionRequestLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()) {
    onPermissionsUpdated()
  }

  private val settingsTogglePermissionRequestLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()) {
    onPermissionsUpdated()
  }

  private val permissions by lazy {
    getRequiredPermissions(requireContext())
  }

  companion object {

    @JvmStatic
    fun newInstance(context: Context): PermissionsFragment {
      return PermissionsFragment().apply {
        arguments = Bundle().apply {
          putCharSequence(KEY_ONBOARDING_TITLE,
            context.getString(R.string.onboarding_title_permissions))
          putCharSequence(KEY_ONBOARDING_SUBTITLE,
            context.getString(R.string.onboarding_subtitle_permissions))
        }
      }
    }

    @JvmStatic
    fun getRequiredPermissions(context: Context): List<OnboardingPermissionItem> {
      val permissions = mutableListOf<OnboardingPermissionItem>()

      permissions.add(OnboardingPermissionItem(Manifest.permission_group.STORAGE,
        R.string.permission_title_storage, R.string.permission_desc_storage,
        isStoragePermissionGranted(context)))

      permissions.add(OnboardingPermissionItem(Manifest.permission.REQUEST_INSTALL_PACKAGES,
        R.string.permission_title_install_packages, R.string.permission_desc_install_packages,
        canRequestPackageInstalls(context)))

      return permissions
    }

    @JvmStatic
    fun areAllPermissionsGranted(context: Context) : Boolean = getRequiredPermissions(context).all { it.isGranted }

    @JvmStatic
    fun isStoragePermissionGranted(context: Context): Boolean {
      if (isAtLeastR()) {
        return Environment.isExternalStorageManager()
      }

      return checkSelfPermission(context,
        Manifest.permission.READ_EXTERNAL_STORAGE) && checkSelfPermission(context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @JvmStatic
    fun canRequestPackageInstalls(context: Context): Boolean {
      return context.packageManager.canRequestPackageInstalls()
    }

    @JvmStatic
    fun isPermissionGranted(context: Context, permission: String): Boolean {
      return when (permission) {
        Manifest.permission_group.STORAGE -> isStoragePermissionGranted(context)
        Manifest.permission.REQUEST_INSTALL_PACKAGES -> context.packageManager.canRequestPackageInstalls()
        else -> checkSelfPermission(context, permission)
      }
    }

    @JvmStatic
    fun checkSelfPermission(context: Context, permission: String): Boolean {
      return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
  }

  override fun createAdapter(): RecyclerView.Adapter<*> {
    return OnboardingPermissionsAdapter(permissions,
      this::requestPermission).also { this.adapter = it }
  }

  private fun onPermissionsUpdated() {
    permissions.forEach { it.isGranted = isPermissionGranted(requireContext(), it.permission) }
    recyclerView?.adapter = createAdapter()
  }

  private fun requestPermission(permission: String) {
    when (permission) {
      Manifest.permission_group.STORAGE -> requestStoragePermission()
      Manifest.permission.REQUEST_INSTALL_PACKAGES -> requestSettingsTogglePermission(
        Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
    }
  }

  private fun requestStoragePermission() {
    if (isAtLeastR()) {
      requestSettingsTogglePermission(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
      return
    }

    storagePermissionRequestLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE))
  }

  private fun requestSettingsTogglePermission(action: String) {
    val intent = Intent(action)
    intent.setData(Uri.fromParts("package", BuildInfo.PACKAGE_NAME, null))
    settingsTogglePermissionRequestLauncher.launch(intent)
  }

  override val isPolicyRespected: Boolean
    get() = permissions.all { it.isGranted }

  override fun onUserIllegallyRequestedNextPage() {
    activity?.flashError(R.string.msg_grant_permissions)
  }
}