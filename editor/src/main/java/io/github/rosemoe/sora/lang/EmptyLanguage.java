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

import io.github.rosemoe.sora.lang.analysis.AnalyzeManager;
import io.github.rosemoe.sora.lang.analysis.StyleReceiver;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.lang.smartEnter.NewlineHandler;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.widget.SymbolPairMatch;

/**
 * Empty language
 *
 * @author Rosemoe
 */
public class EmptyLanguage implements Language {

    @Override
    public CharSequence format(CharSequence text) {
        return text;
    }

    @Override
    public SymbolPairMatch getSymbolPairs() {
        return new SymbolPairMatch.DefaultSymbolPairs();
    }

    @Override
    public void requireAutoComplete(@NonNull ContentReference content, @NonNull CharPosition position, @NonNull CompletionPublisher publisher, @NonNull Bundle extraArguments) {

    }

    @Override
    public int getInterruptionLevel() {
        return INTERRUPTION_LEVEL_STRONG;
    }

    @Override
    public NewlineHandler[] getNewlineHandlers() {
        return new NewlineHandler[0];
    }

    @NonNull
    @Override
    public AnalyzeManager getAnalyzeManager() {
        return EmptyAnalyzeManager.INSTANCE;
    }

    @Override
    public int getIndentAdvance(@NonNull ContentReference content, int line, int column) {
        return 0;
    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean useTab() {
        return false;
    }



    public static class EmptyAnalyzeManager implements AnalyzeManager {

        public final static EmptyAnalyzeManager INSTANCE = new EmptyAnalyzeManager();

        @Override
        public void setReceiver(@Nullable StyleReceiver receiver) {

        }

        @Override
        public void reset(@NonNull ContentReference content, @NonNull Bundle extraArguments) {

        }

        @Override
        public void insert(CharPosition start, CharPosition end, CharSequence insertedContent) {

        }

        @Override
        public void delete(CharPosition start, CharPosition end, CharSequence deletedContent) {

        }

        @Override
        public void rerun() {

        }

        @Override
        public void destroy() {

        }
    }
}

