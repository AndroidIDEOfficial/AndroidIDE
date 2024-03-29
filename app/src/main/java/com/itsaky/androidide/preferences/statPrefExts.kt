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
import androidx.preference.Preference
import com.itsaky.androidide.preferences.internal.StatPreferences
import com.itsaky.androidide.resources.R
import com.itsaky.androidide.stats.AndroidIDEStats
import com.itsaky.androidide.stats.StatUploadWorker
import kotlinx.parcelize.Parcelize

@Parcelize
class StatPreferencesScreen(
  override val key: String = "idepref_stats",
  override val title: Int = R.string.title_androidide_statistics,
  override val summary: Int? = R.string.summary_anonymous_usage_stats,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceScreen() {

  init {
    addPreference(StatsCollection())
    addPreference(PreviewDataPreferences())
  }
}

@Parcelize
class StatsCollection(
  override val key: String = StatPreferences.STAT_OPT_IN,
  override val title: Int = R.string.idepref_stats_optIn_title,
  override val summary: Int? = R.string.idepref_stats_optIn_summary,
  override val icon: Int? = null
) : SwitchPreference(setValue = StatPreferences::statOptIn::set,
  getValue = StatPreferences::statOptIn::get)

@Parcelize
class PreviewDataPreferences(
  override val key: String = "idepref_privacy",
  override val title: Int = R.string.title_preview_data,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {

  init {
    addPreference(UniqueID())
    addPreference(DeviceName())
    addPreference(AndroidVersion())
    addPreference(AppVersion())
    addPreference(CpuArch())
    addPreference(Country())
  }
}

private abstract class StatDataPreference(
  private val getValue: () -> String
) : SimplePreference() {

  override val summary: Int? = null
  override val icon: Int? = null

  override fun onCreatePreference(context: Context): Preference {
    return super.onCreatePreference(context).apply {
      summary = getValue()
    }
  }
}

@Parcelize
private class UniqueID(
  override val key: String = StatUploadWorker.KEY_DEVICE_ID,
  override val title: Int = R.string.title_unique_id,
) : StatDataPreference({ AndroidIDEStats.uniqueDeviceId })

@Parcelize
private class DeviceName(
  override val key: String = StatUploadWorker.KEY_DEVICE_NAME,
  override val title: Int = R.string.title_device
) : StatDataPreference({ AndroidIDEStats.deviceModel })

@Parcelize
private class AndroidVersion(
  override val key: String = StatUploadWorker.KEY_ANDROID_VERSION,
  override val title: Int = R.string.title_android_version
) : StatDataPreference({ AndroidIDEStats.androidVersion.toString() })

@Parcelize
private class AppVersion(
  override val key: String = StatUploadWorker.KEY_APP_VERSION,
  override val title: Int = R.string.title_app_version
) : StatDataPreference({ AndroidIDEStats.appVersion })

@Parcelize
private class Country(
  override val key: String = StatUploadWorker.KEY_DEVICE_COUNTRY,
  override val title: Int = R.string.title_country
) : StatDataPreference({ AndroidIDEStats.country })

@Parcelize
private class CpuArch(
  override val key: String = StatUploadWorker.KEY_APP_CPU_ARCH,
  override val title: Int = R.string.title_cpu_arch
) : StatDataPreference({ AndroidIDEStats.cpuArch })