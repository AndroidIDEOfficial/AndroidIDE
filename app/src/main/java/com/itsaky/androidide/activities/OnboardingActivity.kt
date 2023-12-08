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
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroPageTransformerType
import com.itsaky.androidide.R
import com.itsaky.androidide.R.string
import com.itsaky.androidide.fragments.onboarding.OnboardingFragment
import com.itsaky.androidide.preferences.internal.statConsentDialogShown
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.utils.Environment

class OnboardingActivity : AppIntro2() {

  private val onBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      finishAffinity()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setTransformer(AppIntroPageTransformerType.Fade)
    isIndicatorEnabled = true
    isWizardMode = true
    setProgressIndicator()
    showStatusBar(true)

    if (!statConsentDialogShown) {
      addSlide(OnboardingFragment.newInstance(
        string.title_androidide_statistics,
        string.msg_androidide_statistics,
        R.raw.statistics_animation,
        "statistics"
      ))
    }
    if (!checkToolsIsInstalled()) {
      addSlide(OnboardingFragment.newInstance(
        string.title_install_jdk_sdk,
        string.msg_require_install_jdk_and_android_sdk,
        R.raw.java_animation,
        "install_jdk_sdk"
      ))
    }

    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
    if (fragment is OnboardingFragment) {
      when (fragment.name) {
        "statistics" -> {
          statOptIn = true
          statConsentDialogShown = true
        }
      }
    }
  }

  override fun onDonePressed(currentFragment: Fragment?) {
    super.onDonePressed(currentFragment)
    if (currentFragment is OnboardingFragment) {
      when (currentFragment.name) {
        "install_jdk_sdk" -> openActivity(TerminalActivity::class.java)
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

  private fun isSetupDone(): Boolean = checkToolsIsInstalled() && statConsentDialogShown
}