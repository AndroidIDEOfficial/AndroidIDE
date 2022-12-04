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
 * A Class that takes an angle, start point and end point that it uses to calculate controls points
 * which can be used to describe a curve between the two points that approximates an arc on a
 * circle.
 */
public class CubicBezierArc {

  private Point startPoint;
  private Point endPoint;
  private Point controlPoint1;
  private Point controlPoint2;
  private Point reflectedControlPoint1;
  private Point reflectedControlPoint2;

  /**
   * Create a CubicBezierArc class with arc angle and start and end points
   *
   * @param angle The angle used to describe the arc, the greater the angle the more curved the arc
   *     will be (min = 1 , max = 179)
   * @param startX the x coord of the start point
   * @param startY the y coord of the start point
   * @param endX the x coord of the end point
   * @param endY the y coord of the end point
   */
  public CubicBezierArc(float angle, float startX, float startY, float endX, float endY) {

    Point start = new Point(startX, startY);
    Point end = new Point(endX, endY);

    if (start.x == end.x && start.y == end.y) {
      throw new IllegalArgumentException("Start and end points cannot be the same");
    }
    if (angle < 1 || angle > 179) {
      throw new IllegalArgumentException("Arc angle must be between 1 and 179 degrees");
    }
    startPoint = start;
    endPoint = end;
    calculateControlPoints(angle, start, end);
  }

  private void calculateControlPoints(float angle, Point start, Point end) {
    double angleRadians = Math.toRadians(angle);

    float deltaX = start.x - end.x;
    float deltaY = start.y - end.y;
    float halfChordLength = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY) / 2;
    float radius = halfChordLength / (float) Math.sin(angleRadians / 2.0f);
    // The length of the line from the start or end point to the control point
    float controlLength = (float) ((4f / 3f) * Math.tan(angleRadians / 4)) * radius;

    float angleToControl = (float) Math.toDegrees(Math.atan(controlLength / radius));

    // The mid point of the line from start to end
    Point midPointChord = new Point((start.x + end.x) / 2, (start.y + end.y) / 2);

    // Angle between line from circle centre to control point, and line between control points
    float chordRadiusAngle = 180 - 90 - (angle / 2);

    Point center = getTrianglePoint(chordRadiusAngle, midPointChord, end);
    controlPoint2 = getTrianglePoint(angleToControl, end, center);
    controlPoint1 = getReflectedPointAboutLine(center, midPointChord, controlPoint2);

    // Get reflected control points
    reflectedControlPoint1 = getReflectedPointAboutLine(start, end, controlPoint1);
    reflectedControlPoint2 = getReflectedPointAboutLine(start, end, controlPoint2);
  }

  private Point getTrianglePoint(float angle, Point a, Point b) {
    double angleRadians = Math.toRadians(angle);
    Point thirdPoint = new Point();
    thirdPoint.x = (float) Math.tan(angleRadians) * (b.y - a.y) * -1;
    thirdPoint.y = (float) Math.tan(angleRadians) * (b.x - a.x);
    thirdPoint.x = thirdPoint.x + a.x;
    thirdPoint.y = thirdPoint.y + a.y;
    return thirdPoint;
  }

  private Point getReflectedPointAboutLine(Point start, Point end, Point reflect) {
    Point reflectedPoint = new Point();
    if (start.x != end.x) {
      float m = (start.y - end.y) / (start.x - end.x);
      float c = end.y - (m * end.x);
      float d = (reflect.x + (reflect.y - c) * m) / (1 + (m * m));
      reflectedPoint.x = (2 * d) - reflect.x;
      reflectedPoint.y = (2 * d * m) - reflect.y + (2 * c);
    } else {
      reflectedPoint.y = reflect.y;
      reflectedPoint.x = start.x - (reflect.x - start.x);
    }
    return reflectedPoint;
  }

  /**
   * Returns the start point
   *
   * @return The start point
   */
  public Point getStartPoint() {
    return startPoint;
  }

  /**
   * Returns the end point
   *
   * @return The end point
   */
  public Point getEndPoint() {
    return endPoint;
  }

  /**
   * Returns the first control point
   *
   * @return The first control point
   */
  public Point getControlPoint1() {
    return controlPoint1;
  }

  /**
   * Returns the second control point
   *
   * @return The second control point
   */
  public Point getControlPoint2() {
    return controlPoint2;
  }

  /**
   * Returns the first control point when reflected about line between start and end points
   *
   * @return Reflected control point 1
   */
  public Point getReflectedControlPoint1() {
    return reflectedControlPoint1;
  }

  /**
   * Returns the second control point when reflected about line between start and end points
   *
   * @return Reflected control point 2
   */
  public Point getReflectedControlPoint2() {
    return reflectedControlPoint2;
  }

  public static class Point {
    public float x;
    public float y;

    public Point() {}

    public Point(float x, float y) {
      this.x = x;
      this.y = y;
    }
  }
}
