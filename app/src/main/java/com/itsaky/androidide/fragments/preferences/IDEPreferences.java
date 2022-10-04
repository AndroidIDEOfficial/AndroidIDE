/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide.fragments.preferences;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.itsaky.androidide.AboutActivity;
import com.itsaky.androidide.BuildConfig;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.app.IDEApplication;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.toaster.Toaster;
import com.itsaky.toaster.ToastUtilsKt;

public class IDEPreferences extends BasePreferenceFragment
    implements Preference.OnPreferenceClickListener {

  public static final String KEY_GENERAL = "idepref_general";
  public static final String KEY_EDITOR = "idepref_editor";
  public static final String KEY_BUILD = "idepref_build";
  public static final String KEY_TG_GROUP = "idepref_tg_group";
  public static final String KEY_TG_CHANNEL = "idepref_tg_channel";
  public static final String KEY_ISSUES = "idepref_issues";
  public static final String KEY_CHANGELOG = "idepref_changelog";
  public static final String KEY_ABOUT = "idepref_about";
  private static final ILogger LOG = ILogger.newInstance("IDEPreferences");
  private GeneralPreferences mGeneralPrefs;
  private BuildPreferences mBuildPreferences;
  private EditorPreferences mEditorPreferences;

  @Override
  public void onCreatePreferences(Bundle savedState, String rootKey) {
    super.onCreatePreferences(savedState, rootKey);
    if (getContext() == null) return;

    final PreferenceScreen screen = getPreferenceScreen();
    final Preference general = new Preference(getContext());
    final Preference editor = new Preference(getContext());
    final Preference build = new Preference(getContext());
    final Preference tg_channel = new Preference(getContext());
    final Preference tg_group = new Preference(getContext());
    final Preference github = new Preference(getContext());
    final Preference changelog = new Preference(getContext());
    final Preference about = new Preference(getContext());

    general.setKey(KEY_GENERAL);
    general.setIconSpaceReserved(false);
    general.setFragment(getGeneralFrag().getClass().getName());
    general.setTitle(R.string.title_general);

    editor.setKey(KEY_EDITOR);
    editor.setIconSpaceReserved(false);
    editor.setFragment(getEditorFrag().getClass().getName());
    editor.setTitle(R.string.idepref_editor_title);

    build.setKey(KEY_BUILD);
    build.setIconSpaceReserved(false);
    build.setFragment(getBuildFrag().getClass().getName());
    build.setTitle(R.string.idepref_build_title);

    github.setKey(KEY_ISSUES);
    github.setIconSpaceReserved(false);
    github.setTitle(R.string.title_github);

    tg_channel.setKey(KEY_TG_CHANNEL);
    tg_channel.setIconSpaceReserved(false);
    tg_channel.setTitle(R.string.official_tg_channel);

    tg_group.setKey(KEY_TG_GROUP);
    tg_group.setIconSpaceReserved(false);
    tg_group.setTitle(R.string.discussions_on_telegram);

    changelog.setKey(KEY_CHANGELOG);
    changelog.setIconSpaceReserved(false);
    changelog.setTitle(R.string.pref_changelog);

    about.setKey(KEY_ABOUT);
    about.setIconSpaceReserved(false);
    about.setTitle(R.string.idepref_about_title);

    screen.addPreference(general);
    screen.addPreference(editor);
    screen.addPreference(build);
    screen.addPreference(github);
    screen.addPreference(tg_channel);
    screen.addPreference(tg_group);
    screen.addPreference(changelog);
    screen.addPreference(about);

    github.setOnPreferenceClickListener(this);
    tg_channel.setOnPreferenceClickListener(this);
    tg_group.setOnPreferenceClickListener(this);
    changelog.setOnPreferenceClickListener(this);
    about.setOnPreferenceClickListener(this);
  }

  private GeneralPreferences getGeneralFrag() {
    return mGeneralPrefs == null ? mGeneralPrefs = new GeneralPreferences() : mGeneralPrefs;
  }

  private EditorPreferences getEditorFrag() {
    return mEditorPreferences == null
        ? mEditorPreferences = new EditorPreferences()
        : mEditorPreferences;
  }

  private BuildPreferences getBuildFrag() {
    return mBuildPreferences == null
        ? mBuildPreferences = new BuildPreferences()
        : mBuildPreferences;
  }

  @Override
  public boolean onPreferenceClick(@NonNull Preference preference) {
    final String key = preference.getKey();
    switch (key) {
      case KEY_CHANGELOG:
        showChangelog();
        break;
      case KEY_TG_CHANNEL:
        IDEApplication.getInstance().openTelegramChannel();
        break;
      case KEY_TG_GROUP:
        IDEApplication.getInstance().openTelegramGroup();
        break;
      case KEY_ISSUES:
        IDEApplication.getInstance().openGitHub();
        break;
      case KEY_ABOUT:
        startActivity(new Intent(getContext(), AboutActivity.class));
        break;
    }
    return true;
  }

  private void showChangelog() {
    final var intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(
        Uri.parse(
            BaseApplication.GITHUB_URL.concat("/releases/tag/v").concat(BuildConfig.VERSION_NAME)));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      startActivity(intent);
    } catch (Throwable th) {
      LOG.error("Unable to start activity to show changelog", th);
      ToastUtilsKt.toast("Unable to start activity", Toaster.Type.ERROR);
    }
  }
}
