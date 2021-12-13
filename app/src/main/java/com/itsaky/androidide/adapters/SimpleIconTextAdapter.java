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

import com.itsaky.androidide.databinding.LayoutSimpleIconTextBinding;
import com.itsaky.androidide.models.IconTextListItem;

import java.util.List;

/**
 * A RecyclerView.Adapter which can be used to show a list with an icon and a text.
 *
 * @author Akash Yadav
 */
public class SimpleIconTextAdapter extends RecyclerView.Adapter<SimpleIconTextAdapter.VH> {

    private final List<? extends IconTextListItem> items;
    private OnBindListener bindListener;

    public SimpleIconTextAdapter(@NonNull List<? extends IconTextListItem> items) {
        this.items = items;
    }

    public SimpleIconTextAdapter setOnBindListener (OnBindListener listener) {
        this.bindListener = listener;
        return this;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH (LayoutSimpleIconTextBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        final var binding = holder.binding;
        final var item = items.get(position);

        if (this.bindListener != null && this.bindListener.onBind(item, holder, position)) {
            return;
        }

        final var icon = item.getIconResource();

        if (icon == -1) {
            binding.icon.setVisibility(View.GONE);
        } else {
            binding.icon.setImageResource(icon);
        }
        binding.text.setText(item.getText());

        if (this.bindListener != null) {
            this.bindListener.postBind(item, holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public IconTextListItem getItemAt (int index) {
        return this.items.get(index);
    }

    public static class VH extends RecyclerView.ViewHolder {

        public final LayoutSimpleIconTextBinding binding;

        public VH(@NonNull LayoutSimpleIconTextBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    public static interface OnBindListener {
        boolean onBind (IconTextListItem item, VH holder, int position);

        default void postBind (IconTextListItem item, VH holder, int position) {}
    }
}
