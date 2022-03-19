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

package com.itsaky.androidide.models;

import androidx.annotation.NonNull;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.lsp.java.models.DefaultJavaServerSettings;

/**
 * Server settings for the java language server.
 *
 * @author Akash Yadav
 */
public class PrefBasedJavaServerSettings extends DefaultJavaServerSettings {

    public static final String KEY_JAVA_PREF_MATCH_LOWER = "idepref_editor_java_matchLower";
    public static final String KEY_JAVA_PREF_GOOGLE_CODE_STYLE =
            "idepref_editor_java_googleCodeStyle";

    private static PrefBasedJavaServerSettings instance;

    private final PreferenceManager prefs;

    public PrefBasedJavaServerSettings() {
        this.prefs = StudioApp.getInstance().getPrefManager();
    }

    @NonNull
    public static PrefBasedJavaServerSettings getInstance() {
        if (instance == null) {
            instance = new PrefBasedJavaServerSettings();
        }

        return instance;
    }

    @Override
    public boolean shouldMatchAllLowerCase() {
        return prefs.getBoolean(KEY_JAVA_PREF_MATCH_LOWER, false);
    }

    @Override
    public int getCodeStyle() {
        if (prefs.getBoolean(KEY_JAVA_PREF_GOOGLE_CODE_STYLE, false)) {
            return CODE_STYLE_GOOGLE;
        }

        return CODE_STYLE_AOSP;
    }
}
