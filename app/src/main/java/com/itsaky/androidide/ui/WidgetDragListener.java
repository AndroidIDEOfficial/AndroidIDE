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
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.DesignerActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.UIWidget;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.layoutinflater.IAttribute;
import com.itsaky.layoutinflater.IView;
import com.itsaky.layoutinflater.IViewGroup;
import com.itsaky.layoutinflater.impl.UiAttribute;
import com.itsaky.layoutinflater.impl.UiView;
import com.itsaky.layoutinflater.impl.UiViewGroup;

import org.jetbrains.annotations.Contract;

/**
 * Common drag listener for handling drag events in all ViewGroups in the UI Designer.
 *
 * @author Akash Yadav
 */
public class WidgetDragListener implements View.OnDragListener {

    private final View placeholder;
    private final IViewGroup viewGroup;
    private final OnViewAddedListener addedListener;

    private final int PLACEHOLDER_HEIGHT = 20; // in dp
    private final int PLACEHOLDER_WIDTH = 40; // in dp

    public static final String ANDROID_NS = "android";

    private static final Logger LOG = Logger.instance("WidgetDragListener");

    public WidgetDragListener(@NonNull Context context, IViewGroup viewGroup, OnViewAddedListener addedListener) {
        this.placeholder = new View(context);
        this.viewGroup = viewGroup;
        this.addedListener = addedListener;
        this.placeholder.setBackgroundResource(R.drawable.bg_widget_drag_placeholder);
        this.placeholder.setLayoutParams(new ViewGroup.LayoutParams(SizeUtils.dp2px(PLACEHOLDER_WIDTH), SizeUtils.dp2px(PLACEHOLDER_HEIGHT)));
    }

    @Override
    public boolean onDrag(View v, @NonNull DragEvent event) {
        if (!(v instanceof ViewGroup)) {
            throw new UnsupportedOperationException("WidgetDragListener can be set only to a ViewGroup");
        }

        final var group = (ViewGroup) v; // Assuming that this drag listener is always set to ViewGroup(s)
        final var action = event.getAction();
        final var clipDesc = event.getClipDescription();
        final var dragData = (WidgetDragData) event.getLocalState();

        var canHandle = true;

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                canHandle = clipDesc.hasMimeType(DesignerActivity.DRAGGING_WIDGET_MIME);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                removePlaceholderFromParent(); // If placeholder is added in another group, remove it.
                group.addView(this.placeholder);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                removePlaceholderFromParent();
                break;
            case DragEvent.ACTION_DROP:
                removePlaceholderFromParent();
                var view = dragData.isAlreadyInflated ? dragData.alreadyInflatedView : createView(dragData.newDragData);
                applyBasicAttributes(view);

                // Remove from parent if it has a parent
                view.removeFromParent();

                this.viewGroup.addView(view);

                if (this.addedListener != null) {
                    this.addedListener.onViewAdded(view);
                }

                break;
        }

        return canHandle;
    }

    private void applyBasicAttributes (@NonNull IView view) {
        final var resFinder = StudioApp.getInstance().getResFinder();
        view.addAttribute(layoutWidthAttr(), resFinder);
        view.addAttribute(layoutHeightAttr(), resFinder);
    }

    @NonNull
    @Contract(" -> new")
    private IAttribute layoutHeightAttr() {
        return new UiAttribute(ANDROID_NS, "layout_height", "wrap_content");
    }

    @NonNull
    @Contract(" -> new")
    private IAttribute layoutWidthAttr() {
        return new UiAttribute(ANDROID_NS, "layout_width", "wrap_content");
    }

    @NonNull
    private IView createView (@NonNull UIWidget widget) {
        try {
            final var clazz = widget.asClass();
            final var constructor = clazz.getConstructor(Context.class);
            constructor.setAccessible(true);
            final var instance = constructor.newInstance(this.viewGroup.asView().getContext());

            if (instance instanceof ViewGroup) {
                return new UiViewGroup(clazz.getName(), (ViewGroup) instance);
            } else {
                return new UiView(clazz.getName(), instance);
            }
        } catch (Throwable e) {
            LOG.error("Unable to add widget", e);
            throw new RuntimeException("Unable to add widget", e);
        }
    }

    private void removePlaceholderFromParent () {
        if (this.placeholder.getParent() != null) {
            ((ViewGroup) this.placeholder.getParent()).removeView((this.placeholder));
        }
    }

    public static interface OnViewAddedListener {
        void onViewAdded (IView view);
    }
}
