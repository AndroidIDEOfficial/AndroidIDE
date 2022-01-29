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

/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jaxp.w3c.dom.html;

import jaxp.w3c.dom.Element;

/**
 *  All HTML element interfaces derive from this class. Elements that only
 * expose the HTML core attributes are represented by the base
 * <code>HTMLElement</code> interface. These elements are as follows:  HEAD
 * special: SUB, SUP, SPAN, BDO font: TT, I, B, U, S, STRIKE, BIG, SMALL
 * phrase: EM, STRONG, DFN, CODE, SAMP, KBD, VAR, CITE, ACRONYM, ABBR list:
 * DD, DT NOFRAMES, NOSCRIPT ADDRESS, CENTER The <code>style</code> attribute
 * of an HTML element is accessible through the
 * <code>ElementCSSInlineStyle</code> interface which is defined in the  .
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLElement extends Element {
    /**
     *  The element's identifier. See the  id attribute definition in HTML 4.0.
     */
    public String getId();
    public void setId(String id);

    /**
     *  The element's advisory title. See the  title attribute definition in
     * HTML 4.0.
     */
    public String getTitle();
    public void setTitle(String title);

    /**
     *  Language code defined in RFC 1766. See the  lang attribute definition
     * in HTML 4.0.
     */
    public String getLang();
    public void setLang(String lang);

    /**
     *  Specifies the base direction of directionally neutral text and the
     * directionality of tables. See the  dir attribute definition in HTML
     * 4.0.
     */
    public String getDir();
    public void setDir(String dir);

    /**
     *  The class attribute of the element. This attribute has been renamed
     * due to conflicts with the "class" keyword exposed by many languages.
     * See the  class attribute definition in HTML 4.0.
     */
    public String getClassName();
    public void setClassName(String className);

}