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
import jaxp.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;

/**
 *
 * Content model Uni-Op node.
 *
 * @xerces.internal
 *
 * @author Neil Graham, IBM
 * @version $$
 */
public class XSCMUniOp extends CMNode {
    // -------------------------------------------------------------------
    //  Constructors
    // -------------------------------------------------------------------
    public XSCMUniOp(int type, CMNode childNode) {
        super(type);

        // Insure that its one of the types we require
        if ((type() != XSParticleDecl.PARTICLE_ZERO_OR_ONE)
        &&  (type() != XSParticleDecl.PARTICLE_ZERO_OR_MORE)
        &&  (type() != XSParticleDecl.PARTICLE_ONE_OR_MORE)) {
            throw new RuntimeException("ImplementationMessages.VAL_UST");
        }

        // Store the node and init any data that needs it
        fChild = childNode;
    }


    // -------------------------------------------------------------------
    //  Package, final methods
    // -------------------------------------------------------------------
    final CMNode getChild() {
        return fChild;
    }


    // -------------------------------------------------------------------
    //  Package, inherited methods
    // -------------------------------------------------------------------
    public boolean isNullable() {
        //
        //  For debugging purposes, make sure we got rid of all non '*'
        //  repetitions. Otherwise, '*' style nodes are always nullable.
        //
        if (type() == XSParticleDecl.PARTICLE_ONE_OR_MORE)
                return fChild.isNullable();
            else
                return true;
    }


    // -------------------------------------------------------------------
    //  Protected, inherited methods
    // -------------------------------------------------------------------
    protected void calcFirstPos(CMStateSet toSet) {
        // Its just based on our child node's first pos
        toSet.setTo(fChild.firstPos());
    }

    protected void calcLastPos(CMStateSet toSet) {
        // Its just based on our child node's last pos
        toSet.setTo(fChild.lastPos());
    }

    /**
     * Allows the user to set arbitrary data on this content model
     * node. This is used by the a{n,m} optimization that runs
     * in constant space. For convenience, set user data in
     * children node too.
     */
    @Override
    public void setUserData(Object userData) {
        super.setUserData(userData);
        fChild.setUserData(userData);
    }


    // -------------------------------------------------------------------
    //  Private data members
    //
    //  fChild
    //      This is the reference to the one child that we have for this
    //      unary operation.
    // -------------------------------------------------------------------
    private CMNode  fChild;
} // XSCMUniOp
