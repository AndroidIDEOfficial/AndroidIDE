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

package com.itsaky.androidide.activities

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.app.IDEApplication
import com.itsaky.androidide.app.IDEBuildConfigProvider
import com.itsaky.androidide.fragments.onboarding.OnboardingFragment
import com.itsaky.androidide.fragments.onboarding.StatisticsFragment
import com.itsaky.androidide.preferences.internal.prefManager
import com.itsaky.androidide.preferences.internal.statConsentDialogShown
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.flashError

class OnboardingActivity : AppIntro2() {

  private var statisticsFragmentHasShown = false

  private val onBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      finishAffinity()
    }
  }

  companion object {

    const val FRAGMENT_SETUP_SDK = "install_jdk_sdk"
    const val FRAGMENT_DEVICE_NOT_SUPPORTED = "device_not_supported"
  }

  private val isStoragePermissionGranted: Boolean
    get() =
      (ContextCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED &&
          ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) ==
          PackageManager.PERMISSION_GRANTED)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (isSetupDone()) {
      openActivity(MainActivity::class.java)
      finish()
    }

    setTransformer(AppIntroPageTransformerType.Fade)
    isIndicatorEnabled = true
    isWizardMode = true
    setProgressIndicator()
    showStatusBar(true)

    if (checkDeviceSupported()) {
      if (!statConsentDialogShown || !statOptIn) {
        addSlide(StatisticsFragment.newInstance())
        statConsentDialogShown = true
        statisticsFragmentHasShown = true
      }
      if (!isStoragePermissionGranted) {
        addSlide(OnboardingFragment.newInstance(
          string.title_file_access,
          string.msg_file_access,
          R.raw.statistics_animation // TODO: Replace the animation with a more appropriate one
        ))
        askForPermissions(
          permissions = arrayOf(
            permission.WRITE_EXTERNAL_STORAGE,
            permission.READ_EXTERNAL_STORAGE
          ),
          slideNumber = if (archConfigWarnHasShown()) {
            if (statisticsFragmentHasShown) 3 else 2
          } else {
            if (statisticsFragmentHasShown) 2 else 1
          },
          required = true
        )
      }
      if (!checkToolsIsInstalled()) {
        addSlide(OnboardingFragment.newInstance(
          string.title_install_jdk_sdk,
          string.msg_require_install_jdk_and_android_sdk,
          R.raw.java_animation,
          FRAGMENT_SETUP_SDK
        ))
      }
    }

    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
  }

  override fun onUserDeniedPermission(permissionName: String) {
    // User pressed "Deny" on the permission dialog
    flashError(string.msg_storage_denied)
  }

  override fun onUserDisabledPermission(permissionName: String) {
    // User pressed "Deny" + "Don't ask again" on the permission dialog
  }

  override fun onResume() {
    super.onResume()
    if (isSetupDone()) {
      openActivity(MainActivity::class.java)
      finish()
    }
  }

  override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
    super.onSlideChanged(oldFragment, newFragment)
    checkConsent(oldFragment)
  }

  override fun onNextPressed(currentFragment: Fragment?) {
    super.onNextPressed(currentFragment)
    checkConsent(currentFragment)
  }

  private fun checkConsent(fragment: Fragment?) {
    if (fragment is StatisticsFragment) {
      statOptIn = fragment.statOptIn
      IDEApplication.instance.reportStatsIfNecessary()
    }
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    if (currentFragment is OnboardingFragment) {
      when (currentFragment.name) {
        FRAGMENT_SETUP_SDK -> openActivity(TerminalActivity::class.java)
        FRAGMENT_DEVICE_NOT_SUPPORTED -> finishAffinity()
        else -> openActivity(MainActivity::class.java)
      }
    }
  }

  private fun openActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
  }

  private fun checkToolsIsInstalled(): Boolean {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists()
  }

  private fun isSetupDone() =
    (checkToolsIsInstalled() && statConsentDialogShown && isStoragePermissionGranted)

  private fun checkDeviceSupported(): Boolean {
    val configProvider = IDEBuildConfigProvider.getInstance()

    val supported = if (
      configProvider.isArm64v8aBuild()
      && !configProvider.isArm64v8aDevice()
      && configProvider.isArmeabiv7aDevice()
    ) {
      // IDE = 64-bit
      // Device = 32-bit
      // NOT SUPPORTED
      addSlide(OnboardingFragment.newInstance(
        string.title_device_not_supported,
        string.msg_64bit_on_32bit_device,
        R.raw.statistics_animation // TODO: Replace the animation with a more appropriate one
      ))
      false

    } else if (
      configProvider.isArmeabiv7aBuild()
      && configProvider.isArm64v8aDevice()
    ) {
      // IDE = 32-bit
      // Device = 64-bit
      // SUPPORTED, but warn the user
      if (!archConfigWarnHasShown()) {
        addSlide(OnboardingFragment.newInstance(
          string.title_32bit_on_64bit_device,
          string.msg_32bit_on_64bit_device,
          R.raw.statistics_animation // TODO: Replace the animation with a more appropriate one
        ))
        prefManager.putBoolean("ide.archConfigWarn.hasShown", true)
      }
      true

    } else configProvider.supportsBuildFlavor()

    if (!supported) {
      addSlide(OnboardingFragment.newInstance(
        string.title_device_not_supported,
        string.msg_device_not_supported,
        R.raw.statistics_animation, // TODO: Replace the animation with a more appropriate one
        FRAGMENT_DEVICE_NOT_SUPPORTED
      ))
    }

    return supported
  }

  private fun archConfigWarnHasShown() =
    prefManager.getBoolean("ide.archConfigWarn.hasShown", false)
}