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

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.FlakyTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.adevinta.android.barista.assertion.BaristaCheckedAssertions.assertChecked
import com.adevinta.android.barista.assertion.BaristaCheckedAssertions.assertUnchecked
import com.adevinta.android.barista.assertion.BaristaEnabledAssertions.assertDisabled
import com.adevinta.android.barista.assertion.BaristaEnabledAssertions.assertEnabled
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickBack
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.rule.BaristaRule
import com.google.common.truth.Truth.assertThat
import com.itsaky.androidide.R
import com.itsaky.androidide.preferences.internal.statOptIn
import com.itsaky.androidide.testing.android.launchAndroidIDE
import com.itsaky.androidide.testing.android.stringRes
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


/**
 * @author Akash Yadav
 */
@Suppress("TestFunctionName")
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class OnboardingActivityStatesTest {

  @JvmField
  @Rule
  val baristaActivityRule = BaristaRule.create(OnboardingActivity::class.java)

  @Test
  fun A_testOnboarding_welcomeScreen() {
    baristaActivityRule.launchActivity()

    assertDisplayed(stringRes(R.string.greeting_title))
    assertDisplayed(stringRes(R.string.greeting_subtitle))
    assertDisplayed(R.id.next)
    assertNotDisplayed(R.id.back)
  }

  @Test
  fun B_testOnboarding_statConsentScreen() {
    baristaActivityRule.launchActivity()

    clickOn(R.id.next)

    assertDisplayed(R.id.next)
    assertDisplayed(stringRes(R.string.title_androidide_statistics))
    assertDisplayed(stringRes(R.string.idepref_stats_optIn_summary))
    assertDisplayed(stringRes(R.string.msg_androidide_statistics))

    assertDisplayed(R.id.stat_opt_in, stringRes(R.string.title_androidide_statistics))

    assertThat(statOptIn).isTrue()
    assertChecked(R.id.stat_opt_in)

    // disable and navigate to next screen, then verify
    clickOn(R.id.stat_opt_in)
    clickOn(R.id.next)
    assertThat(statOptIn).isFalse()
    assertUnchecked(R.id.stat_opt_in)

    // Navigate back (statistics)
    clickBack()

    // enable again and navigate to next screen, then verify
    clickOn(R.id.stat_opt_in)
    clickOn(R.id.next)
    assertThat(statOptIn).isTrue()
    assertChecked(R.id.stat_opt_in)
  }

  @Test
  fun F_testOnboarding_toolsSetup() {

    baristaActivityRule.launchActivity()

    val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    device.grantAndroidIDEPermissions()

    assertDisplayed(stringRes(R.string.title_install_tools))
    assertDisplayed(stringRes(R.string.subtitle_install_tools))
    assertDisplayed(R.id.auto_install_switch)
    assertDisplayed(R.id.sdk_version_layout)
    assertDisplayed(R.id.jdk_version_layout)
    assertDisplayed(R.id.install_git)
    assertDisplayed(R.id.install_openssh)

    assertEnabled(R.id.install_git)
    assertEnabled(R.id.install_openssh)
    assertEnabled(R.id.sdk_version_layout)
    assertEnabled(R.id.jdk_version_layout)

    assertChecked(R.id.auto_install_switch)
    assertChecked(R.id.install_git)
    assertChecked(R.id.install_openssh)

    // switch to manual install
    clickOn(R.id.auto_install_switch)

    // check that the auto-install related fields are disabled
    assertUnchecked(R.id.auto_install_switch)
    assertDisabled(R.id.install_git)
    assertDisabled(R.id.install_openssh)
    assertDisabled(R.id.sdk_version_layout)
    assertDisabled(R.id.jdk_version_layout)

    // switch back to auto-install
    clickOn(R.id.auto_install_switch)

    // check that the auto-instal related fields are enabled again
    assertChecked(R.id.auto_install_switch)
    assertEnabled(R.id.install_git)
    assertEnabled(R.id.install_openssh)
    assertEnabled(R.id.sdk_version_layout)
    assertEnabled(R.id.jdk_version_layout)
  }
}