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
 * This interface provides access to the post schema validation infoset for an
 * API that provides a streaming document infoset, such as SAX, XNI, and
 * others.
 * <p>For implementations that would like to provide access to the PSVI in a
 * streaming model, a parser object should also implement the
 * <code>PSVIProvider</code> interface. Within the scope of the methods
 * handling the start and end of an element, applications may use the
 * <code>PSVIProvider</code> to retrieve the PSVI related to the element and
 * its attributes.
 */
public interface PSVIProvider {
    /**
     *  Provides the post schema validation item for the current element
     * information item. The method must be called by an application while
     * in the scope of the methods which report the start and end of an
     * element. For example, for SAX the method must be called within the
     * scope of the document handler's <code>startElement</code> or
     * <code>endElement</code> call. If the method is called outside of the
     * specified scope, the return value is undefined.
     * @return The post schema validation infoset for the current element. If
     *   an element information item is valid, then in the
     *   post-schema-validation infoset the following properties must be
     *   available for the element information item: The following
     *   properties are available in the scope of the method that reports
     *   the start of an element: {element declaration}, {validation
     *   context}, {notation}. The {schema information} property is
     *   available for the validation root. The {error codes} property is
     *   available if any errors occured during validation.  The following
     *   properties are available in the scope of the method that reports
     *   the end of an element: {nil}, {schema specified}, {normalized
     *   value},{ member type definition}, {validity}, {validation attempted}
     *   . If the declaration has a value constraint, the property {schema
     *   default} is available. The {error codes} property is available if
     *   any errors occured during validation. Note: some processors may
     *   choose to provide all the PSVI properties in the scope of the
     *   method that reports the end of an element.
     */
    public ElementPSVI getElementPSVI();

    /**
     * Provides <code>AttributePSVI</code> given the index of an attribute
     * information item in the current element's attribute list. The method
     * must be called by an application while in the scope of the methods
     * which report the start and end of an element at a point where the
     * attribute list is available. For example, for SAX the method must be
     * called while in the scope of the document handler's
     * <code>startElement</code> call. If the method is called outside of
     * the specified scope, the return value is undefined.
     * @param index The attribute index.
     * @return The post schema validation properties of the attribute.
     */
    public AttributePSVI getAttributePSVI(int index);

    /**
     * Provides <code>AttributePSVI</code> given the namespace name and the
     * local name of an attribute information item in the current element's
     * attribute list. The method must be called by an application while in
     * the scope of the methods which report the start and end of an element
     * at a point where the attribute list is available. For example, for
     * SAX the method must be called while in the scope of the document
     * handler's <code>startElement</code> call. If the method is called
     * outside of the specified scope, the return value is undefined.
     * @param uri The namespace name of an attribute.
     * @param localname The local name of an attribute.
     * @return The post schema validation properties of the attribute.
     */
    public AttributePSVI getAttributePSVIByName(String uri,
                                                String localname);

}
