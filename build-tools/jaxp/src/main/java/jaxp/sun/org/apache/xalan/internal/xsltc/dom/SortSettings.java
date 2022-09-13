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
 * $Id: SortSettings.java,v 1.2.4.1 2005/09/06 10:19:22 pvedula Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.dom;

import java.text.Collator;
import java.util.Locale;

import jaxp.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;

/**
 * Class for carrying settings that are to be used for a particular set
 * of <code>xsl:sort</code> elements.
 */
final class SortSettings {
    /**
     * A reference to the translet object for the transformation.
     */
    private AbstractTranslet _translet;

    /**
     * The sort order (ascending or descending) for each level of
     * <code>xsl:sort</code>
     */
    private int[] _sortOrders;

    /**
     * The type of comparison (text or number) for each level of
     * <code>xsl:sort</code>
     */
    private int[] _types;

    /**
     * The Locale for each level of <code>xsl:sort</code>, based on any lang
     * attribute or the default Locale.
     */
    private Locale[] _locales;

    /**
     * The Collator object in effect for each level of <code>xsl:sort</code>
     */
    private Collator[] _collators;

    /**
     * Case ordering for each level of <code>xsl:sort</code>.
     */
    private String[] _caseOrders;

    /**
     * Create an instance of <code>SortSettings</code>.
     * @param translet {@link AbstractTranslet}
     *                 object for the transformation
     * @param sortOrders an array specifying the sort order for each sort level
     * @param types an array specifying the type of comparison for each sort
     *              level (text or number)
     * @param locales an array specifying the Locale for each sort level
     * @param collators an array specifying the Collation in effect for each
     *                  sort level
     * @param caseOrders an array specifying whether upper-case, lower-case
     *                   or neither is to take precedence for each sort level.
     *                   The value of each element is equal to one of
     *                   <code>"upper-first", "lower-first", or ""</code>.
     */
    SortSettings(AbstractTranslet translet, int[] sortOrders, int[] types,
                 Locale[] locales, Collator[] collators, String[] caseOrders) {
        _translet = translet;
        _sortOrders = sortOrders;
        _types = types;
        _locales = locales;
        _collators = collators;
        _caseOrders = caseOrders;
    }

    /**
     * @return A reference to the translet object for the transformation.
     */
    AbstractTranslet getTranslet() {
        return _translet;
    }

    /**
     * @return An array containing the sort order (ascending or descending)
     *         for each level of <code>xsl:sort</code>
     */
    int[] getSortOrders() {
        return _sortOrders;
    }

    /**
     * @return An array containing the type of comparison (text or number)
     *         to perform for each level of <code>xsl:sort</code>
     */
    int[] getTypes() {
        return _types;
    }

    /**
     * @return An array containing the Locale object in effect for each level
     *         of <code>xsl:sort</code>
     */
    Locale[] getLocales() {
        return _locales;
    }

    /**
     * @return An array containing the Collator object in effect for each level
     *         of <code>xsl:sort</code>
     */
    Collator[] getCollators() {
        return _collators;
    }

    /**
     * @return An array specifying the case ordering for each level of
     *         <code>xsl:sort</code>.
     */
    String[] getCaseOrders() {
        return _caseOrders;
    }
}
