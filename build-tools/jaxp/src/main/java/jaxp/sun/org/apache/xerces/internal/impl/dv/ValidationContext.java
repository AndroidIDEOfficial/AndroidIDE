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

package jaxp.sun.org.apache.xerces.internal.impl.dv;

import java.util.Locale;

/**
 * ValidationContext has all the information required for the
 * validation of: id, idref, entity, notation, qname
 *
 * @xerces.internal
 *
 * @author Sandy Gao, IBM
 * @version $Id: ValidationContext.java,v 1.6 2010/07/23 02:09:29 joehw Exp $
 */
public interface ValidationContext {
    // whether to validate against facets
    public boolean needFacetChecking();

    // whether to do extra id/idref/entity checking
    public boolean needExtraChecking();

    // whether we need to normalize the value that is passed!
    public boolean needToNormalize();

    // are namespaces relevant in this context?
    public boolean useNamespaces();

    // entity
    public boolean isEntityDeclared (String name);
    public boolean isEntityUnparsed (String name);

    // id
    public boolean isIdDeclared (String name);
    public void    addId(String name);

    // idref
    public void addIdRef(String name);

    // get symbol from symbol table
    public String getSymbol (String symbol);

    // qname
    public String getURI(String prefix);

    // Locale
    public Locale getLocale();

}
