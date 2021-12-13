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
package io.github.rosemoe.editor.util;

/**
 * Pack two int into a long
 * Also unpack it
 * This is convenient while passing data
 *
 * @author Rose
 */
public class IntPair {

    /**
     * Convert an integer to a long whose binary bits are equal to the given integer
     */
    private static long toUnsignedLong(int x) {
        return ((long) x) & 0xffffffffL;
    }

    /**
     * Pack two int into a long
     *
     * @param first  First of pair
     * @param second Second of pair
     * @return Packed value
     */
    public static long pack(int first, int second) {
        return (toUnsignedLong(first) << 32L) | toUnsignedLong(second);
    }

    /**
     * Get second of pair
     *
     * @param packedValue Packed value
     * @return Second of pair
     */
    public static int getSecond(long packedValue) {
        return (int) (packedValue & 0xFFFFFFFFL);
    }

    /**
     * Get first of pair
     *
     * @param packedValue Packed value
     * @return First of pair
     */
    public static int getFirst(long packedValue) {
        return (int) (packedValue >> 32L);
    }

}
