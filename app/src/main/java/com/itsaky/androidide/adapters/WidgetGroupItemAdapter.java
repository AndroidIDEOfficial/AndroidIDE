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

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.models.IconTextListItem;
import com.itsaky.androidide.models.UIWidgetGroup;

import java.util.List;

public class WidgetGroupItemAdapter extends SimpleIconTextAdapter
    implements SimpleIconTextAdapter.OnBindListener<IconTextListItem> {

  private final OnGroupClickListener clickListener;

  public WidgetGroupItemAdapter(
      @NonNull List<UIWidgetGroup> groups, @Nullable OnGroupClickListener clickListener) {
    super(groups);
    this.clickListener = clickListener;

    super.setOnBindListener(this);
  }

  @Override
  public boolean onBind(IconTextListItem item, VH holder, int position) {
    // Ignored
    return false;
  }

  @Override
  public void postBind(IconTextListItem item, @NonNull VH holder, int position) {
    final var binding = holder.binding;
    final var group = (UIWidgetGroup) item;

    if (group.isSelected()) {
      binding
          .getRoot()
          .setBackground(
              ContextCompat.getDrawable(
                  binding.getRoot().getContext(), R.drawable.bg_widget_group_selected));
    } else {
      binding.getRoot().setBackgroundColor(Color.TRANSPARENT);
    }

    if (this.clickListener != null) {
      binding.getRoot().setOnClickListener(v -> this.clickListener.onGroupClick(group));
    }
  }

  public interface OnGroupClickListener {
    void onGroupClick(UIWidgetGroup group);
  }
}
