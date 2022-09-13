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

/**
 * All primitive types plus ID/IDREF/ENTITY/INTEGER are derived from this abstract
 * class. It provides extra information XSSimpleTypeDecl requires from each
 * type: allowed facets, converting String to actual value, check equality,
 * comparison, etc.
 *
 * @xerces.internal
 *
 * @author Neeraj Bajaj, Sun Microsystems, inc.
 * @author Sandy Gao, IBM
 *
 */
public abstract class TypeValidator {

    // which facets are allowed for this type
    public abstract short getAllowedFacets();

    // convert a string to an actual value. for example,
    // for number types (decimal, double, float, and types derived from them),
    // get the BigDecimal, Double, Flout object.
    // for some types (string and derived), they just return the string itself
    public abstract Object getActualValue(String content, ValidationContext context)
        throws InvalidDatatypeValueException;

    // for ID/IDREF/ENTITY types, do some extra checking after the value is
    // checked to be valid with respect to both lexical representation and
    // facets
    public void checkExtraRules(Object value, ValidationContext context) throws InvalidDatatypeValueException {
    }

    // the following methods might not be supported by every DV.
    // but XSSimpleTypeDecl should know which type supports which methods,
    // and it's an *internal* error if a method is called on a DV that
    // doesn't support it.

    //order constants
    public static final short LESS_THAN     = -1;
    public static final short EQUAL         = 0;
    public static final short GREATER_THAN  = 1;
    public static final short INDETERMINATE = 2;

    // where there is distinction between identity and equality, this method
    // will be overwritten
    // checks whether the two values are identical; for ex, this distinguishes
    // -0.0 from 0.0
    public boolean isIdentical (Object value1, Object value2) {
        return value1.equals(value2);
    }

    // check the order relation between the two values
    // the parameters are in compiled form (from getActualValue)
    public int compare(Object value1, Object value2) {
        return -1;
    }

    // get the length of the value
    // the parameters are in compiled form (from getActualValue)
    public int getDataLength(Object value) {
        return (value instanceof String) ? ((String)value).length() : -1;
    }

    // get the number of digits of the value
    // the parameters are in compiled form (from getActualValue)
    public int getTotalDigits(Object value) {
        return -1;
    }

    // get the number of fraction digits of the value
    // the parameters are in compiled form (from getActualValue)
    public int getFractionDigits(Object value) {
        return -1;
    }

    // check whether the character is in the range 0x30 ~ 0x39
    public static final boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    // if the character is in the range 0x30 ~ 0x39, return its int value (0~9),
    // otherwise, return -1
    public static final int getDigit(char ch) {
        return isDigit(ch) ? ch - '0' : -1;
    }

} // interface TypeValidator
