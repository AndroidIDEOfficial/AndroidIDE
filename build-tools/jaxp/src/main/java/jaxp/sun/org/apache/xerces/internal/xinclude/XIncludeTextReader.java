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
 * Copyright 2003-2005 The Apache Software Foundation.
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
package jaxp.sun.org.apache.xerces.internal.xinclude;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import jaxp.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import jaxp.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import jaxp.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import jaxp.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import jaxp.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import jaxp.sun.org.apache.xerces.internal.util.EncodingMap;
import jaxp.sun.org.apache.xerces.internal.util.HTTPInputSource;
import jaxp.sun.org.apache.xerces.internal.util.MessageFormatter;
import jaxp.sun.org.apache.xerces.internal.util.XMLChar;
import jaxp.sun.org.apache.xerces.internal.xni.XMLString;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

/**
 * This class is used for reading resources requested in &lt;include&gt; elements,
 * when the parse attribute of the &lt;include&gt; element is "text".  Using this
 * class will open the location, detect the encoding, and discard the byte order
 * mark, if applicable.
 *
 * REVISIT:
 * Much of the code in this class is taken from XMLEntityManager.  It would be nice
 * if this code could be shared in some way.  However, since XMLEntityManager is used
 * for reading files as XML, and this needs to read files as text, there would need
 * to be some refactoring done.
 *
 * @author Michael Glavassevich, IBM
 * @author Peter McCracken, IBM
 * @author Ankit Pasricha, IBM
 * @author Arun Yadav, Sun Microsystems Inc.
 *
 *
 * @see XIncludeHandler
 */
public class XIncludeTextReader {

    private Reader fReader;
    private XIncludeHandler fHandler;
    private XMLInputSource fSource;
    private XMLErrorReporter fErrorReporter;
    private XMLString fTempString = new XMLString();

    /**
     * Construct the XIncludeReader using the XMLInputSource and XIncludeHandler.
     *
     * @param source The XMLInputSource to use.
     * @param handler The XIncludeHandler to use.
     * @param bufferSize The size of this text reader's buffer.
     */
    public XIncludeTextReader(XMLInputSource source, XIncludeHandler handler, int bufferSize)
        throws IOException {
        fHandler = handler;
        fSource = source;
        fTempString = new XMLString(new char[bufferSize + 1], 0, 0);
    }

    /**
     * Sets the XMLErrorReporter used for reporting errors while
     * reading the text include.
     *
     * @param errorReporter the XMLErrorReporter to be used for
     * reporting errors.
     */
    public void setErrorReporter(XMLErrorReporter errorReporter) {
        fErrorReporter = errorReporter;
    }

