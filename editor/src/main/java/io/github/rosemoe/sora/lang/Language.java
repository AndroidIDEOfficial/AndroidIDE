/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.lang;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.completion.CompletionCancelledException;
import io.github.rosemoe.sora.lang.completion.CompletionHelper;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

/**
 * Language for editor
 * <p>
 * A Language helps editor to highlight text and provide auto-completion.
 * Implement this interface when you want to add new language support for editor.
 * <p>
 * <strong>NOTE:</strong> A language must not be single instance.
 * One language instance should always serve for only one editor.
 * It means that you should not give one language object to other editor instances
 * after it has been applied to one editor.
 *
 * @author Rosemoe
 */
public interface Language {

    /**
     * Set the thread's interrupted flag by calling {@link Thread#interrupt()}.
     *
     * Throw {@link io.github.rosemoe.sora.lang.completion.CompletionCancelledException} exceptions
     * from {@link ContentReference} and {@link CompletionPublisher}.
     *
     * Set thread's flag for abortion.
     */
    int INTERRUPTION_LEVEL_STRONG = 0;
    /**
     * Throw {@link io.github.rosemoe.sora.lang.completion.CompletionCancelledException} exceptions
     * from {@link ContentReference} and {@link CompletionPublisher}.
     *
     * Set thread's flag for abortion.
     */
    int INTERRUPTION_LEVEL_SLIGHT = 1;
    /**
     * Throw {@link io.github.rosemoe.sora.lang.completion.CompletionCancelledException} exceptions
     * from {@link ContentReference}
     *
     * Set thread's flag for abortion.
     */
    int INTERRUPTION_LEVEL_NONE = 2;

    /**
     * Get {@link AnalyzeManager} of the language.
     * This is called from time to time by the editor. Cache your instance please.
     */
    @NonNull
    AnalyzeManager getAnalyzeManager();

    /**
     * Get the interruption level for auto-completion.
     *
     * @see #INTERRUPTION_LEVEL_STRONG
     * @see #INTERRUPTION_LEVEL_SLIGHT
     * @see #INTERRUPTION_LEVEL_NONE
     */
    int getInterruptionLevel();

    /**
     * Request to auto-complete the code at the given {@code position}.
     * This is called in a worker thread other than UI thread.
     *
     * @see ContentReference
     * @see CompletionPublisher
     * @see #getInterruptionLevel()
     * @see CompletionHelper#checkCancelled()
     * @param content Read-only reference of content
     * @param position The position for auto-complete
     * @param publisher The publisher used to update items
     * @param extraArguments Arguments set by {@link io.github.rosemoe.sora.widget.CodeEditor#setText(CharSequence, Bundle)}
     * @throws io.github.rosemoe.sora.lang.completion.CompletionCancelledException This thread can be abandoned
     * by the editor framework because the auto-completion items of
     *  this invocation are no longer needed by the user. This can either be thrown
     * by {@link ContentReference} or {@link CompletionPublisher}. How the exceptions will be thrown is according to
     * your settings: {@link #getInterruptionLevel()}
     */
    @WorkerThread
    void requireAutoComplete(@NonNull ContentReference content, @NonNull CharPosition position,
                             @NonNull CompletionPublisher publisher,
                             @NonNull Bundle extraArguments) throws CompletionCancelledException;

    /**
     * Get advance for indent
     *
     * @param content Content of a line
     * @return Advance space count
     */
    @UiThread
    int getIndentAdvance(@NonNull ContentReference content, int line, int column);

    /**
     * Use tab to format
     */
    @UiThread
    boolean useTab();

    /**
     * Format the given content
     *
     * @param text Content to format
     * @return Formatted code
     */
    @WorkerThread
    CharSequence format(CharSequence text);

    /**
     * Returns language specified symbol pairs.
     * The method is called only once when the language is applied.
     */
    @UiThread
    SymbolPairMatch getSymbolPairs();

    /**
     * Get newline handlers of this language.
     * This method is called each time the user presses ENTER key.
     * <p>
     * Pay attention to the performance as this method is called frequently
     *
     * @return NewlineHandlers , maybe null
     */
    @UiThread
    @Nullable
    NewlineHandler[] getNewlineHandlers();

    void destroy();

}
