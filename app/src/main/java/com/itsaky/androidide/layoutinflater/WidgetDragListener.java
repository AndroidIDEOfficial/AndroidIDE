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

package com.itsaky.androidide.layoutinflater;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.DesignerActivity;
import com.itsaky.androidide.R;
import com.itsaky.toaster.Toaster;

public class WidgetDragListener implements View.OnDragListener {

    private final View placeholder;

    private final int PLACEHOLDER_HEIGHT = 20; // in dp
    private final int PLACEHOLDER_WIDTH = 40; // in dp

    public WidgetDragListener(@NonNull Context context) {
        this.placeholder = new View(context);
        this.placeholder.setBackgroundResource(R.drawable.bg_widget_drag_placeholder);
        this.placeholder.setLayoutParams(new ViewGroup.LayoutParams(SizeUtils.dp2px(PLACEHOLDER_WIDTH), SizeUtils.dp2px(PLACEHOLDER_HEIGHT)));
    }

    @Override
    public boolean onDrag(View v, @NonNull DragEvent event) {
        final var group = (ViewGroup) v; // Assuming that this drag listener is always set to ViewGroup(s)
        final var action = event.getAction();
        final var clipDesc = event.getClipDescription();

        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (clipDesc.hasMimeType(DesignerActivity.DRAGGING_WIDGET_MIME)) {
                    return true;
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                removePlaceholderFromParent();
                group.addView(this.placeholder);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                removePlaceholderFromParent();
                break;
            case DragEvent.ACTION_DROP:
                removePlaceholderFromParent();
                Toast.makeText(group.getContext(), "Widget dropped", Toast.LENGTH_SHORT).show();
                break;

        }

        return false;
    }

    private void removePlaceholderFromParent () {
        if (this.placeholder.getParent() != null) {
            ((ViewGroup) this.placeholder.getParent()).removeView((this.placeholder));
        }
    }

}
