/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.fragments.sheets;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.itsaky.androidide.editor.ui.IDEEditor;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.utils.TypefaceUtilsKt;

import io.github.rosemoe.sora.lang.EmptyLanguage;

public class TextSheetFragment extends BaseBottomSheetFragment {

  private SpannableStringBuilder outputBuilder;
  private IDEEditor editor;

  public TextSheetFragment setTextSelectable(boolean textSelectable) {
    return this;
  }

  public TextSheetFragment setTitleText(int res) {
    super.setTitle(res);
    return this;
  }

  public TextSheetFragment setTitleText(String res) {
    super.setTitle(res);
    return this;
  }

  public void append(String text) {
    append(text, -1, true);
  }

  public void append(String text, int spanColor, boolean appendLine) {
    if (text != null && text.trim().length() > 0) {
      appendInternal(appendLine && !text.endsWith("\n") ? text.concat("\n") : text, spanColor);
    }
  }

  private void appendInternal(String text, int spanColor) {
    SpannableString str = new SpannableString(text);
    str.setSpan(
        new ForegroundColorSpan(spanColor == -1 ? Color.WHITE : spanColor),
        0,
        text.length(),
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
    getOutputBuilder().append(str);
    if (mDialog != null && mDialog.isShowing()) {
      setToEditor(str);
    }
  }

  private void setToEditor(SpannableString line) {
    if (getActivity() != null)
      getActivity()
          .runOnUiThread(
              () -> {
                editor.append(line);
                scrollToBottom();
              });
  }

  private void scrollToBottom() {
    if (getActivity() != null) getActivity().runOnUiThread(() -> editor.goToEnd());
  }

  @Override
  protected String getTitle() {
    return getString(R.string.build_output);
  }

  @Override
  protected void onShow() {
    super.onShow();
    editor.setText(getOutputBuilder());
  }

  @Override
  protected void bind(@NonNull LinearLayout container) {
    final IDEEditor e = getEditor();
    container.setPadding(0, 0, 0, 0);
    container.setPaddingRelative(0, 0, 0, 0);
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, 0);
    p.weight = 1.0f;
    if (e.getParent() != null && e.getParent() instanceof ViewGroup) {
      ((ViewGroup) e.getParent()).removeView(e);
    }
    container.addView(getEditor(), p);
  }

  private IDEEditor getEditor() {
    return editor == null ? createEditor() : editor;
  }

  private IDEEditor createEditor() {
    editor = new IDEEditor(getContext());
    editor.setEditable(false);
    editor.setDividerWidth(0);
    editor.setEditorLanguage(new EmptyLanguage());
    editor.setWordwrap(false);
    editor.setUndoEnabled(false);
    editor.setTypefaceLineNumber(TypefaceUtilsKt.jetbrainsMono());
    editor.setTypefaceText(TypefaceUtilsKt.jetbrainsMono());
    editor.setTextSize(12);
    return editor;
  }

  private SpannableStringBuilder getOutputBuilder() {
    return outputBuilder == null ? outputBuilder = new SpannableStringBuilder() : outputBuilder;
  }
}
