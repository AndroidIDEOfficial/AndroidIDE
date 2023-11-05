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

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.getDefaultNightMode
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.os.LocaleListCompat
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent
import com.itsaky.androidide.preferences.internal.SELECTED_LOCALE
import com.itsaky.androidide.preferences.internal.UI_MODE
import com.itsaky.androidide.preferences.internal.uiMode
import com.itsaky.androidide.ui.themes.ThemeManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.util.Locale

abstract class IDEActivity : BaseIDEActivity() {

  val app: IDEApplication
    get() = application as IDEApplication

  override fun onStart() {
    super.onStart()
    EventBus.getDefault().register(this)
  }

  override fun onStop() {
    super.onStop()
    EventBus.getDefault().unregister(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    ThemeManager.applyTheme(this)
    super.onCreate(savedInstanceState)
  }

  @Subscribe(threadMode = MAIN)
  fun onPrefChanged(event: PreferenceChangeEvent) {
    if (event.key == UI_MODE && uiMode != getDefaultNightMode()) {
      setDefaultNightMode(uiMode)
    } else if (event.key == SELECTED_LOCALE) {

      // Use empty locale list if the locale has been reset to 'System Default'
      val selectedLocale = com.itsaky.androidide.preferences.internal.selectedLocale
      val localeListCompat = selectedLocale?.let {
        LocaleListCompat.create(Locale.forLanguageTag(selectedLocale))
      } ?: LocaleListCompat.getEmptyLocaleList()

      AppCompatDelegate.setApplicationLocales(localeListCompat)
    }
  }
}
