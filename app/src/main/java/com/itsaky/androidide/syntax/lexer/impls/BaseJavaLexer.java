/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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


package com.itsaky.androidide.syntax.lexer.impls;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.itsaky.androidide.syntax.lexer.DefaultLexer;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.Span;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.widget.EditorColorScheme;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

/**
 * Java and Groovy lexer is almost same
 * Only difference is that groovy has a static field SINGLE_QUOTE_STRING
 * 
 * This class contains common properties included in both lexers
 */
public abstract class BaseJavaLexer extends DefaultLexer {
	
    protected Lexer lexer;
	protected Content content;
	public int lastLine;
	public int maxSwitch;
	public int currSwitch;
	protected int previous;
	protected boolean wasClassName;
	protected boolean isFirst;
	protected org.antlr.v4.runtime.Token currentToken;

	protected TextAnalyzeResult colors;
	public final Stack<BlockLine> stack = new Stack<BlockLine>();
	protected ArrayList<Integer> builtinTypes;
	
	protected void addHexColorIfPresent(@NonNull Token token, final Span span, TextAnalyzeResult result, int endLine, int endColumn) {
		Matcher m = HEX.matcher(token.getText());
		if (m.find()) {
			try {
				span.underlineColor = Color.parseColor(token.getText().substring(m.start(), m.end()));
				span.underlineHeight = Span.HEX_COLOR_UNDERLINE_HEIGHT;

				result.addIfNeeded(endLine, endColumn, EditorColorScheme.TEXT_NORMAL);
			} catch (Throwable th) {
				// Ignored
				// May happen because of invalid hex color code
			}
		}
	}

	protected final Pattern HEX = Pattern.compile("#[a-fA-F0-9]{3,8}");
}
