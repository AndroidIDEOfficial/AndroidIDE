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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.AttrValueCompletionAdapter;
import com.itsaky.androidide.databinding.LayoutDimensionAttrEditorBinding;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.TextWatcherAdapter;
import com.itsaky.inflater.values.FrameworkValues;
import com.itsaky.inflater.values.ValuesTableFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/** @author Akash Yadav */
public class DimensionEditor extends BaseValueEditorFragment {

    private static final Logger LOG = Logger.instance("DimensionEditor");
    public TextWatcherAdapter dimensionInputWatcher;
    public TextWatcherAdapter dimensionResInputWatcher;
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

        final var dimensionInput =
                Objects.requireNonNull(this.binding.dimensionInput.getEditText());
        if (dimensionInputWatcher != null) {
            dimensionInput.removeTextChangedListener(dimensionInputWatcher);
        }

        dimensionInputWatcher =
                new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(@NonNull Editable s) {
                        final var text = s.toString().trim();
                        final var unit =
                                getDimensionUnits()[binding.unitSelector.getSelectedItemPosition()];

                        // This will call CommonParseUtils#parseDimension
                        notifyValueChanged(text.concat(unit));
                    }
                };

        dimensionInput.addTextChangedListener(dimensionInputWatcher);

        final var dimensionResInput =
                (MaterialAutoCompleteTextView)
                        Objects.requireNonNull(binding.dimensionResInput.getEditText());

        if (dimensionResInputWatcher != null) {
            dimensionResInput.removeTextChangedListener(dimensionResInputWatcher);
        }

        dimensionResInputWatcher =
                new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(@NonNull Editable s) {
                        final var text = s.toString().trim();
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }

                        notifyValueChanged(text);
                    }
                };

        dimensionResInput.addTextChangedListener(dimensionResInputWatcher);

        final var future =
                CompletableFuture.supplyAsync(
                        () -> {
                            final var list = new ArrayList<String>();
                            final var tables = ValuesTableFactory.getAllTables();
                            for (var entry : tables.entrySet()) {
                                final var dimens = entry.getValue().getTable("dimen");
                                if (dimens != null) {
                                    list.addAll(
                                            dimens.keySet().stream()
                                                    .map("@dimen/"::concat)
                                                    .collect(Collectors.toSet()));
                                }
                            }

                            list.addAll(
                                    FrameworkValues.listDimens().stream()
                                            .map("@android:dimen/"::concat)
                                            .collect(Collectors.toList()));
                            return list;
                        });
        future.whenComplete(
                (list, throwable) -> {
                    if (list == null || list.isEmpty()) {
                        LOG.error("No completion items found");
                        return;
                    }

                    LOG.debug("Found", list.size(), "dimension resources for completion");

                    ThreadUtils.runOnUiThread(
                            () -> {
                                final var adapter =
                                        new AttrValueCompletionAdapter(requireContext(), list);
                                dimensionResInput.setThreshold(1);
                                dimensionResInput.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            });
                });
    }

    @NonNull
    private String[] getDimensionUnits() {
        if (dimensionUnits == null) {
            dimensionUnits =
                    requireContext().getResources().getStringArray(R.array.dimension_units);
        }

        return dimensionUnits;
    }

    @Override
    protected void notifyValueChanged(@NonNull String newValue) {
        try {
            super.notifyValueChanged(newValue);
        } catch (Throwable e) {
            LOG.error("Unable to update dimension value to '" + newValue + "'", e);
        }
    }
}
