package com.itsaky.androidide.syntax.lexer.impls;

import android.graphics.Color;
import com.itsaky.androidide.syntax.lexer.DefaultLexer;
import io.github.rosemoe.editor.struct.BlockLine;
import io.github.rosemoe.editor.struct.HexColor;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.Lexer;

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
	public final HashMap<HexColor, Integer> lineColors = new HashMap<>();
	public final Stack<BlockLine> stack = new Stack<BlockLine>();
	protected ArrayList<Integer> builtinTypes;
	
	protected void addHexColorIfPresent() {
		Matcher m = HEX.matcher(text());
		final int line= line();
		final int column = column();
		if (m.find()) {
			try {
				final CharPosition start = new CharPosition(line, column + m.start());
				final CharPosition end = new CharPosition(line, start.column + (m.end() - m.start()));
				final HexColor key = new HexColor(start, end);
				final Integer value = Color.parseColor(text().substring(m.start(), m.end()));

				// Null checks
				start.getClass();
				end.getClass();
				key.getClass();
                
				value.getClass();

				lineColors.put(key, value);
			} catch (Throwable th) {}
		}
	}

	protected final Pattern HEX = Pattern.compile("#[a-fA-F0-9]{3,8}");
}
