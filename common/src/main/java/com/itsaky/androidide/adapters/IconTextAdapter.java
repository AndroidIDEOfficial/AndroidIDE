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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.common.databinding.LayoutSimpleIconTextBinding;

/**
 * A RecyclerView.Adapter which can be used to show a list with an icon and a text.
 *
 * @author Akash Yadav
 */
public abstract class IconTextAdapter<E> extends RecyclerView.Adapter<IconTextAdapter.VH> {

  private OnBindListener<E> bindListener;

  public IconTextAdapter<E> setOnBindListener(OnBindListener<E> listener) {
    this.bindListener = listener;
    return this;
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VH(
        LayoutSimpleIconTextBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
            false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    final var binding = holder.binding;
    final var item = getItemAt(position);

    if (this.bindListener != null && this.bindListener.onBind(item, holder, position)) {
      return;
    }

    final var icon = getIconResource(position);

    if (icon == -1) {
      binding.icon.setVisibility(View.GONE);
    } else {
      binding.icon.setImageResource(icon);
    }

    binding.text.setText(getItemText(position));

    if (this.bindListener != null) {
      this.bindListener.postBind(item, holder, position);
    }
  }

  /**
   * Get the list item at the given position.
   *
   * @param index The index of the item to retrieve.
   * @return The item at the given index. Must not be <code>null</code>.
   */
  @NonNull
  public abstract E getItemAt(int index);

  /**
   * Get the icon resource ID of the item the given index.
   *
   * @param index The index of the item.
   * @return The icon resource id or <b>-1</b> to hide the icon.
   */
  public abstract int getIconResource(int index);

  /**
   * Get the title of the item at the given index.
   *
   * @param index The index of the item.
   * @return The title of the item.
   */
  @NonNull
  public abstract String getItemText(int index);

  public static class VH extends RecyclerView.ViewHolder {

    public final LayoutSimpleIconTextBinding binding;

    public VH(@NonNull LayoutSimpleIconTextBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }

  public interface OnBindListener<T> {

    default boolean onBind(T item, VH holder, int position) {
      return false;
    }

    default void postBind(T item, VH holder, int position) {
    }
  }
}
