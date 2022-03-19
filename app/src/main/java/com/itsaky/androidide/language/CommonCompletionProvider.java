/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.language;

import androidx.annotation.NonNull;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.api.ICompletionProvider;
import com.itsaky.lsp.api.ILanguageServer;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.CompletionParams;
import com.itsaky.lsp.models.CompletionResult;
import com.itsaky.lsp.models.Position;
import io.github.rosemoe.sora.lang.completion.CompletionHelper;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.util.MyCharacter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;

/**
 * Common implementation of completion provider which requests completions to provided language
 * server.
 *
 * @author Akash Yadav
 */
public class CommonCompletionProvider {

    private CompletableFuture<CompletionResult> future;

    private final ILanguageServer server;

    public CommonCompletionProvider(ILanguageServer server) {
        this.server = server;
    }

    /**
     * Computes completion items using the provided language server instance.
     *
     * @param content The reference to the content of the editor.
     * @param file The file to compute completions for.
     * @param position The position of the cursor in the content.
     * @return The computed completion items. May return an empty list if the there was an error
     *     computing the completion items.
     */
    public List<CompletionItem> complete(
            ContentReference content,
            Path file,
            CharPosition position,
            Predicate<Character> prefixMatcher) {
        if (this.future != null && !this.future.isDone()) {
            try {
                this.future.cancel(true);
            } catch (CancellationException e) {
                return new ArrayList<>();
            }
        }

        this.future =
                CompletableFuture.supplyAsync(
                        () -> {
                            final var prefix =
                                    CompletionHelper.computePrefix(
                                            content, position, prefixMatcher::test);
                            final var completer = server.getCompletionProvider();

                            if (!completer.canComplete(file)) {
                                return ICompletionProvider.EMPTY;
                            }

                            final var params =
                                    new CompletionParams(
                                            new Position(
                                                    position.line, position.column, position.index),
                                            file);
                            params.setContent(content);
                            params.setPrefix(prefix);
                            return completer.complete(params);
                        });

        try {
            return finalizeResults(future.get().getItems());
        } catch (Throwable e) {
            LOG.error("Unable to compute completions", e);
            return Collections.emptyList();
        }
    }

    public static boolean checkJavaCompletionChar(char c) {
        return MyCharacter.isJavaIdentifierPart(c) || c == '.';
    }

    public static boolean checkXMLCompletionChar(char c) {
        return MyCharacter.isJavaIdentifierPart(c) || c == '<' || c == '/';
    }

    @NonNull
    @Contract("_ -> param1")
    private List<CompletionItem> finalizeResults(@NonNull List<CompletionItem> items) {
        items.sort(RESULT_SORTER);
        return items;
    }

    private static final Comparator<CompletionItem> RESULT_SORTER =
            (p1, p2) -> {
                if (p1 == null && p2 == null) {
                    return 0;
                } else if (p1 == null) {
                    return -1;
                } else if (p2 == null) {
                    return 1;
                }

                String s1 = p1.getSortText() == null ? p1.getLabel() : p1.getSortText();
                String s2 = p2.getSortText() == null ? p2.getLabel() : p2.getSortText();
                return s1.compareTo(s2);
            };

    private static final Logger LOG = Logger.instance("CommonCompletionProvider");
}
