package com.itsaky.androidide.models;

import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.widget.CodeEditor;

public class GradleSuggestItem implements SuggestItem {
	
	private String label;
	private String desc;
	private String prefix;
	private boolean isArtifact;

	public GradleSuggestItem(String label, String desc, String prefix, boolean isArtifact) {
		this.label = label;
		this.desc = desc;
		this.prefix = prefix;
		this.isArtifact = isArtifact;
	}
	
	@Override
	public String getName() {
		return label;
	}
	
	@Override
	public String getDescription() {
		return desc;
	}
	
	@Override
	public String getReturnType() {
		return "";
	}
	
	@Override
	public char getTypeHeader() {
		return ' ';
	}
	
	@Override
	public int getSuggestionPriority() {
		return 1;
	}
	
	@Override
	public void onSelectThis(CodeEditor editor) {
		try {
			final int length = prefix.length();
			final int start = editor.getCursor().getLeft() - length;
			final int end = editor.getCursor().getLeft();
			final CharPosition s = editor.getText().getIndexer().getCharPosition(start);
			Content editable = editor.getText();
			editable.delete(start, end);
			editable.insert(s.line, s.column, getName());
		} catch (Throwable ignored) {}
	}
}
