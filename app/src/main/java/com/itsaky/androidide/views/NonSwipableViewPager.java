/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


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
