package com.itsaky.androidide.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.Color;

public class TooltipLayout extends LinearLayout {
    
    private final float dp8;
    private final Path path;
    private final Paint paint;
    
    public TooltipLayout (Context ctx) {
        this(ctx, null);
    }
    
    public TooltipLayout (Context ctx, AttributeSet attrs) {
        this(ctx, attrs, 0);
    }
    
    public TooltipLayout (Context ctx, AttributeSet attrs, int style) {
        super(ctx, attrs, style);
        
        dp8 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        path = new Path();
        
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#424242"));
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        final int width = getWidth();
        final int height = getHeight();
        final float midX = width / 2;
        
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - dp8);
        path.lineTo(midX + dp8, height - dp8);
        path.lineTo(midX, height );
        path.lineTo(midX - dp8, height - dp8);
        path.lineTo(0, height - dp8);
        path.close();
        
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }
}
