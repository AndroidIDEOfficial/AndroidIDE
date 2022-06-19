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

package com.itsaky.androidide.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;

import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.FragmentLogViewBinding;
import com.itsaky.androidide.language.logs.LogLanguageImpl;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.androidide.utils.SingleTextWatcher;
import io.github.rosemoe.sora.lang.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LogViewFragment extends Fragment {

  private FragmentLogViewBinding binding;

  private final Language language = getLanguage();
  private final List<LogLine> unsavedLines = new ArrayList<>();

  private char priority;
  private String tag;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentLogViewBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final var editor = getEditor();
    editor.setEditable(false);
    editor.setDividerWidth(0);
    editor.setWordwrap(false);
    editor.setUndoEnabled(false);
    editor.setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
    editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
    editor.setTextSize(12);
    editor.setColorScheme(new SchemeAndroidIDE());
    editor.setEditorLanguage(language);

    final var arr = requireContext().getResources().getStringArray(R.array.logcat_units);
    binding.logPrioritySpinner.setAdapter(
        new ArrayAdapter<>(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            arr));

    binding.logPrioritySpinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            priority = parent.getSelectedItem().toString().charAt(0);
            getEditor().setText("");
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    binding
        .tagFilterInput
        .getEditText()
        .addTextChangedListener(
            new SingleTextWatcher() {
              @Override
              public void afterTextChanged(Editable editable) {
                tag = editable.toString();
                getEditor().setText("");
              }
            });

    if (!unsavedLines.isEmpty()) {
      for (var line : unsavedLines) {
        getEditor().append(line.toString().trim() + "\n");
        applyToLanguage(line);
      }
      unsavedLines.clear();
    }
  }

  private boolean filterLog(char priority, String tag, LogLine l) {
    return 'V' == priority
        || (l.priorityChar == priority)
            && (TextUtils.isEmpty(tag)
                || (!TextUtils.isEmpty(tag) && (l.tag.toLowerCase().contains(tag.toLowerCase()))));
  }

  private void applyToLanguage(LogLine line) {
    if (language instanceof LogLanguageImpl) {
      ((LogLanguageImpl) this.language).addLine(line);
    }
  }

  @Nullable
  public IDEEditor getEditor() {
    if (binding == null) {
      return null;
    }
    return binding.editor;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public void appendLog(LogLine line) {
    if (filterLog(priority, tag, line)) {
      if (getEditor() == null) {
        unsavedLines.add(line);
        return;
      }

      final var lineString = onCreateLogString(line);
      final var msg = lineString.endsWith("\n") ? lineString : lineString + "\n";

      ThreadUtils.runOnUiThread(() -> getEditor().append(msg));
      applyToLanguage(line);
    }
  }

  protected String onCreateLogString(@NonNull LogLine line) {
    return line.toString();
  }

  protected Language getLanguage() {
    return new LogLanguageImpl();
  }
}
