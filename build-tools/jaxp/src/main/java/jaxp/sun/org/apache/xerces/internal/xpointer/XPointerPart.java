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
package jaxp.sun.org.apache.xerces.internal.xpointer;

import jaxp.sun.org.apache.xerces.internal.xni.Augmentations;
import jaxp.sun.org.apache.xerces.internal.xni.QName;
import jaxp.sun.org.apache.xerces.internal.xni.XMLAttributes;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;

/**
 * <p>
 * Used for scheme specific parsing and evaluation of an XPointer expression.
 * This interface applies to both ShortHand and SchemeBased XPointer
 * expressions.
 * </p>
 *
 * @xerces.internal
 *
 */
public interface XPointerPart {

    // The start element event
    public static final int EVENT_ELEMENT_START = 0;

    // The end element event
    public static final int EVENT_ELEMENT_END = 1;

    // The empty element event
    public static final int EVENT_ELEMENT_EMPTY = 2;

    /**
     * Provides scheme specific parsing of a XPointer expression i.e.
     * the PointerPart or ShortHandPointer.
     *
     * @param  xpointer A String representing the PointerPart or ShortHandPointer.
     * @throws XNIException Thrown if the PointerPart string does not conform to
     *         the syntax defined by its scheme.
     *
     */
    public void parseXPointer(String part) throws XNIException;

    /**
     * Evaluates an XML resource with respect to an XPointer expressions
     * by checking if it's element and attributes parameters match the
     * criteria specified in the xpointer expression.
     *
     * @param element - The name of the element.
     * @param attributes - The element attributes.
     * @param augs - Additional information that may include infoset augmentations
     * @param event - An integer indicating
     *                0 - The start of an element
     *                1 - The end of an element
     *                2 - An empty element call
     * @throws XNIException Thrown to signal an error
     *
     */
    public boolean resolveXPointer(QName element, XMLAttributes attributes,
                                   Augmentations augs, int event) throws XNIException;

    /**
     * Returns true if the XPointer expression resolves to a resource fragment
     * specified as input else returns false.
     *
     * @return True if the xpointer expression matches a fragment in the resource
     *         else returns false.
     * @throws XNIException Thrown to signal an error
     *
     */
    public boolean isFragmentResolved() throws XNIException;

    /**
     * Returns true if the XPointer expression resolves to a non-element child
     * of the current resource fragment.
     *
     * @return True if the XPointer expression resolves to a non-element child
     *         of the current resource fragment.
     * @throws XNIException Thrown to signal an error
     *
     */
    public boolean isChildFragmentResolved() throws XNIException;

    /**
     * Returns a String containing the scheme name of the PointerPart
     * or the name of the ShortHand Pointer.
     *
     * @return A String containing the scheme name of the PointerPart.
     *
     */
    public String getSchemeName();

    /**
     * Returns a String containing the scheme data of the PointerPart.
     *
     * @return A String containing the scheme data of the PointerPart.
     *
     */
    public String getSchemeData();

    /**
     * Sets the scheme name of the PointerPart or the ShortHand Pointer name.
     *
     * @param schemeName A String containing the scheme name of the PointerPart.
     *
     */
    public void setSchemeName(String schemeName);

    /**
     * Sets the scheme data of the PointerPart.
     *
     * @param schemeData A String containing the scheme data of the PointerPart.
     *
     */
    public void setSchemeData(String schemeData);

}
