package io.github.rosemoe.editor.struct;

import io.github.rosemoe.editor.text.CharPosition;

public class HexColor {
	public CharPosition start;
	public CharPosition end;

	public HexColor(CharPosition start, CharPosition end) {
		this.start = start;
		this.end = end;
	}
	
	public boolean isSameLine(int line) {
		return start.line == line && end.line == line;
	}
	
	public boolean isSameLine(HexColor color) {
		return color != null && start.line == color.start.line && end.line == color.end.line;
	}
	
	public boolean isSame(HexColor color) {
		return color != null && color.start.equals(start) && color.end.equals(end);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof HexColor && isSame((HexColor) obj);
	}
}
