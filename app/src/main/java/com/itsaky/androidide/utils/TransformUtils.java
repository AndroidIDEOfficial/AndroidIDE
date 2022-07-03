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
 *
 */
package com.itsaky.androidide.utils;

import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

public class TransformUtils {

  public static MaterialContainerTransform createContainerTransformFor(
      View start, View end, View drawingView) {
    MaterialContainerTransform transform = new MaterialContainerTransform();
    transform.setStartView(start);
    transform.setEndView(end);
    transform.addTarget(end);
    transform.setDrawingViewId(drawingView.getId());
    transform.setAllContainerColors(
        ContextCompat.getColor(
            drawingView.getContext(), com.itsaky.androidide.R.color.primaryDarkColor));
    transform.setElevationShadowEnabled(true);
    transform.setPathMotion(new MaterialArcMotion());
    transform.setScrimColor(Color.TRANSPARENT);
    return transform;
  }
}
