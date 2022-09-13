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
 * $Id: TransletOutputHandlerFactory.java,v 1.2.4.2 2005/09/15 19:12:05 jeffsuttor Exp $
 */

package jaxp.sun.org.apache.xalan.internal.xsltc.runtime.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import jaxp.xml.parsers.ParserConfigurationException;
import jaxp.xml.stream.XMLEventWriter;
import jaxp.xml.stream.XMLStreamWriter;

import jaxp.sun.org.apache.xalan.internal.xsltc.trax.SAX2DOM;
import jaxp.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXEventWriter;
import jaxp.sun.org.apache.xalan.internal.xsltc.trax.SAX2StAXStreamWriter;

import jaxp.sun.org.apache.xml.internal.serializer.ToHTMLSAXHandler;
import jaxp.sun.org.apache.xml.internal.serializer.ToHTMLStream;
import jaxp.sun.org.apache.xml.internal.serializer.ToTextSAXHandler;
import jaxp.sun.org.apache.xml.internal.serializer.ToTextStream;
import jaxp.sun.org.apache.xml.internal.serializer.ToUnknownStream;
import jaxp.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler;
import jaxp.sun.org.apache.xml.internal.serializer.ToXMLStream;
import jaxp.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.w3c.dom.Node;

import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * @author Santiago Pericas-Geertsen
 */
public class TransletOutputHandlerFactory {

    public static final int STREAM = 0;
    public static final int SAX    = 1;
    public static final int DOM    = 2;
    public static final int STAX   = 3;

    private String _encoding                        = "utf-8";
    private String _method                          = null;
    private int    _outputType                      = STREAM;
    private OutputStream _ostream                   = System.out;
    private Writer _writer                          = null;
    private Node _node                              = null;
    private Node   _nextSibling                     = null;
    private XMLEventWriter _xmlStAXEventWriter      = null;
    private XMLStreamWriter _xmlStAXStreamWriter    = null;
    private int _indentNumber                       = -1;
    private ContentHandler _handler                 = null;
    private LexicalHandler _lexHandler              = null;

    private boolean _useServicesMechanism;

    static public TransletOutputHandlerFactory newInstance() {
        return new TransletOutputHandlerFactory(true);
    }
    static public TransletOutputHandlerFactory newInstance(boolean useServicesMechanism) {
        return new TransletOutputHandlerFactory(useServicesMechanism);
    }

    public TransletOutputHandlerFactory(boolean useServicesMechanism) {
        _useServicesMechanism = useServicesMechanism;
    }
    public void setOutputType(int outputType) {
        _outputType = outputType;
    }

    public void setEncoding(String encoding) {
        if (encoding != null) {
            _encoding = encoding;
        }
    }

    public void setOutputMethod(String method) {
        _method = method;
    }

    public void setOutputStream(OutputStream ostream) {
        _ostream = ostream;
    }

    public void setWriter(Writer writer) {
        _writer = writer;
    }

    public void setHandler(ContentHandler handler) {
        _handler = handler;
    }

    public void setLexicalHandler(LexicalHandler lex) {
        _lexHandler = lex;
    }

    public void setNode(Node node) {
        _node = node;
    }

    public Node getNode() {
        return (_handler instanceof SAX2DOM) ? ((SAX2DOM)_handler).getDOM()
           : null;
    }

    public void setNextSibling(Node nextSibling) {
        _nextSibling = nextSibling;
    }

    public XMLEventWriter getXMLEventWriter() {
        return (_handler instanceof SAX2StAXEventWriter) ? ((SAX2StAXEventWriter) _handler).getEventWriter() : null;
    }

    public void setXMLEventWriter(XMLEventWriter eventWriter) {
        _xmlStAXEventWriter = eventWriter;
    }

    public XMLStreamWriter getXMLStreamWriter() {
        return (_handler instanceof SAX2StAXStreamWriter) ? ((SAX2StAXStreamWriter) _handler).getStreamWriter() : null;
    }

    public void setXMLStreamWriter(XMLStreamWriter streamWriter) {
        _xmlStAXStreamWriter = streamWriter;
    }

    public void setIndentNumber(int value) {
        _indentNumber = value;
    }

    public SerializationHandler getSerializationHandler()
        throws IOException, ParserConfigurationException
    {
        SerializationHandler result = null;
        switch (_outputType)
        {
            case STREAM :

                if (_method == null)
                {
                    result = new ToUnknownStream();
                }
                else if (_method.equalsIgnoreCase("xml"))
                {

                    result = new ToXMLStream();

                }
                else if (_method.equalsIgnoreCase("html"))
                {

                    result = new ToHTMLStream();

                }
                else if (_method.equalsIgnoreCase("text"))
                {

                    result = new ToTextStream();

                }

                if (result != null && _indentNumber >= 0)
                {
                    result.setIndentAmount(_indentNumber);
                }

                result.setEncoding(_encoding);

                if (_writer != null)
                {
                    result.setWriter(_writer);
                }
                else
                {
                    result.setOutputStream(_ostream);
                }
                return result;

            case DOM :
                _handler = (_node != null) ? new SAX2DOM(_node, _nextSibling, _useServicesMechanism) : new SAX2DOM(_useServicesMechanism);
                _lexHandler = (LexicalHandler) _handler;
                // falls through
            case STAX :
                if (_xmlStAXEventWriter != null) {
                    _handler =  new SAX2StAXEventWriter(_xmlStAXEventWriter);
                } else if (_xmlStAXStreamWriter != null) {
                    _handler =  new SAX2StAXStreamWriter(_xmlStAXStreamWriter);
                }
                _lexHandler = (LexicalHandler) _handler;
                // again falls through - Padmaja Vedula
            case SAX :
                if (_method == null)
                {
                    _method = "xml"; // default case
                }

                if (_method.equalsIgnoreCase("xml"))
                {

                    if (_lexHandler == null)
                    {
                        result = new ToXMLSAXHandler(_handler, _encoding);
                    }
                    else
                    {
                        result =
                            new ToXMLSAXHandler(
                                _handler,
                                _lexHandler,
                                _encoding);
                    }

                }
                else if (_method.equalsIgnoreCase("html"))
                {

                    if (_lexHandler == null)
                    {
                        result = new ToHTMLSAXHandler(_handler, _encoding);
                    }
                    else
                    {
                        result =
                            new ToHTMLSAXHandler(
                                _handler,
                                _lexHandler,
                                _encoding);
                    }

                }
                else if (_method.equalsIgnoreCase("text"))
                {

                    if (_lexHandler == null)
                    {
                        result = new ToTextSAXHandler(_handler, _encoding);
                    }
                    else
                    {
                        result =
                            new ToTextSAXHandler(
                                _handler,
                                _lexHandler,
                                _encoding);
                    }

                }
                return result;
        }
        return null;
    }

}
