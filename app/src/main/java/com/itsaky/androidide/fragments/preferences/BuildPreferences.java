/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
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
 **************************************************************************************/

package com.itsaky.androidide.fragments.preferences;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.blankj.utilcode.util.FileUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Environment;

import java.io.File;

public class BuildPreferences extends BasePreferenceFragment
    implements Preference.OnPreferenceClickListener {

  public static final String KEY_GRADLE_COMMANDS = "idepref_build_gradleCommands";
  public static final String KEY_GRADLE_CLEAR_CACHE = "idepref_build_gradleClearCache";

  private ProgressSheet progressSheet;

  @Override
  public void onCreatePreferences(Bundle p1, String p2) {
    super.onCreatePreferences(p1, p2);
    if (getContext() == null) return;
    final PreferenceScreen screen = getPreferenceScreen();
    final PreferenceCategory categoryGradle = new PreferenceCategory(getContext());
    final Preference customCommands = new Preference(getContext());
    final Preference clearCache = new Preference(getContext());

    screen.addPreference(categoryGradle);

    customCommands.setKey(KEY_GRADLE_COMMANDS);
    customCommands.setIcon(R.drawable.ic_bash_commands);
    customCommands.setTitle(R.string.idepref_build_customgradlecommands_title);
    customCommands.setSummary(R.string.idepref_build_customgradlecommands_summary);

    clearCache.setKey(KEY_GRADLE_CLEAR_CACHE);
    clearCache.setIcon(R.drawable.ic_delete);
    clearCache.setTitle(R.string.idepref_build_clearCache_title);
    clearCache.setSummary(R.string.idepref_build_clearCache_summary);

    categoryGradle.setTitle(R.string.gradle);
    categoryGradle.addPreference(customCommands);
    categoryGradle.addPreference(clearCache);

    setPreferenceScreen(screen);

    customCommands.setOnPreferenceClickListener(this);
    clearCache.setOnPreferenceClickListener(this);
  }

  @Override
  public boolean onPreferenceClick(Preference p1) {
    final String key = p1.getKey();
    if (key.equals(KEY_GRADLE_COMMANDS)) {
      showGradleCommandsDialog();
    } else if (key.equals(KEY_GRADLE_CLEAR_CACHE)) {
      showClearCacheDialog();
    }
    return true;
  }

  private void showGradleCommandsDialog() {
    final String[] labels = {
      "--stacktrace",
      "--info",
      "--debug",
      "--scan",
      "--warning-mode all",
      "--build-cache",
      "--offline"
    };
    final boolean[] checked = {
      getPrefManager().isStackTraceEnabled(),
      getPrefManager().isGradleInfoEnabled(),
      getPrefManager().isGradleDebugEnabled(),
      getPrefManager().isGradleScanEnabled(),
      getPrefManager().isGradleWarningEnabled(),
      getPrefManager().isGradleBuildCacheEnabled(),
      getPrefManager().isGradleOfflineModeEnabled()
    };
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    builder.setTitle(R.string.idepref_build_customgradlecommands_title);
    builder.setMultiChoiceItems(
        labels,
        checked,
        (p1, p2, p3) -> {
          if (p2 == 0) {
            getPrefManager().setGradleStacktraceEnabled(p3);
          } else if (p2 == 1) {
            getPrefManager().setGradleInfoEnabled(p3);
          } else if (p2 == 2) {
            getPrefManager().setGradleDebugEnabled(p3);
          } else if (p2 == 3) {
            getPrefManager().setGradleScanEnabled(p3);
          } else if (p2 == 4) {
            getPrefManager().setGradleWarningEnabled(p3);
          } else if (p2 == 5) {
            getPrefManager().setGradleBuildCacheEnabled(p3);
          } else if (p2 == 6) {
            getPrefManager().setGradleOfflineModeEnabled(p3);
          }
        });
    builder.setPositiveButton(android.R.string.ok, null);
    builder.setCancelable(false);
    builder.create().show();
  }

  private void showClearCacheDialog() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    builder.setTitle(R.string.idepref_build_clearCache_title);
    builder.setMessage(R.string.msg_clear_cache);
    builder.setCancelable(false);
    builder.setPositiveButton(
        R.string.yes,
        (p1, p2) -> {
          p1.dismiss();
          getProgressSheet().show(getChildFragmentManager(), "progress_sheet");
          new TaskExecutor().executeAsync(this::deleteCaches, __ -> getProgressSheet().dismiss());
        });
    builder.setNegativeButton(R.string.no, null);
    builder.create().show();
  }

  private Object deleteCaches() {
    File file = new File(Environment.GRADLE_USER_HOME, "caches");
    if (file.exists()) {
      FileUtils.delete(file);
    }
    return null;
  }

  private ProgressSheet getProgressSheet() {
    return progressSheet == null
        ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait))
        : progressSheet;
  }
}
