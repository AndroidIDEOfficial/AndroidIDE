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
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: SerializerFactory.java,v 1.2.4.1 2005/09/15 08:15:24 suresh_emailid Exp $
 */
package jaxp.sun.org.apache.xml.internal.serializer;

import java.util.Hashtable;
import java.util.Properties;

import jaxp.xml.transform.OutputKeys;

import jaxp.sun.org.apache.xml.internal.serializer.utils.MsgKey;
import jaxp.sun.org.apache.xml.internal.serializer.utils.Utils;
import jaxp.sun.org.apache.xalan.internal.utils.ObjectFactory;

import jaxp.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import org.xml.sax.ContentHandler;

/**
 * This class is a public API, it is a factory for creating serializers.
   *
   * The properties object passed to the getSerializer() method should be created by
   * the OutputPropertiesFactory. Although the properties object
   * used to create a serializer does not need to be obtained
   * from OutputPropertiesFactory,
   * using this factory ensures that the default key/value properties
   * are set for the given output "method".
   *
   * <p>
   * The standard property keys supported are: "method", "version", "encoding",
   * "omit-xml-declaration", "standalone", doctype-public",
   * "doctype-system", "cdata-section-elements", "indent", "media-type".
   * These property keys and their values are described in the XSLT recommendation,
   * see {@link <a href="http://www.w3.org/TR/1999/REC-xslt-19991116"> XSLT 1.0 recommendation</a>}
   *
   * <p>
   * The value of the "cdata-section-elements" property key is a whitespace
   * separated list of elements. If the element is in a namespace then
   * value is passed in this format: {uri}localName
   *
   * <p>
   * The non-standard property keys supported are defined in {@link OutputPropertiesFactory}.
   *
   * @see OutputPropertiesFactory
   * @see Method
   * @see Serializer
   */
public final class SerializerFactory
{
  /**
   * This constructor is private just to prevent the creation of such an object.
   */

  private SerializerFactory() {

  }
  /**
   * Associates output methods to default output formats.
   */
  private static Hashtable m_formats = new Hashtable();

  /**
   * Returns a serializer for the specified output method. The output method
   * is specified by the value of the property associated with the "method" key.
   * If no implementation exists that supports the specified output method
   * an exception of some type will be thrown.
   * For a list of the output "method" key values see {@link Method}.
   *
   * @param format The output format, minimally the "method" property must be set.
   * @return A suitable serializer.
   * @throws IllegalArgumentException if method is
   * null or an appropriate serializer can't be found
   * @throws Exception if the class for the serializer is found but does not
   * implement ContentHandler.
   * @throws WrappedRuntimeException if an exception is thrown while trying to find serializer
   */
  public static Serializer getSerializer(Properties format)
  {
      Serializer ser;

      try
      {
        String method = format.getProperty(OutputKeys.METHOD);

        if (method == null) {
            String msg = Utils.messages.createMessage(
                MsgKey.ER_FACTORY_PROPERTY_MISSING,
                new Object[] { OutputKeys.METHOD});
            throw new IllegalArgumentException(msg);
        }

        String className =
            format.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);


        if (null == className)
        {
            // Missing Content Handler property, load default using OutputPropertiesFactory
            Properties methodDefaults =
                OutputPropertiesFactory.getDefaultMethodProperties(method);
            className =
            methodDefaults.getProperty(OutputPropertiesFactory.S_KEY_CONTENT_HANDLER);
            if (null == className) {
                String msg = Utils.messages.createMessage(
                    MsgKey.ER_FACTORY_PROPERTY_MISSING,
                    new Object[] { OutputPropertiesFactory.S_KEY_CONTENT_HANDLER});
                throw new IllegalArgumentException(msg);
            }

        }



        Class cls = ObjectFactory.findProviderClass(className, true);

        // _serializers.put(method, cls);

        Object obj = cls.newInstance();

        if (obj instanceof SerializationHandler)
        {
              // this is one of the supplied serializers
            ser = (Serializer) cls.newInstance();
            ser.setOutputFormat(format);
        }
        else
        {
              /*
               *  This  must be a user defined Serializer.
               *  It had better implement ContentHandler.
               */
               if (obj instanceof ContentHandler)
               {

                  /*
                   * The user defined serializer defines ContentHandler,
                   * but we need to wrap it with ToXMLSAXHandler which
                   * will collect SAX-like events and emit true
                   * SAX ContentHandler events to the users handler.
                   */
                  className = SerializerConstants.DEFAULT_SAX_SERIALIZER;
                  cls = ObjectFactory.findProviderClass(className, true);
                  SerializationHandler sh =
                      (SerializationHandler) cls.newInstance();
                  sh.setContentHandler( (ContentHandler) obj);
                  sh.setOutputFormat(format);

                  ser = sh;
               }
               else
               {
                  // user defined serializer does not implement
                  // ContentHandler, ... very bad
                   throw new Exception(
                       Utils.messages.createMessage(
                           MsgKey.ER_SERIALIZER_NOT_CONTENTHANDLER,
                               new Object[] { className}));
               }

        }
      }
      catch (Exception e)
      {
        throw new WrappedRuntimeException(e);
      }

      // If we make it to here ser is not null.
      return ser;
  }
}
