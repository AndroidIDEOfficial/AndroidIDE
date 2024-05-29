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
import com.itsaky.androidide.editor.ui.IDEEditor;
import com.itsaky.androidide.models.Symbol;
import io.github.rosemoe.sora.widget.SelectionMovement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  private IDEEditor editor;
  private final List<Symbol> symbols;

  public SymbolInputAdapter(IDEEditor editor) {
    this(editor, null);
  }

  public SymbolInputAdapter(IDEEditor editor, List<Symbol> symbols) {
    this.editor = editor;
    this.symbols = new ArrayList<>();
    this.updateItems(symbols);
  }

  private void updateItems(List<Symbol> symbols) {
    if (symbols == null) {
      return;
    }

    this.symbols.clear();
    this.symbols.addAll(symbols);
    this.symbols.removeIf(Objects::isNull);
  }

  @SuppressLint("NotifyDataSetChanged")
  public void refresh(IDEEditor editor, List<Symbol> newSymbols) {
    this.editor = Objects.requireNonNull(editor);

    if (this.symbols.equals(newSymbols)) {
      // no need to update symbols
      return;
    }

    updateItems(newSymbols);
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public VH onCreateViewHolder(@NonNull ViewGroup parent, int itemType) {
    return new VH(
        LayoutSymbolItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull VH holder, int position) {
    final Symbol symbol = symbols.get(position);
    holder.binding.symbol.setText(symbol.getLabel());
    holder.binding.symbol.setTextColor(
        resolveAttr(holder.binding.symbol.getContext(), R.attr.colorOnSurface));
    holder.binding.symbol.setOnClickListener(
        __ -> insertSymbol(symbol.getCommit(), symbol.getOffset()));
  }

  @Override
  public int getItemCount() {
    return symbols.size();
  }

  void insertSymbol(String text, int selectionOffset) {
    if (selectionOffset < 0 || selectionOffset > text.length()) {
      return;
    }

    final var controller = editor.getSnippetController();
    if ("\t".equals(text) && controller.isInSnippet()) {
      controller.shiftToNextTabStop();
      return;
    }

    if ("\t".equals(text)) {
      editor.indentOrCommitTab();
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
      editor.moveSelection(SelectionMovement.RIGHT);
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
