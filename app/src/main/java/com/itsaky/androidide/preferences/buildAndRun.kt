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
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.itsaky.androidide.resources.R.drawable
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.preferences.internal.CUSTOM_GRADLE_INSTALLATION
import com.itsaky.androidide.preferences.internal.GRADLE_CLEAR_CACHE
import com.itsaky.androidide.preferences.internal.GRADLE_COMMANDS
import com.itsaky.androidide.preferences.internal.TP_FIX
import com.itsaky.androidide.preferences.internal.gradleInstallationDir
import com.itsaky.androidide.preferences.internal.isBuildCacheEnabled
import com.itsaky.androidide.preferences.internal.isDebugEnabled
import com.itsaky.androidide.preferences.internal.isInfoEnabled
import com.itsaky.androidide.preferences.internal.isOfflineEnabled
import com.itsaky.androidide.preferences.internal.isScanEnabled
import com.itsaky.androidide.preferences.internal.isStacktraceEnabled
import com.itsaky.androidide.preferences.internal.isWarningModeAllEnabled
import com.itsaky.androidide.preferences.internal.tpFix
import com.itsaky.androidide.tasks.executeAsync
import com.itsaky.androidide.utils.Environment.GRADLE_USER_HOME
import com.itsaky.toaster.toastError
import com.itsaky.toaster.toastSuccess
import java.io.File
import kotlinx.parcelize.Parcelize

@Parcelize
class BuildAndRunPreferences(
  override val key: String = "idepref_build_n_run",
  override val title: Int = string.idepref_build_title,
  override val summary: Int? = string.idepref_buildnrun_summary,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceScreen() {
  init {
    addPreference(GradleOptions())
  }
}

@Parcelize
private class GradleOptions(
  override val key: String = "idepref_build_gradle",
  override val title: Int = string.gradle,
  override val children: List<IPreference> = mutableListOf(),
) : IPreferenceGroup() {
  init {
    addPreference(GradleCommands())
    addPreference(GradleDistrubution())
    addPreference(GradleClearCache())
    if (BaseApplication.isAarch64() && VERSION.SDK_INT == VERSION_CODES.R) {
      addPreference(TagPointersFix())
    }
  }
}

@Parcelize
private class GradleCommands(
  override val key: String = GRADLE_COMMANDS,
  override val title: Int = string.idepref_build_customgradlecommands_title,
  override val summary: Int? = string.idepref_build_customgradlecommands_summary,
  override val icon: Int? = drawable.ic_bash_commands,
) : MultiChoicePreference() {

  override fun getCheckedItems(): BooleanArray {
    return booleanArrayOf(
      isStacktraceEnabled,
      isInfoEnabled,
      isDebugEnabled,
      isScanEnabled,
      isWarningModeAllEnabled,
      isBuildCacheEnabled,
      isOfflineEnabled
    )
  }

  override fun getChoices(context: Context): Array<String> {
    return arrayOf(
      "--stacktrace",
      "--info",
      "--debug",
      "--scan",
      "--warning-mode all",
      "--build-cache",
      "--offline"
    )
  }

  override fun onItemSelected(position: Int, isSelected: Boolean) {
    when (position) {
      0 -> isStacktraceEnabled = isSelected
      1 -> isInfoEnabled = isSelected
      2 -> isDebugEnabled = isSelected
      3 -> isScanEnabled = isSelected
      4 -> isWarningModeAllEnabled = isSelected
      5 -> isBuildCacheEnabled = isSelected
      6 -> isOfflineEnabled = isSelected
    }
  }
}

@Parcelize
private class GradleDistrubution(
  override val key: String = CUSTOM_GRADLE_INSTALLATION,
  override val title: Int = string.idepref_title_customGradleInstallation,
  override val summary: Int? = string.idepref_msg_customGradleInstallation,
  override val icon: Int? = drawable.ic_gradle,
) : EditTextPreference() {

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    gradleInstallationDir = newValue as String? ?: ""
    return true
  }

  override fun onConfigureTextInput(input: TextInputLayout) {
    input.setStartIconDrawable(drawable.ic_gradle)
    input.setHint(string.msg_gradle_installation_path)
    input.helperText = input.context.getString(string.msg_gradle_installation_input_help)
    input.isCounterEnabled = false
    input.editText!!.setText(gradleInstallationDir)
  }
}

@Parcelize
private class TagPointersFix(
  override val key: String = TP_FIX,
  override val title: Int = string.idepref_title_tpFix,
  override val summary: Int? = string.idepref_msg_tpFix,
  override val icon: Int? = drawable.ic_language_java,
) : SwitchPreference() {

  override fun onCreatePreference(context: Context): Preference {
    val preference = super.onCreatePreference(context) as androidx.preference.SwitchPreference
    preference.isChecked = tpFix
    return preference
  }

  override fun onPreferenceChanged(preferece: Preference, newValue: Any?): Boolean {
    tpFix = newValue as Boolean? ?: false
    return true
  }
}

@Parcelize
private class GradleClearCache (
  override val key: String = GRADLE_CLEAR_CACHE,
  override val title: Int = string.idepref_build_clearCache_title,
  override val summary: Int? = string.idepref_build_clearCache_summary,
  override val icon: Int? = drawable.ic_delete,
  override val dialogMessage: Int? = string.msg_clear_cache
) : DialogPreference() {

  override fun onConfigureDialog(preference: Preference, dialog: MaterialAlertDialogBuilder) {
    super.onConfigureDialog(preference, dialog)
    dialog.setPositiveButton(string.yes) { dlg, _ ->
      dlg.dismiss()
      executeAsync(callable = this::deleteCaches) {
        if (it == true) {
          toastSuccess(string.deleted)
        } else {
          toastError(string.delete_failed)
        }
      }
    }
    dialog.setNegativeButton(string.no) { dlg, _ -> dlg.dismiss() }
  }

  private fun deleteCaches(): Boolean {
    val caches = File(GRADLE_USER_HOME, "caches")
    if (caches.exists()) {
      return caches.deleteRecursively()
    }
    return false
  }
}