    /**
     * Return the Reader for given XMLInputSource.
     *
     * @param source The XMLInputSource to use.
     */
    protected Reader getReader(XMLInputSource source) throws IOException {
        if (source.getCharacterStream() != null) {
            return source.getCharacterStream();
        }
        else {
            InputStream stream = null;

            String encoding = source.getEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            if (source.getByteStream() != null) {
                stream = source.getByteStream();
                // Wrap the InputStream so that it is possible to rewind it.
                if (!(stream instanceof BufferedInputStream)) {
                    stream = new BufferedInputStream(stream, fTempString.ch.length);
                }
            }
            else {
                String expandedSystemId = XMLEntityManager.expandSystemId(source.getSystemId(), source.getBaseSystemId(), false);

                URL url = new URL(expandedSystemId);
                URLConnection urlCon = url.openConnection();

                // If this is an HTTP connection attach any request properties to the request.
                if (urlCon instanceof HttpURLConnection && source instanceof HTTPInputSource) {
                    final HttpURLConnection urlConnection = (HttpURLConnection) urlCon;
                    final HTTPInputSource httpInputSource = (HTTPInputSource) source;

                    // set request properties
                    Iterator propIter = httpInputSource.getHTTPRequestProperties();
                    while (propIter.hasNext()) {
                        Map.Entry entry = (Map.Entry) propIter.next();
                        urlConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
                    }

                    // set preference for redirection
                    boolean followRedirects = httpInputSource.getFollowHTTPRedirects();
                    if (!followRedirects) {
                        XMLEntityManager.setInstanceFollowRedirects(urlConnection, followRedirects);
                    }
                }

                // Wrap the InputStream so that it is possible to rewind it.
                stream = new BufferedInputStream(urlCon.getInputStream());

                // content type will be string like "text/xml; charset=UTF-8" or "text/xml"
                String rawContentType = urlCon.getContentType();

                // text/xml and application/xml offer only one optional parameter
                int index = (rawContentType != null) ? rawContentType.indexOf(';') : -1;

                String contentType = null;
                String charset = null;
                if (index != -1) {
                    // this should be something like "text/xml"
                    contentType = rawContentType.substring(0, index).trim();

                    // this should be something like "charset=UTF-8", but we want to
                    // strip it down to just "UTF-8"
                    charset = rawContentType.substring(index + 1).trim();
                    if (charset.startsWith("charset=")) {
                        // 8 is the length of "charset="
                        charset = charset.substring(8).trim();
                        // strip quotes, if present
                        if ((charset.charAt(0) == '"'
                            && charset.charAt(charset.length() - 1) == '"')
                            || (charset.charAt(0) == '\''
                                && charset.charAt(charset.length() - 1)
                                    == '\'')) {
                            charset =
                                charset.substring(1, charset.length() - 1);
                        }
                    }
                    else {
                        charset = null;
                    }
                }
                else {
                    contentType = rawContentType.trim();
                }

                String detectedEncoding = null;
                /**  The encoding of such a resource is determined by:
                    1 external encoding information, if available, otherwise
                         -- the most common type of external information is the "charset" parameter of a MIME package
                    2 if the media type of the resource is text/xml, application/xml, or matches the conventions text/*+xml or application/*+xml as described in XML Media Types [IETF RFC 3023], the encoding is recognized as specified in XML 1.0, otherwise
                    3 the value of the encoding attribute if one exists, otherwise
                    4 UTF-8.
                 **/
                if (contentType.equals("text/xml")) {
                    if (charset != null) {
                        detectedEncoding = charset;
                    }
                    else {
                        // see RFC2376 or 3023, section 3.1
                        detectedEncoding = "US-ASCII";
                    }
                }
                else if (contentType.equals("application/xml")) {
                    if (charset != null) {
                        detectedEncoding = charset;
                    }
                    else {
                        // see RFC2376 or 3023, section 3.2
                        detectedEncoding = getEncodingName(stream);
                    }
                }
                else if (contentType.endsWith("+xml")) {
                    detectedEncoding = getEncodingName(stream);
                }

                if (detectedEncoding != null) {
                    encoding = detectedEncoding;
                }
                // else 3 or 4.
            }

            encoding = encoding.toUpperCase(Locale.ENGLISH);

            // eat the Byte Order Mark
            encoding = consumeBOM(stream, encoding);

            // If the document is UTF-8 or US-ASCII use
            // the Xerces readers for these encodings. For
            // US-ASCII consult the encoding map since
            // this encoding has many aliases.
            if (encoding.equals("UTF-8")) {
                return new UTF8Reader(stream,
                    fTempString.ch.length,
                    fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN),
                    fErrorReporter.getLocale() );
            }

            // Try to use a Java reader.
            String javaEncoding = EncodingMap.getIANA2JavaMapping(encoding);

            // If the specified encoding wasn't a recognized IANA encoding throw an IOException.
            // The XIncludeHandler will report this as a ResourceError and then will
            // attempt to include a fallback if there is one.
            if (javaEncoding == null) {
                MessageFormatter aFormatter =
                    fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN);
                Locale aLocale = fErrorReporter.getLocale();
                throw new IOException( aFormatter.formatMessage( aLocale,
                    "EncodingDeclInvalid",
                    new Object[] {encoding} ) );
            }
            else if (javaEncoding.equals("ASCII")) {
                return new ASCIIReader(stream,
                    fTempString.ch.length,
                    fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN),
                    fErrorReporter.getLocale() );
            }

            return new InputStreamReader(stream, javaEncoding);
        }
    }

    /**
     * XMLEntityManager cares about endian-ness, since it creates its own optimized
     * readers. Since we're just using generic Java readers for now, we're not caring
     * about endian-ness.  If this changes, even more code needs to be copied from
     * XMLEntity manager. -- PJM
     */
    protected String getEncodingName(InputStream stream) throws IOException {
        final byte[] b4 = new byte[4];
        String encoding = null;

        // this has the potential to throw an exception
        // it will be fixed when we ensure the stream is rewindable (see note above)
        stream.mark(4);
        int count = stream.read(b4, 0, 4);
        stream.reset();
        if (count == 4) {
            encoding = getEncodingName(b4);
        }

        return encoding;
    }

    /**
     * Removes the byte order mark from the stream, if
     * it exists and returns the encoding name.
     *
     * @param stream
     * @param encoding
     * @throws IOException
     */
    protected String consumeBOM(InputStream stream, String encoding)
        throws IOException {

        byte[] b = new byte[3];
        int count = 0;
        stream.mark(3);
        if (encoding.equals("UTF-8")) {
            count = stream.read(b, 0, 3);
            if (count == 3) {
                final int b0 = b[0] & 0xFF;
                final int b1 = b[1] & 0xFF;
                final int b2 = b[2] & 0xFF;
                if (b0 != 0xEF || b1 != 0xBB || b2 != 0xBF) {
                    // First three bytes are not BOM, so reset.
                    stream.reset();
                }
            }
            else {
                stream.reset();
            }
        }
        else if (encoding.startsWith("UTF-16")) {
            count = stream.read(b, 0, 2);
            if (count == 2) {
                final int b0 = b[0] & 0xFF;
                final int b1 = b[1] & 0xFF;
                if (b0 == 0xFE && b1 == 0xFF) {
                    return "UTF-16BE";
                }
                else if (b0 == 0xFF && b1 == 0xFE) {
                    return "UTF-16LE";
                }
            }
            // First two bytes are not BOM, so reset.
            stream.reset();
        }
        // We could do UTF-32, but since the getEncodingName() doesn't support that
        // we won't support it here.
        // To implement UTF-32, look for:  00 00 FE FF for big-endian
        //                             or  FF FE 00 00 for little-endian
        return encoding;
    }

    /**
     * REVISIT: This code is taken from jaxp.sun.org.apache.xerces.internal.impl.XMLEntityManager.
     *          Is there any way we can share the code, without having it implemented twice?
     *          I think we should make it public and static in XMLEntityManager. --PJM
     *
     * Returns the IANA encoding name that is auto-detected from
     * the bytes specified, with the endian-ness of that encoding where appropriate.
     *
     * @param b4    The first four bytes of the input.
     * @return the encoding name, or null if no encoding could be detected
     */
    protected String getEncodingName(byte[] b4) {

        // UTF-16, with BOM
        int b0 = b4[0] & 0xFF;
        int b1 = b4[1] & 0xFF;
        if (b0 == 0xFE && b1 == 0xFF) {
            // UTF-16, big-endian
            return "UTF-16BE";
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            // UTF-16, little-endian
            return "UTF-16LE";
        }

        // UTF-8 with a BOM
        int b2 = b4[2] & 0xFF;
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return "UTF-8";
        }

        // other encodings
        int b3 = b4[3] & 0xFF;
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {
            // UCS-4, big endian (1234)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, little endian (4321)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {
            // UCS-4, unusual octet order (2143)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, unusual octect order (3412)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
            // UTF-16, big-endian, no BOM
            // (or could turn out to be UCS-2...
            return "UTF-16BE";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
            // UTF-16, little-endian, no BOM
            // (or could turn out to be UCS-2...
            return "UTF-16LE";
        }
        if (b0 == 0x4C && b1 == 0x6F && b2 == 0xA7 && b3 == 0x94) {
            // EBCDIC
            // a la xerces1, return CP037 instead of EBCDIC here
            return "CP037";
        }

        // this signals us to use the value from the encoding attribute
        return null;

    } // getEncodingName(byte[]):Object[]

    /**
     * Read the input stream as text, and pass the text on to the XIncludeHandler
     * using calls to characters().  This will read all of the text it can from the
     * resource.
     *
     * @throws IOException
     */
    public void parse() throws IOException {

        fReader = getReader(fSource);
        fSource = null;
        int readSize = fReader.read(fTempString.ch, 0, fTempString.ch.length - 1);
        while (readSize != -1) {
            for (int i = 0; i < readSize; ++i) {
                char ch = fTempString.ch[i];
                if (!isValid(ch)) {
                    if (XMLChar.isHighSurrogate(ch)) {
                        int ch2;
                        // retrieve next character
                        if (++i < readSize) {
                            ch2 = fTempString.ch[i];
                        }
                        // handle rare boundary case
                        else {
                            ch2 = fReader.read();
                            if (ch2 != -1) {
                                fTempString.ch[readSize++] = (char) ch2;
                            }
                        }
                        if (XMLChar.isLowSurrogate(ch2)) {
                            // convert surrogates to a supplemental character
                            int sup = XMLChar.supplemental(ch, (char)ch2);
                            if (!isValid(sup)) {
                                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                                                           "InvalidCharInContent",
                                                           new Object[] { Integer.toString(sup, 16) },
                                                           XMLErrorReporter.SEVERITY_FATAL_ERROR);
                            }
                        }
                        else {
                            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                                                       "InvalidCharInContent",
                                                       new Object[] { Integer.toString(ch2, 16) },
                                                       XMLErrorReporter.SEVERITY_FATAL_ERROR);
                        }
                    }
                    else {
                        fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                                                   "InvalidCharInContent",
                                                   new Object[] { Integer.toString(ch, 16) },
                                                   XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }
            }
            if (fHandler != null && readSize > 0) {
                fTempString.offset = 0;
                fTempString.length = readSize;
                fHandler.characters(
                    fTempString,
                    fHandler.modifyAugmentations(null, true));
            }
            readSize = fReader.read(fTempString.ch, 0, fTempString.ch.length - 1);
        }

    }

    /**
     * Sets the input source on this text reader.
     *
     * @param source The XMLInputSource to use.
     */
    public void setInputSource(XMLInputSource source) {
        fSource = source;
    }

    /**
     * Closes the stream.  Call this after parse(), or when there is no longer any need
     * for this object.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (fReader != null) {
            fReader.close();
            fReader = null;
        }
    }

    /**
     * Returns true if the specified character is a valid XML character
     * as per the rules of XML 1.0.
     *
     * @param ch The character to check.
     */
    protected boolean isValid(int ch) {
        return XMLChar.isValid(ch);
    }

    /**
     * Sets the buffer size property for the reader which decides the chunk sizes that are parsed
     * by the reader at a time and passed to the handler
     *
     * @param bufferSize The size of the buffer desired
     */
    protected void setBufferSize(int bufferSize) {
        if (fTempString.ch.length != ++bufferSize) {
            fTempString.ch = new char[bufferSize];
        }
    }

}
