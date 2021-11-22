/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


package com.itsaky.androidide.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.databinding.LayoutSymbolItemBinding;
import com.itsaky.androidide.views.SymbolInputView.Symbol;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.widget.SymbolChannel;

public class SymbolInputAdapter extends RecyclerView.Adapter<SymbolInputAdapter.VH> {
	
	private SymbolChannel channel;
	private Symbol[] symbols;
	
	public SymbolInputAdapter(CodeEditor editor) {
		this(editor, null);
	}

	public SymbolInputAdapter(CodeEditor editor, Symbol[] symbols) {
		this.channel = editor.createNewSymbolChannel();
		this.symbols = symbols == null ? new Symbol[0] : symbols;
	}
	
	public SymbolInputAdapter setSymbols(Symbol... symbols) {
		return setSymbols(false, symbols);
	}
	
    @SuppressLint("NotifyDataSetChanged")
	public SymbolInputAdapter setSymbols(boolean notify, Symbol... symbols) {
		this.symbols = symbols;
		if(notify) {
			notifyDataSetChanged();
		}
		return this;
	}
	
	@Override
	public VH onCreateViewHolder(ViewGroup parent, int itemType) {
		return new VH(LayoutSymbolItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
	}
	
	@Override
	public void onBindViewHolder(VH holder, int position) {
		if(symbols == null || symbols[position] == null) return;
		final Symbol symbol = symbols[position];
		holder.binding.symbol.setText(symbol.label);
        holder.binding.symbol.setOnClickListener(__ -> channel.insertSymbol(symbol.commit, symbol.offset));
	}
	
	@Override
	public int getItemCount() {
		return symbols == null ? 0 : symbols.length;
	}
	
	public class VH extends RecyclerView.ViewHolder {
		LayoutSymbolItemBinding binding;
		
		public VH(LayoutSymbolItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}
}
