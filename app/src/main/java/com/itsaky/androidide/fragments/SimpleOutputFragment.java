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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleOutputFragment extends NonEditableEditorFragment {

  private final List<String> unsavedLines = new ArrayList<>();

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (!unsavedLines.isEmpty()) {
      for (String line : unsavedLines) {
        Objects.requireNonNull(getEditor()).append(line.trim() + "\n");
      }
      unsavedLines.clear();
    }
  }

  public void appendOutput(String output) {
    if (getEditor() == null) {
      unsavedLines.add(output);
      return;
    }
    ThreadUtils.runOnUiThread(
        () -> {
          final var message = output == null || output.endsWith("\n") ? output : output + "\n";
          getEditor().append(message);
        });
  }
}
