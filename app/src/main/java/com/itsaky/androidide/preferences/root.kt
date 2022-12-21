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

import android.content.Intent
import com.itsaky.androidide.activities.AboutActivity
import com.itsaky.androidide.resources.R.string
import com.itsaky.androidide.app.BaseApplication
import com.itsaky.androidide.app.IDEApplication
import kotlinx.parcelize.Parcelize

private const val KEY_GITHUB = "idepref_gh"
private const val KEY_TG_CHANNEL = "idepref_tg_channel"
private const val KEY_TG_GROUP = "idepref_tg_group"
private const val KEY_CHANGELOG = "idepref_changelog"
private const val KEY_ABOUT = "idepref_about"

val github =
  SimpleClickablePreference(
    key = KEY_GITHUB,
    title = string.title_github,
    summary = string.idepref_github_summary
  ) {
    BaseApplication.getBaseInstance().openGitHub()
    true
  }
val channel =
  SimpleClickablePreference(
    key = KEY_TG_CHANNEL,
    title = string.official_tg_channel,
    summary = string.idepref_channel_summary
  ) {
    BaseApplication.getBaseInstance().openTelegramChannel()
    true
  }
val group =
  SimpleClickablePreference(
    key = KEY_TG_GROUP,
    title = string.discussions_on_telegram,
    summary = string.idepref_group_summary
  ) {
    BaseApplication.getBaseInstance().openTelegramGroup()
    true
  }
val changelog =
  SimpleClickablePreference(
    key = KEY_CHANGELOG,
    title = string.pref_changelog,
    summary = string.idepref_changelog_summary
  ) {
    IDEApplication.instance.showChangelog()
    true
  }
val about =
  SimpleClickablePreference(
    key = KEY_ABOUT,
    title = string.idepref_about_title,
    summary = string.idepref_about_summary
  ) {
    it.context.startActivity(Intent(it.context, AboutActivity::class.java))
    true
  }

@Parcelize
class ConfigurationPreferences(
  override val key: String = "idepref_configure",
  override val title: Int = string.configure,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {
  init {
    addPreference(GeneralPreferences())
    addPreference(EditorPreferences())
    addPreference(BuildAndRunPreferences())
  }
}

@Parcelize
class AboutPreferences(
  override val key: String = "idepref_category_about",
  override val title: Int = string.about,
  override val children: List<IPreference> = mutableListOf()
) : IPreferenceGroup() {
  init {
    addPreference(github)
    addPreference(channel)
    addPreference(group)
    addPreference(changelog)
    addPreference(about)
  }
}
