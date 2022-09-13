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
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * $Id: MethodGenerator.java,v 1.10 2010-11-01 04:34:19 joehw Exp $
 */
package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;

/**
 * <p>This pseudo-instruction marks the beginning of a region of byte code that
 * can be copied into a new method, termed an "outlineable" chunk.  The size of
 * the Java stack must be the same at the start of the region as it is at the
 * end of the region, any value on the stack at the start of the region must not
 * be consumed by an instruction in the region of code, the region must not
 * contain a return instruction, no branch instruction in the region is
 * permitted to have a target that is outside the region, and no branch
 * instruction outside the region is permitted to have a target that is inside
 * the region.</p>
 * <p>The end of the region is marked by an {@link OutlineableChunkEnd}
 * pseudo-instruction.</p>
 * <p>Such a region of code may contain other outlineable regions.</p>
 */
class OutlineableChunkStart extends MarkerInstruction {
    /**
     * A constant instance of {@link OutlineableChunkStart}.  As it has no fields,
     * there should be no need to create an instance of this class.
     */
    public static final Instruction OUTLINEABLECHUNKSTART =
                                                new OutlineableChunkStart();

    /**
     * Private default constructor.  As it has no fields,
     * there should be no need to create an instance of this class.  See
     * {@link OutlineableChunkStart#OUTLINEABLECHUNKSTART}.
     */
    private OutlineableChunkStart() {
    }

    /**
     * Get the name of this instruction.  Used for debugging.
     * @return the instruction name
     */
    public String getName() {
        return OutlineableChunkStart.class.getName();
    }

    /**
     * Get the name of this instruction.  Used for debugging.
     * @return the instruction name
     */
    public String toString() {
        return getName();
    }

    /**
     * Get the name of this instruction.  Used for debugging.
     * @return the instruction name
     */
    public String toString(boolean verbose) {
        return getName();
    }
}
