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
package io.github.rosemoe.sora.data;

import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.sora.lang.styling.CodeBlock;

/**
 * An object provider for speed improvement
 * Now meaningless because it is not as well as it expected
 *
 * @author Rose
 */
public class ObjectAllocator {

    private static final int RECYCLE_LIMIT = 1024 * 8;
    private static List<CodeBlock> codeBlocks;
    private static List<CodeBlock> tempArray;

    public static void recycleBlockLines(List<CodeBlock> src) {
        if (src == null) {
            return;
        }
        if (codeBlocks == null) {
            codeBlocks = src;
            return;
        }
        int size = codeBlocks.size();
        int sizeAnother = src.size();
        while (sizeAnother > 0 && size < RECYCLE_LIMIT) {
            size++;
            sizeAnother--;
            var obj = src.get(sizeAnother);
            obj.clear();
            codeBlocks.add(obj);
        }
        src.clear();
        synchronized (ObjectAllocator.class) {
            tempArray = src;
        }
    }

    public static List<CodeBlock> obtainList() {
        List<CodeBlock> temp = null;
        synchronized (ObjectAllocator.class) {
            temp = tempArray;
            tempArray = null;
        }
        if (temp == null) {
            temp = new ArrayList<>(128);
        }
        return temp;
    }

    public static CodeBlock obtainBlockLine() {
        return (codeBlocks == null || codeBlocks.isEmpty()) ? new CodeBlock() : codeBlocks.remove(codeBlocks.size() - 1);
    }

}
