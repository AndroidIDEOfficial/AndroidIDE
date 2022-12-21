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

package jaxp.sun.org.apache.xerces.internal.xinclude;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponent;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLDocumentFilter;



public interface XPointerSchema extends XMLComponent, XMLDocumentFilter{

    /**
     * set the Schema Name  eg element , xpointer
     */
    public void setXPointerSchemaName(String schemaName);

    /**
     * Return  Schema Name  eg element , xpointer
     *
     */
    public String getXpointerSchemaName();

    /**
     * Parent Contenhandler for the this contenthandler.
     * // not sure about the parameter type. It can be Contenthandler instead of Object type.
     */
    public void setParent(Object parent);

    /**
     * return the Parent Contenthandler
     */
    public Object getParent();

    /**
     * Content of the XPointer Schema. Xpath to be resolved.
     */
    public void setXPointerSchemaPointer(String content);

    /**
     * Return the XPointer Schema.
     */
    public String getXPointerSchemaPointer();

    public boolean isSubResourceIndentified();

    public void reset();

}
