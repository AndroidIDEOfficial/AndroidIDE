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
import jaxp.sun.org.apache.xerces.internal.impl.xs.XSModelGroupImpl;

/**
 *
 * Content model Bin-Op node.
 *
 * @xerces.internal
 *
 * @author Neil Graham, IBM
 */
public class XSCMBinOp extends CMNode {
    // -------------------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------------------
    public XSCMBinOp(int type, CMNode leftNode, CMNode rightNode)
    {
        super(type);

        // Insure that its one of the types we require
        if ((type() != XSModelGroupImpl.MODELGROUP_CHOICE)
        &&  (type() != XSModelGroupImpl.MODELGROUP_SEQUENCE)) {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }

        // Store the nodes and init any data that needs it
        fLeftChild = leftNode;
        fRightChild = rightNode;
    }


    // -------------------------------------------------------------------
    //  Package, final methods
    // -------------------------------------------------------------------
    final CMNode getLeft() {
        return fLeftChild;
    }

    final CMNode getRight() {
        return fRightChild;
    }


    // -------------------------------------------------------------------
    //  Package, inherited methods
    // -------------------------------------------------------------------
    public boolean isNullable() {
        //
        //  If its an alternation, then if either child is nullable then
        //  this node is nullable. If its a concatenation, then both of
        //  them have to be nullable.
        //
        if (type() == XSModelGroupImpl.MODELGROUP_CHOICE)
            return (fLeftChild.isNullable() || fRightChild.isNullable());
        else if (type() == XSModelGroupImpl.MODELGROUP_SEQUENCE)
            return (fLeftChild.isNullable() && fRightChild.isNullable());
        else
            throw new RuntimeException("ImplementationMessages.VAL_BST");
    }


    // -------------------------------------------------------------------
    //  Protected, inherited methods
    // -------------------------------------------------------------------
    protected void calcFirstPos(CMStateSet toSet) {
        if (type() == XSModelGroupImpl.MODELGROUP_CHOICE) {
            // Its the the union of the first positions of our children.
            toSet.setTo(fLeftChild.firstPos());
            toSet.union(fRightChild.firstPos());
        }
         else if (type() == XSModelGroupImpl.MODELGROUP_SEQUENCE) {
            //
            //  If our left child is nullable, then its the union of our
            //  children's first positions. Else is our left child's first
            //  positions.
            //
            toSet.setTo(fLeftChild.firstPos());
            if (fLeftChild.isNullable())
                toSet.union(fRightChild.firstPos());
        }
         else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }

    protected void calcLastPos(CMStateSet toSet) {
        if (type() == XSModelGroupImpl.MODELGROUP_CHOICE) {
            // Its the the union of the first positions of our children.
            toSet.setTo(fLeftChild.lastPos());
            toSet.union(fRightChild.lastPos());
        }
        else if (type() == XSModelGroupImpl.MODELGROUP_SEQUENCE) {
            //
            //  If our right child is nullable, then its the union of our
            //  children's last positions. Else is our right child's last
            //  positions.
            //
            toSet.setTo(fRightChild.lastPos());
            if (fRightChild.isNullable())
                toSet.union(fLeftChild.lastPos());
        }
        else {
            throw new RuntimeException("ImplementationMessages.VAL_BST");
        }
    }


    // -------------------------------------------------------------------
    //  Private data members
    //
    //  fLeftChild
    //  fRightChild
    //      These are the references to the two nodes that are on either
    //      side of this binary operation.
    // -------------------------------------------------------------------
    private CMNode  fLeftChild;
    private CMNode  fRightChild;
} // XSCMBinOp
