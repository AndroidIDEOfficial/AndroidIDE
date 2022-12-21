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
 * Copyright 2001-2004 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.xni;

/**
 * Location information.
 *
 * @author Andy Clark, IBM
 *
 */
public interface XMLLocator {

    //
    // XMLLocator methods
    //

    /** Returns the public identifier. */
    public String getPublicId();

    /** Returns the literal system identifier. */
    public String getLiteralSystemId();

    /** Returns the base system identifier. */
    public String getBaseSystemId();

    /** Returns the expanded system identifier. */
    public String getExpandedSystemId();

    /** Returns the line number, or <code>-1</code> if no line number is available. */
    public int getLineNumber();

    /** Returns the column number, or <code>-1</code> if no column number is available. */
    public int getColumnNumber();

    /** Returns the character offset, or <code>-1</code> if no character offset is available. */
    public int getCharacterOffset();

    /**
     * Returns the encoding of the current entity.
     * Note that, for a given entity, this value can only be
     * considered final once the encoding declaration has been read (or once it
     * has been determined that there is no such declaration) since, no encoding
     * having been specified on the XMLInputSource, the parser
     * will make an initial "guess" which could be in error.
     */
    public String getEncoding();

    /**
     * Returns the XML version of the current entity. This will normally be the
     * value from the XML or text declaration or defaulted by the parser. Note that
     * that this value may be different than the version of the processing rules
     * applied to the current entity. For instance, an XML 1.1 document may refer to
     * XML 1.0 entities. In such a case the rules of XML 1.1 are applied to the entire
     * document. Also note that, for a given entity, this value can only be considered
     * final once the XML or text declaration has been read or once it has been
     * determined that there is no such declaration.
     */
    public String getXMLVersion();


} // interface XMLLocator
