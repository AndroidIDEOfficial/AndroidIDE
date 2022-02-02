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
package io.github.rosemoe.sora.text;

import android.annotation.SuppressLint;
import android.icu.text.Bidi;
import android.os.Build;

import androidx.annotation.IntRange;
import androidx.annotation.RequiresApi;

import io.github.rosemoe.sora.annotations.UnsupportedUserUsage;

/**
 * Bidi algorithm for text directions.
 *
 * Replacement of android.text.AndroidBidi
 * @author Rosemoe
 */
@SuppressLint("PrivateApi")
@UnsupportedUserUsage
public final class AndroidBidi {

    public final static int RUN_LENGTH_MASK = 0x03ffffff;
    public static final int RUN_LEVEL_SHIFT = 26;
    public static final int RUN_LEVEL_MASK = 0x3f;
    public static final int RUN_RTL_FLAG = 1 << RUN_LEVEL_SHIFT;

    public static final int DIR_LEFT_TO_RIGHT = 1;
    public static final int DIR_RIGHT_TO_LEFT = -1;

    public static final Directions DIRS_ALL_LEFT_TO_RIGHT =
            new Directions(new int[] { 0, RUN_LENGTH_MASK });

    public static final Directions DIRS_ALL_RIGHT_TO_LEFT =
            new Directions(new int[] { 0, RUN_LENGTH_MASK | RUN_RTL_FLAG });

    public static final int DIR_REQUEST_LTR = 1;
    public static final int DIR_REQUEST_RTL = -1;
    public static final int DIR_REQUEST_DEFAULT_LTR = 2;
    public static final int DIR_REQUEST_DEFAULT_RTL = -2;

