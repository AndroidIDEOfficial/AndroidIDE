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
 * Copyright 2001, 2002,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.impl.dv.dtd;

import jaxp.sun.org.apache.xerces.internal.impl.dv.DTDDVFactory;
import jaxp.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import java.util.Hashtable;

/**
 * the factory to create/return built-in schema DVs and create user-defined DVs
 *
 * @xerces.internal
 *
 * @author Sandy Gao, IBM
 *
 */
public class DTDDVFactoryImpl extends DTDDVFactory {

    static Hashtable fBuiltInTypes = new Hashtable();
    static {
        createBuiltInTypes();
    }

    /**
     * return a dtd type of the given name
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public DatatypeValidator getBuiltInDV(String name) {
        return (DatatypeValidator)fBuiltInTypes.get(name);
    }

    /**
     * get all built-in DVs, which are stored in a hashtable keyed by the name
     *
     * @return      a hashtable which contains all datatypes
     */
    public Hashtable getBuiltInTypes() {
        return (Hashtable)fBuiltInTypes.clone();
    }

    // create all built-in types
    static void createBuiltInTypes() {

        DatatypeValidator dvTemp;

        fBuiltInTypes.put("string", new StringDatatypeValidator());
        fBuiltInTypes.put("ID", new IDDatatypeValidator());
        dvTemp = new IDREFDatatypeValidator();
        fBuiltInTypes.put("IDREF", dvTemp);
        fBuiltInTypes.put("IDREFS", new ListDatatypeValidator(dvTemp));
        dvTemp = new ENTITYDatatypeValidator();
        fBuiltInTypes.put("ENTITY", new ENTITYDatatypeValidator());
        fBuiltInTypes.put("ENTITIES", new ListDatatypeValidator(dvTemp));
        fBuiltInTypes.put("NOTATION", new NOTATIONDatatypeValidator());
        dvTemp = new NMTOKENDatatypeValidator();
        fBuiltInTypes.put("NMTOKEN", dvTemp);
        fBuiltInTypes.put("NMTOKENS", new ListDatatypeValidator(dvTemp));

    }//createBuiltInTypes()

}// DTDDVFactoryImpl
