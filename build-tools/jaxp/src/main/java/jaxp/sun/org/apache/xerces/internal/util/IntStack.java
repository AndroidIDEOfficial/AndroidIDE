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

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2000-2002,2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.util;

/**
 * A simple integer based stack.
 *
 * moved to com.sun.org.apache.xerces.internal.util by neilg to support the
 * XPathMatcher.
 * @author  Andy Clark, IBM
 *
 */
public final class IntStack {

    //
    // Data
    //

    /** Stack depth. */
    private int fDepth;

    /** Stack data. */
    private int[] fData;

    //
    // Public methods
    //

    /** Returns the size of the stack. */
    public int size() {
        return fDepth;
    }

    /** Pushes a value onto the stack. */
    public void push(int value) {
        ensureCapacity(fDepth + 1);
        fData[fDepth++] = value;
    }

    /** Peeks at the top of the stack. */
    public int peek() {
        return fData[fDepth - 1];
    }

    /** Returns the element at the specified depth in the stack. */
    public int elementAt(int depth) {
        return fData[depth];
    }

    /** Pops a value off of the stack. */
    public int pop() {
        return fData[--fDepth];
    }

    /** Clears the stack. */
    public void clear() {
        fDepth = 0;
    }

    // debugging

    /** Prints the stack. */
    public void print() {
        System.out.print('(');
        System.out.print(fDepth);
        System.out.print(") {");
        for (int i = 0; i < fDepth; i++) {
            if (i == 3) {
                System.out.print(" ...");
                break;
            }
            System.out.print(' ');
            System.out.print(fData[i]);
            if (i < fDepth - 1) {
                System.out.print(',');
            }
        }
        System.out.print(" }");
        System.out.println();
    }

    //
    // Private methods
    //

    /** Ensures capacity. */
    private void ensureCapacity(int size) {
        if (fData == null) {
            fData = new int[32];
        }
        else if (fData.length <= size) {
            int[] newdata = new int[fData.length * 2];
            System.arraycopy(fData, 0, newdata, 0, fData.length);
            fData = newdata;
        }
    }

} // class IntStack
