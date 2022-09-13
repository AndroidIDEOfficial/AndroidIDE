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

package jaxp.sun.org.apache.xerces.internal.impl.dv.xs;

import jaxp.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import jaxp.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import jaxp.sun.org.apache.xerces.internal.util.XMLChar;

/**
 * Represent the schema type "IDREF"
 *
 * @xerces.internal
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 */
public class IDREFDV extends TypeValidator{

    public short getAllowedFacets(){
        return (XSSimpleTypeDecl.FACET_LENGTH | XSSimpleTypeDecl.FACET_MINLENGTH | XSSimpleTypeDecl.FACET_MAXLENGTH | XSSimpleTypeDecl.FACET_PATTERN | XSSimpleTypeDecl.FACET_ENUMERATION | XSSimpleTypeDecl.FACET_WHITESPACE );
    }

    public Object getActualValue(String content, ValidationContext context) throws InvalidDatatypeValueException {
        if (!XMLChar.isValidNCName(content)) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "NCName"});
        }
        return content;
    }

    public void checkExtraRules(Object value, ValidationContext context) throws InvalidDatatypeValueException {
        context.addIdRef((String)value);
    }

}//IDREF class
