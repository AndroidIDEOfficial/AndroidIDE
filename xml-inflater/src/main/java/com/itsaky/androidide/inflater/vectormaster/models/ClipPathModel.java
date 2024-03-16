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

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import com.itsaky.androidide.inflater.vectormaster.utilities.parser.PathParser;

public class ClipPathModel {
  private String name;
  private String pathData;

  private Path originalPath;
  private Path path;

  private Paint clipPaint;

  public ClipPathModel() {
    path = new Path();

    clipPaint = new Paint();
    clipPaint.setAntiAlias(true);
    clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
  }

  public void buildPath(boolean useLegacyParser) {
    if (useLegacyParser) {
      originalPath =
          com.itsaky.androidide.inflater.vectormaster.utilities.legacyparser.PathParser.createPathFromPathData(
              pathData);
    } else {
      originalPath = PathParser.doPath(pathData);
    }

    path = new Path(originalPath);
  }

  public void transform(Matrix matrix) {
    path = new Path(originalPath);

    path.transform(matrix);
  }

  public Paint getClipPaint() {
    return clipPaint;
  }

  public void setClipPaint(Paint clipPaint) {
    this.clipPaint = clipPaint;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPathData() {
    return pathData;
  }

  public void setPathData(String pathData) {
    this.pathData = pathData;
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
}
