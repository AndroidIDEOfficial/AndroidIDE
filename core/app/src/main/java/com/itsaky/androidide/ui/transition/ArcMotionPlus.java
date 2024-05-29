/*
 * Copyright (C) 2016 Neil Davies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itsaky.androidide.ui.transition;

/**
 * A PathMotion that generates a curved path along an arc on an imaginary circle containing the two
 * points. The two points reside on the circle and a line between them describes a chord for the
 * circle that is symmetrical. i.e the angle between the chord and the tangent to each point will be
 * the same.
 *
 * <p>A perpendicular line from the midpoint of the line between the two points will intersect the
 * center of the circle. Everything about the line from the center of the circle to the midpoint of
 * the chord is symmetrical.
 *
 * <p>This may be used in XML as an element inside a transition.
 *
 * <pre>{@code
 * <changeBounds>
 *   <pathMotion class="com.oeri.arcmotionplus.ArcMotionPlus"
 *              app:arcAngle="90"
 *              app:reflect="true"
 *              />
 * </changeBounds>
 * }</pre>
 */

import android.content.Context;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.transition.PathMotion;

public class ArcMotionPlus extends PathMotion {

  private static final float DEFAULT_ARC_ANGLE = 90f;
  private static final boolean DEFAULT_REFLECT = false;

  private boolean isReflectedArc = DEFAULT_REFLECT;
  private float arcAngle = DEFAULT_ARC_ANGLE;

  public ArcMotionPlus() {}

  public ArcMotionPlus(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  /**
   * Returns if we are reflecting the arc
   *
   * <p>The default value is false.
   *
   * @return Is the arc reflected
   */
  public boolean isReflectedArc() {
    return isReflectedArc;
  }

  /**
   * Sets whether we should reflect the arc about the line drawn between the start and end points
   *
   * <p>The default value is false.
   *
   * @param isReflectedArc Is the arc reflected about the line between start and end points
   */
  public void setReflectedArc(boolean isReflectedArc) {
    this.isReflectedArc = isReflectedArc;
  }

  /**
   * Returns the arc angle that describes the curve. The larger the angle the more pronounce the
   * curve will be. Min value is 1, Max value si 179
   *
   * <p>The default value is 90.
   *
   * @return The angle of the arc on a circle describing the Path between two points.
   */
  public float getArcAngle() {
    return arcAngle;
  }

  /**
   * Sets the arc angle that describes the curve. The larger the angle the more pronounce the curve
   * will be. Min value is 1, Max value si 179
   *
   * <p>The default value is 90.
   *
   * @param angle The angle of the arc on a circle describing the Path between two points.
   */
  public ArcMotionPlus setArcAngle(float angle) {
    arcAngle = angle;
    return this;
  }

  @Override
  public Path getPath(float startX, float startY, float endX, float endY) {
    float controlP1X, controlP1Y, controlP2X, controlP2Y;

    Path path = new Path();
    path.moveTo(startX, startY);

    CubicBezierArc cubicBezierArc = new CubicBezierArc(arcAngle, startX, startY, endX, endY);

    if (isReflectedArc) {
      controlP1X = cubicBezierArc.getReflectedControlPoint1().x;
      controlP1Y = cubicBezierArc.getReflectedControlPoint1().y;
      controlP2X = cubicBezierArc.getReflectedControlPoint2().x;
      controlP2Y = cubicBezierArc.getReflectedControlPoint2().y;

    } else {
      controlP1X = cubicBezierArc.getControlPoint1().x;
      controlP1Y = cubicBezierArc.getControlPoint1().y;
      controlP2X = cubicBezierArc.getControlPoint2().x;
      controlP2Y = cubicBezierArc.getControlPoint2().y;
    }

    path.cubicTo(controlP1X, controlP1Y, controlP2X, controlP2Y, endX, endY);
    return path;
  }
}
