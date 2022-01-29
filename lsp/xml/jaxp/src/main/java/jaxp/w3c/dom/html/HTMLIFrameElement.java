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

import jaxp.w3c.dom.Document;

/**
 *  Inline subwindows. See the  IFRAME element definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLIFrameElement extends HTMLElement {
    /**
     *  Aligns this object (vertically or horizontally)  with respect to its
     * surrounding text. See the  align attribute definition in HTML 4.0.
     * This attribute is deprecated in HTML 4.0.
     */
    public String getAlign();
    public void setAlign(String align);

    /**
     *  Request frame borders. See the  frameborder attribute definition in
     * HTML 4.0.
     */
    public String getFrameBorder();
    public void setFrameBorder(String frameBorder);

    /**
     *  Frame height. See the  height attribute definition in HTML 4.0.
     */
    public String getHeight();
    public void setHeight(String height);

    /**
     *  URI designating a long description of this image or frame. See the
     * longdesc attribute definition in HTML 4.0.
     */
    public String getLongDesc();
    public void setLongDesc(String longDesc);

    /**
     *  Frame margin height, in pixels. See the  marginheight attribute
     * definition in HTML 4.0.
     */
    public String getMarginHeight();
    public void setMarginHeight(String marginHeight);

    /**
     *  Frame margin width, in pixels. See the  marginwidth attribute
     * definition in HTML 4.0.
     */
    public String getMarginWidth();
    public void setMarginWidth(String marginWidth);

    /**
     *  The frame name (object of the <code>target</code> attribute). See the
     * name attribute definition in HTML 4.0.
     */
    public String getName();
    public void setName(String name);

    /**
     *  Specify whether or not the frame should have scrollbars. See the
     * scrolling attribute definition in HTML 4.0.
     */
    public String getScrolling();
    public void setScrolling(String scrolling);

    /**
     *  A URI designating the initial frame contents. See the  src attribute
     * definition in HTML 4.0.
     */
    public String getSrc();
    public void setSrc(String src);

    /**
     *  Frame width. See the  width attribute definition in HTML 4.0.
     */
    public String getWidth();
    public void setWidth(String width);

    /**
     *  The document this frame contains, if there is any and it is available,
     * or <code>null</code> otherwise.
     * @since DOM Level 2
     */
    public Document getContentDocument();

}
