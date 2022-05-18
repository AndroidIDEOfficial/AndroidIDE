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
package com.itsaky.androidide.fragments.attr;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itsaky.androidide.R;

import java.util.Objects;

/**
 * @author Akash Yadav
 */
public abstract class FixedValueEditor extends BaseValueEditorFragment {

  protected ChipGroup chipGroup;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.chipGroup = new ChipGroup(inflater.getContext());
    this.chipGroup.setLayoutParams(
        new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    this.chipGroup.setSelectionRequired(true);
    this.chipGroup.setSingleSelection(false);

    return this.chipGroup;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    Objects.requireNonNull(this.attribute);
    Objects.requireNonNull(this.chipGroup);

    this.chipGroup.setSingleSelection(false);
    this.chipGroup.setSelectionRequired(true);

    final var attr = this.attribute.getAttr();
    for (var value : attr.possibleValues) {
      if (TextUtils.isEmpty(value)) {
        continue;
      }

      this.chipGroup.addView(newChip(value, value.equals(this.attribute.getValue())));
    }

    this.chipGroup.setOnCheckedChangeListener(this::onCheckChanged);
  }

  protected abstract void onCheckChanged(@NonNull ChipGroup group, int checkedId);

  protected Chip newChip(String title, boolean checked) {
    final var chip = new Chip(requireContext());
    chip.setId(View.generateViewId());
    chip.setText(title);
    chip.setCheckedIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_ok));
    chip.setChecked(checked);
    chip.setChipBackgroundColor(
        ContextCompat.getColorStateList(requireContext(), R.color.bg_enum_chips));
    chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor));
    chip.setLayoutParams(
        new ChipGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    chip.setChipStrokeWidth(1f);
    chip.setChipStrokeColor(
        ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryDarkColor)));

    return chip;
  }
}
