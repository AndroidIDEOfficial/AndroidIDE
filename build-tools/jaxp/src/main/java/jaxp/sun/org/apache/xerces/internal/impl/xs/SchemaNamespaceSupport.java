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

package jaxp.sun.org.apache.xerces.internal.impl.xs;

import jaxp.sun.org.apache.xerces.internal.util.NamespaceSupport;

/**
 * This class customizes the behaviour of the util.NamespaceSupport
 * class in order to easily implement some features that we need for
 * efficient schema handling.  It will not be generally useful.
 *
 * @xerces.internal
 *
 * @author Neil Graham, IBM
 *
 */
public class SchemaNamespaceSupport
    extends NamespaceSupport {

    public SchemaNamespaceSupport () {
        super();
    } // constructor

    // more effecient than NamespaceSupport(NamespaceContext)
    public SchemaNamespaceSupport(SchemaNamespaceSupport nSupport) {
        fNamespaceSize = nSupport.fNamespaceSize;
        if (fNamespace.length < fNamespaceSize)
            fNamespace = new String[fNamespaceSize];
        System.arraycopy(nSupport.fNamespace, 0, fNamespace, 0, fNamespaceSize);
        fCurrentContext = nSupport.fCurrentContext;
        if (fContext.length <= fCurrentContext)
            fContext = new int[fCurrentContext+1];
        System.arraycopy(nSupport.fContext, 0, fContext, 0, fCurrentContext+1);
    } // end constructor

    /**
     * This method takes a set of Strings, as stored in a
     * NamespaceSupport object, and "fools" the object into thinking
     * that this is one unified context.  This is meant to be used in
     * conjunction with things like local elements, whose declarations
     * may be deeply nested but which for all practical purposes may
     * be regarded as being one level below the global <schema>
     * element--at least with regard to namespace declarations.
     * It's worth noting that the context from which the strings are
     * being imported had better be using the same SymbolTable.
     */
    public void setEffectiveContext (String [] namespaceDecls) {
        if(namespaceDecls == null || namespaceDecls.length == 0) return;
        pushContext();
        int newSize = fNamespaceSize + namespaceDecls.length;
        if (fNamespace.length < newSize) {
            // expand namespace's size...
            String[] tempNSArray = new String[newSize];
            System.arraycopy(fNamespace, 0, tempNSArray, 0, fNamespace.length);
            fNamespace = tempNSArray;
        }
        System.arraycopy(namespaceDecls, 0, fNamespace, fNamespaceSize,
                         namespaceDecls.length);
        fNamespaceSize = newSize;
    } // setEffectiveContext(String):void

    /**
     * This method returns an array of Strings, as would be stored in
     * a NamespaceSupport object.  This array contains all
     * declarations except those at the global level.
     */
    public String [] getEffectiveLocalContext() {
        // the trick here is to recognize that all local contexts
        // happen to start at fContext[3].
        // context 1: empty
        // context 2: decls for xml and xmlns;
        // context 3: decls on <xs:schema>: the global ones
        String[] returnVal = null;
        if (fCurrentContext >= 3) {
            int bottomLocalContext = fContext[3];
            int copyCount = fNamespaceSize - bottomLocalContext;
            if (copyCount > 0) {
                returnVal = new String[copyCount];
                System.arraycopy(fNamespace, bottomLocalContext, returnVal, 0,
                                 copyCount);
            }
        }
        return returnVal;
    } // getEffectiveLocalContext():String

    // This method removes from this object all the namespaces
    // returned by getEffectiveLocalContext.
    public void makeGlobal() {
        if (fCurrentContext >= 3) {
            fCurrentContext = 3;
            fNamespaceSize = fContext[3];
        }
    } // makeGlobal
} // class NamespaceSupport
