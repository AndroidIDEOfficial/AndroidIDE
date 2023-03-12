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

package com.itsaky.androidide.editor.ui;

import static android.view.View.MeasureSpec.AT_MOST;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.blankj.utilcode.util.SizeUtils.dp2px;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.utils.ResourceUtilsKt;
import io.github.rosemoe.sora.widget.base.EditorPopupWindow;

/**
 * An {@link EditorPopupWindow} implementation which shows only a single textview.
 *
 * @author Akash Yadav
 */
public abstract class BaseEditorWindow extends AbstractPopupWindow {

  protected final TextView text;

  /**
   * Create a popup window for editor
   *
   * @param editor The editor
   * @see #FEATURE_SCROLL_AS_CONTENT
   * @see #FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED
   * @see #FEATURE_HIDE_WHEN_FAST_SCROLL
   */
  public BaseEditorWindow(@NonNull IDEEditor editor) {
    super(editor, getFeatureFlags());

    this.text = onCreateTextView(editor);
    setContentView(onCreateContentView(editor.getContext()));
  }

  private static int getFeatureFlags() {
    return FEATURE_SCROLL_AS_CONTENT | FEATURE_SHOW_OUTSIDE_VIEW_ALLOWED;
  }

  protected View onCreateContentView(@NonNull Context context) {
    return this.text;
  }

  protected TextView onCreateTextView(@NonNull IDEEditor editor) {
    final var context = editor.getContext();
    final var dp4 = dp2px(4);
    final var dp8 = dp4 * 2;

    final var text = new TextView(context);
    text.setBackground(createBackground(context));
    text.setTextColor(ResourceUtilsKt.resolveAttr(context, R.attr.colorOnPrimaryContainer));
    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    text.setClickable(false);
    text.setFocusable(false);
    text.setPaddingRelative(dp8, dp4, dp8, dp4);
    text.setLayoutParams(
      new ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    return text;
  }

  protected Drawable createBackground(final Context context) {
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setColor(ResourceUtilsKt.resolveAttr(context, R.attr.colorSurface));
    background.setStroke(dp2px(1f), ResourceUtilsKt.resolveAttr(context, R.attr.colorOutline));
    background.setCornerRadius(8);
    return background;
  }

  public void displayWindow() {
    final var dp16 = dp2px(16f);
    final int width = getEditor().getWidth() - dp16;
    final int height = getEditor().getHeight() - dp16;
    final var widthMeasureSpec = makeMeasureSpec(width, AT_MOST);
    final var heightMeasureSpec = makeMeasureSpec(height, AT_MOST);
    this.getRootView().measure(widthMeasureSpec, heightMeasureSpec);
    this.setSize(this.getRootView().getMeasuredWidth(), this.getRootView().getMeasuredHeight());

    final var line = getEditor().getCursor().getLeftLine();
    final var column = getEditor().getCursor().getLeftColumn();
    int x = (int) ((getEditor().getOffset(line, column) - (getWidth() / 2)));
    int y = (int) (getEditor().getRowHeight() * line) - getEditor().getOffsetY() - getHeight() - 5;
    setLocationAbsolutely(x, y);
    show();
  }

  protected View getRootView() {
    return this.text;
  }

  @NonNull
  @Override
  public IDEEditor getEditor() {
    return (IDEEditor) super.getEditor();
  }
}
