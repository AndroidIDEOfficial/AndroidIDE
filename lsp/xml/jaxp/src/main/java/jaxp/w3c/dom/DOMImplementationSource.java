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

package jaxp.w3c.dom;

/**
 * This interface permits a DOM implementer to supply one or more
 * implementations, based upon requested features and versions, as specified
 * in <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407/core.html#DOMFeatures'>DOM
 * Features</a>. Each implemented <code>DOMImplementationSource</code> object is
 * listed in the binding-specific list of available sources so that its
 * <code>DOMImplementation</code> objects are made available.
 * <p>See also the <a href='http://www.w3.org/TR/2004/REC-DOM-Level-3-Core-20040407'>Document Object Model (DOM) Level 3 Core Specification</a>.
 * @since DOM Level 3
 */
public interface DOMImplementationSource {
    /**
     *  A method to request the first DOM implementation that supports the
     * specified features.
     * @param features  A string that specifies which features and versions
     *   are required. This is a space separated list in which each feature
     *   is specified by its name optionally followed by a space and a
     *   version number.  This method returns the first item of the list
     *   returned by <code>getDOMImplementationList</code>.  As an example,
     *   the string <code>"XML 3.0 Traversal +Events 2.0"</code> will
     *   request a DOM implementation that supports the module "XML" for its
     *   3.0 version, a module that support of the "Traversal" module for
     *   any version, and the module "Events" for its 2.0 version. The
     *   module "Events" must be accessible using the method
     *   <code>Node.getFeature()</code> and
     *   <code>DOMImplementation.getFeature()</code>.
     * @return The first DOM implementation that support the desired
     *   features, or <code>null</code> if this source has none.
     */
    public DOMImplementation getDOMImplementation(String features);

    /**
     * A method to request a list of DOM implementations that support the
     * specified features and versions, as specified in .
     * @param features A string that specifies which features and versions
     *   are required. This is a space separated list in which each feature
     *   is specified by its name optionally followed by a space and a
     *   version number. This is something like: "XML 3.0 Traversal +Events
     *   2.0"
     * @return A list of DOM implementations that support the desired
     *   features.
     */
    public DOMImplementationList getDOMImplementationList(String features);

}
