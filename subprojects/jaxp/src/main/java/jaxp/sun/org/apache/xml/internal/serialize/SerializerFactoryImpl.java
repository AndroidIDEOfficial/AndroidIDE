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
 * Copyright 1999-2002,2004 The Apache Software Foundation.
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
import java.io.UnsupportedEncodingException;
import jaxp.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;

/**
 * Default serializer factory can construct serializers for the three
 * markup serializers (XML, HTML, XHTML ).
 *
 *
 * @author <a href="mailto:Scott_Boag/CAM/Lotus@lotus.com">Scott Boag</a>
 * @author <a href="mailto:arkin@intalio.com">Assaf Arkin</a>
 */
final class SerializerFactoryImpl
    extends SerializerFactory
{


    private String _method;


    SerializerFactoryImpl( String method )
    {
        _method = method;
        if ( ! _method.equals( Method.XML ) &&
             ! _method.equals( Method.HTML ) &&
             ! _method.equals( Method.XHTML ) &&
             ! _method.equals( Method.TEXT ) ) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "MethodNotSupported", new Object[]{method});
            throw new IllegalArgumentException(msg);
        }
    }


    public Serializer makeSerializer( OutputFormat format )
    {
        Serializer serializer;

        serializer = getSerializer( format );
        serializer.setOutputFormat( format );
        return serializer;
    }



    public Serializer makeSerializer( Writer writer,
                                      OutputFormat format )
    {
        Serializer serializer;

        serializer = getSerializer( format );
        serializer.setOutputCharStream( writer );
        return serializer;
    }


    public Serializer makeSerializer( OutputStream output,
                                      OutputFormat format )
        throws UnsupportedEncodingException
    {
        Serializer serializer;

        serializer = getSerializer( format );
        serializer.setOutputByteStream( output );
        return serializer;
    }


    private Serializer getSerializer( OutputFormat format )
    {
        if ( _method.equals( Method.XML ) ) {
            return new XMLSerializer( format );
        } else if ( _method.equals( Method.HTML ) ) {
            return new HTMLSerializer( format );
        }  else if ( _method.equals( Method.XHTML ) ) {
            return new XHTMLSerializer( format );
        }  else if ( _method.equals( Method.TEXT ) ) {
            return new TextSerializer();
        } else {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "MethodNotSupported", new Object[]{_method});
            throw new IllegalStateException(msg);
        }
    }


    protected String getSupportedMethod()
    {
        return _method;
    }


}
