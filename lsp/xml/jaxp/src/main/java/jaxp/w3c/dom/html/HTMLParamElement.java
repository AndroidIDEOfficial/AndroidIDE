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
 *  Parameters fed to the <code>OBJECT</code> element. See the  PARAM element
 * definition in HTML 4.0.
 * <p>See also the <a href='http://www.w3.org/TR/2000/CR-DOM-Level-2-20000510'>Document Object Model (DOM) Level 2 Specification</a>.
 */
public interface HTMLParamElement extends HTMLElement {
    /**
     *  The name of a run-time parameter. See the  name attribute definition
     * in HTML 4.0.
     */
    public String getName();
    public void setName(String name);

    /**
     *  Content type for the <code>value</code> attribute when
     * <code>valuetype</code> has the value "ref". See the  type attribute
     * definition in HTML 4.0.
     */
    public String getType();
    public void setType(String type);

    /**
     *  The value of a run-time parameter. See the  value attribute definition
     * in HTML 4.0.
     */
    public String getValue();
    public void setValue(String value);

    /**
     *  Information about the meaning of the <code>value</code> attribute
     * value. See the  valuetype attribute definition in HTML 4.0.
     */
    public String getValueType();
    public void setValueType(String valueType);

}
