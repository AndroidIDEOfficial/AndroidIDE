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
 * Copyright 1999-2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.dom;

import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;

/**
 * Processing Instructions (PIs) permit documents to carry
 * processor-specific information alongside their actual content. PIs
 * are most common in XML, but they are supported in HTML as well.
 *
 * This class inherits from CharacterDataImpl to reuse its setNodeValue method.
 *
 * @xerces.internal
 *
 * @since  PR-DOM-Level-1-19980818.
 */
public class ProcessingInstructionImpl
    extends CharacterDataImpl
    implements ProcessingInstruction {

    //
    // Constants
    //

    /** Serialization version. */
    static final long serialVersionUID = 7554435174099981510L;

    //
    // Data
    //

    protected String target;

    //
    // Constructors
    //

    /** Factory constructor. */
    public ProcessingInstructionImpl(CoreDocumentImpl ownerDoc,
                                     String target, String data) {
        super(ownerDoc, data);
        this.target = target;
    }

    //
    // Node methods
    //

    /**
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.PROCESSING_INSTRUCTION_NODE;
    }

    /**
     * Returns the target
     */
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return target;
    }

    //
    // ProcessingInstruction methods
    //

    /**
     * A PI's "target" states what processor channel the PI's data
     * should be directed to. It is defined differently in HTML and XML.
     * <p>
     * In XML, a PI's "target" is the first (whitespace-delimited) token
     * following the "<?" token that begins the PI.
     * <p>
     * In HTML, target is always null.
     * <p>
     * Note that getNodeName is aliased to getTarget.
     */
    public String getTarget() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return target;

    } // getTarget():String

    /**
     * A PI's data content tells the processor what we actually want it
     * to do.  It is defined slightly differently in HTML and XML.
     * <p>
     * In XML, the data begins with the non-whitespace character
     * immediately after the target -- @see getTarget().
     * <p>
     * In HTML, the data begins with the character immediately after the
     * "&lt;?" token that begins the PI.
     * <p>
     * Note that getNodeValue is aliased to getData
     */
    public String getData() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return data;

    } // getData():String

    /**
     * Change the data content of this PI.
     * Note that setData is aliased to setNodeValue.
     * @see #getData().
     * @throws DOMException(NO_MODIFICATION_ALLOWED_ERR) if node is read-only.
     */
    public void setData(String data) {
        // Hand off to setNodeValue for code-reuse reasons (mutation
        // events, readonly protection, synchronizing, etc.)
        setNodeValue(data);
    } // setData(String)



   /**
     * Returns the absolute base URI of this node or null if the implementation
     * wasn't able to obtain an absolute URI. Note: If the URI is malformed, a
     * null is returned.
     *
     * @return The absolute base URI of this node or null.
     * @since DOM Level 3
     */
    public String getBaseURI() {

        if (needsSyncData()) {
            synchronizeData();
        }
        return ownerNode.getBaseURI();
    }


} // class ProcessingInstructionImpl