    private static Class<?> bidiClass;
    static {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            try {
                bidiClass = Class.forName("android.text.AndroidBidi");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                bidiClass = null;
            }
        } else {
            bidiClass = null;
        }
    }

    public static int bidi(int dir, char[] chs, byte[] chInfo) {
        // TODO
        return 0;
    }

    private static int bidiImplLollipop(int dir, char[] chs, byte[] chInfo) {
        // Android API 21-27
        // public static int bidi(int dir, char[] chs, byte[] chInfo, int n, boolean haveInfo)
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private static int bidiImplP(int dir, char[] chs, byte[] chInfo) {
        // Android API 28+
        // public static int bidi(int dir, char[] chs, byte[] chInfo)
       return 0;
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private static int bidiImplQ(int dir, char[] chs, byte[] chInfo) {
        if (chs == null || chInfo == null) {
            throw new NullPointerException();
        }

        final int length = chs.length;
        if (chInfo.length < length) {
            throw new IndexOutOfBoundsException();
        }

        final byte paraLevel;
        switch (dir) {
            case DIR_REQUEST_RTL: paraLevel = Bidi.RTL; break;
            case DIR_REQUEST_DEFAULT_LTR: paraLevel = Bidi.LEVEL_DEFAULT_LTR; break;
            case DIR_REQUEST_DEFAULT_RTL: paraLevel = Bidi.LEVEL_DEFAULT_RTL; break;
            case DIR_REQUEST_LTR:
            default: paraLevel = Bidi.LTR; break;
        }
        final Bidi icuBidi = new Bidi(length /* maxLength */, 0 /* maxRunCount */);
        icuBidi.setPara(chs, paraLevel, null /* embeddingLevels */);
        for (int i = 0; i < length; i++) {
            chInfo[i] = icuBidi.getLevelAt(i);
        }
        final byte result = icuBidi.getParaLevel();
        return (result & 0x1) == 0 ? DIR_LEFT_TO_RIGHT : DIR_RIGHT_TO_LEFT;
    }

    /**
     * Returns run direction information for a line within a paragraph.
     *
     * @param dir baseline direction, either Layout.DIR_LEFT_TO_RIGHT or
     *     Layout.DIR_RIGHT_TO_LEFT
     * @param levels levels as returned from {@link #bidi}
     * @param lstart start of the line in the levels array
     * @param chars the character array (used to determine whitespace)
     * @param cstart the start of the line in the chars array
     * @param len the length of the line
     * @return the directions
     */
    public static Directions directions(int dir, byte[] levels, int lstart,
                                        char[] chars, int cstart, int len) {
        if (len == 0) {
            return DIRS_ALL_LEFT_TO_RIGHT;
        }

        int baseLevel = dir == DIR_LEFT_TO_RIGHT ? 0 : 1;
        int curLevel = levels[lstart];
        int minLevel = curLevel;
        int runCount = 1;
        for (int i = lstart + 1, e = lstart + len; i < e; ++i) {
            int level = levels[i];
            if (level != curLevel) {
                curLevel = level;
                ++runCount;
            }
        }

        // add final run for trailing counter-directional whitespace
        int visLen = len;
        if ((curLevel & 1) != (baseLevel & 1)) {
            // look for visible end
            while (--visLen >= 0) {
                char ch = chars[cstart + visLen];

                if (ch == '\n') {
                    --visLen;
                    break;
                }

                if (ch != ' ' && ch != '\t') {
                    break;
                }
            }
            ++visLen;
            if (visLen != len) {
                ++runCount;
            }
        }

        if (runCount == 1 && minLevel == baseLevel) {
            // we're done, only one run on this line
            if ((minLevel & 1) != 0) {
                return DIRS_ALL_RIGHT_TO_LEFT;
            }
            return DIRS_ALL_LEFT_TO_RIGHT;
        }

        int[] ld = new int[runCount * 2];
        int maxLevel = minLevel;
        int levelBits = minLevel << RUN_LEVEL_SHIFT;
        {
            // Start of first pair is always 0, we write
            // length then start at each new run, and the
            // last run length after we're done.
            int n = 1;
            int prev = lstart;
            curLevel = minLevel;
            for (int i = lstart, e = lstart + visLen; i < e; ++i) {
                int level = levels[i];
                if (level != curLevel) {
                    curLevel = level;
                    if (level > maxLevel) {
                        maxLevel = level;
                    } else if (level < minLevel) {
                        minLevel = level;
                    }
                    // XXX ignore run length limit of 2^RUN_LEVEL_SHIFT
                    ld[n++] = (i - prev) | levelBits;
                    ld[n++] = i - lstart;
                    levelBits = curLevel << RUN_LEVEL_SHIFT;
                    prev = i;
                }
            }
            ld[n] = (lstart + visLen - prev) | levelBits;
            if (visLen < len) {
                ld[++n] = visLen;
                ld[++n] = (len - visLen) | (baseLevel << RUN_LEVEL_SHIFT);
            }
        }

        // See if we need to swap any runs.
        // If the min level run direction doesn't match the base
        // direction, we always need to swap (at this point
        // we have more than one run).
        // Otherwise, we don't need to swap the lowest level.
        // Since there are no logically adjacent runs at the same
        // level, if the max level is the same as the (new) min
        // level, we have a series of alternating levels that
        // is already in order, so there's no more to do.
        //
        boolean swap;
        if ((minLevel & 1) == baseLevel) {
            minLevel += 1;
            swap = maxLevel > minLevel;
        } else {
            swap = runCount > 1;
        }
        if (swap) {
            for (int level = maxLevel - 1; level >= minLevel; --level) {
                for (int i = 0; i < ld.length; i += 2) {
                    if (levels[ld[i]] >= level) {
                        int e = i + 2;
                        while (e < ld.length && levels[ld[e]] >= level) {
                            e += 2;
                        }
                        for (int low = i, hi = e - 2; low < hi; low += 2, hi -= 2) {
                            int x = ld[low]; ld[low] = ld[hi]; ld[hi] = x;
                            x = ld[low+1]; ld[low+1] = ld[hi+1]; ld[hi+1] = x;
                        }
                        i = e + 2;
                    }
                }
            }
        }
        return new Directions(ld);
    }

    /**
     * Replacement of {@link android.text.Layout.Directions}
     */
    public static class Directions {

        private final int[] mDirections;

        public Directions(int[] directions) {
            mDirections = directions;
        }

        /**
         * Returns number of BiDi runs.
         */
        public @IntRange(from = 0) int getRunCount() {
            return mDirections.length / 2;
        }

        /**
         * Returns the start offset of the BiDi run.
         *
         * @param runIndex the index of the BiDi run
         * @return the start offset of the BiDi run.
         */
        public @IntRange(from = 0) int getRunStart(@IntRange(from = 0) int runIndex) {
            return mDirections[runIndex * 2];
        }

        /**
         * Returns the length of the BiDi run.
         *
         * Note that this method may return too large number due to reducing the number of object
         * allocations. The too large number means the remaining part is assigned to this run. The
         * caller must clamp the returned value.
         *
         * @param runIndex the index of the BiDi run
         * @return the length of the BiDi run.
         */
        public @IntRange(from = 0) int getRunLength(@IntRange(from = 0) int runIndex) {
            return mDirections[runIndex * 2 + 1] & RUN_LENGTH_MASK;
        }

        /**
         * Returns true if the BiDi run is RTL.
         *
         * @param runIndex the index of the BiDi run
         * @return true if the BiDi run is RTL.
         */
        public boolean isRunRtl(int runIndex) {
            return (mDirections[runIndex * 2 + 1] & RUN_RTL_FLAG) != 0;
        }

    }

}
