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

package jaxp.sun.org.apache.xerces.internal.impl.dv.dtd;

import jaxp.sun.org.apache.xerces.internal.impl.dv.*;

import jaxp.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import jaxp.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import jaxp.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/**
 * <P>ENTITYDatatypeValidator implements the
 * DatattypeValidator interface.
 * This validator embodies the ENTITY attribute type
 * from XML1.0 recommendation.
 * The Value space of ENTITY is the set of all strings
 * that match the NCName production and have been
 * declared as an unparsed entity in a document
 * type definition.
 * The Lexical space of Entity is the set of all
 * strings that match the NCName production.
 * The value space of ENTITY is scoped to a specific
 * instance document.</P>
 *
 * @xerces.internal
 *
 * @author Jeffrey Rodriguez, IBM
 * @author Sandy Gao, IBM
 *
 */
public class ENTITYDatatypeValidator implements DatatypeValidator {

    // construct an ENTITY datatype validator
    public ENTITYDatatypeValidator() {
    }

    /**
     * Checks that "content" string is valid ID value.
     * If invalid a Datatype validation exception is thrown.
     *
     * @param content       the string value that needs to be validated
     * @param context       the validation context
     * @throws InvalidDatatypeException if the content is
     *         invalid according to the rules for the validators
     * @see InvalidDatatypeValueException
     */
    public void validate(String content, ValidationContext context) throws InvalidDatatypeValueException {

        if (!context.isEntityUnparsed(content))
            throw new InvalidDatatypeValueException("ENTITYNotUnparsed", new Object[]{content});

    }

}
