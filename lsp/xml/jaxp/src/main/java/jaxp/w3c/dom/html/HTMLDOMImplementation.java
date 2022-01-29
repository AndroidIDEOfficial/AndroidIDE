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

package jaxp.w3c.dom.html;

import jaxp.w3c.dom.DOMImplementation;

/**
 *  The <code>HTMLDOMImplementation</code> interface extends the
 * <code>DOMImplementation</code> interface with a method for creating an
 * HTML document instance.
 * @since DOM Level 2
 */
public interface HTMLDOMImplementation extends DOMImplementation {
    /**
     *  Creates an <code>HTMLDocument</code> object with the minimal tree made
     * of the following elements: <code>HTML</code> , <code>HEAD</code> ,
     * <code>TITLE</code> , and <code>BODY</code> .
     * @param title  The title of the document to be set as the content of the
     *   <code>TITLE</code> element, through a child <code>Text</code> node.
     * @return  A new <code>HTMLDocument</code> object.
     */
    public HTMLDocument createHTMLDocument(String title);

}
