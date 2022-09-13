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
 * Copyright 2003,2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xs;

/**
 * This interface represents the Identity-constraint Definition schema
 * component.
 */
public interface XSIDCDefinition extends XSObject {
    // Identity Constraints
    /**
     * See the definition of <code>key</code> in the identity-constraint
     * category.
     */
    public static final short IC_KEY                    = 1;
    /**
     * See the definition of <code>keyref</code> in the identity-constraint
     * category.
     */
    public static final short IC_KEYREF                 = 2;
    /**
     * See the definition of <code>unique</code> in the identity-constraint
     * category.
     */
    public static final short IC_UNIQUE                 = 3;

    /**
     * [identity-constraint category]: one of key, keyref or unique.
     */
    public short getCategory();

    /**
     * [selector]: a restricted XPath 1.0 expression.
     */
    public String getSelectorStr();

    /**
     * [fields]: a non-empty list of restricted  XPath 1.0 expressions.
     */
    public StringList getFieldStrs();

    /**
     * [referenced key]: required if [identity-constraint category] is keyref,
     * <code>null</code> otherwise. An identity-constraint definition with [
     * identity-constraint category] equal to key or unique.
     */
    public XSIDCDefinition getRefKey();

    /**
     * A sequence of [annotations] or an empty  <code>XSObjectList</code>.
     */
    public XSObjectList getAnnotations();

}
