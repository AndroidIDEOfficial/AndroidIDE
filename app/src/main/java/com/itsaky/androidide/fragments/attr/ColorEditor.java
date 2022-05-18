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

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.colorpicker.ColorPickerView;
import com.itsaky.androidide.databinding.LayoutColorAttrEditorBinding;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.inflater.values.FrameworkValues;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Akash Yadav
 */
public class ColorEditor extends AbstractReferenceEditor {

  private LayoutColorAttrEditorBinding binding;
  private Dialog colorPickerDialog;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutColorAttrEditorBinding.inflate(inflater, container, false);
    return this.binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    this.binding.pickColor.setOnClickListener(v -> getColorPickerDialog().show());
    setupReferenceInput((MaterialAutoCompleteTextView) this.binding.colorResInput.getEditText());

    try {
      final var col = Color.parseColor(attribute.getValue());
      this.binding.colorPreview.setCardBackgroundColor(col);
    } catch (Throwable th) {
      // ignored
    }
  }

  @Override
  protected List<String> computeReferenceItems() {
    final var list = new ArrayList<String>();
    final var tables = ValuesTableFactory.getAllTables();
    for (var entry : tables.entrySet()) {
      final var dimens = entry.getValue().getTable("color");
      if (dimens != null) {
        list.addAll(dimens.keySet().stream().map("@color/"::concat).collect(Collectors.toSet()));
      }
    }

    list.addAll(
        FrameworkValues.listColors().stream()
            .map("@android:color/"::concat)
            .collect(Collectors.toList()));

    final var resTable = StudioApp.getInstance().getResourceTable();
    list.addAll(resTable.listResourceNames("color"));

    return list;
  }

  private Dialog getColorPickerDialog() {

    if (colorPickerDialog == null) {
      final var builder = DialogUtils.newMaterialDialogBuilder(requireContext());
      ColorPickerView colorPickerView = new ColorPickerView(requireContext());

      builder.setView(colorPickerView);

      colorPickerView.setOnPickListener(
          (color, hexCode) -> {
            if (colorPickerDialog != null) {
              colorPickerDialog.dismiss();
            }

            this.binding.colorPreview.setCardBackgroundColor(color);
            notifyValueChanged(hexCode);
          });

      colorPickerDialog = builder.create();
    }

    return colorPickerDialog;
  }
}
