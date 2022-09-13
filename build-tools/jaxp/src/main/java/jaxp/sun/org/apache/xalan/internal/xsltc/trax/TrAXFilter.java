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
 * Copyright 2001-2004 The Apache Software Foundation.
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
 * $Id: TrAXFilter.java,v 1.2.4.1 2005/09/06 12:23:19 pvedula Exp $
 */


package jaxp.sun.org.apache.xalan.internal.xsltc.trax;

import java.io.IOException;

import jaxp.xml.XMLConstants;
import jaxp.xml.parsers.FactoryConfigurationError;
import jaxp.xml.parsers.ParserConfigurationException;
import jaxp.xml.parsers.SAXParser;
import jaxp.xml.parsers.SAXParserFactory;
import jaxp.xml.transform.ErrorListener;
import jaxp.xml.transform.Templates;
import jaxp.xml.transform.Transformer;
import jaxp.xml.transform.TransformerConfigurationException;
import jaxp.xml.transform.sax.SAXResult;

import jaxp.sun.org.apache.xml.internal.utils.XMLReaderManager;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * skeleton extension of XMLFilterImpl for now.
 * @author Santiago Pericas-Geertsen
 * @author G. Todd Miller
 */
public class TrAXFilter extends XMLFilterImpl {
    private Templates              _templates;
    private TransformerImpl        _transformer;
    private TransformerHandlerImpl _transformerHandler;
    private boolean _useServicesMechanism = true;

    public TrAXFilter(Templates templates)  throws
        TransformerConfigurationException
    {
        _templates = templates;
        _transformer = (TransformerImpl) templates.newTransformer();
        _transformerHandler = new TransformerHandlerImpl(_transformer);
        _useServicesMechanism = _transformer.useServicesMechnism();
    }

    public Transformer getTransformer() {
        return _transformer;
    }

    private void createParent() throws SAXException {
        XMLReader parent = null;
        try {
            SAXParserFactory pfactory = SAXParserFactory.newInstance();
            pfactory.setNamespaceAware(true);

            if (_transformer.isSecureProcessing()) {
                try {
                    pfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                }
                catch (SAXException e) {}
            }

            SAXParser saxparser = pfactory.newSAXParser();
            parent = saxparser.getXMLReader();
        }
        catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
        catch (FactoryConfigurationError e) {
            throw new SAXException(e.toString());
        }

        if (parent == null) {
            parent = XMLReaderFactory.createXMLReader();
        }

        // make this XMLReader the parent of this filter
        setParent(parent);
    }

    public void parse (InputSource input) throws SAXException, IOException
    {
        XMLReader managedReader = null;

        try {
            if (getParent() == null) {
                try {
                    managedReader = XMLReaderManager.getInstance(_useServicesMechanism)
                                                    .getXMLReader();
                    setParent(managedReader);
                } catch (SAXException  e) {
                    throw new SAXException(e.toString());
                }
            }

            // call parse on the parent
            getParent().parse(input);
        } finally {
            if (managedReader != null) {
                XMLReaderManager.getInstance(_useServicesMechanism).releaseXMLReader(managedReader);
            }
        }
    }

    public void parse (String systemId) throws SAXException, IOException
    {
        parse(new InputSource(systemId));
    }

    public void setContentHandler (ContentHandler handler)
    {
        _transformerHandler.setResult(new SAXResult(handler));
        if (getParent() == null) {
                try {
                    createParent();
                }
                catch (SAXException  e) {
                   return;
                }
        }
        getParent().setContentHandler(_transformerHandler);
    }

    public void setErrorListener (ErrorListener handler) { }
}
