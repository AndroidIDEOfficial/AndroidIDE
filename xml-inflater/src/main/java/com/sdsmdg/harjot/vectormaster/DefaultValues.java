package com.sdsmdg.harjot.vectormaster;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class DefaultValues {

    public static String[] PATH_ATTRIBUTES = {"name",
            "fillAlpha",
            "fillColor",
            "fillType",
            "pathData",
            "strokeAlpha",
            "strokeColor",
            "strokeLineCap",
            "strokeLineJoin",
            "strokeMiterLimit",
            "strokeWidth"};

    public final static int PATH_FILL_COLOR = Color.TRANSPARENT;
    public final static int PATH_STROKE_COLOR = Color.TRANSPARENT;
    public final static float PATH_STROKE_WIDTH = 0.0f;
    public final static float PATH_STROKE_ALPHA = 1.0f;
    public final static float PATH_FILL_ALPHA = 1.0f;
    public final static Paint.Cap PATH_STROKE_LINE_CAP = Paint.Cap.BUTT;
    public final static Paint.Join PATH_STROKE_LINE_JOIN = Paint.Join.MITER;
    public final static float PATH_STROKE_MITER_LIMIT = 4.0f;
    public final static float PATH_STROKE_RATIO = 1.0f;
    // WINDING fill type is equivalent to NON_ZERO
    public final static Path.FillType PATH_FILL_TYPE = Path.FillType.WINDING;
    public final static float PATH_TRIM_PATH_START = 0.0f;
    public final static float PATH_TRIM_PATH_END = 1.0f;
    public final static float PATH_TRIM_PATH_OFFSET = 0.0f;

    public final static float VECTOR_VIEWPORT_WIDTH = 0.0f;
    public final static float VECTOR_VIEWPORT_HEIGHT = 0.0f;
    public final static float VECTOR_WIDTH = 0.0f;
    public final static float VECTOR_HEIGHT = 0.0f;
    public final static float VECTOR_ALPHA = 1.0f;

    public final static float GROUP_ROTATION = 0.0f;
    public final static float GROUP_PIVOT_X = 0.0f;
    public final static float GROUP_PIVOT_Y = 0.0f;
    public final static float GROUP_SCALE_X = 1.0f;
    public final static float GROUP_SCALE_Y = 1.0f;
    public final static float GROUP_TRANSLATE_X = 0.0f;
    public final static float GROUP_TRANSLATE_Y = 0.0f;

}
