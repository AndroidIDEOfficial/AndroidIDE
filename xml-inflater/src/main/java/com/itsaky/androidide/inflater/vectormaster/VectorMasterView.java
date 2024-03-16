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

package com.itsaky.androidide.inflater.vectormaster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.itsaky.androidide.inflater.vectormaster.models.ClipPathModel;
import com.itsaky.androidide.inflater.vectormaster.models.GroupModel;
import com.itsaky.androidide.inflater.vectormaster.models.PathModel;
import com.itsaky.androidide.inflater.vectormaster.models.VectorModel;
import com.itsaky.androidide.inflater.vectormaster.utilities.Utils;
import com.itsaky.androidide.resources.R;
import java.io.IOException;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class VectorMasterView extends View {

  private Matrix scaleMatrix;
  private float scaleRatio, strokeRatio;
  VectorModel vectorModel;
  Context context;
  Resources resources;
  int resID = -1;
  boolean useLegacyParser = true;
  XmlPullParser xpp;
  int width = 0, height = 0;

  public VectorMasterView(Context context) {
    super(context);
    this.context = context;
  }

  public VectorMasterView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    init(attrs);
  }

  void init(AttributeSet attrs) {
    resources = context.getResources();

    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VectorMasterView);
    final int N = a.getIndexCount();
    for (int i = 0; i < N; ++i) {
      int attr = a.getIndex(i);
      if (attr == R.styleable.VectorMasterView_vector_src) {
        resID = a.getResourceId(attr, -1);
      } else if (attr == R.styleable.VectorMasterView_use_legacy_parser) {
        useLegacyParser = a.getBoolean(attr, false);
      }
    }
    a.recycle();

    buildVectorModel();
  }

  void buildVectorModel() {

    if (resID == -1) {
      vectorModel = null;
      return;
    }

    xpp = resources.getXml(resID);

    int tempPosition;
    PathModel pathModel = new PathModel();
    vectorModel = new VectorModel();
    GroupModel groupModel = new GroupModel();
    ClipPathModel clipPathModel = new ClipPathModel();
    Stack<GroupModel> groupModelStack = new Stack<>();

    try {
      int event = xpp.getEventType();
      while (event != XmlPullParser.END_DOCUMENT) {
        String name = xpp.getName();
        switch (event) {
          case XmlPullParser.START_TAG:
            if (name.equals("vector")) {
              tempPosition = getAttrPosition(xpp, "viewportWidth");
              vectorModel.setViewportWidth(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.VECTOR_VIEWPORT_WIDTH);

              tempPosition = getAttrPosition(xpp, "viewportHeight");
              vectorModel.setViewportHeight(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.VECTOR_VIEWPORT_HEIGHT);

              tempPosition = getAttrPosition(xpp, "alpha");
              vectorModel.setAlpha(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.VECTOR_ALPHA);

              tempPosition = getAttrPosition(xpp, "name");
              vectorModel.setName(
                  (tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              tempPosition = getAttrPosition(xpp, "width");
              vectorModel.setWidth(
                  (tempPosition != -1)
                      ? Utils.getFloatFromDimensionString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.VECTOR_WIDTH);

              tempPosition = getAttrPosition(xpp, "height");
              vectorModel.setHeight(
                  (tempPosition != -1)
                      ? Utils.getFloatFromDimensionString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.VECTOR_HEIGHT);
            } else if (name.equals("path")) {
              pathModel = new PathModel();

              tempPosition = getAttrPosition(xpp, "name");
              pathModel.setName((tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              tempPosition = getAttrPosition(xpp, "fillAlpha");
              pathModel.setFillAlpha(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_FILL_ALPHA);

              tempPosition = getAttrPosition(xpp, "fillColor");
              pathModel.setFillColor(
                  (tempPosition != -1)
                      ? Utils.getColorFromString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_FILL_COLOR);

              tempPosition = getAttrPosition(xpp, "fillType");
              pathModel.setFillType(
                  (tempPosition != -1)
                      ? Utils.getFillTypeFromString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_FILL_TYPE);

              tempPosition = getAttrPosition(xpp, "pathData");
              pathModel.setPathData(
                  (tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              tempPosition = getAttrPosition(xpp, "strokeAlpha");
              pathModel.setStrokeAlpha(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_ALPHA);

              tempPosition = getAttrPosition(xpp, "strokeColor");
              pathModel.setStrokeColor(
                  (tempPosition != -1)
                      ? Utils.getColorFromString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_COLOR);

              tempPosition = getAttrPosition(xpp, "strokeLineCap");
              pathModel.setStrokeLineCap(
                  (tempPosition != -1)
                      ? Utils.getLineCapFromString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_LINE_CAP);

              tempPosition = getAttrPosition(xpp, "strokeLineJoin");
              pathModel.setStrokeLineJoin(
                  (tempPosition != -1)
                      ? Utils.getLineJoinFromString(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_LINE_JOIN);

              tempPosition = getAttrPosition(xpp, "strokeMiterLimit");
              pathModel.setStrokeMiterLimit(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_MITER_LIMIT);

              tempPosition = getAttrPosition(xpp, "strokeWidth");
              pathModel.setStrokeWidth(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_STROKE_WIDTH);

              tempPosition = getAttrPosition(xpp, "trimPathEnd");
              pathModel.setTrimPathEnd(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_TRIM_PATH_END);

              tempPosition = getAttrPosition(xpp, "trimPathOffset");
              pathModel.setTrimPathOffset(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_TRIM_PATH_OFFSET);

              tempPosition = getAttrPosition(xpp, "trimPathStart");
              pathModel.setTrimPathStart(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.PATH_TRIM_PATH_START);

              pathModel.buildPath(useLegacyParser);
            } else if (name.equals("group")) {
              groupModel = new GroupModel();

              tempPosition = getAttrPosition(xpp, "name");
              groupModel.setName((tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              tempPosition = getAttrPosition(xpp, "pivotX");
              groupModel.setPivotX(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_PIVOT_X);

              tempPosition = getAttrPosition(xpp, "pivotY");
              groupModel.setPivotY(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_PIVOT_Y);

              tempPosition = getAttrPosition(xpp, "rotation");
              groupModel.setRotation(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_ROTATION);

              tempPosition = getAttrPosition(xpp, "scaleX");
              groupModel.setScaleX(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_SCALE_X);

              tempPosition = getAttrPosition(xpp, "scaleY");
              groupModel.setScaleY(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_SCALE_Y);

              tempPosition = getAttrPosition(xpp, "translateX");
              groupModel.setTranslateX(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_TRANSLATE_X);

              tempPosition = getAttrPosition(xpp, "translateY");
              groupModel.setTranslateY(
                  (tempPosition != -1)
                      ? Float.parseFloat(xpp.getAttributeValue(tempPosition))
                      : DefaultValues.GROUP_TRANSLATE_Y);

              groupModelStack.push(groupModel);
            } else if (name.equals("clip-path")) {
              clipPathModel = new ClipPathModel();

              tempPosition = getAttrPosition(xpp, "name");
              clipPathModel.setName(
                  (tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              tempPosition = getAttrPosition(xpp, "pathData");
              clipPathModel.setPathData(
                  (tempPosition != -1) ? xpp.getAttributeValue(tempPosition) : null);

              clipPathModel.buildPath(useLegacyParser);
            }
            break;

          case XmlPullParser.END_TAG:
            if (name.equals("path")) {
              if (groupModelStack.size() == 0) {
                vectorModel.addPathModel(pathModel);
              } else {
                groupModelStack.peek().addPathModel(pathModel);
              }
              vectorModel.getFullpath().addPath(pathModel.getPath());
            } else if (name.equals("clip-path")) {
              if (groupModelStack.size() == 0) {
                vectorModel.addClipPathModel(clipPathModel);
              } else {
                groupModelStack.peek().addClipPathModel(clipPathModel);
              }
            } else if (name.equals("group")) {
              GroupModel topGroupModel = groupModelStack.pop();
              if (groupModelStack.size() == 0) {
                topGroupModel.setParent(null);
                vectorModel.addGroupModel(topGroupModel);
              } else {
                topGroupModel.setParent(groupModelStack.peek());
                groupModelStack.peek().addGroupModel(topGroupModel);
              }
            } else if (name.equals("vector")) {
              vectorModel.buildTransformMatrices();
            }
            break;
        }
        event = xpp.next();
      }
    } catch (XmlPullParserException | IOException e) {
      e.printStackTrace();
    }
  }

  int getAttrPosition(XmlPullParser xpp, String attrName) {
    for (int i = 0; i < xpp.getAttributeCount(); i++) {
      if (xpp.getAttributeName(i).equals(attrName)) {
        return i;
      }
    }
    return -1;
  }

  public VectorMasterView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    init(attrs);
  }

  public int getResID() {
    return resID;
  }

  public void setResID(int resID) {
    this.resID = resID;
  }

  public Path getFullPath() {
    if (vectorModel != null) {
      return vectorModel.getFullpath();
    }
    return null;
  }

  public GroupModel getGroupModelByName(String name) {
    GroupModel gModel;
    for (GroupModel groupModel : vectorModel.getGroupModels()) {
      if (Utils.isEqual(groupModel.getName(), name)) {
        return groupModel;
      } else {
        gModel = groupModel.getGroupModelByName(name);
        if (gModel != null) return gModel;
      }
    }
    return null;
  }

  public PathModel getPathModelByName(String name) {
    PathModel pModel = null;
    for (PathModel pathModel : vectorModel.getPathModels()) {
      if (Utils.isEqual(pathModel.getName(), name)) {
        return pathModel;
      }
    }
    for (GroupModel groupModel : vectorModel.getGroupModels()) {
      pModel = groupModel.getPathModelByName(name);
      if (pModel != null && Utils.isEqual(pModel.getName(), name)) return pModel;
    }
    return pModel;
  }

  public ClipPathModel getClipPathModelByName(String name) {
    ClipPathModel cModel = null;
    for (ClipPathModel clipPathModel : vectorModel.getClipPathModels()) {
      if (Utils.isEqual(clipPathModel.getName(), name)) {
        return clipPathModel;
      }
    }
    for (GroupModel groupModel : vectorModel.getGroupModels()) {
      cModel = groupModel.getClipPathModelByName(name);
      if (cModel != null && Utils.isEqual(cModel.getName(), name)) return cModel;
    }
    return cModel;
  }

  public void update() {
    invalidate();
  }

  public float getScaleRatio() {
    return scaleRatio;
  }

  public float getStrokeRatio() {
    return strokeRatio;
  }

  public Matrix getScaleMatrix() {
    return scaleMatrix;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    if (w != 0 && h != 0) {
      width = w;
      height = h;

      buildScaleMatrix();
      scaleAllPaths();
      scaleAllStrokes();
    }
  }

  @SuppressLint("CanvasSize")
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    width = canvas.getWidth();
    height = canvas.getHeight();

    if (vectorModel == null) {
      return;
    }

    setAlpha(vectorModel.getAlpha());

    vectorModel.drawPaths(canvas);
  }

  void buildScaleMatrix() {

    scaleMatrix = new Matrix();

    scaleMatrix.postTranslate(
        width / 2 - vectorModel.getViewportWidth() / 2,
        height / 2 - vectorModel.getViewportHeight() / 2);

    float widthRatio = width / vectorModel.getViewportWidth();
    float heightRatio = height / vectorModel.getViewportHeight();
    float ratio = Math.min(widthRatio, heightRatio);

    scaleRatio = ratio;

    scaleMatrix.postScale(ratio, ratio, width / 2, height / 2);
  }

  void scaleAllPaths() {
    vectorModel.scaleAllPaths(scaleMatrix);
  }

  void scaleAllStrokes() {
    strokeRatio = Math.min(width / vectorModel.getWidth(), height / vectorModel.getHeight());
    vectorModel.scaleAllStrokeWidth(strokeRatio);
  }
}
