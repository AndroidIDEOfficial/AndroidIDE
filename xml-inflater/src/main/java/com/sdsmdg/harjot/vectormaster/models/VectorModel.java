package com.sdsmdg.harjot.vectormaster.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Region;

import com.sdsmdg.harjot.vectormaster.enums.TintMode;

import java.util.ArrayList;

public class VectorModel {

    private String name;

    private float width, height;

    private float alpha = 1.0f;

    private boolean autoMirrored = false;

    private int tint = Color.TRANSPARENT;
    private TintMode tintMode = TintMode.SCR_IN;

    private float viewportWidth, viewportHeight;

    private ArrayList<GroupModel> groupModels;
    private ArrayList<PathModel> pathModels;
    private ArrayList<ClipPathModel> clipPathModels;

    private Path fullpath;

    private Matrix scaleMatrix;

    public VectorModel() {
        groupModels = new ArrayList<>();
        pathModels = new ArrayList<>();
        clipPathModels = new ArrayList<>();
        fullpath = new Path();
    }

    public void drawPaths(Canvas canvas, float offsetX, float offsetY, float scaleX, float scaleY) {
        for (ClipPathModel clipPathModel : clipPathModels) {
            canvas.clipPath(clipPathModel.getScaledAndOffsetPath(offsetX, offsetY, scaleX, scaleY));
        }
        for (GroupModel groupModel : groupModels) {
            groupModel.drawPaths(canvas, offsetX, offsetY, scaleX, scaleY);
        }
        for (PathModel pathModel : pathModels) {
            if (pathModel.isFillAndStroke()) {
                pathModel.makeFillPaint();
                canvas.drawPath(pathModel.getScaledAndOffsetPath(offsetX, offsetY, scaleX, scaleY), pathModel.getPathPaint());
                pathModel.makeStrokePaint();
                canvas.drawPath(pathModel.getScaledAndOffsetPath(offsetX, offsetY, scaleX, scaleY), pathModel.getPathPaint());
            } else {
                canvas.drawPath(pathModel.getScaledAndOffsetPath(offsetX, offsetY, scaleX, scaleY), pathModel.getPathPaint());
            }
        }
    }

    public void drawPaths(Canvas canvas) {
        for (ClipPathModel clipPathModel : clipPathModels) {
            canvas.clipPath(clipPathModel.getPath());
        }
        for (GroupModel groupModel : groupModels) {
            groupModel.drawPaths(canvas);
        }
        for (PathModel pathModel : pathModels) {
            if (pathModel.isFillAndStroke()) {
                pathModel.makeFillPaint();
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
                pathModel.makeStrokePaint();
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
            } else {
                canvas.drawPath(pathModel.getPath(), pathModel.getPathPaint());
            }
        }
    }

    public void scaleAllPaths(Matrix scaleMatrix) {
        this.scaleMatrix = scaleMatrix;
        for (GroupModel groupModel : groupModels) {
            groupModel.scaleAllPaths(scaleMatrix);
        }
        for (PathModel pathModel : pathModels) {
            pathModel.transform(scaleMatrix);
        }
        for (ClipPathModel clipPathModel : clipPathModels) {
            clipPathModel.transform(scaleMatrix);
        }
    }

    public void scaleAllStrokeWidth(float ratio) {
        for (GroupModel groupModel : groupModels) {
            groupModel.scaleAllStrokeWidth(ratio);
        }
        for (PathModel pathModel : pathModels) {
            pathModel.setStrokeRatio(ratio);
        }
    }

    public void buildTransformMatrices() {
        for (GroupModel groupModel : groupModels) {
            groupModel.buildTransformMatrix();
        }
    }

    public void addGroupModel(GroupModel groupModel) {
        groupModels.add(groupModel);
    }

    public ArrayList<GroupModel> getGroupModels() {
        return groupModels;
    }

    public void addPathModel(PathModel pathModel) {
        pathModels.add(pathModel);
    }

    public ArrayList<PathModel> getPathModels() {
        return pathModels;
    }

    public void addClipPathModel(ClipPathModel clipPathModel) {
        clipPathModels.add(clipPathModel);
    }

    public ArrayList<ClipPathModel> getClipPathModels() {
        return clipPathModels;
    }

    public Path getFullpath() {
        return fullpath;
    }

    public void setFullpath(Path fullpath) {
        this.fullpath = fullpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isAutoMirrored() {
        return autoMirrored;
    }

    public void setAutoMirrored(boolean autoMirrored) {
        this.autoMirrored = autoMirrored;
    }

    public int getTint() {
        return tint;
    }

    public void setTint(int tint) {
        this.tint = tint;
    }

    public TintMode getTintMode() {
        return tintMode;
    }

    public void setTintMode(TintMode tintMode) {
        this.tintMode = tintMode;
    }

    public float getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(float viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public float getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(float viewportHeight) {
        this.viewportHeight = viewportHeight;
    }
}
