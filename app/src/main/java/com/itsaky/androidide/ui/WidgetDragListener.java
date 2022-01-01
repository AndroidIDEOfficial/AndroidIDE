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

import android.content.Context;
import android.graphics.RectF;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.DesignerActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.models.UIWidget;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.inflater.IAttribute;
import com.itsaky.inflater.IView;
import com.itsaky.inflater.IViewGroup;
import com.itsaky.inflater.impl.UiAttribute;
import com.itsaky.inflater.impl.UiView;
import com.itsaky.inflater.impl.UiViewGroup;

import org.jetbrains.annotations.Contract;

/**
 * Common drag listener for handling drag events in all ViewGroups in the UI Designer.
 *
 * @author Akash Yadav
 */
public class WidgetDragListener implements View.OnDragListener {
    
    private final IView placeholder;
    private final IViewGroup viewGroup;
    private final OnViewAddedListener addedListener;
    
    private final int PLACEHOLDER_HEIGHT = 20; // in dp
    private final int PLACEHOLDER_WIDTH = 40; // in dp
    
    public static final String ANDROID_NS = "android";
    
    private static final Logger LOG = Logger.instance ("WidgetDragListener");
    
    public WidgetDragListener (@NonNull Context context, IViewGroup viewGroup, OnViewAddedListener addedListener) {
        this.placeholder = new UiView ("android.view.View", new View (context), true);
        this.viewGroup = viewGroup;
        this.addedListener = addedListener;
        this.placeholder.asView ().setBackgroundResource (R.drawable.bg_widget_drag_placeholder);
        this.placeholder.asView ().setLayoutParams (new ViewGroup.LayoutParams (SizeUtils.dp2px (PLACEHOLDER_WIDTH), SizeUtils.dp2px (PLACEHOLDER_HEIGHT)));
    }
    
    @Override
    public boolean onDrag (View v, @NonNull DragEvent event) {
        final var action = event.getAction ();
        final var clipDesc = event.getClipDescription ();
        final var dragData = (WidgetDragData) event.getLocalState ();
        
        var handled = true;
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                handled = clipDesc.hasMimeType (DesignerActivity.DRAGGING_WIDGET_MIME);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_LOCATION :
                this.placeholder.removeFromParent (); // If placeholder is added in another group, remove it.
                this.viewGroup.addView (this.placeholder, findPlaceHolderIndex (event.getX (), event.getY ()));
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                this.placeholder.removeFromParent ();
                break;
            case DragEvent.ACTION_DROP:
                final var index = this.viewGroup.indexOfChild (this.placeholder);
                this.placeholder.removeFromParent ();
                
                var view = dragData.isAlreadyInflated ? dragData.alreadyInflatedView : createView (dragData.newDragData);
                if (!dragData.isAlreadyInflated) {
                    applyBasicAttributes (view);
                }
                
                // Remove from parent if it has a parent
                view.removeFromParent ();
                this.viewGroup.addView (view, index);
                
                if (this.addedListener != null) {
                    this.addedListener.onViewAdded (view);
                }
                
                break;
        }
        
        return handled;
    }
    
    private void applyBasicAttributes (@NonNull IView view) {
        view.addAttribute (layoutWidthAttr ());
        view.addAttribute (layoutHeightAttr ());
    }
    
    /**
     * Finds the index at which the placeholder will be added
     * in the current view group.
     *
     * @param x The x coordinate of the touch event
     * @param y The y coordinate of the touch event
     * @return The index at which the placeholder of the view should be added.
     * The returned index is always valid index in the {@link #viewGroup}.
     */
    private int findPlaceHolderIndex (final float x, final float y) {
        final var count = this.viewGroup.getChildCount ();
        for (int i = 0; i < this.viewGroup.getChildCount (); i++) {
            final var child = this.viewGroup.getChildAt (i);
            final var rect = getViewRect (child);
            if (rect.contains (x, y)) {
                final var top = topHalf (rect);
                final var bottom = bottomHalf (rect);
                if (top.contains (x, y)) {
                    return Math.max (0, i - 1);
                } else if (bottom.contains (x, y)){
                    return Math.min (count, i + 1);
                }
            }
        }
        
        // If we don't find a suitable index, return the last index
        return count;
    }
    
    @NonNull
    private RectF getViewRect (@NonNull IView view) {
        final var v = view.asView ();
        
        final var rect = new RectF ();
        rect.left = v.getLeft ();
        rect.top = v.getTop ();
        rect.right = rect.left + v.getWidth ();
        rect.bottom = rect.top + v.getHeight ();
        
        return rect;
    }
    
    private RectF topHalf (RectF src) {
        final RectF result = new RectF (src);
        result.bottom -= result.height () / 2;
        return src;
    }
    
    private RectF bottomHalf (RectF src) {
        final RectF result = new RectF (src);
        result.top += result.height () / 2;
        return src;
    }
    
    private RectF leftHalf (RectF src) {
        final RectF result = new RectF (src);
        result.right -= result.width () / 2;
        return src;
    }
    
    private RectF rightHalf (RectF src) {
        final RectF result = new RectF (src);
        result.left += result.width () / 2;
        return src;
    }
    
    @NonNull
    @Contract(" -> new")
    private IAttribute layoutHeightAttr () {
        return new UiAttribute (ANDROID_NS, "layout_height", "wrap_content");
    }
    
    @NonNull
    @Contract(" -> new")
    private IAttribute layoutWidthAttr () {
        return new UiAttribute (ANDROID_NS, "layout_width", "wrap_content");
    }
    
    @NonNull
    private IView createView (@NonNull UIWidget widget) {
        try {
            final var clazz = widget.asClass ();
            final var constructor = clazz.getConstructor (Context.class);
            constructor.setAccessible (true);
            final var instance = constructor.newInstance (this.viewGroup.asView ().getContext ());
            
            if (instance instanceof ViewGroup) {
                return new UiViewGroup (clazz.getName (), (ViewGroup) instance);
            } else {
                return new UiView (clazz.getName (), instance);
            }
        } catch (Throwable e) {
            LOG.error ("Unable to add widget", e);
            throw new RuntimeException ("Unable to add widget", e);
        }
    }
    
    public static interface OnViewAddedListener {
        void onViewAdded (IView view);
    }
}
