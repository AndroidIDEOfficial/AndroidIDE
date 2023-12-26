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

package com.itsaky.androidide.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.LimitlessIDEActivity;
import com.itsaky.androidide.databinding.ActivityPreferencesBinding;
import com.itsaky.androidide.fragments.IDEPreferencesFragment;
import com.itsaky.androidide.preferences.IDEPreferences;
import com.itsaky.androidide.preferences.RootPrefExtsKt;
import java.util.ArrayList;

public class PreferencesActivity extends LimitlessIDEActivity {

  private ActivityPreferencesBinding binding;
  private IDEPreferencesFragment rootFragment;

  public PreferencesActivity() {
    super(true);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setSupportActionBar(binding.toolbar);
    getSupportActionBar().setTitle(R.string.ide_preferences);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());

    if (savedInstanceState != null) {
      return;
    }

    final var prefs = IDEPreferences.INSTANCE;
    prefs.getChildren().clear();

    RootPrefExtsKt.addRootPreferences(prefs);

    final var args = new Bundle();
    args.putParcelableArrayList(
        IDEPreferencesFragment.EXTRA_CHILDREN,
        new ArrayList<>(prefs.getChildren())
    );

    final var root = getRootFragment();
    root.setArguments(args);
    loadFragment(root);
  }

  @Override
  public void onInsetsUpdated(@NonNull Rect insets) {
    super.onInsetsUpdated(insets);

    View toolbar = binding.toolbar;
    toolbar.setPadding(
        toolbar.getPaddingLeft() + insets.left,
        toolbar.getPaddingTop(),
        toolbar.getPaddingRight() + insets.right,
        toolbar.getPaddingBottom()
    );

    View fragmentContainer = binding.fragmentContainerParent;
    fragmentContainer.setPadding(
        fragmentContainer.getPaddingLeft() + insets.left,
        fragmentContainer.getPaddingTop(),
        fragmentContainer.getPaddingRight() + insets.right,
        fragmentContainer.getPaddingBottom()
    );
  }

  @Override
  @NonNull
  protected View bindLayout() {
    binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  private IDEPreferencesFragment getRootFragment() {
    return rootFragment == null ? rootFragment = new IDEPreferencesFragment() : rootFragment;
  }

  private void loadFragment(Fragment fragment) {
    super.loadFragment(fragment, binding.fragmentContainer.getId());
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
