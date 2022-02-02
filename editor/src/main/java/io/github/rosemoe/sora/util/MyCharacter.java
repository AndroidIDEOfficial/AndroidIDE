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
package io.github.rosemoe.sora.util;

import java.util.Arrays;

/**
 * @author Rose
 * Get whether Identifier part/start quickly
 */
public class MyCharacter {

    static {
        initMapInternal();
    }

    /**
     * Compressed bit set for isJavaIdentifierStart()
     */
    private static int[] bitsIsStart;

    /**
     * Compressed bit set for isJavaIdentifierPart()
     */
    private static int[] bitsIsPart;

    /**
     * Get bit in compressed bit set
     *
     * @param values   Compressed bit set
     * @param bitIndex Target index
     * @return Boolean value at the index
     */
    private static boolean get(int[] values, int bitIndex) {
        return ((values[bitIndex / 32] & (1 << (bitIndex % 32))) != 0);
    }

    /**
     * Make the given position's bit true
     *
     * @param values   Compressed bit set
     * @param bitIndex Index of bit
     */
    private static void set(int[] values, int bitIndex) {
        values[bitIndex / 32] |= (1 << (bitIndex % 32));
    }

    /**
     * Init maps
     *
     * @deprecated The class will be initialized automatically
     */
    @Deprecated
    public static void initMap() {
        // Empty
    }

    /**
     * Init maps
     */
    private static void initMapInternal() {
        if (bitsIsStart != null) {
            return;
        }
        bitsIsPart = new int[2048];
        bitsIsStart = new int[2048];
        Arrays.fill(bitsIsPart, 0);
        Arrays.fill(bitsIsStart, 0);
        for (int i = 0; i <= 65535; i++) {
            if (Character.isJavaIdentifierPart((char) i)) {
                set(bitsIsPart, i);
            }
            if (Character.isJavaIdentifierStart((char) i)) {
                set(bitsIsStart, i);
            }
        }
    }

    /**
     * @param key Character
     * @return Whether a identifier part
     * @see Character#isJavaIdentifierPart(char)
     */
    public static boolean isJavaIdentifierPart(int key) {
        return get(bitsIsPart, key);
    }

    /**
     * @param key Character
     * @return Whether a identifier start
     * @see Character#isJavaIdentifierStart(char)
     */
    public static boolean isJavaIdentifierStart(int key) {
        return get(bitsIsStart, key);
    }

}

