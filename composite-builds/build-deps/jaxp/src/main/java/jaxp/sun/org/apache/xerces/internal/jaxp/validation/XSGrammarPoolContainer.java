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

package jaxp.sun.org.apache.xerces.internal.jaxp.validation;

import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/**
 * <p>A container for grammar pools which only contain schema grammars.</p>
 *
 * @author Michael Glavassevich, IBM
 * @version $Id: XSGrammarPoolContainer.java,v 1.6 2010-11-01 04:40:08 joehw Exp $
 */
public interface XSGrammarPoolContainer {

    /**
     * <p>Returns the grammar pool contained inside the container.</p>
     *
     * @return the grammar pool contained inside the container
     */
    public XMLGrammarPool getGrammarPool();

    /**
     * <p>Returns whether the schema components contained in this object
     * can be considered to be a fully composed schema and should be
     * used to the exclusion of other schema components which may be
     * present elsewhere.</p>
     *
     * @return whether the schema components contained in this object
     * can be considered to be a fully composed schema
     */
    public boolean isFullyComposed();

    /**
     * Returns the initial value of a feature for validators created
     * using this grammar pool container or null if the validators
     * should use the default value.
     */
    public Boolean getFeature(String featureId);

    /*
     * Set a feature on the schema
     */
    public void setFeature(String featureId, boolean state);

    /**
     * Returns the initial value of a property for validators created
     * using this grammar pool container or null if the validators
     * should use the default value.
     */
    public Object getProperty(String propertyId);

    /*
     * Set a property on the schema
     */
    public void setProperty(String propertyId, Object state);

}
