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
package com.itsaky.androidide.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import com.itsaky.androidide.eventbus.events.preferences.PreferenceChangeEvent;
import com.itsaky.androidide.eventbus.events.preferences.PreferenceRemoveEvent;
import kotlin.text.StringsKt;
import org.greenrobot.eventbus.EventBus;

public class PreferenceManager {

  private final SharedPreferences prefs;
  private boolean isReadOnly = false;

  public PreferenceManager(Context ctx) {
    this(ctx, null);
  }

  public PreferenceManager(Context ctx, String preferenceMode) {
    this(ctx, preferenceMode, Context.MODE_PRIVATE);
  }

  @SuppressLint("CommitPrefEdits")
  public PreferenceManager(Context ctx, String preferenceName, int prefMode) {
    if (preferenceName == null || StringsKt.isBlank(preferenceName)) {
      this.prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
    } else {
      this.prefs = ctx.getSharedPreferences(preferenceName, prefMode);
    }
  }

  public PreferenceManager remove(String key) {
    final var edit = prefs.edit();
    edit.remove(key);
    applyChanges(edit);
    dispatchRemoveEvent(key);
    return this;
  }

  protected void dispatchRemoveEvent(final String key) {
    EventBus.getDefault().post(new PreferenceRemoveEvent(key));
  }

  /**
   * @return Whether this preference manager is read-only.
   */
  public boolean isReadOnly() {
    return isReadOnly;
  }

  /**
   * Whether this preference manager is read-only.
   *
   * @param readOnly <code>true</code>, if the preference manager is read-only, <code>false</code>
   *                 otherwise.
   */
  public void setReadOnly(boolean readOnly) {
    isReadOnly = readOnly;
  }

  public PreferenceManager putInt(String key, int val) {
    final var edit = prefs.edit();
    edit.putInt(key, val);
    applyChanges(edit);
    dispatchChangeEvent(key, val);
    return this;
  }

  protected void dispatchChangeEvent(final String key, final Object value) {
    dispatchChangeEvent(new PreferenceChangeEvent(key, value));
  }

  protected void dispatchChangeEvent(final PreferenceChangeEvent event) {
    EventBus.getDefault().post(event);
  }

  public void putFloat(String key, float val) {
    final var edit = prefs.edit();
    edit.putFloat(key, val);
    applyChanges(edit);
    dispatchChangeEvent(key, val);
  }

  public float getFloat(String key) {
    return prefs.getFloat(key, 0f);
  }

  public float getFloat(String key, float def) {
    return prefs.getFloat(key, def);
  }

  public String getString(String key) {
    return prefs.getString(key, null);
  }

  public String getString(String key, String defaultValue) {
    return prefs.getString(key, defaultValue);
  }

  public PreferenceManager putString(String key, String value) {
    final var edit = prefs.edit();
    edit.putString(key, value);
    applyChanges(edit);
    dispatchChangeEvent(key, value);
    return this;
  }

  public boolean getBoolean(String key) {
    return getBoolean(key, false);
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return prefs.getBoolean(key, defaultValue);
  }

  public void putBoolean(String key, boolean value) {
    final var edit = prefs.edit();
    edit.putBoolean(key, value);
    applyChanges(edit);
    dispatchChangeEvent(key, value);
  }

  public long getLong(String key, long defaultValue) {
    return prefs.getLong(key, defaultValue);
  }

  public void putLong(String key, long value) {
    final var edit = prefs.edit();
    edit.putLong(key, value);
    applyChanges(edit);
    dispatchChangeEvent(key, value);
  }

  public int getInt(String key, int def) {
    return prefs.getInt(key, def);
  }

  protected void applyChanges(SharedPreferences.Editor editor) {
    editor.apply();
  }
}
