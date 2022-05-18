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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutReferenceAttrEditorBinding;
import com.itsaky.inflater.values.FrameworkValues;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Akash Yadav
 */
public class ReferenceEditor extends AbstractReferenceEditor {

  private LayoutReferenceAttrEditorBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutReferenceAttrEditorBinding.inflate(inflater, container, false);
    return this.binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setupReferenceInput((MaterialAutoCompleteTextView) this.binding.resInput.getEditText());
  }

  @Override
  protected List<String> computeReferenceItems() {
    final var resTable = StudioApp.getInstance().getResourceTable();
    final var list = new ArrayList<>(resTable.listResourceNames(null));

    final var tables = ValuesTableFactory.getAllTables();
    for (var entry : tables.entrySet()) {
      final var resourceMap = entry.getValue().getResourceMap();
      for (var resourceEntries : resourceMap.entrySet()) {
        final var name = resourceEntries.getKey();
        list.addAll(
            resourceEntries.getValue().keySet().stream()
                .map(("@android:" + name + "/")::concat)
                .collect(Collectors.toList()));
      }
    }

    list.addAll(FrameworkValues.listAllResources());

    return list;
  }
}
