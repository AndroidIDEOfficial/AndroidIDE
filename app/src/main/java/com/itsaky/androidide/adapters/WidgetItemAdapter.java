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

import android.content.ClipData;
import android.view.View;
import androidx.annotation.NonNull;
import com.itsaky.androidide.DesignerActivity;
import com.itsaky.androidide.models.IconTextListItem;
import com.itsaky.androidide.models.UIWidget;
import com.itsaky.androidide.ui.WidgetDragData;
import com.itsaky.androidide.ui.WidgetDragShadowBuilder;
import java.util.List;

public class WidgetItemAdapter extends SimpleIconTextAdapter
    implements SimpleIconTextAdapter.OnBindListener<IconTextListItem> {

  private final OnDragStartListener dragStartListener;

  public WidgetItemAdapter(List<UIWidget> widgets, OnDragStartListener dragStartListener) {
    super(widgets);
    this.dragStartListener = dragStartListener;

    setOnBindListener(this);
  }

  @Override
  public boolean onBind(IconTextListItem item, @NonNull VH holder, int position) {
    return false; // Returning true will not set the text and icon
  }

  @Override
  public void postBind(IconTextListItem item, VH holder, int position) {
    final var binding = holder.binding;
    final var root = binding.getRoot();
    final var widget = (UIWidget) getItemAt(position);

    root.setOnLongClickListener(
        v -> {
          final var dragData = new WidgetDragData(false, null, widget);
          final var shadow = new WidgetDragShadowBuilder(binding.icon);
          final var dataItem = new ClipData.Item(DesignerActivity.DRAGGING_WIDGET_TAG);
          final var data =
              new ClipData(
                  DesignerActivity.DRAGGING_WIDGET_TAG,
                  new String[] {DesignerActivity.DRAGGING_WIDGET_MIME},
                  dataItem);
          binding.icon.startDragAndDrop(data, shadow, dragData, 0);

          if (this.dragStartListener != null) {
            this.dragStartListener.onDragStarted(v);
          }

          return true;
        });
  }

  public interface OnDragStartListener {
    void onDragStarted(View view);
  }
}
