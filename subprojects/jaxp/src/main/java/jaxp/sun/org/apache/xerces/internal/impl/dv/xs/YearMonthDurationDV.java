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
 * Copyright 2004,2005 The Apache Software Foundation.
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

import java.math.BigInteger;

import jaxp.xml.datatype.DatatypeConstants;
import jaxp.xml.datatype.Duration;

import jaxp.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import jaxp.sun.org.apache.xerces.internal.impl.dv.ValidationContext;

/**
 * Used to validate the <yearMonthDuration> type
 *
 * @xerces.internal
 *
 * @author Ankit Pasricha, IBM
 *
 * @version $Id: YearMonthDurationDV.java,v 1.6 2010-11-01 04:39:47 joehw Exp $
 */
class YearMonthDurationDV extends DurationDV {

    public Object getActualValue(String content, ValidationContext context)
        throws InvalidDatatypeValueException {
        try {
            return parse(content, YEARMONTHDURATION_TYPE);
        }
        catch (Exception ex) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{content, "yearMonthDuration"});
        }
    }

    protected Duration getDuration(DateTimeData date) {
        int sign = 1;
        if ( date.year<0 || date.month<0) {
            sign = -1;
        }
        return datatypeFactory.newDuration(sign == 1,
                date.year != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.year):null,
                date.month != DatatypeConstants.FIELD_UNDEFINED?BigInteger.valueOf(sign*date.month):null,
                null,
                null,
                null,
                null);
    }
}
