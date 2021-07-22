package com.itsaky.androidide.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/**
	Copied from StackOverflow
    @see https://stackoverflow.com/a/47143096
*/

public class NonSwipableViewPager extends ViewPager
{
	public NonSwipableViewPager(Context ctx)
	{
		super(ctx);
	}
	
	public NonSwipableViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return false;
	}
}
