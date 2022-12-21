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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.impl.xs.models;

import jaxp.sun.org.apache.xerces.internal.impl.xs.SubstitutionGroupHandler;
import jaxp.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException;
import jaxp.sun.org.apache.xerces.internal.xni.QName;

import java.util.Vector;
import java.util.ArrayList;

/**
 * XSEmptyCM is a derivative of the abstract content model base class that
 * handles a content model with no chilren (elements).
 *
 * This model validated on the way in.
 *
 * @xerces.internal
 *
 * @author Elena Litani, Lisa Martin
 * @author IBM
 * @version $Id: XSEmptyCM.java,v 1.7 2009/07/28 15:18:11 spericas Exp $
 */
public class XSEmptyCM  implements XSCMValidator {

    //
    // Constants
    //

    // start the content model: did not see any children
    private static final short STATE_START = 0;

    private static final Vector EMPTY = new Vector(0);

    //
    // Data
    //

    //
    // XSCMValidator methods
    //

    /**
     * This methods to be called on entering a first element whose type
     * has this content model. It will return the initial state of the content model
     *
     * @return Start state of the content model
     */
    public int[] startContentModel(){
        return (new int[] {STATE_START});
    }


    /**
     * The method corresponds to one transaction in the content model.
     *
     * @param elementName the qualified name of the element
     * @param currentState Current state
     * @param subGroupHandler the substitution group handler
     * @return element index corresponding to the element from the Schema grammar
     */
    public Object oneTransition (QName elementName, int[] currentState, SubstitutionGroupHandler subGroupHandler){

        // error state
        if (currentState[0] < 0) {
            currentState[0] = SUBSEQUENT_ERROR;
            return null;
        }

        currentState[0] = FIRST_ERROR;
        return null;
    }


    /**
     * The method indicates the end of list of children
     *
     * @param currentState Current state of the content model
     * @return true if the last state was a valid final state
     */
    public boolean endContentModel (int[] currentState){
        boolean isFinal =  false;
        int state = currentState[0];

        // restore content model state:

        // error
        if (state < 0) {
            return false;
        }


        return true;
    }

    /**
     * check whether this content violates UPA constraint.
     *
     * @param subGroupHandler the substitution group handler
     * @return true if this content model contains other or list wildcard
     */
    public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler) throws XMLSchemaException {
        return false;
    }

    /**
     * Check which elements are valid to appear at this point. This method also
     * works if the state is in error, in which case it returns what should
     * have been seen.
     *
     * @param state  the current state
     * @return       a Vector whose entries are instances of
     *               either XSWildcardDecl or XSElementDecl.
     */
    public Vector whatCanGoHere(int[] state) {
        return EMPTY;
    }

    public ArrayList checkMinMaxBounds() {
        return null;
    }

} // class XSEmptyCM
