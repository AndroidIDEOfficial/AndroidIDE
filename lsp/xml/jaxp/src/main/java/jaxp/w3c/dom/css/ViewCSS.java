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

package jaxp.w3c.dom.css;

import jaxp.w3c.dom.Element;
import jaxp.w3c.dom.views.AbstractView;

/**
 *  This interface represents a CSS view. The <code>getComputedStyle</code>
 * method provides a read only access to the computed values of an element.
 * <p> The expectation is that an instance of the <code>ViewCSS</code>
 * interface can be obtained by using binding-specific casting methods on an
 * instance of the <code>AbstractView</code> interface.
 * <p> Since a computed style is related to an <code>Element</code> node, if
 * this element is removed from the document, the associated
 * <code>CSSStyleDeclaration</code> and <code>CSSValue</code> related to
 * this declaration are no longer valid.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface ViewCSS extends AbstractView {
    /**
     *  This method is used to get the computed style as it is defined in [<a href='http://www.w3.org/TR/1998/REC-CSS2-19980512'>CSS2</a>].
     * @param elt  The element whose style is to be computed. This parameter
     *   cannot be null.
     * @param pseudoElt  The pseudo-element or <code>null</code> if none.
     * @return  The computed style. The <code>CSSStyleDeclaration</code> is
     *   read-only and contains only absolute values.
     */
    public CSSStyleDeclaration getComputedStyle(Element elt,
                                                String pseudoElt);

}
