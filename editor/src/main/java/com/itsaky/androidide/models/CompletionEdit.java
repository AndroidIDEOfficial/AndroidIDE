package com.itsaky.androidide.models;

public class CompletionEdit {
	public String insertText;
	public InsertPosition position;

	public CompletionEdit(String insertText, InsertPosition position) {
		this.insertText = insertText;
		this.position = position;
	}
	
	public static class InsertPosition {
		public int line;
		public int column;
		
		public InsertPosition(int line) {
			this(line, -1);
		}
		
		public InsertPosition(int line, int column) {
			this.line = line;
			this.column = column;
		}
	}
}
