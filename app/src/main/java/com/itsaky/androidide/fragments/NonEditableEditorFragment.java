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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itsaky.androidide.databinding.FragmentNonEditableEditorBinding;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.TypefaceUtilsKt;
import com.itsaky.androidide.editor.ui.IDEEditor;

import io.github.rosemoe.sora.lang.EmptyLanguage;

public abstract class NonEditableEditorFragment extends Fragment
    implements ShareableOutputFragment {
  
  private FragmentNonEditableEditorBinding binding;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = FragmentNonEditableEditorBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    final var editor = binding.getRoot();
    editor.setEditable(false);
    editor.setDividerWidth(0);
    editor.setEditorLanguage(new EmptyLanguage());
    editor.setWordwrap(false);
    editor.setUndoEnabled(false);
    editor.setTypefaceLineNumber(TypefaceUtilsKt.jetbrainsMono());
    editor.setTypefaceText(TypefaceUtilsKt.jetbrainsMono());
    editor.setTextSize(12);
    editor.setColorScheme(SchemeAndroidIDE.newInstance(requireContext()));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @NonNull
  @Override
  public String getContent() {
    final var editor = getEditor();
    if (editor == null) {
      return "";
    }

    return editor.getText().toString();
  }

  @Nullable
  public IDEEditor getEditor() {
    if (binding == null) {
      return null;
    }
    return binding.editor;
  }

  @NonNull
  @Override
  public String getFilename() {
    return "build_output";
  }

  @Override
  public void clearOutput() {
    final var editor = getEditor();
    if (editor == null) {
      return;
    }

    // Editing CodeEditor's content is a synchronized operation
    editor.getText().delete(0, editor.getText().length());
  }
}
