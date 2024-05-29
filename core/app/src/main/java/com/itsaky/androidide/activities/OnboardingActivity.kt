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
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.app.configuration.IDEBuildConfigProvider
import com.itsaky.androidide.app.configuration.IJdkDistributionProvider
import com.itsaky.androidide.fragments.onboarding.GreetingFragment
import com.itsaky.androidide.fragments.onboarding.IdeSetupConfigurationFragment
import com.itsaky.androidide.fragments.onboarding.OnboardingInfoFragment
import com.itsaky.androidide.fragments.onboarding.PermissionsFragment
import com.itsaky.androidide.fragments.onboarding.StatisticsFragment
import com.itsaky.androidide.models.JdkDistribution
import com.itsaky.androidide.preferences.internal.StatPreferences
import com.itsaky.androidide.preferences.internal.prefManager
import com.itsaky.androidide.tasks.launchAsyncWithProgress
import com.itsaky.androidide.ui.themes.IThemeManager
import com.itsaky.androidide.utils.Environment
import com.termux.shared.android.PackageUtils
import com.termux.shared.markdown.MarkdownUtils
import com.termux.shared.termux.TermuxConstants
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext

class OnboardingActivity : AppIntro2() {

  private val terminalActivityCallback = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()) {
    Log.d(TAG, "TerminalActivity: resultCode=${it.resultCode}")
    if (!isFinishing) {
      reloadJdkDistInfo {
        tryNavigateToMainIfSetupIsCompleted()
      }
    }
  }

  private val activityScope =
    CoroutineScope(Dispatchers.Main + CoroutineName("OnboardingActivity"))

  private var listJdkInstallationsJob: Job? = null

  companion object {

    private const val TAG = "OnboardingActivity"
    private const val KEY_ARCHCONFIG_WARNING_IS_SHOWN = "ide.archConfig.experimentalWarning.isShown"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    IThemeManager.getInstance().applyTheme(this)

    super.onCreate(savedInstanceState)

    if (tryNavigateToMainIfSetupIsCompleted()) {
      return
    }

    setSwipeLock(true)
    setTransformer(AppIntroPageTransformerType.Fade)
    setProgressIndicator()
    showStatusBar(true)
    isIndicatorEnabled = true
    isWizardMode = true

    addSlide(GreetingFragment())

    if (!PackageUtils.isCurrentUserThePrimaryUser(this)) {
      val errorMessage = getString(string.bootstrap_error_not_primary_user_message,
        MarkdownUtils.getMarkdownCodeForString(TermuxConstants.TERMUX_PREFIX_DIR_PATH, false))
      addSlide(OnboardingInfoFragment.newInstance(
        getString(string.title_unsupported_user),
        errorMessage,
        R.drawable.ic_alert,
        ContextCompat.getColor(this, R.color.color_error)
      ))
      return
    }

    if (isInstalledOnSdCard()) {
      val errorMessage = getString(string.bootstrap_error_installed_on_portable_sd,
        MarkdownUtils.getMarkdownCodeForString(TermuxConstants.TERMUX_PREFIX_DIR_PATH, false))
      addSlide(OnboardingInfoFragment.newInstance(
        getString(string.title_install_location_error),
        errorMessage,
        R.drawable.ic_alert,
        ContextCompat.getColor(this, R.color.color_error)
      ))
      return
    }

    if (!checkDeviceSupported()) {
      return
    }

    if (!StatPreferences.statConsentDialogShown) {
      addSlide(StatisticsFragment.newInstance(this))
      StatPreferences.statConsentDialogShown = true
    }

    if (!PermissionsFragment.areAllPermissionsGranted(this)) {
      addSlide(PermissionsFragment.newInstance(this))
    }

    if (!checkToolsIsInstalled()) {
      addSlide(IdeSetupConfigurationFragment.newInstance(this))
    }
  }

  override fun onResume() {
    super.onResume()
    reloadJdkDistInfo {
      tryNavigateToMainIfSetupIsCompleted()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    activityScope.cancel("Activity is being destroyed")
  }

  override fun onNextPressed(currentFragment: Fragment?) {
    (currentFragment as? StatisticsFragment?)?.updateStatOptInStatus()
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    (currentFragment as? StatisticsFragment?)?.updateStatOptInStatus()

    if (!IDEBuildConfigProvider.getInstance().supportsCpuAbi()) {
      finishAffinity()
      return
    }

    if (!checkToolsIsInstalled() && currentFragment is IdeSetupConfigurationFragment) {
      val intent = Intent(this, TerminalActivity::class.java)
      if (currentFragment.isAutoInstall()) {
        intent.putExtra(TerminalActivity.EXTRA_ONBOARDING_RUN_IDESETUP, true)
        intent.putExtra(TerminalActivity.EXTRA_ONBOARDING_RUN_IDESETUP_ARGS,
          currentFragment.buildIdeSetupArguments())
      }
      terminalActivityCallback.launch(intent)
      return
    }

    tryNavigateToMainIfSetupIsCompleted()
  }

  private fun checkToolsIsInstalled(): Boolean {
    return IJdkDistributionProvider.getInstance().installedDistributions.isNotEmpty()
        && Environment.ANDROID_HOME.exists()
  }

  private fun isSetupCompleted(): Boolean {
    return checkToolsIsInstalled()
        && StatPreferences.statConsentDialogShown
        && PermissionsFragment.areAllPermissionsGranted(this)
  }

  private fun tryNavigateToMainIfSetupIsCompleted(): Boolean {
    if (isSetupCompleted()) {
      startActivity(Intent(this, MainActivity::class.java))
      finish()
      return true
    }

    return false
  }

  private inline fun reloadJdkDistInfo(crossinline distConsumer: (List<JdkDistribution>) -> Unit) {
    listJdkInstallationsJob?.cancel("Reloading JDK distributions")

    listJdkInstallationsJob = activityScope.launchAsyncWithProgress(Dispatchers.Default,
      configureFlashbar = { builder, _ ->
        builder.message(string.please_wait)
      }) { _, _ ->
      val distributionProvider = IJdkDistributionProvider.getInstance()
      distributionProvider.loadDistributions()
      withContext(Dispatchers.Main) {
        distConsumer(distributionProvider.installedDistributions)
      }
    }.also {
      it?.invokeOnCompletion {
        listJdkInstallationsJob = null
      }
    }
  }

  private fun isInstalledOnSdCard(): Boolean {
    // noinspection SdCardPath
    return PackageUtils.isAppInstalledOnExternalStorage(this) &&
        TermuxConstants.TERMUX_FILES_DIR_PATH != filesDir.absolutePath
      .replace("^/data/user/0/".toRegex(), "/data/data/")
  }

  private fun checkDeviceSupported(): Boolean {
    val configProvider = IDEBuildConfigProvider.getInstance()

    if (!configProvider.supportsCpuAbi()) {
      addSlide(OnboardingInfoFragment.newInstance(
        getString(string.title_unsupported_device),
        getString(
          string.msg_unsupported_device,
          configProvider.cpuArch.abi,
          configProvider.deviceArch.abi
        ),
        R.drawable.ic_alert,
        ContextCompat.getColor(this, R.color.color_error)
      ))
      return false
    }

    if (configProvider.cpuArch != configProvider.deviceArch) {
      // IDE's build flavor is NOT the primary arch of the device
      // warn the user
      if (!archConfigExperimentalWarningIsShown()) {
        addSlide(OnboardingInfoFragment.newInstance(
          getString(string.title_experiment_flavor),
          getString(string.msg_experimental_flavor,
            configProvider.cpuArch.abi,
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