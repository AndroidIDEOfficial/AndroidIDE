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
 * The <code>XSObject</code> is a base object for the XML Schema component
 * model.
 */
public interface XSObject {
    /**
     *  The <code>type</code> of this object, i.e.
     * <code>ELEMENT_DECLARATION</code>.
     */
    public short getType();

    /**
     * The name of type <code>NCName</code>, as defined in XML Namespaces, of
     * this declaration specified in the <code>{name}</code> property of the
     * component or <code>null</code> if the definition of this component
     * does not have a <code>{name}</code> property. For anonymous types,
     * the processor must construct and expose an anonymous type name that
     * is distinct from the name of every named type and the name of every
     * other anonymous type.
     */
    public String getName();

    /**
     *  The [target namespace] of this object, or <code>null</code> if it is
     * unspecified.
     */
    public String getNamespace();

    /**
     * A namespace schema information item corresponding to the target
     * namespace of the component, if it is globally declared; or
     * <code>null</code> otherwise.
     */
    public XSNamespaceItem getNamespaceItem();

}
