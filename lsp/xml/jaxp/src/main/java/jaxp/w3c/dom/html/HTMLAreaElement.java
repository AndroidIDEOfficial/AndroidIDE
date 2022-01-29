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
 *  Client-side image map area definition. See the  AREA element definition in
 * HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLAreaElement extends HTMLElement {
    /**
     *  A single character access key to give access to the form control. See
     * the  accesskey attribute definition in HTML 4.0.
     */
    public String getAccessKey();
    public void setAccessKey(String accessKey);

    /**
     *  Alternate text for user agents not rendering the normal content of
     * this element. See the  alt attribute definition in HTML 4.0.
     */
    public String getAlt();
    public void setAlt(String alt);

    /**
     *  Comma-separated list of lengths, defining an active region geometry.
     * See also <code>shape</code> for the shape of the region. See the
     * coords attribute definition in HTML 4.0.
     */
    public String getCoords();
    public void setCoords(String coords);

    /**
     *  The URI of the linked resource. See the  href attribute definition in
     * HTML 4.0.
     */
    public String getHref();
    public void setHref(String href);

    /**
     *  Specifies that this area is inactive, i.e., has no associated action.
     * See the  nohref attribute definition in HTML 4.0.
     */
    public boolean getNoHref();
    public void setNoHref(boolean noHref);

    /**
     *  The shape of the active area. The coordinates are given by
     * <code>coords</code> . See the  shape attribute definition in HTML 4.0.
     */
    public String getShape();
    public void setShape(String shape);

    /**
     *  Index that represents the element's position in the tabbing order. See
     * the  tabindex attribute definition in HTML 4.0.
     */
    public int getTabIndex();
    public void setTabIndex(int tabIndex);

    /**
     *  Frame to render the resource in. See the  target attribute definition
     * in HTML 4.0.
     */
    public String getTarget();
    public void setTarget(String target);

}
