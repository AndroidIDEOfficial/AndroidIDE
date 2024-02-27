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
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: QName.java,v 1.2.4.1 2005/09/02 11:45:56 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.compiler;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class QName {
    private final String _localname;
    private String _prefix;
    private String _namespace;
    private String _stringRep;
    private int    _hashCode;

    public QName(String namespace, String prefix, String localname) {
        _namespace = namespace;
        _prefix    = prefix;
        _localname = localname;

        _stringRep =
            (namespace != null && !namespace.equals(Constants.EMPTYSTRING)) ?
            (namespace + ':' + localname) : localname;

        _hashCode  = _stringRep.hashCode() + 19; // cached for speed
    }

    public void clearNamespace() {
        _namespace = Constants.EMPTYSTRING;
    }

    public String toString() {
        return _stringRep;
    }

    public String getStringRep() {
        return _stringRep;
    }

    public boolean equals(Object other) {
        return (this == other)
                   || (other instanceof QName
                           && _stringRep.equals(((QName) other).getStringRep()));
    }

    public String getLocalPart() {
        return _localname;
    }

    public String getNamespace() {
        return _namespace;
    }

    public String getPrefix() {
        return _prefix;
    }

    public int hashCode() {
        return _hashCode;
    }

    public String dump() {
        return "QName: " + _namespace + "(" + _prefix + "):" + _localname;
    }
}
