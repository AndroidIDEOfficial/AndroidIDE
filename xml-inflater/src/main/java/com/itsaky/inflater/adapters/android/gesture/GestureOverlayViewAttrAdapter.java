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
package com.itsaky.inflater.adapters.android.gesture;

import android.gesture.GestureOverlayView;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ReflectUtils;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IResourceTable;
import com.itsaky.inflater.adapters.android.widget.FrameLayoutAttrAdapter;

/**
 * Attribute adapter for {@link GestureOverlayView}
 *
 * @author Akash Yadav
 */
public class GestureOverlayViewAttrAdapter extends FrameLayoutAttrAdapter {

  public GestureOverlayViewAttrAdapter(
      @NonNull IResourceTable resourceFinder, DisplayMetrics displayMetrics) {
    super(resourceFinder, displayMetrics);
  }

  @Override
  public boolean isApplicableTo(View view) {
    return view instanceof GestureOverlayView;
  }

  @Override
  public boolean apply(@NonNull IAttribute attribute, @NonNull View view) {
    final var gesture = (GestureOverlayView) view;
    final var name = attribute.getAttributeName();
    final var value = attribute.getValue();

    if (!canHandleNamespace(attribute)) {
      return false;
    }

    boolean handled = true;
    switch (name) {
      case "eventsInterceptionEnabled":
        gesture.setEventsInterceptionEnabled(parseBoolean(value));
        break;
      case "fadeDuration":
        ReflectUtils.reflect(gesture).field("mFadeDuration", parseLong(value, 150));
        break;
      case "fadeEnabled":
        gesture.setFadeEnabled(parseBoolean(value));
        break;
      case "fadeOffset":
        gesture.setFadeOffset(parseLong(value, 420));
        break;
      case "gestureColor":
        gesture.setGestureColor(parseColor(value, gesture.getContext()));
        break;
      case "gestureStrokeAngleThreshold":
        gesture.setGestureStrokeAngleThreshold(parseFloat(value));
        break;
      case "gestureStrokeLengthThreshold":
        gesture.setGestureStrokeLengthThreshold(parseFloat(value));
        break;
      case "gestureStrokeSquarenessThreshold":
        gesture.setGestureStrokeSquarenessTreshold(parseFloat(value));
        break;
      case "gestureStrokeType":
        gesture.setGestureStrokeType(parseGestureStrokeType(value));
        break;
      case "gestureStrokeWidth":
        gesture.setGestureStrokeWidth(parseFloat(value));
        break;
      case "orientation":
        gesture.setOrientation(parseOrientation(value));
        break;
      case "uncertainGestureColor":
        gesture.setUncertainGestureColor(parseColor(value, gesture.getContext()));
        break;
      default:
        handled = false;
        break;
    }

    if (!handled) {
      handled = super.apply(attribute, view);
    }

    return handled;
  }

  private int parseOrientation(String value) {
    if ("horizontal".equals(value)) {
      return GestureOverlayView.ORIENTATION_HORIZONTAL;
    }

    return GestureOverlayView.ORIENTATION_VERTICAL;
  }

  protected int parseGestureStrokeType(String value) {
    if ("multiple".equals(value)) {
      return GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE;
    }
    return GestureOverlayView.GESTURE_STROKE_TYPE_SINGLE;
  }
}
