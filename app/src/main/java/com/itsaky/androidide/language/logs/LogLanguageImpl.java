package com.itsaky.androidide.language.logs;

import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.utils.Either;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.EditorLanguage;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.SymbolPairMatch;
import java.util.ArrayList;
import java.util.List;

public class LogLanguageImpl implements EditorLanguage {
	
	private static final LogAnalyzer analyzer = new LogAnalyzer();
	private static final LogCompletor completor = new LogCompletor();
	
	public LogAnalyzer addLine(LogLine line) {
		return analyzer.addLine(line);
	}
	
	@Override
	public CodeAnalyzer getAnalyzer() {
		return analyzer;
	}
	
	@Override
	public AutoCompleteProvider getAutoCompleteProvider() {
		return completor;
	}
	
	@Override
	public boolean isAutoCompleteChar(char ch) {
		return false;
	}
	
	@Override
	public int getIndentAdvance(String content) {
		return 0;
	}
	
	@Override
	public boolean useTab() {
		return false;
	}
	
	@Override
	public CharSequence format(CharSequence text) {
		return text;
	}
	
	@Override
	public SymbolPairMatch getSymbolPairs() {
		return new SymbolPairMatch.DefaultSymbolPairs();
	}
	
	@Override
	public NewlineHandler[] getNewlineHandlers() {
		return new NewlineHandler[0];
	}
	
	private static class LogAnalyzer implements CodeAnalyzer {
		
		private static final List<LogLine> lines = new ArrayList<>();
		
		public LogAnalyzer addLine(LogLine line) {
			if(line != null)
				lines.add(line);
				
			return this;
		}
		
		@Override
		public void analyze(CharSequence content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
			int lastLine = 0;
			for(int i=0;i<lines.size() && delegate.shouldAnalyze();i++) {
				if(i==0) colors.addNormalIfNull();
				LogLine line = lines.get(i);
				colors.addIfNeeded(i, 0, line.priority);
				lastLine = i;
			}
			colors.determine(lastLine);
		}
	}
	
	private static class LogCompletor implements AutoCompleteProvider {
		
		@Override
		public List<Either<SuggestItem, CompletionItem>> getAutoCompleteItems(String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int line) {
			return new ArrayList<Either<SuggestItem, CompletionItem>>();
		}
	}
}
