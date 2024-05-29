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

package com.itsaky.androidide.preferences

import android.content.Context
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import com.itsaky.androidide.R
import com.itsaky.androidide.app.IDEApplication
import com.termux.shared.logger.Logger
import com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KMutableProperty0

private const val KEY_TERMUX_PREFERENCES = "ide.preferences.terminal"
private const val KEY_TERMUX_DEBUGGING_PREFERENCES = "${KEY_TERMUX_PREFERENCES}.debugging"
private const val KEY_TERMUX_DEBUGGING_LOG_LEVEL_PREFERENCE = "${KEY_TERMUX_DEBUGGING_PREFERENCES}.logLevel"
private const val KEY_TERMUX_DEBUGGING_TERMINAL_VIEW_KEY_LOGGING_PREFERENCE = "${KEY_TERMUX_DEBUGGING_PREFERENCES}.terminalViewKeyLogging"
private const val KEY_TERMUX_DEBUGGING_CRASH_REPORT_NOTIFICATIONS_PREFERENCE = "${KEY_TERMUX_DEBUGGING_PREFERENCES}.crashReportNotifications"
private const val KEY_TERMUX_KBD_PREFERENCES = "${KEY_TERMUX_PREFERENCES}.keyboard"
private const val KEY_TERMUX_KBD_SOFT_KDB_ENABLED_PREFERENCE = "${KEY_TERMUX_KBD_PREFERENCES}.softKeyboardEnabled"
private const val KEY_TERMUX_KBD_SOFT_KDB_ONLY_IF_NO_HARD_KBD_PREFERENCE = "${KEY_TERMUX_KBD_PREFERENCES}.softKbdOnlyIfNoHardKbd"
private const val KEY_TERMUX_VIEW_PREFERENCES = "${KEY_TERMUX_PREFERENCES}.view"
private const val KEY_TERMUX_VIEW_MARGIN_ADJUSTMENT_ENABLED_PREFERENCE = "${KEY_TERMUX_VIEW_PREFERENCES}.marginAdjustment"

abstract class TermuxSwitchPreference(
  @StringRes private val summaryOn: Int,
  @StringRes private val summaryOff: Int,
  property: KMutableProperty0<Boolean>
) : SwitchPreference(property) {

  override fun onCreatePreference(context: Context): Preference {
    return super.onCreatePreference(context).also { preference ->
      updateSummary(preference)
    }
  }

  override fun onPreferenceChanged(preference: Preference, newValue: Any?): Boolean {
    return super.onPreferenceChanged(preference, newValue).also {
      updateSummary(preference)
    }
  }

  private fun updateSummary(preference: Preference) {
    preference.summary = (if (getValue?.invoke() == true) {
      summaryOn
    } else {
      summaryOff
    }).let { summary ->
      ContextCompat.getString(preference.context, summary)
    }
  }
}

