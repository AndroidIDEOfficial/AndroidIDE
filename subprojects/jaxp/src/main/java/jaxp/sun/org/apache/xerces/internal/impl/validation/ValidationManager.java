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

package jaxp.sun.org.apache.xerces.internal.impl.validation;

import java.util.Vector;

/**
 * ValidationManager is a coordinator property for validators in the
 * pipeline. Each validator must know how to interact with
 * this property. Validators are not required to know what kind of
 * other validators present in the pipeline, but should understand
 * that there are others and that some coordination is required.
 *
 * @xerces.internal
 *
 * @author Elena Litani, IBM
 */
public class ValidationManager {

    protected final Vector fVSs = new Vector();
    protected boolean fGrammarFound = false;

    // used by the DTD validator to tell other components that it has a
    // cached DTD in hand so there's no reason to
    // scan external subset or entity decls.
    protected boolean fCachedDTD = false;

    /**
     * Each validator should call this method to add its ValidationState into
     * the validation manager.
     */
    public final void addValidationState(ValidationState vs) {
        fVSs.addElement(vs);
    }

    /**
     * Set the information required to validate entity values.
     */
    public final void setEntityState(EntityState state) {
        for (int i = fVSs.size()-1; i >= 0; i--) {
            ((ValidationState)fVSs.elementAt(i)).setEntityState(state);
        }
    }

    public final void setGrammarFound(boolean grammar){
        fGrammarFound = grammar;
    }

    public final boolean isGrammarFound(){
        return fGrammarFound;
    }

    public final void setCachedDTD(boolean cachedDTD) {
        fCachedDTD = cachedDTD;
    } // setCachedDTD(boolean)

    public final boolean isCachedDTD() {
        return fCachedDTD;
    } // isCachedDTD():  boolean


    public final void reset (){
        fVSs.removeAllElements();
        fGrammarFound = false;
        fCachedDTD = false;
    }
}
