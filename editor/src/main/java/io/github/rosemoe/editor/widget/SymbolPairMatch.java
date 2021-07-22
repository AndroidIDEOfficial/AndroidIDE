/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.widget;

import java.util.HashMap;
import java.util.Map;

/**
 * Define symbol pairs to complete them automatically when the user
 * enters the first character of pair.
 *
 * @author Rosemoe
 */
public class SymbolPairMatch {

    private Map<Character, Replacement> pairMaps = new HashMap<>();

    private SymbolPairMatch parent;

    public SymbolPairMatch() {
        this(null);
    }

    public SymbolPairMatch(SymbolPairMatch parent) {
        setParent(parent);
    }

    protected void setParent(SymbolPairMatch parent) {
        this.parent = parent;
    }

    /**
     * Put a pair of symbol completion
     * When the user types the {@param firstCharacter}, it will be replaced by {@param replacement}
     * Replacement maybe null to disable completion for this character.
     *
     * @see Replacement
     */
    public void putPair(char firstCharacter, Replacement replacement) {
        pairMaps.put(firstCharacter, replacement);
    }

    public final Replacement getCompletion(char character) {
        Replacement result = parent != null ? parent.getCompletion(character) : null;
        if (result == null) {
            result = pairMaps.get(character);
        }
        return result;
    }

    public void removeAllRules() {
        pairMaps.clear();
    }

    /**
     * Defines a replacement of input
     */
    public static class Replacement {

        /**
         * Defines that this character does not have to be replaced
         */
        public final static Replacement NO_REPLACEMENT = new Replacement("", 0);

        public final String text;

        public final int selection;

        /**
         * The entered character will be replaced to {@param text} and
         * the new cursor position will be {@param selection}
         * The value of {@param selection} maybe 0 to {@param text}.length()
         */
        public Replacement(String text, int selection) {
            this.selection = selection;
            this.text = text;
            if (selection < 0 || selection > text.length()) {
                throw new IllegalArgumentException("invalid selection value");
            }
        }

    }

    public final static class DefaultSymbolPairs extends SymbolPairMatch {

        public DefaultSymbolPairs() {
            super.putPair('{', new Replacement("{}", 1));
            super.putPair('(', new Replacement("()", 1));
            super.putPair('[', new Replacement("[]", 1));
            super.putPair('"', new Replacement("\"\"", 1));
            super.putPair('\'', new Replacement("''", 1));
        }

    }

}
