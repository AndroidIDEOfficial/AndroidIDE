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
 * $Id: OutlineableChunkEnd.java,v 1.10 2010-11-01 04:34:19 joehw Exp $
 */
package jaxp.sun.org.apache.xalan.internal.xsltc.compiler.util;
import jaxp.sun.org.apache.bcel.internal.generic.Instruction;

/**
 * <p>Marks the end of a region of byte code that can be copied into a new
 * method.  See the {@link OutlineableChunkStart} pseudo-instruction for
 * details.</p>
 */
class OutlineableChunkEnd extends MarkerInstruction {
    /**
     * A constant instance of {@link OutlineableChunkEnd}.  As it has no fields,
     * there should be no need to create an instance of this class.
     */
    public static final Instruction OUTLINEABLECHUNKEND =
                                                new OutlineableChunkEnd();

    /**
     * Private default constructor.  As it has no fields,
     * there should be no need to create an instance of this class.  See
     * {@link OutlineableChunkEnd#OUTLINEABLECHUNKEND}.
     */
    private OutlineableChunkEnd() {
    }

    /**
     * Get the name of this instruction.  Used for debugging.
     * @return the instruction name
     */
    public String getName() {
        return OutlineableChunkEnd.class.getName();
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
