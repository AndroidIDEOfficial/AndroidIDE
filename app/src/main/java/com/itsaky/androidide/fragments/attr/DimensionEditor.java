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

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutDimensionAttrEditorBinding;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.TextWatcherAdapter;
import com.itsaky.inflater.values.FrameworkValues;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Akash Yadav
 */
public class DimensionEditor extends AbstractReferenceEditor {

  private static final ILogger LOG = ILogger.newInstance("DimensionEditor");
  public TextWatcherAdapter dimensionInputWatcher;

  private LayoutDimensionAttrEditorBinding binding;
  private String[] dimensionUnits;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutDimensionAttrEditorBinding.inflate(inflater, container, false);
    return this.binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final var dimensionInput = Objects.requireNonNull(this.binding.dimensionInput.getEditText());
    setDimensionValue(dimensionInput);
    if (dimensionInputWatcher != null) {
      dimensionInput.removeTextChangedListener(dimensionInputWatcher);
    }

    dimensionInputWatcher =
        new TextWatcherAdapter() {
          @Override
          public void afterTextChanged(@NonNull Editable s) {
            final var text = s.toString().trim();
            final var unit = getDimensionUnits()[binding.unitSelector.getSelectedItemPosition()];

            // This will call CommonParseUtils#parseDimension
            notifyValueChanged(text.concat(unit));
          }
        };

    dimensionInput.addTextChangedListener(dimensionInputWatcher);

    final var dimensionResInput =
        (MaterialAutoCompleteTextView)
            Objects.requireNonNull(binding.dimensionResInput.getEditText());
    setupReferenceInput(dimensionResInput);
  }

  private void setDimensionValue(EditText dimensionInput) {
    final var val = attribute.getValue();
    if (TextUtils.isEmpty(val)) {
      return;
    }

    var dimension = val.substring(0, val.length() - 2);
    if (TextUtils.isDigitsOnly(dimension)) {
      dimensionInput.setText(dimension);
      final var arr = requireContext().getResources().getStringArray(R.array.dimension_units);
      final var index = Arrays.asList(arr).indexOf(val.substring(dimension.length()));
      if (index >= 0) {
        this.binding.unitSelector.setSelection(index);
      }
    }
  }

  @NonNull
  private String[] getDimensionUnits() {
    if (dimensionUnits == null) {
      dimensionUnits = requireContext().getResources().getStringArray(R.array.dimension_units);
    }

    return dimensionUnits;
  }

  @Override
  protected List<String> computeReferenceItems() {
    final var list = new ArrayList<String>();
    final var tables = ValuesTableFactory.getAllTables();
    for (var entry : tables.entrySet()) {
      final var dimens = entry.getValue().getTable("dimen");
      if (dimens != null) {
        list.addAll(dimens.keySet().stream().map("@dimen/"::concat).collect(Collectors.toSet()));
      }
    }

    list.addAll(
        FrameworkValues.listDimens().stream()
            .map("@android:dimen/"::concat)
            .collect(Collectors.toList()));
    return list;
  }
}
