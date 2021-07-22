package com.itsaky.androidide.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.utils.TypefaceUtils;
import io.github.rosemoe.editor.widget.CodeEditor;
import io.github.rosemoe.editor.widget.SymbolChannel;
import android.util.TypedValue;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.adapters.SymbolInputAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

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
	
	public void bindEditor(CodeEditor editor) {
		adapter = new SymbolInputAdapter(editor);
		setAdapter(adapter);
		setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
	}
	
	public void setSymbols(Symbol... symbols) {
		if(adapter != null) {
			adapter.setSymbols(true, symbols);
		}
	}
	
	public static class Symbol {
		public String label;
		public String commit;
		public int offset;
		
		public Symbol(String both) {
			this(both, 1);
		}
		
		public Symbol(String both, int offset) {
			this(both, both, offset);
		}
		
		public Symbol(String label, String commit) {
			this(label, commit, 1);
		}
		
		public Symbol(String label, String commit, int offset) {
			this.label = label;
			this.commit = commit;
			this.offset = offset;
		}
	}
}
