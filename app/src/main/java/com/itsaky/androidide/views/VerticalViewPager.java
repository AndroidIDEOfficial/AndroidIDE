package com.itsaky.androidide.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPager extends ViewPager
{
    public static final String TAG = "VerticalViewPager";
	private boolean scrollEnabled = true;
	
	public VerticalViewPager(Context context)
	{
        super(context);
        init();
    }
	
    public VerticalViewPager(Context context, AttributeSet attrs)
	{
        super(context, attrs);
        init();
    }

	public void setScrollEnabled(boolean scrollEnabled)
	{
		this.scrollEnabled = scrollEnabled;
	}

	public boolean isScrollEnabled()
	{
		return scrollEnabled;
	}
	
    private void init()
	{
        setPageTransformer(true, new VerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }
	
	private class VerticalPageTransformer implements ViewPager.PageTransformer
	{
        @Override
        public void transformPage(View view, float position)
		{
            if (position < -1)
			{
                view.setAlpha(0);
            } else if (position <= 1)
			{
                view.setAlpha(1);
                view.setTranslationX(view.getWidth() * -position);
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            } else
			{
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev)
	{
        float width = getWidth();
        float height = getHeight();
        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;
        ev.setLocation(newX, newY);
        return ev;
    }

	@SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
	{
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }
	
	@SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev)
	{
        return isScrollEnabled() ? super.onTouchEvent(swapXY(ev)) : true;
    }
}
