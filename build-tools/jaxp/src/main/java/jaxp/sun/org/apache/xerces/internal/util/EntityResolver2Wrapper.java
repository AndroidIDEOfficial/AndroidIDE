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
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.util;

import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;

import jaxp.sun.org.apache.xerces.internal.impl.ExternalSubsetResolver;
import jaxp.sun.org.apache.xerces.internal.impl.XMLEntityDescription;
import jaxp.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

import org.xml.sax.ext.EntityResolver2;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * <p>This class wraps a SAX entity resolver (EntityResolver2) in an XNI entity resolver.</p>
 *
 * @author Michael Glavassevich, IBM
 *
 */
public class EntityResolver2Wrapper
    implements ExternalSubsetResolver {

    //
    // Data
    //

    /** An instance of SAX2 Extensions 1.1's EntityResolver2. */
    protected EntityResolver2 fEntityResolver;

    //
    // Constructors
    //

    /** Default constructor. */
    public EntityResolver2Wrapper() {}

    /**
     * <p>Creates a new instance wrapping the given SAX entity resolver.</p>
     *
     * @param entityResolver the SAX entity resolver to wrap
     */
    public EntityResolver2Wrapper(EntityResolver2 entityResolver) {
        setEntityResolver(entityResolver);
    } // <init>(EntityResolver2)

    //
    // Public methods
    //

    /**
     * <p>Sets the SAX entity resolver wrapped by this object.</p>
     *
     * @param entityResolver the SAX entity resolver to wrap
     */
    public void setEntityResolver(EntityResolver2 entityResolver) {
        fEntityResolver = entityResolver;
    } // setEntityResolver(EntityResolver2)

    /**
     * <p>Returns the SAX entity resolver wrapped by this object.</p>
     *
     * @return the SAX entity resolver wrapped by this object
     */
    public EntityResolver2 getEntityResolver() {
        return fEntityResolver;
    } // getEntityResolver():EntityResolver2

    //
    // ExternalSubsetResolver methods
    //

    /**
     * <p>Locates an external subset for documents which do not explicitly
     * provide one. If no external subset is provided, this method should
     * return <code>null</code>.</p>
     *
     * @param grammarDescription a description of the DTD
     *
     * @throws XNIException Thrown on general error.
     * @throws IOException  Thrown if resolved entity stream cannot be
     *                      opened or some other i/o error occurs.
     */
    public XMLInputSource getExternalSubset(XMLDTDDescription grammarDescription)
            throws XNIException, IOException {

        if (fEntityResolver != null) {

            String name = grammarDescription.getRootName();
            String baseURI = grammarDescription.getBaseSystemId();

            // Resolve using EntityResolver2
            try {
                InputSource inputSource = fEntityResolver.getExternalSubset(name, baseURI);
                return (inputSource != null) ? createXMLInputSource(inputSource, baseURI) : null;
            }
            // error resolving external subset
            catch (SAXException e) {
                Exception ex = e.getException();
                if (ex == null) {
                    ex = e;
                }
                throw new XNIException(ex);
            }
        }

        // unable to resolve external subset
        return null;

    } // getExternalSubset(XMLDTDDescription):XMLInputSource

    //
    // XMLEntityResolver methods
    //

    /**
     * Resolves an external parsed entity. If the entity cannot be
     * resolved, this method should return null.
     *
     * @param resourceIdentifier contains the physical co-ordinates of the resource to be resolved
     *
     * @throws XNIException Thrown on general error.
     * @throws IOException  Thrown if resolved entity stream cannot be
     *                      opened or some other i/o error occurs.
     */
    public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
            throws XNIException, IOException {

        if (fEntityResolver != null) {

            String pubId = resourceIdentifier.getPublicId();
            String sysId = resourceIdentifier.getLiteralSystemId();
            String baseURI = resourceIdentifier.getBaseSystemId();
            String name = null;
            if (resourceIdentifier instanceof XMLDTDDescription) {
                name = "[dtd]";
            }
            else if (resourceIdentifier instanceof XMLEntityDescription) {
                name = ((XMLEntityDescription) resourceIdentifier).getEntityName();
            }

            // When both pubId and sysId are null, the user's entity resolver
            // can do nothing about it. We'd better not bother calling it.
            // This happens when the resourceIdentifier is a GrammarDescription,
            // which describes a schema grammar of some namespace, but without
            // any schema location hint. -Sg
            if (pubId == null && sysId == null) {
                return null;
            }

            // Resolve using EntityResolver2
            try {
                InputSource inputSource =
                    fEntityResolver.resolveEntity(name, pubId, baseURI, sysId);
                return (inputSource != null) ? createXMLInputSource(inputSource, baseURI) : null;
            }
            // error resolving entity
            catch (SAXException e) {
                Exception ex = e.getException();
                if (ex == null) {
                    ex = e;
                }
                throw new XNIException(ex);
            }
        }

        // unable to resolve entity
        return null;

    } // resolveEntity(XMLResourceIdentifier):XMLInputSource

    /**
     * Creates an XMLInputSource from a SAX InputSource.
     */
    private XMLInputSource createXMLInputSource(InputSource source, String baseURI) {

        String publicId = source.getPublicId();
        String systemId = source.getSystemId();
        String baseSystemId = baseURI;
        InputStream byteStream = source.getByteStream();
        Reader charStream = source.getCharacterStream();
        String encoding = source.getEncoding();
        XMLInputSource xmlInputSource =
            new XMLInputSource(publicId, systemId, baseSystemId);
        xmlInputSource.setByteStream(byteStream);
        xmlInputSource.setCharacterStream(charStream);
        xmlInputSource.setEncoding(encoding);
        return xmlInputSource;

    } // createXMLInputSource(InputSource,String):XMLInputSource

} // class EntityResolver2Wrapper
