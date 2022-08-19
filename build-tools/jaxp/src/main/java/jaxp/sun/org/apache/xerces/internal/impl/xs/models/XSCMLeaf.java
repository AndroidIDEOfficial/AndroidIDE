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

package jaxp.sun.org.apache.xerces.internal.impl.xs.models;

import jaxp.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import jaxp.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;

/**
 * Content model leaf node.
 *
 * @xerces.internal
 *
 * @author Neil Graham, IBM
 */
public class XSCMLeaf
    extends CMNode {

    //
    // Data
    //

    /** This is the leaf: element decl or wildcard decl. */
    private Object fLeaf = null;

    /**
     * Identify the particle: for UPA checking
     */
    private int fParticleId = -1;

    /**
     * Part of the algorithm to convert a regex directly to a DFA
     * numbers each leaf sequentially. If its -1, that means its an
     * epsilon node. Zero and greater are non-epsilon positions.
     */
    private int fPosition = -1;

    //
    // Constructors
    //

    /** Constructs a content model leaf. */
    public XSCMLeaf(int type, Object leaf, int id, int position)  {
        super(type);

        // Store the element index and position
        fLeaf = leaf;
        fParticleId = id;
        fPosition = position;
    }

    //
    // Package methods
    //

    final Object getLeaf() {
        return fLeaf;
    }

    final int getParticleId() {
        return fParticleId;
    }

    final int getPosition() {
        return fPosition;
    }

    final void setPosition(int newPosition) {
        fPosition = newPosition;
    }

    //
    // CMNode methods
    //

    // package

    public boolean isNullable() {
        // Leaf nodes are never nullable unless its an epsilon node
        return (fPosition == -1);
    }

    public String toString() {
        StringBuffer strRet = new StringBuffer(fLeaf.toString());
        if (fPosition >= 0) {
            strRet.append
            (
                " (Pos:"
                + Integer.toString(fPosition)
                + ")"
            );
        }
        return strRet.toString();
    }

    // protected

    protected void calcFirstPos(CMStateSet toSet) {
        // If we are an epsilon node, then the first pos is an empty set
        if (fPosition == -1)
            toSet.zeroBits();

        // Otherwise, its just the one bit of our position
        else
            toSet.setBit(fPosition);
    }

    protected void calcLastPos(CMStateSet toSet) {
        // If we are an epsilon node, then the last pos is an empty set
        if (fPosition == -1)
            toSet.zeroBits();

        // Otherwise, its just the one bit of our position
        else
            toSet.setBit(fPosition);
    }

} // class XSCMLeaf
