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

package com.itsaky.inflater.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.itsaky.inflater.IView;

import org.jetbrains.annotations.Contract;

/**
 * A view which is used to show views that cannot be inflated. This view will preserve the
 * attributes and the children if the view is a view group;
 *
 * @author Akash Yadav
 */
public class ErrorUiView extends UiViewGroup {

  private ErrorUiView(String qualifiedName, ViewGroup view) {
    super(qualifiedName, view);
  }

  private ErrorUiView(String qualifiedName, ViewGroup view, boolean isPlaceholder) {
    super(qualifiedName, view, isPlaceholder);
  }

  /**
   * Create a new error view for the given qualified name of the view that was not inflated
   * correctly.
   *
   * <p>We add a text directly in this view. Any other views that will be added to this group will
   * have visibility {@link View#GONE} by default.
   *
   * @param context The context that will be used to create the view.
   * @param name The qualified name of the view that was not inflated.
   * @param message The message that should be shown to the user.
   * @return A newly created instance of the error view.
   */
  @NonNull
  @Contract("_, _, _ -> new")
  public static IView create(
      @NonNull final Context context, @NonNull final String name, @NonNull final String message) {
    final var container = new LinearLayout(context);
    final var error = new TextView(context);
    error.setText(message);
    error.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    error.setTextColor(ContextCompat.getColor(context, android.R.color.black));
    error.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

    container.setOrientation(LinearLayout.HORIZONTAL);
    container.addView(
        error,
        new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    return new ErrorUiView(name, container, true);
  }

  @Override
  public void addView(IView view, int index) {
    super.addView(view, index);
    view.asView().setVisibility(View.GONE);
  }

  @Override
  public int getChildCount() {
    return super.getChildCount() - 1;
  }

  @Override
  public String getXmlTag() {
    // This might be a custom view or something
    // So return the qualified name of the view
    return this.qualifiedName;
  }

  @NonNull
  @Override
  public String generateCode(int indentCount) {
    return super.generateCode(indentCount);
  }
}
