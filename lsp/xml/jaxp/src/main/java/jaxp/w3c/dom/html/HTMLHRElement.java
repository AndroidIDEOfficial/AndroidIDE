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

/**
 *  Create a horizontal rule. See the  HR element definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLHRElement extends HTMLElement {
    /**
     *  Align the rule on the page. See the  align attribute definition in
     * HTML 4.0. This attribute is deprecated in HTML 4.0.
     */
    public String getAlign();
    public void setAlign(String align);

    /**
     *  Indicates to the user agent that there should be no shading in the
     * rendering of this element. See the  noshade attribute definition in
     * HTML 4.0. This attribute is deprecated in HTML 4.0.
     */
    public boolean getNoShade();
    public void setNoShade(boolean noShade);

    /**
     *  The height of the rule. See the  size attribute definition in HTML
     * 4.0. This attribute is deprecated in HTML 4.0.
     */
    public String getSize();
    public void setSize(String size);

    /**
     *  The width of the rule. See the  width attribute definition in HTML
     * 4.0. This attribute is deprecated in HTML 4.0.
     */
    public String getWidth();
    public void setWidth(String width);

}
