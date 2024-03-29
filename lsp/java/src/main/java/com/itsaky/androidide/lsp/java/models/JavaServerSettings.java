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
package com.itsaky.androidide.lsp.java.models;

import androidx.annotation.NonNull;
import com.google.googlejavaformat.java.JavaFormatterOptions;
import com.itsaky.androidide.lsp.util.PrefBasedServerSettings;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.preferences.internal.JavaPreferences;
import com.itsaky.androidide.utils.VMUtils;

/**
 * Server settings for the java language server.
 *
 * @author Akash Yadav
 */
public class JavaServerSettings extends PrefBasedServerSettings {

  public static final String KEY_JAVA_PREF_GOOGLE_CODE_STYLE = JavaPreferences.GOOGLE_CODE_STYLE;
  public static final int CODE_STYLE_AOSP = 0;
  public static final int CODE_STYLE_GOOGLE = 1;
  private static JavaServerSettings instance;

  @NonNull
  public static JavaServerSettings getInstance() {
    if (instance == null) {
      instance = new JavaServerSettings();
    }

    return instance;
  }

  @Override
  public boolean diagnosticsEnabled() {
    return VMUtils.isJvm() || JavaPreferences.INSTANCE.isJavaDiagnosticsEnabled();
  }

  public JavaFormatterOptions getFormatterOptions() {
    return JavaFormatterOptions.builder().formatJavadoc(true).style(getStyle()).build();
  }

  public JavaFormatterOptions.Style getStyle() {
    if (getCodeStyle() == JavaServerSettings.CODE_STYLE_AOSP) {

      return JavaFormatterOptions.Style.AOSP;
    }

    return JavaFormatterOptions.Style.GOOGLE;
  }

  private int getCodeStyle() {
    final PreferenceManager prefs = getPrefs();
    if (prefs != null) {
      if (prefs.getBoolean(KEY_JAVA_PREF_GOOGLE_CODE_STYLE, false)) {
        return CODE_STYLE_GOOGLE;
      }
    }

    return CODE_STYLE_AOSP;
  }
}
