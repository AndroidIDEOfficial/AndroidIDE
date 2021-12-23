/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.itsaky.androidide.R;

/**
 * A container used in UI Designer. This view holds the views inflated from an XML layout.
 * When there are no children in this layout, it shows a message.
 *
 * @author Akash Yadav
 */
public class DesignerLayoutContainer extends LinearLayout {
    
    private final TextView mMessage;
    
    public DesignerLayoutContainer (Context context) {
        this (context, null);
    }
    
    public DesignerLayoutContainer (Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }
    
    public DesignerLayoutContainer (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this (context, attrs, defStyleAttr, 0);
    }
    
    public DesignerLayoutContainer (Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super (context, attrs, defStyleAttr, defStyleRes);
        
        this.mMessage = new TextView (context);
        this.mMessage.setText (context.getString(R.string.msg_empty_ui_layout));
        this.mMessage.setLayoutParams (new LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.mMessage.setGravity (Gravity.CENTER);
        this.mMessage.setTextSize (TypedValue.COMPLEX_UNIT_SP, 16);
    }
    
    @Override
    public void onViewAdded (View child) {
        if (child != this.mMessage) {
            removeView (this.mMessage);
        } else {
            super.onViewAdded ( child);
        }
    }
    
    @Override
    public void onViewRemoved (View child) {
        if (child != this.mMessage && getChildCount () <= 0) {
            addView (mMessage);
        } else {
            super.onViewRemoved (child);
        }
    }
    
    // This is needed by XMLLayoutInflater
    @Override
    protected LayoutParams generateDefaultLayoutParams () {
        return super.generateDefaultLayoutParams ();
    }
}
