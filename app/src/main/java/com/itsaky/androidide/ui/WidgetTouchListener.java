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

package com.itsaky.androidide.ui;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.layoutinflater.IView;

/**
 * A touch listener for handling touch events of a view in the
 * UI Designer.
 *
 * Some of the views have no effect of setting a click listener
 * or a long click listener (e.g. SeekBar). In order to fix this,
 * we use {@link android.view.GestureDetector} and manually handle
 * click events.
 *
 * @author Akash Yadav
 */
public class WidgetTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {

    private final GestureDetector mGestureDetector;
    private final OnClickListener clickListener;
    private final OnLongClickListener longClickListener;

    private final IView mView;

    public WidgetTouchListener (@NonNull IView view, @Nullable OnClickListener clickListener, @Nullable OnLongClickListener longClickListener) {
        this.mView = view;
        this.mGestureDetector = new GestureDetector (view.asView().getContext(), this);
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true; // We would like to get notified about further events.
    }

    // TODO Should we use onSingleTapConfirmed instead?
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (this.clickListener != null) {
            this.clickListener.onClick(this.mView);
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        
        if (this.mView.getParent () == null) {
            // This is the root layout so this should not be draggable
            return;
        }
        
        if (this.longClickListener != null &&
                this.longClickListener.onLongClick(this.mView)) {
            this.mView.asView().performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
    }

    /**
     * A listener which is invoked when we detect a single
     * click event.
     *
     * @author Akash Yadav
     */
    public interface OnClickListener {
        void onClick (IView view);
    }

    /**
     * A listener which is invoked when we detect a long
     * click event.
     *
     * @author Akash Yadav
     */
    public interface OnLongClickListener {
        boolean onLongClick (IView view);
    }
}
