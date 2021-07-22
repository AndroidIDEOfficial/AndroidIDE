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
package io.github.rosemoe.editor.text;

public class TextUtils {

    public static int countLeadingSpaceCount(CharSequence text, int tabWidth) {
        int p = 0, count = 0;
        while (p < text.length()) {
            if (isWhitespace(text.charAt(p))) {
                if (text.charAt(p) == '\t') {
                    count += tabWidth;
                } else {
                    count++;
                }
                p++;
            } else {
                break;
            }
        }
        return count;
    }

    public static String createIndent(int indentSize, int tabWidth, boolean useTab) {
        indentSize = Math.max(0, indentSize);
        int tab = 0;
        int space;
        if (useTab) {
            tab = indentSize / tabWidth;
            space = indentSize % tabWidth;
        } else {
            space = indentSize;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tab; i++) {
            s.append('\t');
        }
        for (int i = 0; i < space; i++) {
            s.append(' ');
        }
        return s.toString();
    }

    private static boolean isWhitespace(char ch) {
        return ch == '\t' || ch == ' ';
    }

}
