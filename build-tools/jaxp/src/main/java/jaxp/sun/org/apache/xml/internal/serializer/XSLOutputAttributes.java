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
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2003-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: XSLOutputAttributes.java,v 1.2.4.1 2005/09/15 08:15:32 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.serializer;

import java.util.Vector;

/**
 * This interface has methods associated with the XSLT xsl:output attribues
 * specified in the stylesheet that effect the format of the document output.
 *
 * In an XSLT stylesheet these attributes appear for example as:
 * <pre>
 * <xsl:output method="xml" omit-xml-declaration="no" indent="yes"/>
 * </pre>
 * The xsl:output attributes covered in this interface are:
 * <pre>
 * version
 * encoding
 * omit-xml-declarations
 * standalone
 * doctype-public
 * doctype-system
 * cdata-section-elements
 * indent
 * media-type
 * </pre>
 *
 * The one attribute not covered in this interface is <code>method</code> as
 * this value is implicitly chosen by the serializer that is created, for
 * example ToXMLStream vs. ToHTMLStream or another one.
 *
 * This interface is only used internally within Xalan.
 *
 * @xsl.usage internal
 */
interface XSLOutputAttributes
{
    /**
     * Returns the previously set value of the value to be used as the public
     * identifier in the document type declaration (DTD).
     *
     *@return the public identifier to be used in the DOCTYPE declaration in the
     * output document.
     */
    public String getDoctypePublic();
    /**
     * Returns the previously set value of the value to be used
     * as the system identifier in the document type declaration (DTD).
     * @return the system identifier to be used in the DOCTYPE declaration in
     * the output document.
     *
     */
    public String getDoctypeSystem();
    /**
     * @return the character encoding to be used in the output document.
     */
    public String getEncoding();
    /**
         * @return true if the output document should be indented to visually
         * indicate its structure.
     */
    public boolean getIndent();

    /**
     * @return the number of spaces to indent for each indentation level.
     */
    public int getIndentAmount();
    /**
     * @return the mediatype the media-type or MIME type associated with the
     * output document.
     */
    public String getMediaType();
    /**
     * @return true if the XML declaration is to be omitted from the output
     * document.
     */
    public boolean getOmitXMLDeclaration();
    /**
      * @return a value of "yes" if the <code>standalone</code> delaration is to
      * be included in the output document.
      */
    public String getStandalone();
    /**
     * @return the version of the output format.
     */
    public String getVersion();






    /**
     * Sets the value coming from the xsl:output cdata-section-elements
     * stylesheet property.
     *
     * This sets the elements whose text elements are to be output as CDATA
     * sections.
     * @param URI_and_localNames pairs of namespace URI and local names that
     * identify elements whose text elements are to be output as CDATA sections.
     * The namespace of the local element must be the given URI to match. The
     * qName is not given because the prefix does not matter, only the namespace
     * URI to which that prefix would map matters, so the prefix itself is not
     * relevant in specifying which elements have their text to be output as
     * CDATA sections.
     */
    public void setCdataSectionElements(Vector URI_and_localNames);

    /** Set the value coming from the xsl:output doctype-public and doctype-system stylesheet properties
     * @param system the system identifier to be used in the DOCTYPE declaration
     * in the output document.
     * @param pub the public identifier to be used in the DOCTYPE declaration in
     * the output document.
     */
    public void setDoctype(String system, String pub);

    /** Set the value coming from the xsl:output doctype-public stylesheet attribute.
      * @param doctype the public identifier to be used in the DOCTYPE
      * declaration in the output document.
      */
    public void setDoctypePublic(String doctype);
    /** Set the value coming from the xsl:output doctype-system stylesheet attribute.
      * @param doctype the system identifier to be used in the DOCTYPE
      * declaration in the output document.
      */
    public void setDoctypeSystem(String doctype);
    /**
     * Sets the character encoding coming from the xsl:output encoding stylesheet attribute.
     * @param encoding the character encoding
     */
    public void setEncoding(String encoding);
    /**
     * Sets the value coming from the xsl:output indent stylesheet
     * attribute.
     * @param indent true if the output document should be indented to visually
     * indicate its structure.
     */
    public void setIndent(boolean indent);
    /**
     * Sets the value coming from the xsl:output media-type stylesheet attribute.
     * @param mediatype the media-type or MIME type associated with the output
     * document.
     */
    public void setMediaType(String mediatype);
    /**
     * Sets the value coming from the xsl:output omit-xml-declaration stylesheet attribute
     * @param b true if the XML declaration is to be omitted from the output
     * document.
     */
    public void setOmitXMLDeclaration(boolean b);
    /**
     * Sets the value coming from the xsl:output standalone stylesheet attribute.
     * @param standalone a value of "yes" indicates that the
     * <code>standalone</code> delaration is to be included in the output
     * document.
     */
    public void setStandalone(String standalone);
    /**
     * Sets the value coming from the xsl:output version attribute.
     * @param version the version of the output format.
     */
    public void setVersion(String version);

}
