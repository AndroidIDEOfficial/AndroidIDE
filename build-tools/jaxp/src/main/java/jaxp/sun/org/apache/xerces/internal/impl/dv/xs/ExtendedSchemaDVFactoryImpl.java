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
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.impl.dv.xs;

import jaxp.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import jaxp.sun.org.apache.xerces.internal.util.SymbolHash;

/**
 * A special factory to create/return built-in schema DVs and create user-defined DVs
 * that includes anyAtomicType, yearMonthDuration and dayTimeDuration
 *
 * @xerces.internal
 *
 * @author Khaled Noaman, IBM
 *
 * @version $Id: ExtendedSchemaDVFactoryImpl.java,v 1.2 2010-10-26 23:01:03 joehw Exp $
 */
public class ExtendedSchemaDVFactoryImpl extends BaseSchemaDVFactory {

    static SymbolHash fBuiltInTypes = new SymbolHash();
    static {
        createBuiltInTypes();
    }

    // create all built-in types
    static void createBuiltInTypes() {
        final String ANYATOMICTYPE     = "anyAtomicType";
        final String DURATION          = "duration";
        final String YEARMONTHDURATION = "yearMonthDuration";
        final String DAYTIMEDURATION   = "dayTimeDuration";

        createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnyAtomicType);

        // add anyAtomicType
        fBuiltInTypes.put(ANYATOMICTYPE, XSSimpleTypeDecl.fAnyAtomicType);

        // add 2 duration types
        XSSimpleTypeDecl durationDV = (XSSimpleTypeDecl)fBuiltInTypes.get(DURATION);
        fBuiltInTypes.put(YEARMONTHDURATION, new XSSimpleTypeDecl(durationDV, YEARMONTHDURATION, XSSimpleTypeDecl.DV_YEARMONTHDURATION, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSSimpleTypeDecl.YEARMONTHDURATION_DT));
        fBuiltInTypes.put(DAYTIMEDURATION, new XSSimpleTypeDecl(durationDV, DAYTIMEDURATION, XSSimpleTypeDecl.DV_DAYTIMEDURATION, XSSimpleType.ORDERED_PARTIAL, false, false, false, true, XSSimpleTypeDecl.DAYTIMEDURATION_DT));
    } //createBuiltInTypes()

    /**
     * Get a built-in simple type of the given name
     * REVISIT: its still not decided within the Schema WG how to define the
     *          ur-types and if all simple types should be derived from a
     *          complex type, so as of now we ignore the fact that anySimpleType
     *          is derived from anyType, and pass 'null' as the base of
     *          anySimpleType. It needs to be changed as per the decision taken.
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType)fBuiltInTypes.get(name);
    }

    /**
     * get all built-in simple types, which are stored in a hashtable keyed by
     * the name
     *
     * @return      a hashtable which contains all built-in simple types
     */
    public SymbolHash getBuiltInTypes() {
        return (SymbolHash)fBuiltInTypes.makeClone();
    }
}
