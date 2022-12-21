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
 * This interface allows one to retrieve an instance of <code>XSLoader</code>.
 * This interface should be implemented on the same object that implements
 * DOMImplementation.
 */
public interface XSImplementation {
    /**
     * A list containing the versions of XML Schema documents recognized by
     * this <code>XSImplemenation</code>.
     */
    public StringList getRecognizedVersions();


    /**
     * Creates a new XSLoader. The newly constructed loader may then be
     * configured and used to load XML Schemas.
     * @param versions  A list containing the versions of XML Schema
     *   documents which can be loaded by the <code>XSLoader</code> or
     *   <code>null</code> to permit XML Schema documents of any recognized
     *   version to be loaded by the XSLoader.
     * @return  An XML Schema loader.
     * @exception XSException
     *   NOT_SUPPORTED_ERR: Raised if the implementation does not support one
     *   of the specified versions.
     */
    public XSLoader createXSLoader(StringList versions)
                                   throws XSException;

}
