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
package com.itsaky.androidide.language.xml;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.language.CommonCompletionProvider;
import com.itsaky.androidide.language.IDELanguage;
import com.itsaky.androidide.lexers.xml.XMLLexer;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.views.editor.IDEEditor;
import com.itsaky.lsp.api.ILanguageServer;
import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class XMLLanguage extends IDELanguage {

    private static final Logger LOG = Logger.instance("XMLLanguage");
    private final CommonCompletionProvider completer;
    private final NewlineHandler[] newlineHandlers;
    private XMLAnalyzer analyzer;

    public XMLLanguage() {
        this.completer = new CommonCompletionProvider(getLanguageServer());
        this.analyzer = new XMLAnalyzer();
        this.newlineHandlers = new NewlineHandler[0];
    }

    public boolean isAutoCompleteChar(char ch) {
        return JavaCharacter.isJavaIdentifierPart(ch) || ch == '<' || ch == '/';
    }

    public int getIndentAdvance(String content) {
        try {
            XMLLexer lexer = new XMLLexer(CharStreams.fromReader(new StringReader(content)));
            Token token;
            int advance = 0;
            while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
                switch (token.getType()) {
                    case XMLLexer.OPEN:
                    case XMLLexer.OPEN_SLASH:
                    case XMLLexer.XMLDeclOpen:
                        advance++;
                        break;
                    case XMLLexer.CLOSE:
                    case XMLLexer.SLASH_CLOSE:
                    case XMLLexer.SPECIAL_CLOSE:
                        advance--;
                        break;
                    default:
                        break;
                }
            }
            advance = Math.max(0, advance);
            return advance * getTabSize();
        } catch (Throwable e) {
            LOG.error("Failed to compute indent advance", e);
        }
        return 0;
    }

    @Override
    public SymbolPairMatch getSymbolPairs() {
        return new SymbolPairMatch.DefaultSymbolPairs();
    }

    @NonNull
    @Override
    public AnalyzeManager getAnalyzeManager() {
        return analyzer;
    }

    @Override
    public int getInterruptionLevel() {
        return INTERRUPTION_LEVEL_STRONG;
    }

    @Override
    public void requireAutoComplete(
            @NonNull ContentReference content,
            @NonNull CharPosition position,
            @NonNull CompletionPublisher publisher,
            @NonNull Bundle extraArguments)
            throws CompletionCancelledException {
        if (!extraArguments.containsKey(IDEEditor.KEY_FILE)) {
            return;
        }

        final var file = Paths.get(extraArguments.getString(IDEEditor.KEY_FILE));
        publisher.setUpdateThreshold(0);
        publisher.addItems(
                new ArrayList<>(
                        completer.complete(
                                content,
                                file,
                                position,
                                CommonCompletionProvider::checkXMLCompletionChar)));
    }

    @Override
    public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
        final var text = content.getLine(line).substring(0, column);
        return getIndentAdvance(text);
    }

    @Override
    public boolean useTab() {
        return false;
    }

    @Override
    protected ILanguageServer getLanguageServer() {
        return StudioApp.getInstance().getXMLLanguageServer();
    }

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return newlineHandlers;
    }

    @Override
    public void destroy() {
        analyzer = null;
    }
}
