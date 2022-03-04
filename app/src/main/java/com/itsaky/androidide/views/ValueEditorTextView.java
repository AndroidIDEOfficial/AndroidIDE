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

package com.itsaky.androidide.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.itsaky.androidide.ui.edit.IValueSuggestionProvider;
import com.itsaky.androidide.utils.TextWatcherAdapter;
import com.itsaky.inflater.IAttribute;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * {@link MaterialAutoCompleteTextView} implementation used for providing suggestions while editing
 * attribute values.
 *
 * @author Akash Yadav
 */
@SuppressLint("ViewConstructor")
public class ValueEditorTextView extends MaterialAutoCompleteTextView {

  private final IAttribute attribute;
  private final int format;
  private final Set<IValueSuggestionProvider> mSuggestionProviders = new HashSet<>();

  private final TextWatcher watcher =
      new TextWatcherAdapter() {

        @Override
        public void afterTextChanged(@NonNull Editable s) {
          final var prefix = s.toString();
          final var items = new ArrayList<String>();

          for (IValueSuggestionProvider provider : mSuggestionProviders) {
            if (provider.checkFormat(format)) {
              items.addAll(provider.suggest(attribute, prefix));
            }
          }

          setThreshold(1);
          setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items));
        }
      };

  public ValueEditorTextView(@NonNull Context context, IAttribute attribute, int format) {
    super(context);
    this.attribute = attribute;
    this.format = format;

    addTextChangedListener(this.watcher);
  }

  public void registerSuggestionProvider(IValueSuggestionProvider provider) {
    Objects.requireNonNull(provider);
    mSuggestionProviders.add(provider);
  }
}
