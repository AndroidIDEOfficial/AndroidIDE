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

package com.itsaky.androidide.utils;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class EditorBottomSheetBehavior<V extends View> extends BottomSheetBehavior<V> {

  private View pager;

  public EditorBottomSheetBehavior(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public void setBinding(View pager) {
    this.pager = pager;
  }

  @Override
  public boolean onInterceptTouchEvent(
      @NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent event) {
    if (pager != null) {
      final var rect = getPagerRect();
      if (rect.contains(event.getX(), event.getY())) {
        return false;
      }
    }
    return super.onInterceptTouchEvent(parent, child, event);
  }

  @NonNull
  private RectF getPagerRect() {
    final var rect = new RectF();
    rect.left = pager.getX();
    rect.top = pager.getY();
    rect.right = rect.left + pager.getWidth();
    rect.bottom = rect.top + pager.getHeight();
    return rect;
  }
}
