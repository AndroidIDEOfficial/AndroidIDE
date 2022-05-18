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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.databinding.LayoutStringAttrEditorBinding;
import com.itsaky.androidide.utils.TextWatcherAdapter;
import com.itsaky.inflater.values.FrameworkValues;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Akash Yadav
 */
public class StringEditor extends AbstractReferenceEditor {

  private LayoutStringAttrEditorBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutStringAttrEditorBinding.inflate(inflater, container, false);
    return this.binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final var stringInput = Objects.requireNonNull(this.binding.stringInput.getEditText());
    stringInput.addTextChangedListener(
        new TextWatcherAdapter() {
          @Override
          public void afterTextChanged(@NonNull Editable s) {
            notifyValueChanged(s.toString());
          }
        });

    stringInput.setText(this.attribute.getValue());
    setupReferenceInput((MaterialAutoCompleteTextView) this.binding.stringResInput.getEditText());
  }

  @Override
  protected List<String> computeReferenceItems() {
    final var list = new ArrayList<String>();
    final var tables = ValuesTableFactory.getAllTables();
    for (var entry : tables.entrySet()) {
      final var dimens = entry.getValue().getTable("string");
      if (dimens != null) {
        list.addAll(dimens.keySet().stream().map("@string/"::concat).collect(Collectors.toSet()));
      }
    }

    list.addAll(
        FrameworkValues.listStrings().stream()
            .map("@android:string/"::concat)
            .collect(Collectors.toList()));
    return list;
  }
}
