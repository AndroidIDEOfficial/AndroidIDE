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

package com.itsaky.androidide;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityPreferencesBinding;
import com.itsaky.androidide.fragments.preferences.IDEPreferences;

public class PreferencesActivity extends StudioActivity {

  private ActivityPreferencesBinding binding;
  private IDEPreferences mPref;

  @Override
  protected View bindLayout() {
    binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setTitle(R.string.ide_preferences);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    loadFragment(getPrefsFragment());
  }

  private IDEPreferences getPrefsFragment() {
    return mPref == null ? mPref = new IDEPreferences() : mPref;
  }

  private void loadFragment(Fragment frag) {
    super.loadFragment(frag, binding.fragmentContainer.getId());
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}
