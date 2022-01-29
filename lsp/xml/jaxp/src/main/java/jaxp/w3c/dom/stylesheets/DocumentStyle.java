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

package jaxp.w3c.dom.stylesheets;

/**
 *  The <code>DocumentStyle</code> interface provides a mechanism by which the
 * style sheets embedded in a document can be retrieved. The expectation is
 * that an instance of the <code>DocumentStyle</code> interface can be
 * obtained by using binding-specific casting methods on an instance of the
 * <code>Document</code> interface.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface DocumentStyle {
    /**
     *  A list containing all the style sheets explicitly linked into or
     * embedded in a document. For HTML documents, this includes external
     * style sheets, included via the HTML  LINK element, and inline  STYLE
     * elements. In XML, this includes external style sheets, included via
     * style sheet processing instructions (see [XML StyleSheet]).
     */
    public StyleSheetList getStyleSheets();

}
