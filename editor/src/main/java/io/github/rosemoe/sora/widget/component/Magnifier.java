/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.widget.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import io.github.rosemoe.sora.R;
import io.github.rosemoe.sora.widget.CodeEditor;

/**
 * Magnifier specially designed for CodeEditor
 *
 * @author Rosemoe
 */
public final class Magnifier implements EditorBuiltinComponent {

    private final CodeEditor view;
    private final PopupWindow popup;
    private final ImageView image;
    private final Paint paint;
    private int x, y;
    private final float maxTextSize;
    private long expectedRequestTime;
    private boolean enabled = true;

    /**
     * Scale factor for regions
     */
    private final float scaleFactor;

    public Magnifier(CodeEditor editor) {
        view = editor;
        popup = new PopupWindow(editor);
        popup.setElevation(view.getDpUnit() * 8);
        @SuppressLint("InflateParams") var view = LayoutInflater.from(editor.getContext()).inflate(R.layout.magnifier_popup, null);
        image = view.findViewById(R.id.magnifier_image_view);
        popup.setHeight((int) (editor.getDpUnit() * 70));
        popup.setWidth((int) (editor.getDpUnit() * 100));
        popup.setContentView(view);
        maxTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 28, view.getResources().getDisplayMetrics());
        scaleFactor = 1.35f;
        paint = new Paint();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            dismiss();
        }
    }

    /**
     * Show the magnifier according to the given position.
     * X and Y are relative to the code editor view
     */
    public void show(int x, int y) {
        if (!enabled) {
            return;
        }
        if (Math.abs(x - this.x) < 2 && Math.abs(y - this.y) < 2) {
            return;
        }
        if (view.getTextSizePx() > maxTextSize) {
            if (isShowing()) {
                dismiss();
            }
            return;
        }
        popup.setWidth(Math.min(view.getWidth() * 3 / 5, (int)view.getDpUnit()) * 250);
        this.x = x;
        this.y = y;
        int[] pos = new int[2];
        view.getLocationInWindow(pos);
        var left = Math.max(pos[0] + x - popup.getWidth() / 2, 0);
        var right = left + popup.getWidth();
        if (right > view.getWidth() + pos[0]) {
            right = view.getWidth() + pos[0];
            left = Math.max(0, right - popup.getWidth());
        }
        var top = Math.max(pos[1] + y - popup.getHeight() - (int) (view.getRowHeight()), 0);
        if (popup.isShowing()) {
            popup.update(left, top, popup.getWidth(), popup.getHeight());
        } else {
            popup.showAtLocation(view, Gravity.START | Gravity.TOP, left, top);
        }
        updateDisplay();
    }

    /**
     * Whether the magnifier is showing
     */
    public boolean isShowing() {
        return popup.isShowing();
    }

    /**
     * Hide the magnifier
     */
    public void dismiss() {
        popup.dismiss();
    }

    /**
     * Update the display of the magnifier without updating the window's
     * location on screen.
     *
     * This should be called when new content has been drawn on the target view so
     * that the content in magnifier will not be invalid.
     *
     * This method does not take effect if the magnifier is not currently shown
     */
    public void updateDisplay() {
        if (!isShowing()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && view.getContext() instanceof Activity) {
            updateDisplayOreo((Activity)view.getContext());
        } else {
            updateDisplayWithinEditor();
        }
    }

    /**
     * Update display on API 26 or later.
     *
     * This will include other view in the window as {@link PixelCopy} is used to capture the
     * screen.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDisplayOreo(Activity activity) {
        var requiredWidth = (int) (popup.getWidth() / scaleFactor);
        var requiredHeight = (int) (popup.getHeight() / scaleFactor);

        var left = Math.max(x - requiredWidth / 2, 0);
        var top = Math.max(y - requiredHeight / 2, 0);
        var right = Math.min(left + requiredWidth, view.getWidth());
        var bottom = Math.min(top + requiredHeight, view.getHeight());
        if (right - left < requiredWidth) {
            left = Math.max(0, right - requiredWidth);
        }
        if (bottom - top < requiredHeight) {
            top = Math.max(0, bottom - requiredHeight);
        }
        if (right - left <= 0 || bottom - top <= 0) {
            dismiss();
            return;
        }
        var pos = new int[2];
        view.getLocationInWindow(pos);
        final var requestTime = System.currentTimeMillis();
        expectedRequestTime = requestTime;
        var clip = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
        try {
            PixelCopy.request(activity.getWindow(), new Rect(pos[0] + left, pos[1] + top, pos[0] + right, pos[1] + bottom), clip, (var statusCode) -> {
                if (requestTime != expectedRequestTime) {
                    return;
                }
                if (statusCode == PixelCopy.SUCCESS) {
                    var dest = Bitmap.createBitmap(popup.getWidth(), popup.getHeight(), Bitmap.Config.ARGB_8888);
                    var scaled = Bitmap.createScaledBitmap(clip, popup.getWidth(), popup.getHeight(), false);
                    clip.recycle();

                    Canvas canvas = new Canvas(dest);
                    paint.reset();
                    paint.setAntiAlias(true);
                    canvas.drawARGB(0, 0, 0, 0);
                    final int roundFactor = 6;
                    canvas.drawRoundRect(0, 0, popup.getWidth(), popup.getHeight(), view.getDpUnit() * roundFactor, view.getDpUnit() * roundFactor, paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(scaled, 0, 0, paint);
                    scaled.recycle();

                    image.setImageBitmap(dest);
                } else {
                    Log.w("Magnifier", "Failed to copy pixels, error = " + statusCode);
                }
            }, view.getHandler());
        } catch (IllegalArgumentException e) {
            // Happens when the view has not been drawn yet
            dismiss();
            if (!clip.isRecycled()) {
                clip.recycle();
            }
        }
    }

    /**
     * Update display on low API devices
     *
     * This method does not include other views as it obtain editor's display by
     * directly calling {@link CodeEditor#drawView(Canvas)}
     */
    private void updateDisplayWithinEditor() {
        var dest = Bitmap.createBitmap(popup.getWidth(), popup.getHeight(), Bitmap.Config.ARGB_8888);
        var requiredWidth = (int) (popup.getWidth() / scaleFactor);
        var requiredHeight = (int) (popup.getHeight() / scaleFactor);

        var left = Math.max(x - requiredWidth / 2, 0);
        var top = Math.max(y - requiredHeight / 2, 0);
        var right = Math.min(left + requiredWidth, view.getWidth());
        var bottom = Math.min(top + requiredHeight, view.getHeight());
        if (right - left < requiredWidth) {
            left = Math.max(0, right - requiredWidth);
        }
        if (bottom - top < requiredHeight) {
            top = Math.max(0, bottom - requiredHeight);
        }
        if (right - left <= 0 || bottom - top <= 0) {
            dismiss();
            dest.recycle();
            return;
        }
        var clip = Bitmap.createBitmap(requiredWidth, requiredHeight, Bitmap.Config.ARGB_8888);
        var viewCanvas = new Canvas(clip);
        viewCanvas.translate(-left, -top);
        view.drawView(viewCanvas);
        var scaled = Bitmap.createScaledBitmap(clip, popup.getWidth(), popup.getHeight(), false);
        clip.recycle();

        Canvas canvas = new Canvas(dest);
        paint.reset();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        final int roundFactor = 6;
        canvas.drawRoundRect(0, 0, popup.getWidth(), popup.getHeight(), view.getDpUnit() * roundFactor, view.getDpUnit() * roundFactor, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaled, 0, 0, paint);
        scaled.recycle();

        image.setImageBitmap(dest);
    }

}
