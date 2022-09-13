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
 * Copyright 2005 The Apache Software Foundation.
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
package jaxp.sun.org.apache.xerces.internal.xs.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * <p>Interface to expose the value of 'decimal' and related datatypes.</p>
 *
 * @author Naela Nissar, IBM
 *
 */
public interface XSDecimal {

    /**
     * @return the <code>BigDecimal</code> representation of this object
     */
    public BigDecimal getBigDecimal();

    /**
     * @return the <code>BigInteger</code> representation of this object
     * @exception NumberFormatException if the value cannot be represented as a <code>BigInteger</code>
     */
    public BigInteger getBigInteger() throws NumberFormatException;

    /**
     * @return the long value representation of this object
     * @exception NumberFormatException if the value cannot be represented as a <code>long</code>
     */
    public long getLong() throws NumberFormatException;

    /**
     * @return the int value representation of this object
     * @exception NumberFormatException if the value cannot be represented as a <code>int</code>
     */
    public int getInt() throws NumberFormatException;

    /**
     * @return the short value representation of this object
     * @exception NumberFormatException if the value cannot be represented as a <code>short</code>
     */
    public short getShort() throws NumberFormatException;

    /**
     * @return the byte value representation of this object
     * @exception NumberFormatException if the value cannot be represented as a <code>byte</code>
     */
    public byte getByte() throws NumberFormatException;
}
