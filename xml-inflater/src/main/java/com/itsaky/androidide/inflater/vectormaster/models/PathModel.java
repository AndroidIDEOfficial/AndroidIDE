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

package com.itsaky.androidide.inflater.vectormaster.models;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import com.itsaky.androidide.inflater.vectormaster.DefaultValues;
import com.itsaky.androidide.inflater.vectormaster.utilities.Utils;
import com.itsaky.androidide.inflater.vectormaster.utilities.parser.PathParser;

public class PathModel {

  private String name;

  private float fillAlpha;
  private int fillColor;

  private Path.FillType fillType;

  private String pathData;

  private float trimPathStart, trimPathEnd, trimPathOffset;

  private float strokeAlpha;
  private int strokeColor;
  private Paint.Cap strokeLineCap;
  private Paint.Join strokeLineJoin;
  private float strokeMiterLimit;
  private float strokeWidth;

  private float strokeRatio;

  private boolean isFillAndStroke = false;

  // Support for trim-paths is not available

  private Path originalPath;
  private Path path;
  private Path trimmedPath;
  private Paint pathPaint;

  private Matrix scaleMatrix;

  public PathModel() {
    fillAlpha = DefaultValues.PATH_FILL_ALPHA;
    fillColor = DefaultValues.PATH_FILL_COLOR;
    fillType = DefaultValues.PATH_FILL_TYPE;
    trimPathStart = DefaultValues.PATH_TRIM_PATH_START;
    trimPathEnd = DefaultValues.PATH_TRIM_PATH_END;
    trimPathOffset = DefaultValues.PATH_TRIM_PATH_OFFSET;
    strokeAlpha = DefaultValues.PATH_STROKE_ALPHA;
    strokeColor = DefaultValues.PATH_STROKE_COLOR;
    strokeLineCap = DefaultValues.PATH_STROKE_LINE_CAP;
    strokeLineJoin = DefaultValues.PATH_STROKE_LINE_JOIN;
    strokeMiterLimit = DefaultValues.PATH_STROKE_MITER_LIMIT;
    strokeWidth = DefaultValues.PATH_STROKE_WIDTH;
    strokeRatio = DefaultValues.PATH_STROKE_RATIO;

    pathPaint = new Paint();
    pathPaint.setAntiAlias(true);
    updatePaint();
  }

  public void updatePaint() {
    pathPaint.setStrokeWidth(strokeWidth * strokeRatio);

    if (fillColor != Color.TRANSPARENT && strokeColor != Color.TRANSPARENT) {
      isFillAndStroke = true;
    } else if (fillColor != Color.TRANSPARENT) {
      pathPaint.setColor(fillColor);
      pathPaint.setAlpha(Utils.getAlphaFromFloat(fillAlpha));
      pathPaint.setStyle(Paint.Style.FILL);
      isFillAndStroke = false;
    } else if (strokeColor != Color.TRANSPARENT) {
      pathPaint.setColor(strokeColor);
      pathPaint.setAlpha(Utils.getAlphaFromFloat(strokeAlpha));
      pathPaint.setStyle(Paint.Style.STROKE);
      isFillAndStroke = false;
    } else {
      pathPaint.setColor(Color.TRANSPARENT);
    }

    pathPaint.setStrokeCap(strokeLineCap);
    pathPaint.setStrokeJoin(strokeLineJoin);
    pathPaint.setStrokeMiter(strokeMiterLimit);
  }

  public void buildPath(boolean useLegacyParser) {
    if (useLegacyParser) {
      originalPath =
          com.itsaky.androidide.inflater.vectormaster.utilities.legacyparser.PathParser.createPathFromPathData(
              pathData);
    } else {
      originalPath = PathParser.doPath(pathData);
    }
    if (originalPath != null) originalPath.setFillType(fillType);

    path = new Path(originalPath);
  }

  public void makeStrokePaint() {
    pathPaint.setColor(strokeColor);
    pathPaint.setAlpha(Utils.getAlphaFromFloat(strokeAlpha));
    pathPaint.setStyle(Paint.Style.STROKE);
  }

  public void makeFillPaint() {
    pathPaint.setColor(fillColor);
    pathPaint.setAlpha(Utils.getAlphaFromFloat(fillAlpha));
    pathPaint.setStyle(Paint.Style.FILL);
  }

  public void transform(Matrix matrix) {
    scaleMatrix = matrix;

    trimPath();
  }

