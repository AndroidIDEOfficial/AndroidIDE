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

import jaxp.w3c.dom.DOMException;

/**
 *  The <code>CSSStyleRule</code> interface represents a single rule set in a
 * CSS style sheet.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface CSSStyleRule extends CSSRule {
    /**
     *  The textual representation of the selector for the rule set. The
     * implementation may have stripped out insignificant whitespace while
     * parsing the selector.
     */
    public String getSelectorText();
    /**
     *  The textual representation of the selector for the rule set. The
     * implementation may have stripped out insignificant whitespace while
     * parsing the selector.
     * @exception DOMException
     *   SYNTAX_ERR: Raised if the specified CSS string value has a syntax
     *   error and is unparsable.
     *   <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this rule is readonly.
     */
    public void setSelectorText(String selectorText)
                        throws DOMException;

    /**
     *  The declaration-block of this rule set.
     */
    public CSSStyleDeclaration getStyle();

}
