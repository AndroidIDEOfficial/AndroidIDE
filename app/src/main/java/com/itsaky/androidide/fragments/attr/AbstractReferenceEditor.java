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

import android.text.Editable;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.adapters.AttrValueCompletionAdapter;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.TextWatcherAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Akash Yadav
 */
public abstract class AbstractReferenceEditor extends BaseValueEditorFragment {

  private static final ILogger LOG = ILogger.newInstance("ReferenceEditor");
  public TextWatcherAdapter resInputWatcher;

  @Override
  protected void notifyValueChanged(@NonNull String newValue) {
    try {
      super.notifyValueChanged(newValue);
    } catch (Throwable e) {
      LOG.error("Unable to update resource value to '" + newValue + "'", e);
    }
  }

  protected void setupReferenceInput(MaterialAutoCompleteTextView referenceInput) {
    Objects.requireNonNull(referenceInput);

    referenceInput.setText(attribute.getValue());

    if (resInputWatcher != null) {
      referenceInput.removeTextChangedListener(resInputWatcher);
    }

    resInputWatcher =
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

    referenceInput.addTextChangedListener(resInputWatcher);

    CompletableFuture.supplyAsync(this::computeReferenceItems)
        .whenComplete((list, error) -> handleAutoCompleteResult(list, error, referenceInput));
  }

  protected void handleAutoCompleteResult(
      List<String> list, Throwable error, AutoCompleteTextView referenceInput) {
    if (list == null || list.isEmpty()) {
      LOG.error("No completion items found");
      LOG.error("Error was:", error);
      return;
    }

    LOG.debug("Found", list.size(), "resource items for completion");

    ThreadUtils.runOnUiThread(
        () -> {
          final var adapter = new AttrValueCompletionAdapter(requireContext(), list);
          referenceInput.setThreshold(1);
          referenceInput.setAdapter(adapter);
          adapter.notifyDataSetChanged();
        });
  }

  protected List<String> computeReferenceItems() {
    return new ArrayList<>();
  }
}
