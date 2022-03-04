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

package com.itsaky.androidide.ui;

import android.graphics.Point;
import android.view.View;

public class WidgetDragShadowBuilder extends View.DragShadowBuilder {

  private final double TOUCH_Y_OFFSET = 0.3;

  public WidgetDragShadowBuilder(View view) {
    super(view);
  }

  @Override
  public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
    if (getView() != null) {
      final var width = getView().getWidth();
      final var height = getView().getHeight();
      outShadowSize.set(width, height);
      outShadowTouchPoint.set(width / 2, height + (int) (height * TOUCH_Y_OFFSET));
    } else {
      super.onProvideShadowMetrics(outShadowSize, outShadowTouchPoint);
    }
  }
}
