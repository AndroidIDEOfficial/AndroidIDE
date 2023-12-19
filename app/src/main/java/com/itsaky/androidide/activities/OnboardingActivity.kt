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

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.app.configuration.IDEBuildConfigProvider
import com.itsaky.androidide.fragments.onboarding.OnboardingGreetingFragment
import com.itsaky.androidide.fragments.onboarding.OnboardingInfoFragment
import com.itsaky.androidide.fragments.onboarding.OnboardingPermissionsFragment
import com.itsaky.androidide.fragments.onboarding.OnboardingToolInstallationFragment
import com.itsaky.androidide.fragments.onboarding.StatisticsFragment
import com.itsaky.androidide.preferences.internal.prefManager
import com.itsaky.androidide.preferences.internal.statConsentDialogShown
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.ui.themes.IThemeManager
import com.itsaky.androidide.utils.Environment
import com.itsaky.androidide.utils.flashError
import com.itsaky.androidide.utils.resolveAttr

class OnboardingActivity : AppIntro2() {

  private val onBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      finishAffinity()
    }
  }

  companion object {

    private const val KEY_ARCHCONFIG_WARNING_IS_SHOWN = "ide.archConfig.experimentalWarning.isShown"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    IThemeManager.getInstance().applyTheme(this)

    super.onCreate(savedInstanceState)

    if (isSetupDone()) {
      openActivity(MainActivity::class.java)
      finish()
    }

    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    setTransformer(AppIntroPageTransformerType.Fade)
    setProgressIndicator()
    showStatusBar(true)
    isIndicatorEnabled = true
    isWizardMode = true

    addSlide(OnboardingGreetingFragment())

    if (!checkDeviceSupported()) {
      return
    }

    if (!statConsentDialogShown) {
      addSlide(StatisticsFragment.newInstance(this))
      statConsentDialogShown = true
    }

    if (!OnboardingPermissionsFragment.areAllPermissionsGranted(this)) {
      addSlide(OnboardingPermissionsFragment.newInstance(this))
    }

    if (!checkToolsIsInstalled()) {
      addSlide(OnboardingToolInstallationFragment.newInstance(this))
    }
  }

  override fun onResume() {
    super.onResume()
    if (isSetupDone()) {
      openActivity(MainActivity::class.java)
      finish()
    }
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    if (!IDEBuildConfigProvider.getInstance().supportsBuildFlavor()) {
      finishAffinity()
      return
    }

    if (!checkToolsIsInstalled()) {
      openActivity(TerminalActivity::class.java)
      return
    }

    openActivity(MainActivity::class.java)
  }

  private fun openActivity(cls: Class<*>) {
    startActivity(Intent(this, cls))
  }

  private fun checkToolsIsInstalled(): Boolean {
    return Environment.JAVA.exists() && Environment.ANDROID_HOME.exists()
  }

  private fun isSetupDone() =
    (checkToolsIsInstalled() && statConsentDialogShown && OnboardingPermissionsFragment.areAllPermissionsGranted(this))

  private fun checkDeviceSupported(): Boolean {
    val configProvider = IDEBuildConfigProvider.getInstance()

    if (!configProvider.supportsBuildFlavor()) {
      addSlide(OnboardingInfoFragment.newInstance(
        getString(string.title_unsupported_device),
        getString(
          string.msg_unsupported_device,
          configProvider.flavorArch.abi,
          configProvider.deviceArch.abi
        ),
        R.drawable.ic_alert,
        ContextCompat.getColor(this, R.color.color_error)
      ))
      return false
    }

    if (configProvider.flavorArch != configProvider.deviceArch) {
      // IDE's build flavor is NOT the primary arch of the device
      // warn the user
      if (!archConfigExperimentalWarningIsShown()) {
        addSlide(OnboardingInfoFragment.newInstance(
          getString(string.title_experiment_flavor),
          getString(string.msg_experimental_flavor,
            configProvider.flavorArch.abi,
            configProvider.deviceArch.abi
          ),
          R.drawable.ic_alert,
          ContextCompat.getColor(this, R.color.color_warning)
        ))
        prefManager.putBoolean(KEY_ARCHCONFIG_WARNING_IS_SHOWN, true)
      }
    }

    return true
  }

  private fun archConfigExperimentalWarningIsShown() =
    prefManager.getBoolean(KEY_ARCHCONFIG_WARNING_IS_SHOWN, false)
}