  public void trimPath() {
    if (scaleMatrix != null) {
      if (trimPathStart == 0 && trimPathEnd == 1 && trimPathOffset == 0) {
        path = new Path(originalPath);
        path.transform(scaleMatrix);
      } else {
        PathMeasure pathMeasure = new PathMeasure(originalPath, false);
        float length = pathMeasure.getLength();
        trimmedPath = new Path();
        pathMeasure.getSegment(
            (trimPathStart + trimPathOffset) * length,
            (trimPathEnd + trimPathOffset) * length,
            trimmedPath,
            true);
        path = new Path(trimmedPath);
        path.transform(scaleMatrix);
      }
    }
  }

  public Path getTrimmedPath() {
    return trimmedPath;
  }

  public void setTrimmedPath(Path trimmedPath) {
    this.trimmedPath = trimmedPath;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public Path getScaledAndOffsetPath(float offsetX, float offsetY, float scaleX, float scaleY) {
    Path newPath = new Path(path);
    newPath.offset(offsetX, offsetY);
    newPath.transform(getScaleMatrix(newPath, scaleX, scaleY));
    return newPath;
  }

  public Matrix getScaleMatrix(Path srcPath, float scaleX, float scaleY) {
    Matrix scaleMatrix = new Matrix();
    RectF rectF = new RectF();
    srcPath.computeBounds(rectF, true);
    scaleMatrix.setScale(scaleX, scaleY, rectF.left, rectF.top);
    return scaleMatrix;
  }

  public Paint getPathPaint() {
    return pathPaint;
  }

  public void setPathPaint(Paint pathPaint) {
    this.pathPaint = pathPaint;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getFillAlpha() {
    return fillAlpha;
  }

  public void setFillAlpha(float fillAlpha) {
    this.fillAlpha = fillAlpha;
    updatePaint();
  }

  public int getFillColor() {
    return fillColor;
  }

  public void setFillColor(int fillColor) {
    this.fillColor = fillColor;
    updatePaint();
  }

  public Path.FillType getFillType() {
    return fillType;
  }

  public void setFillType(Path.FillType fillType) {
    this.fillType = fillType;
    if (originalPath != null) originalPath.setFillType(fillType);
  }

  public String getPathData() {
    return pathData;
  }

  public void setPathData(String pathData) {
    this.pathData = pathData;
  }

  public float getTrimPathStart() {
    return trimPathStart;
  }

  public void setTrimPathStart(float trimPathStart) {
    this.trimPathStart = trimPathStart;
    trimPath();
  }

  public float getTrimPathEnd() {
    return trimPathEnd;
  }

  public void setTrimPathEnd(float trimPathEnd) {
    this.trimPathEnd = trimPathEnd;
    trimPath();
  }

  public float getTrimPathOffset() {
    return trimPathOffset;
  }

  public void setTrimPathOffset(float trimPathOffset) {
    this.trimPathOffset = trimPathOffset;
    trimPath();
  }

  public float getStrokeAlpha() {
    return strokeAlpha;
  }

  public void setStrokeAlpha(float strokeAlpha) {
    this.strokeAlpha = strokeAlpha;
    updatePaint();
  }

  public int getStrokeColor() {
    return strokeColor;
  }

  public void setStrokeColor(int strokeColor) {
    this.strokeColor = strokeColor;
    updatePaint();
  }

  public Paint.Cap getStrokeLineCap() {
    return strokeLineCap;
  }

  public void setStrokeLineCap(Paint.Cap strokeLineCap) {
    this.strokeLineCap = strokeLineCap;
    updatePaint();
  }

  public Paint.Join getStrokeLineJoin() {
    return strokeLineJoin;
  }

  public void setStrokeLineJoin(Paint.Join strokeLineJoin) {
    this.strokeLineJoin = strokeLineJoin;
    updatePaint();
  }

  public float getStrokeMiterLimit() {
    return strokeMiterLimit;
  }

  public void setStrokeMiterLimit(float strokeMiterLimit) {
    this.strokeMiterLimit = strokeMiterLimit;
    updatePaint();
  }

  public float getStrokeWidth() {
    return strokeWidth;
  }

  public void setStrokeWidth(float strokeWidth) {
    this.strokeWidth = strokeWidth;
    updatePaint();
  }

  public float getStrokeRatio() {
    return strokeRatio;
  }

  public void setStrokeRatio(float strokeRatio) {
    this.strokeRatio = strokeRatio;
    updatePaint();
  }

  public boolean isFillAndStroke() {
    return isFillAndStroke;
  }
}
