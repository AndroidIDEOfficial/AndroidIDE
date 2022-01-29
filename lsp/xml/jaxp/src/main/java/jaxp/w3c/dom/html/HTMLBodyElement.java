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
 *  The HTML document body. This element is always present in the DOM API,
 * even if the tags are not present in the source document. See the  BODY
 * element definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLBodyElement extends HTMLElement {
    /**
     *  Color of active links (after mouse-button down, but before
     * mouse-button up). See the  alink attribute definition in HTML 4.0.
     * This attribute is deprecated in HTML 4.0.
     */
    public String getALink();
    public void setALink(String aLink);

    /**
     *  URI of the background texture tile image. See the  background
     * attribute definition in HTML 4.0. This attribute is deprecated in HTML
     * 4.0.
     */
    public String getBackground();
    public void setBackground(String background);

    /**
     *  Document background color. See the  bgcolor attribute definition in
     * HTML 4.0. This attribute is deprecated in HTML 4.0.
     */
    public String getBgColor();
    public void setBgColor(String bgColor);

    /**
     *  Color of links that are not active and unvisited. See the  link
     * attribute definition in HTML 4.0. This attribute is deprecated in HTML
     * 4.0.
     */
    public String getLink();
    public void setLink(String link);

    /**
     *  Document text color. See the  text attribute definition in HTML 4.0.
     * This attribute is deprecated in HTML 4.0.
     */
    public String getText();
    public void setText(String text);

    /**
     *  Color of links that have been visited by the user. See the  vlink
     * attribute definition in HTML 4.0. This attribute is deprecated in HTML
     * 4.0.
     */
    public String getVLink();
    public void setVLink(String vLink);

}
