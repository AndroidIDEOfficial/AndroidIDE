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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;

public abstract class BasePreferenceFragment extends PreferenceFragmentCompat {

  protected PreferenceManager mPrefManager;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    v.setBackgroundColor(
        ContextCompat.getColor(
            StudioApp.getInstance(), com.itsaky.androidide.R.color.content_background));
    return v;
  }

  @Override
  public void onCreatePreferences(Bundle p1, String p2) {
    setPreferencesFromResource(com.itsaky.androidide.R.xml.ide_prefs, p2);
  }

  protected PreferenceManager getPrefManager() {
    return mPrefManager == null ? mPrefManager = new PreferenceManager(getContext()) : mPrefManager;
  }
}
