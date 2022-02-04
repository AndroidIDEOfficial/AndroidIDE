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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutTextActionItemBinding;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.views.editor.IDEEditor;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Adapter for text actions in editor.
 *
 * @author Akash Yadav
 */
public class TextActionItemAdapter extends RecyclerView.Adapter<TextActionItemAdapter.VH> {
    
    private final List<IDEEditor.TextAction> actions;
    private final Consumer<IDEEditor.TextAction> onClick;
    private final boolean isHorizontal;
    
    public TextActionItemAdapter (List<IDEEditor.TextAction> actions, Consumer<IDEEditor.TextAction> onClick) {
        Objects.requireNonNull (actions);
        this.actions = actions;
        this.onClick = onClick;
        this.isHorizontal = StudioApp.getInstance ().getPrefManager ().getBoolean (PreferenceManager.KEY_EDITOR_HORIZONTAL_POPUP, false);
    }
    
    @NonNull
    @Override
    public VH onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        return new VH (LayoutTextActionItemBinding.inflate (LayoutInflater.from (parent.getContext ()), parent, false));
    }
    
    @Override
    public void onBindViewHolder (@NonNull VH holder, int position) {
        final var binding = holder.binding;
        final var action = actions.get (position);
        final var button = binding.getRoot ();
        
        if (isHorizontal) {
            // If not set to wrap_content, one item will take whole screen width.
            final var params = button.getLayoutParams ();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            button.setLayoutParams (params);
            
            // looks better
            button.setGravity (Gravity.CENTER);
        }
        
        button.setText (action.titleId);
        button.setCompoundDrawablesRelative (action.icon, null, null, null);
        
        button.setOnClickListener (v -> {
            if (onClick != null) {
                onClick.accept (action);
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return actions.size ();
    }
    
    static class VH extends RecyclerView.ViewHolder {
        
        private final LayoutTextActionItemBinding binding;
        
        public VH (@NonNull LayoutTextActionItemBinding binding) {
            super (binding.getRoot ());
            this.binding = binding;
        }
    }
}
