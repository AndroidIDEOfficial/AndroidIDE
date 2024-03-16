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

package com.itsaky.androidide.lsp.util;

import androidx.annotation.Nullable;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.lsp.api.IServerSettings;
import com.itsaky.androidide.managers.PreferenceManager;

/**
 * {@link IServerSettings} implementation which uses {@link
 * com.itsaky.androidide.managers.PreferenceManager PreferencesManager} to read common settings.
 *
 * @author Akash Yadav
 */
public abstract class PrefBasedServerSettings extends DefaultServerSettings {

  private PreferenceManager prefs;

  @Override
  public boolean shouldMatchAllLowerCase() {
    final var prefs = getPrefs();
    if (prefs != null) {
      return prefs.getBoolean(KEY_COMPLETIONS_MATCH_LOWER, true);
    }

    return false;
  }

  @Nullable
  public PreferenceManager getPrefs() {
    if (prefs == null) {
      final var app = BaseApplication.getBaseInstance();
      if (app != null) {
        prefs = app.getPrefManager();
      }
    }
    return prefs;
  }
}
