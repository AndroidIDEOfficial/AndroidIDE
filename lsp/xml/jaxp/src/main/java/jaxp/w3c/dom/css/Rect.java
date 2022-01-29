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

/**
 *  The <code>Rect</code> interface is used to represent any rect value. This
 * interface reflects the values in the underlying style property. Hence,
 * modifications made to the <code>CSSPrimitiveValue</code> objects modify
 * the style property.
 * <p>See also the <a href='http://www.w3.org/TR/2000/REC-DOM-Level-2-Style-20001113'>Document Object Model (DOM) Level 2 Style Specification</a>.
 * @since DOM Level 2
 */
public interface Rect {
    /**
     *  This attribute is used for the top of the rect.
     */
    public CSSPrimitiveValue getTop();

    /**
     *  This attribute is used for the right of the rect.
     */
    public CSSPrimitiveValue getRight();

    /**
     *  This attribute is used for the bottom of the rect.
     */
    public CSSPrimitiveValue getBottom();

    /**
     *  This attribute is used for the left of the rect.
     */
    public CSSPrimitiveValue getLeft();

}
