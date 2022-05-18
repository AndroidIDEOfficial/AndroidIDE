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
package com.itsaky.androidide.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

  public static final String KEY_FRAMEWORK_DOWNLOADED = "framework_downloaded";
  public static final String KEY_FRAMEWORK_INSTALLED = "framework_installed";
  public static final String KEY_IS_FIRST_PROJECT_BUILD = "project_isFirstBuild";
  public static final String KEY_OPEN_PROJECTS = "idepref_general_autoOpenProjects";
  public static final String KEY_CONFIRM_PROJECT_OPEN = "idepref_general_confirmProjectOpen";
  public static final String KEY_TERMINAL_USE_SYSTEM_SHELL = "idepref_general_terminalShell";
  public static final String KEY_EDITOR_FLAG_WS_LEADING = "idepref_editor_wsLeading";
  public static final String KEY_EDITOR_FLAG_WS_TRAILING = "idepref_editor_wsTrailing";
  public static final String KEY_EDITOR_FLAG_WS_INNER = "idepref_editor_wsInner";
  public static final String KEY_EDITOR_FLAG_WS_EMPTY_LINE = "idepref_editor_wsEmptyLine";
  public static final String KEY_EDITOR_FLAG_LINE_BREAK = "idepref_editor_lineBreak";
  public static final String KEY_EDITOR_DRAW_HEX = "idepref_editor_drawHexColors";
  public static final String KEY_EDITOR_FONT_SIZE = "idepref_editor_fontSize";
  public static final String KEY_EDITOR_PRINTABLE_CHARS = "idepref_editor_nonPrintableFlags";
  public static final String KEY_EDITOR_TAB_SIZE = "idepref_editor_tabSize";
  public static final String KEY_EDITOR_AUTO_SAVE = "idepref_editor_autoSave";
  public static final String KEY_EDITOR_FONT_LIGATURES = "idepref_editor_fontLigatures";
  public static final String KEY_EDITOR_FLAG_PASSWORD = "idepref_editor_flagPassword";
  public static final String KEY_EDITOR_WORD_WRAP = "idepref_editor_word_wrap";
  public static final String KEY_EDITOR_USE_MAGNIFER = "idepref_editor_use_magnifier";
  public static final String KEY_GRADLE_CMD_STACK_TRACE = "idepref_gradleCmd_stacktrace";
  public static final String KEY_GRADLE_CMD_DEBUG = "idepref_gradleCmd_debug";
  public static final String KEY_GRADLE_CMD_SCAN = "idepref_gradleCmd_scan";
  public static final String KEY_GRADLE_CMD_INFO = "idepref_gradleCmd_info";
  public static final String KEY_GRADLE_CMD_WARNING_MODE = "idepref_gradleCmd_warningMode";
  public static final String KEY_GRADLE_CMD_BUILD_CACHE = "idepref_gradleCmd_buildCache";
  public static final String KEY_GRADLE_CMD_OFFLINE_MODE = "idepref_gradleCmd_offlineMode";
  public static final String KEY_LAST_OPENED_PROJECT = "ide_last_project";
  public static final String NO_OPENED_PROJECT = "<NO_OPENED_PROJECT>";
  private final SharedPreferences prefs;

  @SuppressLint("CommitPrefEdits")
  public PreferenceManager(Context ctx) {
    this.prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
  }

  public PreferenceManager putInt(String key, int val) {
    prefs.edit().putInt(key, val).apply();
    return this;
  }

  public int getInt(String key) {
    return prefs.getInt(key, 0);
  }

  public int getInt(String key, int def) {
    return prefs.getInt(key, def);
  }

  public PreferenceManager putFloat(String key, float val) {
    prefs.edit().putFloat(key, val).apply();
    return this;
  }

  public float getFloat(String key) {
    return prefs.getFloat(key, 0f);
  }

  public float getFloat(String key, float def) {
    return prefs.getFloat(key, def);
  }

  public PreferenceManager putBoolean(String key, boolean value) {
    prefs.edit().putBoolean(key, value).apply();
    return this;
  }

  public boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return prefs.getBoolean(key, defaultValue);
  }

  public PreferenceManager putString(String key, String value) {
    prefs.edit().putString(key, value).apply();
    return this;
  }

  public String getString(String key) {
    return prefs.getString(key, null);
  }

  public String getString(String key, String defaultValue) {
    return prefs.getString(key, defaultValue);
  }

  public String getOpenedProject() {
    return getString(KEY_LAST_OPENED_PROJECT, NO_OPENED_PROJECT);
  }

  public PreferenceManager setOpenedProject(String root) {
    return putString(KEY_LAST_OPENED_PROJECT, root);
  }

  public boolean wasProjectOpened() {
    return !getOpenedProject().equals(NO_OPENED_PROJECT);
  }

  public boolean isFrameworkDownloaded() {
    return getBoolean(KEY_FRAMEWORK_DOWNLOADED);
  }

  public boolean isFrameworkInstalled() {
    return isFrameworkDownloaded() && getBoolean(KEY_FRAMEWORK_INSTALLED);
  }

  public boolean isStackTraceEnabled() {
    return getBoolean(KEY_GRADLE_CMD_STACK_TRACE);
  }

  public boolean isGradleInfoEnabled() {
    return getBoolean(KEY_GRADLE_CMD_INFO, true);
  }

  public PreferenceManager setGradleInfoEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_INFO, enabled);
  }

  public boolean isGradleDebugEnabled() {
    return getBoolean(KEY_GRADLE_CMD_DEBUG);
  }

  public PreferenceManager setGradleDebugEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_DEBUG, enabled);
  }

  public boolean isGradleScanEnabled() {
    return getBoolean(KEY_GRADLE_CMD_SCAN);
  }

  public PreferenceManager setGradleScanEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_SCAN, enabled);
  }

  public boolean isGradleWarningEnabled() {
    return getBoolean(KEY_GRADLE_CMD_WARNING_MODE);
  }

  public boolean isGradleBuildCacheEnabled() {
    return getBoolean(KEY_GRADLE_CMD_BUILD_CACHE);
  }

  public boolean isGradleOfflineModeEnabled() {
    return getBoolean(KEY_GRADLE_CMD_OFFLINE_MODE);
  }

  public PreferenceManager setGradleWarningEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_WARNING_MODE, enabled);
  }

  public PreferenceManager setGradleStacktraceEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_STACK_TRACE, enabled);
  }

  public PreferenceManager setGradleBuildCacheEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_BUILD_CACHE, enabled);
  }

  public PreferenceManager setGradleOfflineModeEnabled(boolean enabled) {
    return putBoolean(KEY_GRADLE_CMD_OFFLINE_MODE, enabled);
  }

  public int getEditorTabSize() {
    return getInt(KEY_EDITOR_TAB_SIZE, 4);
  }

  public boolean autoOpenProject() {
    return getBoolean(KEY_OPEN_PROJECTS, true);
  }

  public boolean confirmProjectOpen() {
    return getBoolean(KEY_CONFIRM_PROJECT_OPEN, false);
  }
}
