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
 * Copyright 1999-2004 The Apache Software Foundation.
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


package jaxp.sun.org.apache.xml.internal.serialize;


import java.io.OutputStream;
import java.io.Writer;


/**
 * Implements an XHTML serializer supporting both DOM and SAX
 * pretty serializing. For usage instructions see either {@link
 * Serializer} or {@link BaseMarkupSerializer}.
 *
 * @deprecated This class was deprecated in Xerces 2.6.2. It is
 * recommended that new applications use JAXP's Transformation API
 * for XML (TrAX) for serializing XHTML. See the Xerces documentation
 * for more information.
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 * @see Serializer
 */
public class XHTMLSerializer
    extends HTMLSerializer
{


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public XHTMLSerializer()
    {
        super( true, new OutputFormat( Method.XHTML, null, false ) );
    }


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public XHTMLSerializer( OutputFormat format )
    {
        super( true, format != null ? format : new OutputFormat( Method.XHTML, null, false ) );
    }


    /**
     * Constructs a new serializer that writes to the specified writer
     * using the specified output format. If <tt>format</tt> is null,
     * will use a default output format.
     *
     * @param writer The writer to use
     * @param format The output format to use, null for the default
     */
    public XHTMLSerializer( Writer writer, OutputFormat format )
    {
        super( true, format != null ? format : new OutputFormat( Method.XHTML, null, false ) );
        setOutputCharStream( writer );
    }


    /**
     * Constructs a new serializer that writes to the specified output
     * stream using the specified output format. If <tt>format</tt>
     * is null, will use a default output format.
     *
     * @param output The output stream to use
     * @param format The output format to use, null for the default
     */
    public XHTMLSerializer( OutputStream output, OutputFormat format )
    {
        super( true, format != null ? format : new OutputFormat( Method.XHTML, null, false ) );
        setOutputByteStream( output );
    }


    public void setOutputFormat( OutputFormat format )
    {
        super.setOutputFormat( format != null ? format : new OutputFormat( Method.XHTML, null, false ) );
    }


}
