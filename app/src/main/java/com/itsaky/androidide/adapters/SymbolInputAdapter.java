/*
 * This file is part of AndroidIDE.
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
 */
package com.itsaky.androidide.adapters;

import static com.itsaky.androidide.utils.ResourceUtilsKt.resolveAttr;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutSymbolItemBinding;
import com.itsaky.androidide.ui.SymbolInputView.Symbol;
import com.itsaky.androidide.editor.ui.IDEEditor;

import java.util.ArrayList;
import java.util.List;

public class SymbolInputAdapter extends RecyclerView.Adapter<SymbolInputAdapter.VH> {

  private static final List<Character> pairs;

  static {
    pairs = new ArrayList<>();
    pairs.add('}');
    pairs.add(')');
    pairs.add(']');
    pairs.add('"');
    pairs.add('\'');
    pairs.add('>');
  }

  private final IDEEditor editor;
  private Symbol[] symbols;

  public SymbolInputAdapter(IDEEditor editor) {
    this(editor, null);
  }

  public SymbolInputAdapter(IDEEditor editor, Symbol[] symbols) {
    this.editor = editor;
    this.symbols = symbols == null ? new Symbol[0] : symbols;
  }

  @SuppressLint("NotifyDataSetChanged")
  public void setSymbols(boolean notify, Symbol... symbols) {
    this.symbols = symbols;
    if (notify) {
      notifyDataSetChanged();
    }
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
    return new VH(
        LayoutSymbolItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    if (symbols == null || symbols[position] == null) return;
    final Symbol symbol = symbols[position];
    holder.binding.symbol.setText(symbol.getLabel());
    holder.binding.symbol.setTextColor(
        resolveAttr(holder.binding.symbol.getContext(), R.attr.colorOnSurface));
    holder.binding.symbol.setOnClickListener(
        __ -> insertSymbol(symbol.getCommit(), symbol.getOffset()));
  }

  @Override
  public int getItemCount() {
    return symbols == null ? 0 : symbols.length;
  }

  void insertSymbol(String text, int selectionOffset) {
    if (selectionOffset < 0 || selectionOffset > text.length()) {
      return;
    }
    var cur = editor.getText().getCursor();
    if (cur.isSelected()) {
      editor
          .getText()
          .delete(cur.getLeftLine(), cur.getLeftColumn(), cur.getRightLine(), cur.getRightColumn());
      editor.notifyIMEExternalCursorChange();
    }

    if (cur.getLeftColumn() < editor.getText().getColumnCount(cur.getLeftLine())
        && text.length() == 1
        && text.charAt(0) == editor.getText().charAt(cur.getLeftLine(), cur.getLeftColumn())
        && pairs.contains(text.charAt(0))) {
      editor.moveSelectionRight();
    } else {
      editor.commitText(text);
      if (selectionOffset != text.length()) {
        editor.setSelection(
            cur.getRightLine(), cur.getRightColumn() - (text.length() - selectionOffset));
      }
    }
  }

  public static class VH extends RecyclerView.ViewHolder {
    LayoutSymbolItemBinding binding;

    public VH(LayoutSymbolItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
