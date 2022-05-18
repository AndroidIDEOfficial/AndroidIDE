package com.sdsmdg.harjot.vectormaster;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class DefaultValues {

  public static String[] PATH_ATTRIBUTES = {
    "name",
    "fillAlpha",
    "fillColor",
    "fillType",
    "pathData",
    "strokeAlpha",
    "strokeColor",
    "strokeLineCap",
    "strokeLineJoin",
    "strokeMiterLimit",
    "strokeWidth"
  };

  public static final int PATH_FILL_COLOR = Color.TRANSPARENT;
  public static final int PATH_STROKE_COLOR = Color.TRANSPARENT;
  public static final float PATH_STROKE_WIDTH = 0.0f;
  public static final float PATH_STROKE_ALPHA = 1.0f;
  public static final float PATH_FILL_ALPHA = 1.0f;
  public static final Paint.Cap PATH_STROKE_LINE_CAP = Paint.Cap.BUTT;
  public static final Paint.Join PATH_STROKE_LINE_JOIN = Paint.Join.MITER;
  public static final float PATH_STROKE_MITER_LIMIT = 4.0f;
  public static final float PATH_STROKE_RATIO = 1.0f;
  // WINDING fill type is equivalent to NON_ZERO
  public static final Path.FillType PATH_FILL_TYPE = Path.FillType.WINDING;
  public static final float PATH_TRIM_PATH_START = 0.0f;
  public static final float PATH_TRIM_PATH_END = 1.0f;
  public static final float PATH_TRIM_PATH_OFFSET = 0.0f;

  public static final float VECTOR_VIEWPORT_WIDTH = 0.0f;
  public static final float VECTOR_VIEWPORT_HEIGHT = 0.0f;
  public static final float VECTOR_WIDTH = 0.0f;
  public static final float VECTOR_HEIGHT = 0.0f;
  public static final float VECTOR_ALPHA = 1.0f;

  public static final float GROUP_ROTATION = 0.0f;
  public static final float GROUP_PIVOT_X = 0.0f;
  public static final float GROUP_PIVOT_Y = 0.0f;
  public static final float GROUP_SCALE_X = 1.0f;
  public static final float GROUP_SCALE_Y = 1.0f;
  public static final float GROUP_TRANSLATE_X = 0.0f;
  public static final float GROUP_TRANSLATE_Y = 0.0f;
}
