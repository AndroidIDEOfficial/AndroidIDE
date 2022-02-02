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
package io.github.rosemoe.sora.graphics;


import android.graphics.Canvas;
import android.graphics.Paint;

public class BufferedDrawPoints {

    private int pointCount;
    private float[] points;

    public BufferedDrawPoints() {
        points = new float[128];
    }

    public void drawPoint(float cx, float cy) {
        // Check buffer size and grow
        if (points.length < (pointCount + 1) * 2) {
            float[] newBuffer = new float[points.length << 1];
            System.arraycopy(points, 0, newBuffer, 0, pointCount * 2);
            points = newBuffer;
        }
        points[pointCount * 2] = cx;
        points[pointCount * 2 + 1] = cy;
        pointCount++;
    }

    public void commitPoints(Canvas canvas, Paint paint) {
        if (pointCount == 0) {
            return;
        }
        canvas.drawPoints(points, 0, pointCount * 2, paint);
        pointCount = 0;
    }

}
