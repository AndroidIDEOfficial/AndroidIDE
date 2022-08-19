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

import java.util.Hashtable;
import jaxp.sun.org.apache.xerces.internal.utils.ObjectFactory;

/**
 * The factory to create and return DTD types. The implementation should
 * store the created datatypes in static data, so that they can be shared by
 * multiple parser instance, and multiple threads.
 *
 * @xerces.internal
 *
 * @author Sandy Gao, IBM
 *
 * @version $Id: DTDDVFactory.java,v 1.6 2010-11-01 04:39:43 joehw Exp $
 */
public abstract class DTDDVFactory {

    private static final String DEFAULT_FACTORY_CLASS = "jaxp.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl";

    /**
     * Get an instance of the default DTDDVFactory implementation.
     *
     * @return  an instance of DTDDVFactory implementation
     * @exception DVFactoryException  cannot create an instance of the specified
     *                                class name or the default class name
     */
    public static final DTDDVFactory getInstance() throws DVFactoryException {
        return getInstance(DEFAULT_FACTORY_CLASS);
    }

    /**
     * Get an instance of DTDDVFactory implementation.
     *
     * @param factoryClass  name of the implementation to load.
     * @return  an instance of DTDDVFactory implementation
     * @exception DVFactoryException  cannot create an instance of the specified
     *                                class name or the default class name
     */
    public static final DTDDVFactory getInstance(String factoryClass) throws DVFactoryException {
        try {
            // if the class name is not specified, use the default one
            return (DTDDVFactory)
                (ObjectFactory.newInstance(factoryClass, true));
        }
        catch (ClassCastException e) {
            throw new DVFactoryException("DTD factory class " + factoryClass + " does not extend from DTDDVFactory.");
        }
    }

    // can't create a new object of this class
    protected DTDDVFactory() {}

    /**
     * return a dtd type of the given name
     *
     * @param name  the name of the datatype
     * @return      the datatype validator of the given name
     */
    public abstract DatatypeValidator getBuiltInDV(String name);

    /**
     * get all built-in DVs, which are stored in a hashtable keyed by the name
     *
     * @return      a hashtable which contains all datatypes
     */
    public abstract Hashtable getBuiltInTypes();

}
