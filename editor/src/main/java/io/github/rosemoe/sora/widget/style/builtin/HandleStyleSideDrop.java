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
package io.github.rosemoe.sora.widget.style.builtin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;

import androidx.annotation.NonNull;

public class HandleStyleSideDrop extends HandleStyleDrop {
    
    private final int size;
    private final Paint paint;

    public HandleStyleSideDrop(Context context) {
        super(context);
        size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22f, context.getResources().getDisplayMetrics());
        paint = new Paint();
        paint.setAntiAlias(true);
    }
    
    @Override
    public void draw(@NonNull Canvas canvas, int handleType, float x, float y, int rowHeight, int color, @NonNull HandleDescriptor descriptor) {
        float radius = size / 2;
        paint.setColor(color);
        if (handleType == HANDLE_TYPE_INSERT || handleType == HANDLE_TYPE_UNDEFINED) {
            super.draw(canvas, handleType, x, y, rowHeight, color, descriptor);
        } else {
            boolean type = handleType == HANDLE_TYPE_LEFT;
            float cx = type ? x - radius : x + radius;
            canvas.drawCircle(cx, y + radius, radius, paint);
            canvas.drawRect(type ? cx : cx - radius, y, type ? cx + radius : cx, y + radius, paint);
            descriptor.set(cx - radius, y, cx + radius, y + 2 * radius, type ? ALIGN_LEFT : ALIGN_RIGHT);
        }
    }
    
}
