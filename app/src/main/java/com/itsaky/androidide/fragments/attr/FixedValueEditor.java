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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.itsaky.androidide.R;

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

    protected Chip newChip(String title, boolean checked) {
        final var chip = new Chip(requireContext());
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
                ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.primaryDarkColor)));

        return chip;
    }
}
