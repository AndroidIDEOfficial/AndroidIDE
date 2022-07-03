/*
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
 */
package com.itsaky.androidide.fragments.sheets;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.databinding.LayoutSheetBinding;

public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment {

  private static final String KEY_SHADOW_ENABLED = "shadowEnabled";
  private static final String KEY_TITLE_ENABLED = "titleEnabled";
  private static final String KEY_TITLE = "fragTitle";
  protected Dialog mDialog;
  protected boolean shadowEnabled = true;
  protected boolean titleEnabled = true;
  private LayoutSheetBinding binding;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mDialog = super.onCreateDialog(savedInstanceState);
    mDialog.setOnShowListener(p1 -> onShow());
    return mDialog;
  }

  protected void onShow() {}

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = LayoutSheetBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    boolean titleSet = false;
    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(KEY_TITLE_ENABLED)) {
        this.titleEnabled = savedInstanceState.getBoolean(KEY_TITLE_ENABLED);
      }

      if (savedInstanceState.containsKey(KEY_SHADOW_ENABLED)) {
        this.shadowEnabled = savedInstanceState.getBoolean(KEY_SHADOW_ENABLED);
      }

      if (savedInstanceState.containsKey(KEY_TITLE)) {
        setTitle(savedInstanceState.getString(KEY_TITLE));
        titleSet = true;
      }
    }

    bind(binding.container);

    if (!titleSet) {
      binding.title.setText(getTitle());
    }

    binding.title.setOnClickListener(v -> handleTitleClick());

    if (shouldHideTitle()) {
      binding.getRoot().removeView(binding.title);
    }

    if (!shadowEnabled) {
      binding.shadow.setVisibility(View.GONE);
    }
  }

  private void handleTitleClick() {
    if (isCancelable()) {
      dismiss();
    }
  }

  protected boolean shouldHideTitle() {
    return !titleEnabled;
  }

  protected String getTitle() {
    return "";
  }

  public BaseBottomSheetFragment setTitle(int title) {
    if (binding != null) {
      binding.title.setText(title);
    }
    return this;
  }

  public BaseBottomSheetFragment setTitle(String title) {
    if (binding != null) {
      binding.title.setText(title);
    }
    return this;
  }

  protected abstract void bind(LinearLayout container);

  public boolean isShowing() {
    return mDialog != null && mDialog.isShowing();
  }

  public void setShowShadow(boolean enabled) {
    this.shadowEnabled = enabled;
  }

  public void setShowTitle(boolean enabled) {
    this.titleEnabled = enabled;
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(KEY_SHADOW_ENABLED, shadowEnabled);
    outState.putBoolean(KEY_TITLE_ENABLED, titleEnabled);

    if (binding != null) {
      outState.putString(KEY_TITLE, binding.title.getText().toString());
    }
  }
}
