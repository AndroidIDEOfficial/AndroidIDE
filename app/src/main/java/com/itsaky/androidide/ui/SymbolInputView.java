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
package com.itsaky.androidide.ui;

import static com.itsaky.androidide.utils.ResourceUtilsKt.resolveAttr;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.adapters.SymbolInputAdapter;
import com.itsaky.androidide.editor.ui.IDEEditor;

public class SymbolInputView extends RecyclerView {

  private SymbolInputAdapter adapter;

  public SymbolInputView(Context context) {
    this(context, null);
  }

  public SymbolInputView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SymbolInputView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
  }

  public void bindEditor(IDEEditor editor) {
    adapter = new SymbolInputAdapter(editor);
    setAdapter(adapter);
    setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
  }

  public void setSymbols(Symbol... symbols) {
    if (adapter != null) {
      adapter.setSymbols(true, symbols);
    }
  }

  public static class Symbol {
    private final String label;
    private final String commit;
    private final int offset;

    public Symbol(String both) {
      this(both, 1);
    }

    public Symbol(String both, int offset) {
      this(both, both, offset);
    }

    public Symbol(String label, String commit, int offset) {
      this.label = label;
      this.commit = commit;
      this.offset = offset;
    }

    public Symbol(String label, String commit) {
      this(label, commit, 1);
    }

    public String getLabel() {
      return label;
    }

    public String getCommit() {
      return commit;
    }

    public int getOffset() {
      return offset;
    }
  }
}
