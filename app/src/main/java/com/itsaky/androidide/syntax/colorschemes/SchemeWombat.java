package com.itsaky.androidide.syntax.colorschemes;

import io.github.rosemoe.editor.widget.EditorColorScheme;

public class SchemeWombat extends EditorColorScheme {

	@Override
	public void applyDefault() {
		super.applyDefault();
		setColor(TEXT_NORMAL, 0xfff6f3e8);
		setColor(WHOLE_BACKGROUND, 0xff242424);
		setColor(LINE_NUMBER_BACKGROUND, 0xff242424);
		setColor(SELECTED_TEXT_BACKGROUND, 0xff898941);
		setColor(MATCHED_TEXT_BACKGROUND, 0xff898941);
		setColor(TEXT_SELECTED, 0xff000000);
		setColor(CURRENT_LINE, 0xff656565);
		setColor(LINE_NUMBER, 0xff656565);
		setColor(LINE_DIVIDER, 0xff656565);
		setColor(COMMENT, 0xff99968b);
		setColor(NON_PRINTABLE_CHAR, 0xff99968b);
		setColor(TYPE_NAME, 0xfff08080);
		setColor(ANNOTATION, 0xff808080);
		setColor(OPERATOR, 0xfff3f6ee);
		setColor(METHOD, 0xfff3f6ee);
		setColor(LITERAL, 0xff96e454);
		setColor(OPERATOR, 0xff8ac6f2);
		setColor(LOCAL_VARIABLE, 0xffd4c4a9);
		setColor(SELECTION_HANDLE, 0xfff44336);
	}
}