@Parcelize
class TermuxPreferences(
  override val key: String = KEY_TERMUX_PREFERENCES,
  override val title: Int = R.string.termux_preferences_title,
  override val summary: Int? = R.string.termux_preferences_summary,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {

  init {
    addPreference(TermuxDebuggingPreferences())
    addPreference(TermuxKeyboardPreferences())
    addPreference(TermuxViewPreferences())
  }
}

@Parcelize
class TermuxDebuggingPreferences(
  override val key: String = KEY_TERMUX_DEBUGGING_PREFERENCES,
  override val title: Int = R.string.termux_debugging_preferences_title,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {

  init {
    addPreference(TermuxDebuggingLogLevelPreference())
    addPreference(TermuxDebuggingTerminalViewKeyLoggingPreference())
    addPreference(TermuxDebuggingCrashReportNotificationsPreference())
  }
}

@Parcelize
class TermuxDebuggingLogLevelPreference(
  override val key: String = KEY_TERMUX_DEBUGGING_LOG_LEVEL_PREFERENCE,
  override val title: Int = R.string.log_level_title,
  override val icon: Int? = R.drawable.ic_bug
) : SingleChoicePreference() {

  override fun getEntries(preference: Preference): Array<PreferenceChoices.Entry> {
    val logLevels = Logger.getLogLevelsArray()
    val logLevelLabels = Logger.getLogLevelLabelsArray(preference.context, logLevels, true)
    val currentLogLevel = TermuxAppSharedPreferences.build(preference.context, false)?.logLevel
      ?: Logger.DEFAULT_LOG_LEVEL
    return Array(logLevels.size) {
      PreferenceChoices.Entry(logLevelLabels[it], currentLogLevel == logLevels[it],
        logLevels[it])
    }
  }

  override fun onChoiceConfirmed(
    preference: Preference,
    entry: PreferenceChoices.Entry?,
    position: Int
  ) {
    val newLevel = (entry?.data as? Int?) ?: Logger.DEFAULT_LOG_LEVEL
    TermuxAppSharedPreferences.build(preference.context, false)
      ?.setLogLevel(preference.context, newLevel)

    preference.summary = Logger.getLogLevelLabel(preference.context, newLevel, true)
  }

  override fun onCreatePreference(context: Context): Preference {
    return super.onCreatePreference(context).also { preference ->
      val currentLogLevel = TermuxAppSharedPreferences.build(preference.context, false)?.logLevel
        ?: Logger.DEFAULT_LOG_LEVEL
      preference.summary = Logger.getLogLevelLabel(context, currentLogLevel, true)
    }
  }
}

private var isTerminalViewKeyLoggingEnabled: Boolean
  get() = TermuxAppSharedPreferences.build(IDEApplication.instance,
    true).isTerminalViewKeyLoggingEnabled
  set(value) {
    TermuxAppSharedPreferences.build(IDEApplication.instance,
      true).isTerminalViewKeyLoggingEnabled = value
  }

@Parcelize
class TermuxDebuggingTerminalViewKeyLoggingPreference(
  override val key: String = KEY_TERMUX_DEBUGGING_TERMINAL_VIEW_KEY_LOGGING_PREFERENCE,
  override val title: Int = R.string.termux_terminal_view_key_logging_enabled_title,
  override val icon: Int? = R.drawable.ic_keyboard,
) : TermuxSwitchPreference(
  summaryOn = R.string.termux_terminal_view_key_logging_enabled_on,
  summaryOff = R.string.termux_terminal_view_key_logging_enabled_off,
  property = ::isTerminalViewKeyLoggingEnabled
)

private var isTerminalCrashReportNotificationsEnabled: Boolean
  get() = TermuxAppSharedPreferences.build(IDEApplication.instance,
    true).areCrashReportNotificationsEnabled(false)
  set(value) {
    TermuxAppSharedPreferences.build(IDEApplication.instance,
      true).setCrashReportNotificationsEnabled(value)
  }

@Parcelize
class TermuxDebuggingCrashReportNotificationsPreference(
  override val key: String = KEY_TERMUX_DEBUGGING_CRASH_REPORT_NOTIFICATIONS_PREFERENCE,
  override val title: Int = R.string.termux_crash_report_notifications_enabled_title,
  override val icon: Int? = R.drawable.ic_bell,
) : TermuxSwitchPreference(
  summaryOn = R.string.termux_crash_report_notifications_enabled_on,
  summaryOff = R.string.termux_crash_report_notifications_enabled_off,
  property = ::isTerminalCrashReportNotificationsEnabled
)

@Parcelize
class TermuxKeyboardPreferences(
  override val key: String = KEY_TERMUX_KBD_PREFERENCES,
  override val title: Int = R.string.termux_keyboard_header,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {

  init {
    addPreference(TermuxKbdSoftKbdEnabledPreference())
    addPreference(TermuxKbdSoftKbdOnlyIfNoHardKbdEnabledPreference())
  }
}

private var isSoftKbdEnabled: Boolean
  get() = TermuxAppSharedPreferences.build(IDEApplication.instance,
    true).isSoftKeyboardEnabled
  set(value) {
    TermuxAppSharedPreferences.build(IDEApplication.instance,
      true).isSoftKeyboardEnabled = value
  }

@Parcelize
class TermuxKbdSoftKbdEnabledPreference(
  override val key: String = KEY_TERMUX_KBD_SOFT_KDB_ENABLED_PREFERENCE,
  override val title: Int = R.string.termux_soft_keyboard_enabled_title,
  override val icon: Int? = R.drawable.ic_keyboard_soft,
) : TermuxSwitchPreference(
  summaryOn = R.string.termux_soft_keyboard_enabled_on,
  summaryOff = R.string.termux_soft_keyboard_enabled_off,
  property = ::isSoftKbdEnabled
)

private var isSoftKbdOnlyIfNoHardKbdEnabled: Boolean
  get() = TermuxAppSharedPreferences.build(IDEApplication.instance,
    true).isSoftKeyboardEnabledOnlyIfNoHardware
  set(value) {
    TermuxAppSharedPreferences.build(IDEApplication.instance,
      true).isSoftKeyboardEnabledOnlyIfNoHardware = value
  }

@Parcelize
class TermuxKbdSoftKbdOnlyIfNoHardKbdEnabledPreference(
  override val key: String = KEY_TERMUX_KBD_SOFT_KDB_ONLY_IF_NO_HARD_KBD_PREFERENCE,
  override val title: Int = R.string.termux_soft_keyboard_enabled_only_if_no_hardware_title,
  override val icon: Int? = R.drawable.ic_keyboard,
) : TermuxSwitchPreference(
  summaryOn = R.string.termux_soft_keyboard_enabled_only_if_no_hardware_on,
  summaryOff = R.string.termux_soft_keyboard_enabled_only_if_no_hardware_off,
  property = ::isSoftKbdOnlyIfNoHardKbdEnabled
)

@Parcelize
class TermuxViewPreferences(
  override val key: String = KEY_TERMUX_VIEW_PREFERENCES,
  override val title: Int = R.string.termux_terminal_view_view_header,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {

  init {
    addPreference(TermuxViewMarginAdjustmentEnabledPreference())
  }
}

private var isViewMarginAdjustmentEnabled: Boolean
  get() = TermuxAppSharedPreferences.build(IDEApplication.instance,
    true).isTerminalMarginAdjustmentEnabled
  set(value) {
    TermuxAppSharedPreferences.build(IDEApplication.instance,
      true).setTerminalMarginAdjustment(value)
  }

@Parcelize
class TermuxViewMarginAdjustmentEnabledPreference(
  override val key: String = KEY_TERMUX_VIEW_MARGIN_ADJUSTMENT_ENABLED_PREFERENCE,
  override val title: Int = R.string.termux_terminal_view_terminal_margin_adjustment_title,
  override val icon: Int? = R.drawable.ic_space,
) : TermuxSwitchPreference(
  summaryOn = R.string.termux_terminal_view_terminal_margin_adjustment_on,
  summaryOff = R.string.termux_terminal_view_terminal_margin_adjustment_off,
  property = ::isViewMarginAdjustmentEnabled
)