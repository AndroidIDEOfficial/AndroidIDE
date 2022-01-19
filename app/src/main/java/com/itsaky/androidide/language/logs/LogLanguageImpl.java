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
package com.itsaky.androidide.language.logs;

import com.itsaky.androidide.language.BaseLanguage;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CompletionItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.interfaces.CodeAnalyzer;
import io.github.rosemoe.editor.interfaces.NewlineHandler;
import io.github.rosemoe.editor.text.Content;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import io.github.rosemoe.editor.text.TextAnalyzer;
import io.github.rosemoe.editor.widget.SymbolPairMatch;

public class LogLanguageImpl extends BaseLanguage {
	
	private static final LogAnalyzer analyzer = new LogAnalyzer();
	private static final LogCompletor completor = new LogCompletor();
	
	public void addLine(LogLine line) {
		analyzer.addLine (line);
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
	
	private static class LogAnalyzer extends io.github.rosemoe.editor.langs.AbstractCodeAnalyzer {
		
		private static final List<LogLine> lines = new ArrayList<>();
		
		public LogAnalyzer addLine(LogLine line) {
			if(line != null)
				lines.add(line);
				
			return this;
		}
		
		@Override
		public void analyze(ILanguageServer server, File file, Content content, TextAnalyzeResult colors, TextAnalyzer.AnalyzeThread.Delegate delegate) {
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
		public List<CompletionItem> getAutoCompleteItems(Content content, String fileUri, String prefix, boolean isInCodeBlock, TextAnalyzeResult colors, int index, int line, int column) {
			return new ArrayList<> ();
		}
	}
}
