/************************************************************************************
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
 **************************************************************************************/
package com.itsaky.androidide.fragments.preferences;

import static com.itsaky.androidide.managers.PreferenceManager.KEY_CONFIRM_PROJECT_OPEN;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_OPEN_PROJECTS;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_TERMINAL_USE_SYSTEM_SHELL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.itsaky.androidide.R;

public class GeneralPreferences extends BasePreferenceFragment
    implements Preference.OnPreferenceChangeListener {

  @Override
  public void onCreatePreferences(Bundle p1, String p2) {
    super.onCreatePreferences(p1, p2);
    if (getContext() == null) return;

    final PreferenceScreen screen = getPreferenceScreen();
    final SwitchPreference openProjects = new SwitchPreference(getContext());
    final SwitchPreference confirmProjectOpen = new SwitchPreference(getContext());
    final SwitchPreference shell = new SwitchPreference(getContext());

    openProjects.setKey(KEY_OPEN_PROJECTS);
    openProjects.setTitle(R.string.title_open_projects);
    openProjects.setIcon(R.drawable.ic_open_project);
    openProjects.setSummary(R.string.msg_open_projects);

    confirmProjectOpen.setKey(KEY_CONFIRM_PROJECT_OPEN);
    confirmProjectOpen.setTitle(R.string.title_confirm_project_open);
    confirmProjectOpen.setSummary(R.string.msg_confirm_project_open);
    confirmProjectOpen.setIcon(R.drawable.ic_open_project);

    shell.setKey(KEY_TERMINAL_USE_SYSTEM_SHELL);
    shell.setTitle(getString(R.string.title_default_shell));
    shell.setSummary(getString(R.string.msg_default_shell));
    shell.setIcon(R.drawable.ic_bash_commands);

    openProjects.setChecked(getPrefManager().autoOpenProject());
    confirmProjectOpen.setChecked(getPrefManager().confirmProjectOpen());
    shell.setChecked(getPrefManager().getBoolean(KEY_TERMINAL_USE_SYSTEM_SHELL));

    screen.addPreference(openProjects);
    screen.addPreference(confirmProjectOpen);
    screen.addPreference(shell);

    setPreferenceScreen(screen);

    openProjects.setOnPreferenceChangeListener(this);
    confirmProjectOpen.setOnPreferenceChangeListener(this);
    shell.setOnPreferenceChangeListener(this);
  }

  @Override
  public boolean onPreferenceChange(@NonNull Preference p1, Object p2) {
    boolean checked = (Boolean) p2;
    final var key = p1.getKey();
    switch (key) {
      case KEY_OPEN_PROJECTS:
        getPrefManager().putBoolean(KEY_OPEN_PROJECTS, checked);
        break;
      case KEY_CONFIRM_PROJECT_OPEN:
        getPrefManager().putBoolean(KEY_CONFIRM_PROJECT_OPEN, checked);
        break;
      case KEY_TERMINAL_USE_SYSTEM_SHELL:
        getPrefManager().putBoolean(KEY_TERMINAL_USE_SYSTEM_SHELL, checked);
        break;
    }
    return true;
  }
}
