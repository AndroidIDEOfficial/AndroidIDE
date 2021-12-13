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

package com.itsaky.androidide.adapters;

import android.view.DragEvent;
import android.view.View;

import androidx.annotation.NonNull;

import com.itsaky.androidide.models.IconTextListItem;
import com.itsaky.androidide.models.UIWidget;

import java.util.List;

public class WidgetItemAdapter extends SimpleIconTextAdapter implements SimpleIconTextAdapter.OnBindListener {

    private final OnWidgetDragListener dragListener;

    public WidgetItemAdapter(List<UIWidget> widgets, OnWidgetDragListener dragListener) {
        super(widgets);
        this.dragListener = dragListener;

        setOnBindListener(this);
    }

    @Override
    public boolean onBind(IconTextListItem item, @NonNull VH holder, int position) {
        final var binding = holder.binding;
        final var widget = (UIWidget) getItemAt(position);

        if (this.dragListener != null) {
            binding.getRoot().setOnDragListener((v, event) -> {
                return this.dragListener.onDrag(widget, v, event);
            });
        }

        return false; // Returning true will not set the text and icon
    }

    public static interface OnWidgetDragListener {
        boolean onDrag(UIWidget widget, View v, DragEvent event);
    }
}
