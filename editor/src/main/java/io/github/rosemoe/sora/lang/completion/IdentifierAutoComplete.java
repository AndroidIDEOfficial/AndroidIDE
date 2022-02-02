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
package io.github.rosemoe.sora.lang.completion;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.github.rosemoe.sora.lang.Language;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;

/**
 * Identifier auto-completion.
 *
 * You can use it to provide identifiers, but you can't update the given {@link CompletionPublisher}
 * if it is used. If you have to mix the result, then you should call {@link CompletionPublisher#setComparator(Comparator)}
 * with null first. Otherwise, your completion list may be corrupted. And in that case, you must do the sorting
 * work by yourself and then add your items.
 *
 * @author Rosemoe
 */
public class IdentifierAutoComplete {

    private String[] mKeywords;
    private boolean mKeywordsAreLowCase;

    public IdentifierAutoComplete() {
    }

    public IdentifierAutoComplete(String[] keywords) {
        this();
        setKeywords(keywords, true);
    }

    public void setKeywords(String[] keywords, boolean lowCase) {
        mKeywords = keywords;
        mKeywordsAreLowCase = lowCase;
    }

    public String[] getKeywords() {
        return mKeywords;
    }

    public static class Identifiers {

        private final List<String> identifiers = new ArrayList<>(128);
        private HashMap<String, Object> cache;
        private final static Object SIGN = new Object();

        public void addIdentifier(String identifier) {
            if (cache == null) {
                throw new IllegalStateException("begin() has not been called");
            }
            if (cache.put(identifier, SIGN) == SIGN) {
                return;
            }
            identifiers.add(identifier);
        }

        public void begin() {
            cache = new HashMap<>();
        }

        public void finish() {
            cache.clear();
            cache = null;
        }

        public List<String> getIdentifiers() {
            return identifiers;
        }

    }

    /**
     * Make completion items for the given arguments.
     * Provide the required arguments passed by {@link Language#requireAutoComplete(ContentReference, CharPosition, CompletionPublisher,  Bundle)}
     * @param prefix The prefix to make completions for.
     */
    public void requireAutoComplete(String prefix, CompletionPublisher publisher, Identifiers userIdentifiers) {
        publisher.setComparator(COMPARATOR);
        publisher.setUpdateThreshold(0);
        int prefixLength = prefix.length();
        if (prefixLength == 0) {
            return;
        }
        final String[] keywordArray = mKeywords;
        final boolean lowCase = mKeywordsAreLowCase;
        String match = prefix.toLowerCase();
        if (keywordArray != null) {
            if (lowCase) {
                for (String kw : keywordArray) {
                    if (kw.startsWith(match)) {
                        publisher.addItem(new SimpleCompletionItem(kw, "Keyword", prefixLength, kw));
                    }
                }
            } else {
                for (String kw : keywordArray) {
                    if (kw.toLowerCase().startsWith(match)) {
                        publisher.addItem(new SimpleCompletionItem(kw, "Keyword", prefixLength, kw));
                    }
                }
            }
        }
        if (userIdentifiers != null) {
            List<CompletionItem> words = new ArrayList<>();
            for (String word : userIdentifiers.getIdentifiers()) {
                if (word.toLowerCase().startsWith(match)) {
                    publisher.addItem(new SimpleCompletionItem(word, "Identifier", prefixLength, word));
                }
            }
        }
    }

    private static String asString(CharSequence str) {
        return (str instanceof String ? (String) str : str.toString());
    }

    private final static Comparator<CompletionItem> COMPARATOR = (p1, p2) -> {
        var cmp1 = asString(p1.desc).compareTo(asString(p2.desc));
        if (cmp1 < 0) {
            return 1;
        } else if (cmp1 > 0) {
            return -1;
        }
        return asString(p1.label).compareTo(asString(p2.label));
    };


}
