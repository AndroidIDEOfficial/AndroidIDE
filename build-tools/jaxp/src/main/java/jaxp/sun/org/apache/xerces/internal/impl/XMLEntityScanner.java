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
 * Copyright (c) 2003, 2006, Oracle and/or its affiliates. All rights reserved.
 */

/*
 * Copyright 2005 The Apache Software Foundation.
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

package jaxp.sun.org.apache.xerces.internal.impl;

import java.io.EOFException;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import jaxp.sun.org.apache.xerces.internal.xni.XMLLocator;
import jaxp.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import jaxp.sun.org.apache.xerces.internal.xni.XNIException;
import jaxp.sun.xml.internal.stream.Entity;
import jaxp.sun.xml.internal.stream.XMLBufferListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


import jaxp.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import jaxp.sun.org.apache.xerces.internal.impl.io.UCSReader;
import jaxp.sun.org.apache.xerces.internal.impl.io.UTF8Reader;


import jaxp.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import jaxp.sun.org.apache.xerces.internal.util.EncodingMap;

import jaxp.sun.org.apache.xerces.internal.util.SymbolTable;
import jaxp.sun.org.apache.xerces.internal.util.XMLChar;
import jaxp.sun.org.apache.xerces.internal.util.XMLStringBuffer;
import jaxp.sun.org.apache.xerces.internal.xni.QName;
import jaxp.sun.org.apache.xerces.internal.xni.XMLString;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import jaxp.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import jaxp.sun.org.apache.xerces.internal.xni.*;

/**
 * Implements the entity scanner methods.
 *
 * @author Neeraj Bajaj, Sun Microsystems
 * @author Andy Clark, IBM
 * @author Arnaud  Le Hors, IBM
 * @author K.Venugopal Sun Microsystems
 *
 */
public class XMLEntityScanner implements XMLLocator {


    protected Entity.ScannedEntity fCurrentEntity = null ;
    protected int fBufferSize = XMLEntityManager.DEFAULT_BUFFER_SIZE;

    protected XMLEntityManager fEntityManager ;

    /** Debug switching readers for encodings. */
    private static final boolean DEBUG_ENCODINGS = false;
    /** Listeners which should know when load is being called */
    private Vector listeners = new Vector();

    private static final boolean [] VALID_NAMES = new boolean[127];

    /**
     * Debug printing of buffer. This debugging flag works best when you
     * resize the DEFAULT_BUFFER_SIZE down to something reasonable like
     * 64 characters.
     */
    private static final boolean DEBUG_BUFFER = false;
    private static final boolean DEBUG_SKIP_STRING = false;
    /**
     * To signal the end of the document entity, this exception will be thrown.
     */
    private static final EOFException END_OF_DOCUMENT_ENTITY = new EOFException() {
        private static final long serialVersionUID = 980337771224675268L;
        public Throwable fillInStackTrace() {
            return this;
        }
    };

    protected SymbolTable fSymbolTable = null;
    protected XMLErrorReporter fErrorReporter = null;
    int [] whiteSpaceLookup = new int[100];
    int whiteSpaceLen = 0;
    boolean whiteSpaceInfoNeeded = true;

    /**
     * Allow Java encoding names. This feature identifier is:
     * http://apache.org/xml/features/allow-java-encodings
     */
    protected boolean fAllowJavaEncodings;

    //Will be used only during internal subsets.
    //for appending data.

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE =
            Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String ERROR_REPORTER =
            Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** Feature identifier: allow Java encodings. */
    protected static final String ALLOW_JAVA_ENCODINGS =
            Constants.XERCES_FEATURE_PREFIX + Constants.ALLOW_JAVA_ENCODINGS_FEATURE;

    protected PropertyManager fPropertyManager = null ;

    boolean isExternal = false;
    static {

        for(int i=0x0041;i<=0x005A ; i++){
            VALID_NAMES[i]=true;
        }
        for(int i=0x0061;i<=0x007A; i++){
            VALID_NAMES[i]=true;
        }
        for(int i=0x0030;i<=0x0039; i++){
            VALID_NAMES[i]=true;
        }
        VALID_NAMES[45]=true;
        VALID_NAMES[46]=true;
        VALID_NAMES[58]=true;
        VALID_NAMES[95]=true;
    }
    // SAPJVM: Remember, that the XML version has explicitly been set,
    // so that XMLStreamReader.getVersion() can find that out.
    boolean xmlVersionSetExplicitly = false;
    //
    // Constructors
    //

    /** Default constructor. */
    public XMLEntityScanner() {
    } // <init>()


    /**  private constructor, this class can only be instantiated within this class. Instance of this class should
     *    be obtained using getEntityScanner() or getEntityScanner(ScannedEntity scannedEntity)
     *    @see getEntityScanner()
     *    @see getEntityScanner(ScannedEntity)
     */
    public XMLEntityScanner(PropertyManager propertyManager, XMLEntityManager entityManager) {
        fEntityManager = entityManager ;
        reset(propertyManager);
    } // <init>()


    // set buffer size:
    public final void setBufferSize(int size) {
        // REVISIT: Buffer size passed to entity scanner
        // was not being kept in synch with the actual size
        // of the buffers in each scanned entity. If any
        // of the buffers were actually resized, it was possible
        // that the parser would throw an ArrayIndexOutOfBoundsException
        // for documents which contained names which are longer than
        // the current buffer size. Conceivably the buffer size passed
        // to entity scanner could be used to determine a minimum size
        // for resizing, if doubling its size is smaller than this
        // minimum. -- mrglavas
        fBufferSize = size;
    }

    /**
     * Resets the components.
     */
    public void reset(PropertyManager propertyManager){
        fSymbolTable = (SymbolTable)propertyManager.getProperty(SYMBOL_TABLE) ;
        fErrorReporter = (XMLErrorReporter)propertyManager.getProperty(ERROR_REPORTER) ;
        fCurrentEntity = null;
        whiteSpaceLen = 0;
        whiteSpaceInfoNeeded = true;
        listeners.clear();
    }

    /**
     * Resets the component. The component can query the component manager
     * about any features and properties that affect the operation of the
     * component.
     *
     * @param componentManager The component manager.
     *
     * @throws SAXException Thrown by component on initialization error.
     *                      For example, if a feature or property is
     *                      required for the operation of the component, the
     *                      component manager may throw a
     *                      SAXNotRecognizedException or a
     *                      SAXNotSupportedException.
     */
    public void reset(XMLComponentManager componentManager)
    throws XMLConfigurationException {

        //System.out.println(" this is being called");
        // xerces features
        fAllowJavaEncodings = componentManager.getFeature(ALLOW_JAVA_ENCODINGS, false);

        //xerces properties
        fSymbolTable = (SymbolTable)componentManager.getProperty(SYMBOL_TABLE);
        fErrorReporter = (XMLErrorReporter)componentManager.getProperty(ERROR_REPORTER);
        fCurrentEntity = null;
        whiteSpaceLen = 0;
        whiteSpaceInfoNeeded = true;
        listeners.clear();
    } // reset(XMLComponentManager)


    public final void reset(SymbolTable symbolTable, XMLEntityManager entityManager,
            XMLErrorReporter reporter) {
        fCurrentEntity = null;
        fSymbolTable = symbolTable;
        fEntityManager = entityManager;
        fErrorReporter = reporter;
    }

    /**
     * Returns the XML version of the current entity. This will normally be the
     * value from the XML or text declaration or defaulted by the parser. Note that
     * that this value may be different than the version of the processing rules
     * applied to the current entity. For instance, an XML 1.1 document may refer to
     * XML 1.0 entities. In such a case the rules of XML 1.1 are applied to the entire
     * document. Also note that, for a given entity, this value can only be considered
     * final once the XML or text declaration has been read or once it has been
     * determined that there is no such declaration.
     */
    public final String getXMLVersion() {
        if (fCurrentEntity != null) {
            return fCurrentEntity.xmlVersion;
        }
        return null;
    } // getXMLVersion():String

    /**
     * Sets the XML version. This method is used by the
     * scanners to report the value of the version pseudo-attribute
     * in an XML or text declaration.
     *
     * @param xmlVersion the XML version of the current entity
     */
    public final void setXMLVersion(String xmlVersion) {
        xmlVersionSetExplicitly = true; // SAPJVM
        fCurrentEntity.xmlVersion = xmlVersion;
    } // setXMLVersion(String)


    /** set the instance of current scanned entity.
     *   @param ScannedEntity
     */

    public final void setCurrentEntity(Entity.ScannedEntity scannedEntity){
        fCurrentEntity = scannedEntity ;
        if(fCurrentEntity != null){
            isExternal = fCurrentEntity.isExternal();
            if(DEBUG_BUFFER)
                System.out.println("Current Entity is "+scannedEntity.name);
        }
    }

    public  Entity.ScannedEntity getCurrentEntity(){
        return fCurrentEntity ;
    }
    //
    // XMLEntityReader methods
    //

    /**
     * Returns the base system identifier of the currently scanned
     * entity, or null if none is available.
     */
    public final String getBaseSystemId() {
        return (fCurrentEntity != null && fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getExpandedSystemId() : null;
    } // getBaseSystemId():String

    /**
     * @see XMLResourceIdentifier#setBaseSystemId(String)
     */
    public void setBaseSystemId(String systemId) {
        //no-op
    }

    ///////////// Locator methods start.
    public final int getLineNumber(){
        //if the entity is closed, we should return -1
        //xxx at first place why such call should be there...
        return fCurrentEntity != null ? fCurrentEntity.lineNumber : -1 ;
    }

    /**
     * @see XMLLocator#setLineNumber(int)
     */
    public void setLineNumber(int line) {
        //no-op
    }


    public final int getColumnNumber(){
        //if the entity is closed, we should return -1
        //xxx at first place why such call should be there...
        return fCurrentEntity != null ? fCurrentEntity.columnNumber : -1 ;
    }

    /**
     * @see XMLLocator#setColumnNumber(int)
     */
    public void setColumnNumber(int col) {
        // no-op
    }


    public final int getCharacterOffset(){
        return fCurrentEntity != null ? fCurrentEntity.fTotalCountTillLastLoad + fCurrentEntity.position : -1 ;
    }

    /** Returns the expanded system identifier.  */
    public final String getExpandedSystemId() {
        return (fCurrentEntity != null && fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getExpandedSystemId() : null;
    }

    /**
     * @see XMLResourceIdentifier#setExpandedSystemId(String)
     */
    public void setExpandedSystemId(String systemId) {
        //no-op
    }

    /** Returns the literal system identifier.  */
    public final String getLiteralSystemId() {
        return (fCurrentEntity != null && fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getLiteralSystemId() : null;
    }

    /**
     * @see XMLResourceIdentifier#setLiteralSystemId(String)
     */
    public void setLiteralSystemId(String systemId) {
        //no-op
    }

    /** Returns the public identifier.  */
    public final String getPublicId() {
        return (fCurrentEntity != null && fCurrentEntity.entityLocation != null) ? fCurrentEntity.entityLocation.getPublicId() : null;
    }

    /**
     * @see XMLResourceIdentifier#setPublicId(String)
     */
    public void setPublicId(String publicId) {
        //no-op
    }

    ///////////////// Locator methods finished.

    /** the version of the current entity being scanned */
    public void setVersion(String version){
        fCurrentEntity.version = version;
    }

    public String getVersion(){
        if (fCurrentEntity != null)
            return fCurrentEntity.version ;
        return null;
    }

    /**
     * Returns the encoding of the current entity.
     * Note that, for a given entity, this value can only be
     * considered final once the encoding declaration has been read (or once it
     * has been determined that there is no such declaration) since, no encoding
     * having been specified on the XMLInputSource, the parser
     * will make an initial "guess" which could be in error.
     */
    public final String getEncoding() {
        if (fCurrentEntity != null) {
            return fCurrentEntity.encoding;
        }
        return null;
    } // getEncoding():String

    /**
     * Sets the encoding of the scanner. This method is used by the
     * scanners if the XMLDecl or TextDecl line contains an encoding
     * pseudo-attribute.
     * <p>
     * <strong>Note:</strong> The underlying character reader on the
     * current entity will be changed to accomodate the new encoding.
     * However, the new encoding is ignored if the current reader was
     * not constructed from an input stream (e.g. an external entity
     * that is resolved directly to the appropriate java.io.Reader
     * object).
     *
     * @param encoding The IANA encoding name of the new encoding.
     *
     * @throws IOException Thrown if the new encoding is not supported.
     *
     * @see EncodingMap
     */
    public final void setEncoding(String encoding) throws IOException {

        if (DEBUG_ENCODINGS) {
            System.out.println("$$$ setEncoding: "+encoding);
        }

        if (fCurrentEntity.stream != null) {
            // if the encoding is the same, don't change the reader and
            // re-use the original reader used by the OneCharReader
            // NOTE: Besides saving an object, this overcomes deficiencies
            //       in the UTF-16 reader supplied with the standard Java
            //       distribution (up to and including 1.3). The UTF-16
            //       decoder buffers 8K blocks even when only asked to read
            //       a single char! -Ac
            if (fCurrentEntity.encoding == null ||
                    !fCurrentEntity.encoding.equals(encoding)) {
                // UTF-16 is a bit of a special case.  If the encoding is UTF-16,
                // and we know the endian-ness, we shouldn't change readers.
                // If it's ISO-10646-UCS-(2|4), then we'll have to deduce
                // the endian-ness from the encoding we presently have.
                if(fCurrentEntity.encoding != null && fCurrentEntity.encoding.startsWith("UTF-16")) {
                    String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
                    if(ENCODING.equals("UTF-16")) return;
                    if(ENCODING.equals("ISO-10646-UCS-4")) {
                        if(fCurrentEntity.encoding.equals("UTF-16BE")) {
                            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, UCSReader.UCS4BE);
                        } else {
                            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, UCSReader.UCS4LE);
                        }
                        return;
                    }
                    if(ENCODING.equals("ISO-10646-UCS-2")) {
                        if(fCurrentEntity.encoding.equals("UTF-16BE")) {
                            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, UCSReader.UCS2BE);
                        } else {
                            fCurrentEntity.reader = new UCSReader(fCurrentEntity.stream, UCSReader.UCS2LE);
                        }
                        return;
                    }
                }
                // wrap a new reader around the input stream, changing
                // the encoding
                if (DEBUG_ENCODINGS) {
                    System.out.println("$$$ creating new reader from stream: "+
                            fCurrentEntity.stream);
                }
                //fCurrentEntity.stream.reset();
                fCurrentEntity.reader = createReader(fCurrentEntity.stream, encoding, null);
                fCurrentEntity.encoding = encoding;

            } else {
                if (DEBUG_ENCODINGS)
                    System.out.println("$$$ reusing old reader on stream");
            }
        }

    } // setEncoding(String)

    /** Returns true if the current entity being scanned is external. */
    public final boolean isExternal() {
        return fCurrentEntity.isExternal();
    } // isExternal():boolean

    public int getChar(int relative) throws IOException{
        if(arrangeCapacity(relative + 1, false)){
            return fCurrentEntity.ch[fCurrentEntity.position + relative];
        }else{
            return -1;
        }
    }//getChar()

    /**
     * Returns the next character on the input.
     * <p>
     * <strong>Note:</strong> The character is <em>not</em> consumed.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public int peekChar() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(peekChar: ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // peek at character
        int c = fCurrentEntity.ch[fCurrentEntity.position];

        // return peeked character
        if (DEBUG_BUFFER) {
            System.out.print(")peekChar: ");
            print();
            if (isExternal) {
                System.out.println(" -> '"+(c!='\r'?(char)c:'\n')+"'");
            } else {
                System.out.println(" -> '"+(char)c+"'");
            }
        }
        if (isExternal) {
            return c != '\r' ? c : '\n';
        } else {
            return c;
        }

    } // peekChar():int

    /**
     * Returns the next character on the input.
     * <p>
     * <strong>Note:</strong> The character is consumed.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public int scanChar() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanChar: ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // scan character
        int c = fCurrentEntity.ch[fCurrentEntity.position++];
        if (c == '\n' ||
                (c == '\r' && isExternal)) {
            fCurrentEntity.lineNumber++;
            fCurrentEntity.columnNumber = 1;
            if (fCurrentEntity.position == fCurrentEntity.count) {
                invokeListeners(1);
                fCurrentEntity.ch[0] = (char)c;
                load(1, false);
            }
            if (c == '\r' && isExternal) {
                if (fCurrentEntity.ch[fCurrentEntity.position++] != '\n') {
                    fCurrentEntity.position--;
                }
                c = '\n';
            }
        }

        // return character that was scanned
        if (DEBUG_BUFFER) {
            System.out.print(")scanChar: ");
            print();
            System.out.println(" -> '"+(char)c+"'");
        }
        fCurrentEntity.columnNumber++;
        return c;

    } // scanChar():int

    /**
     * Returns a string matching the NMTOKEN production appearing immediately
     * on the input as a symbol, or null if NMTOKEN Name string is present.
     * <p>
     * <strong>Note:</strong> The NMTOKEN characters are consumed.
     * <p>
     * <strong>Note:</strong> The string returned must be a symbol. The
     * SymbolTable can be used for this purpose.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     *
     * @see SymbolTable
     * @see XMLChar#isName
     */
    public String scanNmtoken() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanNmtoken: ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // scan nmtoken
        int offset = fCurrentEntity.position;
        boolean vc = false;
        char c;
        while (true){
            //while (XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position])) {
            c = fCurrentEntity.ch[fCurrentEntity.position];
            if(c < 127){
                vc = VALID_NAMES[c];
            }else{
                vc = XMLChar.isName(c);
            }
            if(!vc)break;

            if (++fCurrentEntity.position == fCurrentEntity.count) {
                int length = fCurrentEntity.position - offset;
                invokeListeners(length);
                if (length == fCurrentEntity.fBufferSize) {
                    // bad luck we have to resize our buffer
                    char[] tmp = new char[fCurrentEntity.fBufferSize * 2];
                    System.arraycopy(fCurrentEntity.ch, offset,
                            tmp, 0, length);
                    fCurrentEntity.ch = tmp;
                    fCurrentEntity.fBufferSize *= 2;
                } else {
                    System.arraycopy(fCurrentEntity.ch, offset,
                            fCurrentEntity.ch, 0, length);
                }
                offset = 0;
                if (load(length, false)) {
                    break;
                }
            }
        }
        int length = fCurrentEntity.position - offset;
        fCurrentEntity.columnNumber += length;

        // return nmtoken
        String symbol = null;
        if (length > 0) {
            symbol = fSymbolTable.addSymbol(fCurrentEntity.ch, offset, length);
        }
        if (DEBUG_BUFFER) {
            System.out.print(")scanNmtoken: ");
            print();
            System.out.println(" -> "+String.valueOf(symbol));
        }
        return symbol;

    } // scanNmtoken():String

    /**
     * Returns a string matching the Name production appearing immediately
     * on the input as a symbol, or null if no Name string is present.
     * <p>
     * <strong>Note:</strong> The Name characters are consumed.
     * <p>
     * <strong>Note:</strong> The string returned must be a symbol. The
     * SymbolTable can be used for this purpose.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     *
     * @see SymbolTable
     * @see XMLChar#isName
     * @see XMLChar#isNameStart
     */
    public String scanName() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanName: ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // scan name
        int offset = fCurrentEntity.position;
        if (XMLChar.isNameStart(fCurrentEntity.ch[offset])) {
            if (++fCurrentEntity.position == fCurrentEntity.count) {
                invokeListeners(1);
                fCurrentEntity.ch[0] = fCurrentEntity.ch[offset];
                offset = 0;
                if (load(1, false)) {
                    fCurrentEntity.columnNumber++;
                    String symbol = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);

                    if (DEBUG_BUFFER) {
                        System.out.print(")scanName: ");
                        print();
                        System.out.println(" -> "+String.valueOf(symbol));
                    }
                    return symbol;
                }
            }
            boolean vc =false;
            while (true ){
                //XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position])) ;
                char c = fCurrentEntity.ch[fCurrentEntity.position];
                if(c < 127){
                    vc = VALID_NAMES[c];
                }else{
                    vc = XMLChar.isName(c);
                }
                if(!vc)break;
                if (++fCurrentEntity.position == fCurrentEntity.count) {
                    int length = fCurrentEntity.position - offset;
                    invokeListeners(length);
                    if (length == fCurrentEntity.fBufferSize) {
                        // bad luck we have to resize our buffer
                        char[] tmp = new char[fCurrentEntity.fBufferSize * 2];
                        System.arraycopy(fCurrentEntity.ch, offset,
                                tmp, 0, length);
                        fCurrentEntity.ch = tmp;
                        fCurrentEntity.fBufferSize *= 2;
                    } else {
                        System.arraycopy(fCurrentEntity.ch, offset,
                                fCurrentEntity.ch, 0, length);
                    }
                    offset = 0;
                    if (load(length, false)) {
                        break;
                    }
                }
            }
        }
        int length = fCurrentEntity.position - offset;
        fCurrentEntity.columnNumber += length;

        // return name
        String symbol;
        if (length > 0) {
            symbol = fSymbolTable.addSymbol(fCurrentEntity.ch, offset, length);
        } else
            symbol = null;
        if (DEBUG_BUFFER) {
            System.out.print(")scanName: ");
            print();
            System.out.println(" -> "+String.valueOf(symbol));
        }
        return symbol;

    } // scanName():String

    /**
     * Scans a qualified name from the input, setting the fields of the
     * QName structure appropriately.
     * <p>
     * <strong>Note:</strong> The qualified name characters are consumed.
     * <p>
     * <strong>Note:</strong> The strings used to set the values of the
     * QName structure must be symbols. The SymbolTable can be used for
     * this purpose.
     *
     * @param qname The qualified name structure to fill.
     *
     * @return Returns true if a qualified name appeared immediately on
     *         the input and was scanned, false otherwise.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     *
     * @see SymbolTable
     * @see XMLChar#isName
     * @see XMLChar#isNameStart
     */
    public boolean scanQName(QName qname) throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanQName, "+qname+": ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // scan qualified name
        int offset = fCurrentEntity.position;

        //making a check if if the specified character is a valid name start character
        //as defined by production [5] in the XML 1.0 specification.
        // Name ::= (Letter | '_' | ':') (NameChar)*

        if (XMLChar.isNameStart(fCurrentEntity.ch[offset])) {
            if (++fCurrentEntity.position == fCurrentEntity.count) {
                invokeListeners(1);
                fCurrentEntity.ch[0] = fCurrentEntity.ch[offset];
                offset = 0;

                if (load(1, false)) {
                    fCurrentEntity.columnNumber++;
                    //adding into symbol table.
                    //XXX We are trying to add single character in SymbolTable??????
                    String name = fSymbolTable.addSymbol(fCurrentEntity.ch, 0, 1);
                    qname.setValues(null, name, name, null);
                    if (DEBUG_BUFFER) {
                        System.out.print(")scanQName, "+qname+": ");
                        print();
                        System.out.println(" -> true");
                    }
                    return true;
                }
            }
            int index = -1;
            boolean vc = false;
            while ( true){

                //XMLChar.isName(fCurrentEntity.ch[fCurrentEntity.position])) ;
                char c = fCurrentEntity.ch[fCurrentEntity.position];
                if(c < 127){
                    vc = VALID_NAMES[c];
                }else{
                    vc = XMLChar.isName(c);
                }
                if(!vc)break;
                if (c == ':') {
                    if (index != -1) {
                        break;
                    }
                    index = fCurrentEntity.position;
                }
                if (++fCurrentEntity.position == fCurrentEntity.count) {
                    int length = fCurrentEntity.position - offset;
                    invokeListeners(length);
                    if (length == fCurrentEntity.fBufferSize) {
                        // bad luck we have to resize our buffer
                        char[] tmp = new char[fCurrentEntity.fBufferSize * 2];
                        System.arraycopy(fCurrentEntity.ch, offset,
                                tmp, 0, length);
                        fCurrentEntity.ch = tmp;
                        fCurrentEntity.fBufferSize *= 2;
                    } else {
                        System.arraycopy(fCurrentEntity.ch, offset,
                                fCurrentEntity.ch, 0, length);
                    }
                    if (index != -1) {
                        index = index - offset;
                    }
                    offset = 0;
                    if (load(length, false)) {
                        break;
                    }
                }
            }
            int length = fCurrentEntity.position - offset;
            fCurrentEntity.columnNumber += length;
            if (length > 0) {
                String prefix = null;
                String localpart = null;
                String rawname = fSymbolTable.addSymbol(fCurrentEntity.ch,
                        offset, length);

                if (index != -1) {
                    int prefixLength = index - offset;
                    prefix = fSymbolTable.addSymbol(fCurrentEntity.ch,
                            offset, prefixLength);
                    int len = length - prefixLength - 1;
                    localpart = fSymbolTable.addSymbol(fCurrentEntity.ch,
                            index + 1, len);

                } else {
                    localpart = rawname;
                }
                qname.setValues(prefix, localpart, rawname, null);
                if (DEBUG_BUFFER) {
                    System.out.print(")scanQName, "+qname+": ");
                    print();
                    System.out.println(" -> true");
                }
                return true;
            }
        }

        // no qualified name found
        if (DEBUG_BUFFER) {
            System.out.print(")scanQName, "+qname+": ");
            print();
            System.out.println(" -> false");
        }
        return false;

    } // scanQName(QName):boolean

    /**
     * CHANGED:
     * Scans a range of parsed character data, This function appends the character data to
     * the supplied buffer.
     * <p>
     * <strong>Note:</strong> The characters are consumed.
     * <p>
     * <strong>Note:</strong> This method does not guarantee to return
     * the longest run of parsed character data. This method may return
     * before markup due to reaching the end of the input buffer or any
     * other reason.
     * <p>
     *
     * @param content The content structure to fill.
     *
     * @return Returns the next character on the input, if known. This
     *         value may be -1 but this does <em>note</em> designate
     *         end of file.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public int scanContent(XMLString content) throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanContent: ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        } else if (fCurrentEntity.position == fCurrentEntity.count - 1) {
            invokeListeners(0);
            fCurrentEntity.ch[0] = fCurrentEntity.ch[fCurrentEntity.count - 1];
            load(1, false);
            fCurrentEntity.position = 0;
        }

        // normalize newlines
        int offset = fCurrentEntity.position;
        int c = fCurrentEntity.ch[offset];
        int newlines = 0;
        if (c == '\n' || (c == '\r' && isExternal)) {
            if (DEBUG_BUFFER) {
                System.out.print("[newline, "+offset+", "+fCurrentEntity.position+": ");
                print();
                System.out.println();
            }
            do {
                c = fCurrentEntity.ch[fCurrentEntity.position++];
                if (c == '\r' && isExternal) {
                    newlines++;
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count) {
                        offset = 0;
                        invokeListeners(newlines);
                        fCurrentEntity.position = newlines;
                        if (load(newlines, false)) {
                            break;
                        }
                    }
                    if (fCurrentEntity.ch[fCurrentEntity.position] == '\n') {
                        fCurrentEntity.position++;
                        offset++;
                    }
                    /*** NEWLINE NORMALIZATION ***/
                    else {
                        newlines++;
                    }
                } else if (c == '\n') {
                    newlines++;
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count) {
                        offset = 0;
                        invokeListeners(newlines);
                        fCurrentEntity.position = newlines;
                        if (load(newlines, false)) {
                            break;
                        }
                    }
                } else {
                    fCurrentEntity.position--;
                    break;
                }
            } while (fCurrentEntity.position < fCurrentEntity.count - 1);
            for (int i = offset; i < fCurrentEntity.position; i++) {
                fCurrentEntity.ch[i] = '\n';
            }
            int length = fCurrentEntity.position - offset;
            if (fCurrentEntity.position == fCurrentEntity.count - 1) {
                //CHANGED: dont replace the value.. append to the buffer. This gives control to the callee
                //on buffering the data..
                content.setValues(fCurrentEntity.ch, offset, length);
                //content.append(fCurrentEntity.ch, offset, length);
                if (DEBUG_BUFFER) {
                    System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                    print();
                    System.out.println();
                }
                return -1;
            }
            if (DEBUG_BUFFER) {
                System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                print();
                System.out.println();
            }
        }

        while (fCurrentEntity.position < fCurrentEntity.count) {
            c = fCurrentEntity.ch[fCurrentEntity.position++];
            if (!XMLChar.isContent(c)) {
                fCurrentEntity.position--;
                break;
            }
        }
        int length = fCurrentEntity.position - offset;
        fCurrentEntity.columnNumber += length - newlines;

        //CHANGED: dont replace the value.. append to the buffer. This gives control to the callee
        //on buffering the data..
        content.setValues(fCurrentEntity.ch, offset, length);
        //content.append(fCurrentEntity.ch, offset, length);
        // return next character
        if (fCurrentEntity.position != fCurrentEntity.count) {
            c = fCurrentEntity.ch[fCurrentEntity.position];
            // REVISIT: Does this need to be updated to fix the
            //          #x0D ^#x0A newline normalization problem? -Ac
            if (c == '\r' && isExternal) {
                c = '\n';
            }
        } else {
            c = -1;
        }
        if (DEBUG_BUFFER) {
            System.out.print(")scanContent: ");
            print();
            System.out.println(" -> '"+(char)c+"'");
        }
        return c;

    } // scanContent(XMLString):int

    /**
     * Scans a range of attribute value data, setting the fields of the
     * XMLString structure, appropriately.
     * <p>
     * <strong>Note:</strong> The characters are consumed.
     * <p>
     * <strong>Note:</strong> This method does not guarantee to return
     * the longest run of attribute value data. This method may return
     * before the quote character due to reaching the end of the input
     * buffer or any other reason.
     * <p>
     * <strong>Note:</strong> The fields contained in the XMLString
     * structure are not guaranteed to remain valid upon subsequent calls
     * to the entity scanner. Therefore, the caller is responsible for
     * immediately using the returned character data or making a copy of
     * the character data.
     *
     * @param quote   The quote character that signifies the end of the
     *                attribute value data.
     * @param content The content structure to fill.
     *
     * @return Returns the next character on the input, if known. This
     *         value may be -1 but this does <em>note</em> designate
     *         end of file.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public int scanLiteral(int quote, XMLString content)
    throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(scanLiteral, '"+(char)quote+"': ");
            print();
            System.out.println();
        }
        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        } else if (fCurrentEntity.position == fCurrentEntity.count - 1) {
            invokeListeners(0);
            fCurrentEntity.ch[0] = fCurrentEntity.ch[fCurrentEntity.count - 1];

            load(1, false);
            fCurrentEntity.position = 0;
        }

        // normalize newlines
        int offset = fCurrentEntity.position;
        int c = fCurrentEntity.ch[offset];
        int newlines = 0;
        if(whiteSpaceInfoNeeded)
            whiteSpaceLen=0;
        if (c == '\n' || (c == '\r' && isExternal)) {
            if (DEBUG_BUFFER) {
                System.out.print("[newline, "+offset+", "+fCurrentEntity.position+": ");
                print();
                System.out.println();
            }
            do {
                c = fCurrentEntity.ch[fCurrentEntity.position++];
                if (c == '\r' && isExternal) {
                    newlines++;
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count) {
                        invokeListeners(newlines);
                        offset = 0;
                        fCurrentEntity.position = newlines;
                        if (load(newlines, false)) {
                            break;
                        }
                    }
                    if (fCurrentEntity.ch[fCurrentEntity.position] == '\n') {
                        fCurrentEntity.position++;
                        offset++;
                    }
                    /*** NEWLINE NORMALIZATION ***/
                    else {
                        newlines++;
                    }
                    /***/
                } else if (c == '\n') {
                    newlines++;
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count) {
                        offset = 0;
                        invokeListeners(newlines);
                        fCurrentEntity.position = newlines;
                        if (load(newlines, false)) {
                            break;
                        }
                    }
                    /*** NEWLINE NORMALIZATION ***
                     * if (fCurrentEntity.ch[fCurrentEntity.position] == '\r'
                     * && external) {
                     * fCurrentEntity.position++;
                     * offset++;
                     * }
                     * /***/
                } else {
                    fCurrentEntity.position--;
                    break;
                }
            } while (fCurrentEntity.position < fCurrentEntity.count - 1);
            int i=0;
            for ( i = offset; i < fCurrentEntity.position; i++) {
                fCurrentEntity.ch[i] = '\n';
                storeWhiteSpace(i);
            }

            int length = fCurrentEntity.position - offset;
            if (fCurrentEntity.position == fCurrentEntity.count - 1) {
                content.setValues(fCurrentEntity.ch, offset, length);
                if (DEBUG_BUFFER) {
                    System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                    print();
                    System.out.println();
                }
                return -1;
            }
            if (DEBUG_BUFFER) {
                System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                print();
                System.out.println();
            }
        }

        // scan literal value
        for (; fCurrentEntity.position<fCurrentEntity.count; fCurrentEntity.position++) {
            c = fCurrentEntity.ch[fCurrentEntity.position];
            if ((c == quote &&
                    (!fCurrentEntity.literal || isExternal)) ||
                    c == '%' || !XMLChar.isContent(c)) {
                break;
            }
            if (whiteSpaceInfoNeeded && c == '\t') {
                storeWhiteSpace(fCurrentEntity.position);
            }
        }

        int length = fCurrentEntity.position - offset;
        fCurrentEntity.columnNumber += length - newlines;
        content.setValues(fCurrentEntity.ch, offset, length);

        // return next character
        if (fCurrentEntity.position != fCurrentEntity.count) {
            c = fCurrentEntity.ch[fCurrentEntity.position];
            // NOTE: We don't want to accidentally signal the
            //       end of the literal if we're expanding an
            //       entity appearing in the literal. -Ac
            if (c == quote && fCurrentEntity.literal) {
                c = -1;
            }
        } else {
            c = -1;
        }
        if (DEBUG_BUFFER) {
            System.out.print(")scanLiteral, '"+(char)quote+"': ");
            print();
            System.out.println(" -> '"+(char)c+"'");
        }
        return c;

    } // scanLiteral(int,XMLString):int

    /**
     * Save whitespace information. Increase the whitespace buffer by 100
     * when needed.
     *
     * For XML 1.0, legal characters below 0x20 are 0x09 (TAB), 0x0A (LF) and 0x0D (CR).
     *
     * @param whiteSpacePos position of a whitespace in the scanner entity buffer
     */
    private void storeWhiteSpace(int whiteSpacePos) {
        if (whiteSpaceLen >= whiteSpaceLookup.length) {
            int [] tmp = new int[whiteSpaceLookup.length + 100];
            System.arraycopy(whiteSpaceLookup, 0, tmp, 0, whiteSpaceLookup.length);
            whiteSpaceLookup = tmp;
        }

        whiteSpaceLookup[whiteSpaceLen++] = whiteSpacePos;
    }

    //CHANGED:
    /**
     * Scans a range of character data up to the specified delimiter,
     * setting the fields of the XMLString structure, appropriately.
     * <p>
     * <strong>Note:</strong> The characters are consumed.
     * <p>
     * <strong>Note:</strong> This assumes that the length of the delimiter
     * and that the delimiter contains at least one character.
     * <p>
     * <strong>Note:</strong> This method does not guarantee to return
     * the longest run of character data. This method may return before
     * the delimiter due to reaching the end of the input buffer or any
     * other reason.
     * <p>
     * @param delimiter The string that signifies the end of the character
     *                  data to be scanned.
     * @param buffer    The XMLStringBuffer to fill.
     *
     * @return Returns true if there is more data to scan, false otherwise.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public boolean scanData(String delimiter, XMLStringBuffer buffer)
    throws IOException {

        boolean done = false;
        int delimLen = delimiter.length();
        char charAt0 = delimiter.charAt(0);
        do {
            if (DEBUG_BUFFER) {
                System.out.print("(scanData: ");
                print();
                System.out.println();
            }

            // load more characters, if needed

            if (fCurrentEntity.position == fCurrentEntity.count) {
                load(0, true);
            }

            boolean bNextEntity = false;

            while ((fCurrentEntity.position > fCurrentEntity.count - delimLen)
                && (!bNextEntity))
            {
              System.arraycopy(fCurrentEntity.ch,
                               fCurrentEntity.position,
                               fCurrentEntity.ch,
                               0,
                               fCurrentEntity.count - fCurrentEntity.position);

              bNextEntity = load(fCurrentEntity.count - fCurrentEntity.position, false);
              fCurrentEntity.position = 0;
              fCurrentEntity.startPosition = 0;
            }

            if (fCurrentEntity.position > fCurrentEntity.count - delimLen) {
                // something must be wrong with the input:  e.g., file ends in an unterminated comment
                int length = fCurrentEntity.count - fCurrentEntity.position;
                buffer.append (fCurrentEntity.ch, fCurrentEntity.position, length);
                fCurrentEntity.columnNumber += fCurrentEntity.count;
                fCurrentEntity.baseCharOffset += (fCurrentEntity.position - fCurrentEntity.startPosition);
                fCurrentEntity.position = fCurrentEntity.count;
                fCurrentEntity.startPosition = fCurrentEntity.count;
                load(0, true);
                return false;
            }

            // normalize newlines
            int offset = fCurrentEntity.position;
            int c = fCurrentEntity.ch[offset];
            int newlines = 0;
            if (c == '\n' || (c == '\r' && isExternal)) {
                if (DEBUG_BUFFER) {
                    System.out.print("[newline, "+offset+", "+fCurrentEntity.position+": ");
                    print();
                    System.out.println();
                }
                do {
                    c = fCurrentEntity.ch[fCurrentEntity.position++];
                    if (c == '\r' && isExternal) {
                        newlines++;
                        fCurrentEntity.lineNumber++;
                        fCurrentEntity.columnNumber = 1;
                        if (fCurrentEntity.position == fCurrentEntity.count) {
                            offset = 0;
                            invokeListeners(newlines);
                            fCurrentEntity.position = newlines;
                            if (load(newlines, false)) {
                                break;
                            }
                        }
                        if (fCurrentEntity.ch[fCurrentEntity.position] == '\n') {
                            fCurrentEntity.position++;
                            offset++;
                        }
                        /*** NEWLINE NORMALIZATION ***/
                        else {
                            newlines++;
                        }
                    } else if (c == '\n') {
                        newlines++;
                        fCurrentEntity.lineNumber++;
                        fCurrentEntity.columnNumber = 1;
                        if (fCurrentEntity.position == fCurrentEntity.count) {
                            offset = 0;
                            invokeListeners(newlines);
                            fCurrentEntity.position = newlines;
                            fCurrentEntity.count = newlines;
                            if (load(newlines, false)) {
                                break;
                            }
                        }
                    } else {
                        fCurrentEntity.position--;
                        break;
                    }
                } while (fCurrentEntity.position < fCurrentEntity.count - 1);
                for (int i = offset; i < fCurrentEntity.position; i++) {
                    fCurrentEntity.ch[i] = '\n';
                }
                int length = fCurrentEntity.position - offset;
                if (fCurrentEntity.position == fCurrentEntity.count - 1) {
                    buffer.append(fCurrentEntity.ch, offset, length);
                    if (DEBUG_BUFFER) {
                        System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                        print();
                        System.out.println();
                    }
                    return true;
                }
                if (DEBUG_BUFFER) {
                    System.out.print("]newline, "+offset+", "+fCurrentEntity.position+": ");
                    print();
                    System.out.println();
                }
            }

            // iterate over buffer looking for delimiter
            OUTER: while (fCurrentEntity.position < fCurrentEntity.count) {
                c = fCurrentEntity.ch[fCurrentEntity.position++];
                if (c == charAt0) {
                    // looks like we just hit the delimiter
                    int delimOffset = fCurrentEntity.position - 1;
                    for (int i = 1; i < delimLen; i++) {
                        if (fCurrentEntity.position == fCurrentEntity.count) {
                            fCurrentEntity.position -= i;
                            break OUTER;
                        }
                        c = fCurrentEntity.ch[fCurrentEntity.position++];
                        if (delimiter.charAt(i) != c) {
                            fCurrentEntity.position -= i;
                            break;
                        }
                    }
                    if (fCurrentEntity.position == delimOffset + delimLen) {
                        done = true;
                        break;
                    }
                } else if (c == '\n' || (isExternal && c == '\r')) {
                    fCurrentEntity.position--;
                    break;
                } else if (XMLChar.isInvalid(c)) {
                    fCurrentEntity.position--;
                    int length = fCurrentEntity.position - offset;
                    fCurrentEntity.columnNumber += length - newlines;
                    buffer.append(fCurrentEntity.ch, offset, length);
                    return true;
                }
            }
            int length = fCurrentEntity.position - offset;
            fCurrentEntity.columnNumber += length - newlines;
            if (done) {
                length -= delimLen;
            }
            buffer.append(fCurrentEntity.ch, offset, length);

            // return true if string was skipped
            if (DEBUG_BUFFER) {
                System.out.print(")scanData: ");
                print();
                System.out.println(" -> " + done);
            }
        } while (!done);
        return !done;

    } // scanData(String,XMLString)

    /**
     * Skips a character appearing immediately on the input.
     * <p>
     * <strong>Note:</strong> The character is consumed only if it matches
     * the specified character.
     *
     * @param c The character to skip.
     *
     * @return Returns true if the character was skipped.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public boolean skipChar(int c) throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(skipChar, '"+(char)c+"': ");
            print();
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        // skip character
        int cc = fCurrentEntity.ch[fCurrentEntity.position];
        if (cc == c) {
            fCurrentEntity.position++;
            if (c == '\n') {
                fCurrentEntity.lineNumber++;
                fCurrentEntity.columnNumber = 1;
            } else {
                fCurrentEntity.columnNumber++;
            }
            if (DEBUG_BUFFER) {
                System.out.print(")skipChar, '"+(char)c+"': ");
                print();
                System.out.println(" -> true");
            }
            return true;
        } else if (c == '\n' && cc == '\r' && isExternal) {
            // handle newlines
            if (fCurrentEntity.position == fCurrentEntity.count) {
                invokeListeners(1);
                fCurrentEntity.ch[0] = (char)cc;
                load(1, false);
            }
            fCurrentEntity.position++;
            if (fCurrentEntity.ch[fCurrentEntity.position] == '\n') {
                fCurrentEntity.position++;
            }
            fCurrentEntity.lineNumber++;
            fCurrentEntity.columnNumber = 1;
            if (DEBUG_BUFFER) {
                System.out.print(")skipChar, '"+(char)c+"': ");
                print();
                System.out.println(" -> true");
            }
            return true;
        }

        // character was not skipped
        if (DEBUG_BUFFER) {
            System.out.print(")skipChar, '"+(char)c+"': ");
            print();
            System.out.println(" -> false");
        }
        return false;

    } // skipChar(int):boolean

    public boolean isSpace(char ch){
        return (ch == ' ') || (ch == '\n') || (ch == '\t') || (ch == '\r');
    }
    /**
     * Skips space characters appearing immediately on the input.
     * <p>
     * <strong>Note:</strong> The characters are consumed only if they are
     * space characters.
     *
     * @return Returns true if at least one space character was skipped.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     *
     * @see XMLChar#isSpace
     */
    public boolean skipSpaces() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(skipSpaces: ");
            print();
            System.out.println();
        }
        //boolean entityChanged = false;
        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            invokeListeners(0);
            load(0, true);
        }

        //we are doing this check only in skipSpace() because it is called by
        //fMiscDispatcher and we want the parser to exit gracefully when document
        //is well-formed.
        //it is possible that end of document is reached and
        //fCurrentEntity becomes null
        //nothing was read so entity changed  'false' should be returned.
        if(fCurrentEntity == null){
            return false ;
        }

        // skip spaces
        int c = fCurrentEntity.ch[fCurrentEntity.position];
        if (XMLChar.isSpace(c)) {
            do {
                boolean entityChanged = false;
                // handle newlines
                if (c == '\n' || (isExternal && c == '\r')) {
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count - 1) {
                        invokeListeners(0);
                        fCurrentEntity.ch[0] = (char)c;
                        entityChanged = load(1, true);
                        if (!entityChanged){
                            // the load change the position to be 1,
                            // need to restore it when entity not changed
                            fCurrentEntity.position = 0;
                        }else if(fCurrentEntity == null){
                            return true ;
                        }
                    }
                    if (c == '\r' && isExternal) {
                        // REVISIT: Does this need to be updated to fix the
                        //          #x0D ^#x0A newline normalization problem? -Ac
                        if (fCurrentEntity.ch[++fCurrentEntity.position] != '\n') {
                            fCurrentEntity.position--;
                        }
                    }
                } else {
                    fCurrentEntity.columnNumber++;
                }
                // load more characters, if needed
                if (!entityChanged){
                    fCurrentEntity.position++;
                }

                if (fCurrentEntity.position == fCurrentEntity.count) {
                    invokeListeners(0);
                    load(0, true);

                    //we are doing this check only in skipSpace() because it is called by
                    //fMiscDispatcher and we want the parser to exit gracefully when document
                    //is well-formed.

                    //it is possible that end of document is reached and
                    //fCurrentEntity becomes null
                    //nothing was read so entity changed  'false' should be returned.
                    if(fCurrentEntity == null){
                        return true ;
                    }

                }
            } while (XMLChar.isSpace(c = fCurrentEntity.ch[fCurrentEntity.position]));
            if (DEBUG_BUFFER) {
                System.out.print(")skipSpaces: ");
                print();
                System.out.println(" -> true");
            }
            return true;
        }

        // no spaces were found
        if (DEBUG_BUFFER) {
            System.out.print(")skipSpaces: ");
            print();
            System.out.println(" -> false");
        }
        return false;

    } // skipSpaces():boolean


    /**
     * @param legnth This function checks that following number of characters are available.
     * to the underlying buffer.
     * @return This function returns true if capacity asked is available.
     */
    public boolean arrangeCapacity(int length) throws IOException{
        return arrangeCapacity(length, false);
    }

    /**
     * @param legnth This function checks that following number of characters are available.
     * to the underlying buffer.
     * @param if the underlying function should change the entity
     * @return This function returns true if capacity asked is available.
     *
     */
    public boolean arrangeCapacity(int length, boolean changeEntity) throws IOException{
        //check if the capacity is availble in the current buffer
        //count is no. of characters in the buffer   [x][m][l]
        //position is '0' based
        //System.out.println("fCurrent Entity " + fCurrentEntity);
        if((fCurrentEntity.count - fCurrentEntity.position) >= length) {
            return true;
        }
        if(DEBUG_SKIP_STRING){
            System.out.println("fCurrentEntity.count = " + fCurrentEntity.count);
            System.out.println("fCurrentEntity.position = " + fCurrentEntity.position);
            System.out.println("length = " + length);
        }
        boolean entityChanged = false;
        //load more characters -- this function shouldn't change the entity
        while((fCurrentEntity.count - fCurrentEntity.position) < length){
            if( (fCurrentEntity.ch.length - fCurrentEntity.position) < length){
                invokeListeners(0);
                System.arraycopy(fCurrentEntity.ch, fCurrentEntity.position, fCurrentEntity.ch,0,fCurrentEntity.count - fCurrentEntity.position);
                fCurrentEntity.count = fCurrentEntity.count - fCurrentEntity.position;
                fCurrentEntity.position = 0;
            }

            if((fCurrentEntity.count - fCurrentEntity.position) < length){
                int pos = fCurrentEntity.position;
                invokeListeners(pos);
                entityChanged = load(fCurrentEntity.count, changeEntity);
                fCurrentEntity.position = pos;
                if(entityChanged)break;
            }
            if(DEBUG_SKIP_STRING){
                System.out.println("fCurrentEntity.count = " + fCurrentEntity.count);
                System.out.println("fCurrentEntity.position = " + fCurrentEntity.position);
                System.out.println("length = " + length);
            }
        }
        //load changes the position.. set it back to the point where we started.

        //after loading check again.
        if((fCurrentEntity.count - fCurrentEntity.position) >= length) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Skips the specified string appearing immediately on the input.
     * <p>
     * <strong>Note:</strong> The characters are consumed only if all
     * the characters are skipped.
     *
     * @param s The string to skip.
     *
     * @return Returns true if the string was skipped.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     */
    public boolean skipString(String s) throws IOException {

        final int length = s.length();

        //first make sure that required capacity is avaible
        if(arrangeCapacity(length, false)){
            final int beforeSkip = fCurrentEntity.position ;
            int afterSkip = fCurrentEntity.position + length - 1 ;
            if(DEBUG_SKIP_STRING){
                System.out.println("skipString,length = " + s + "," + length);
                System.out.println("Buffer string to be skipped = " + new String(fCurrentEntity.ch, beforeSkip,  length));
            }

            //s.charAt() indexes are 0 to 'Length -1' based.
            int i = length - 1 ;
            //check from reverse
            while(s.charAt(i--) == fCurrentEntity.ch[afterSkip]){
                if(afterSkip-- == beforeSkip){
                    fCurrentEntity.position = fCurrentEntity.position + length ;
                    fCurrentEntity.columnNumber += length;
                    return true;
                }
            }
        }

        return false;
    } // skipString(String):boolean

    public boolean skipString(char [] s) throws IOException {

        final int length = s.length;
        //first make sure that required capacity is avaible
        if(arrangeCapacity(length, false)){
            int beforeSkip = fCurrentEntity.position ;
            int afterSkip = fCurrentEntity.position + length  ;

            if(DEBUG_SKIP_STRING){
                System.out.println("skipString,length = " + new String(s) + "," + length);
                System.out.println("skipString,length = " + new String(s) + "," + length);
            }

            for(int i=0;i<length;i++){
                if(!(fCurrentEntity.ch[beforeSkip++]==s[i])){
                   return false;
                }
            }
            fCurrentEntity.position = fCurrentEntity.position + length ;
            fCurrentEntity.columnNumber += length;
            return true;

        }

        return false;
    }

    //
    // Locator methods
    //
    //
    // Private methods
    //

    /**
     * Loads a chunk of text.
     *
     * @param offset       The offset into the character buffer to
     *                     read the next batch of characters.
     * @param changeEntity True if the load should change entities
     *                     at the end of the entity, otherwise leave
     *                     the current entity in place and the entity
     *                     boundary will be signaled by the return
     *                     value.
     *
     * @returns Returns true if the entity changed as a result of this
     *          load operation.
     */
    final boolean load(int offset, boolean changeEntity)
    throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(load, "+offset+": ");
            print();
            System.out.println();
        }
        //maintaing the count till last load
        fCurrentEntity.fTotalCountTillLastLoad = fCurrentEntity.fTotalCountTillLastLoad + fCurrentEntity.fLastCount ;
        // read characters
        int length = fCurrentEntity.ch.length - offset;
        if (!fCurrentEntity.mayReadChunks && length > XMLEntityManager.DEFAULT_XMLDECL_BUFFER_SIZE) {
            length = XMLEntityManager.DEFAULT_XMLDECL_BUFFER_SIZE;
        }
        if (DEBUG_BUFFER) System.out.println("  length to try to read: "+length);
        int count = fCurrentEntity.reader.read(fCurrentEntity.ch, offset, length);
        if (DEBUG_BUFFER) System.out.println("  length actually read:  "+count);

        // reset count and position
        boolean entityChanged = false;
        if (count != -1) {
            if (count != 0) {
                // record the last count
                fCurrentEntity.fLastCount = count;
                fCurrentEntity.count = count + offset;
                fCurrentEntity.position = offset;
            }
        }
        // end of this entity
        else {
            fCurrentEntity.count = offset;
            fCurrentEntity.position = offset;
            entityChanged = true;

            if (changeEntity) {
                //notify the entity manager about the end of entity
                fEntityManager.endEntity();
                //return if the current entity becomes null
                if(fCurrentEntity == null){
                    throw END_OF_DOCUMENT_ENTITY;
                }
                // handle the trailing edges
                if (fCurrentEntity.position == fCurrentEntity.count) {
                    load(0, true);
                }
            }

        }
        if (DEBUG_BUFFER) {
            System.out.print(")load, "+offset+": ");
            print();
            System.out.println();
        }

        return entityChanged;

    } // load(int, boolean):boolean

    /**
     * Creates a reader capable of reading the given input stream in
     * the specified encoding.
     *
     * @param inputStream  The input stream.
     * @param encoding     The encoding name that the input stream is
     *                     encoded using. If the user has specified that
     *                     Java encoding names are allowed, then the
     *                     encoding name may be a Java encoding name;
     *                     otherwise, it is an ianaEncoding name.
     * @param isBigEndian   For encodings (like uCS-4), whose names cannot
     *                      specify a byte order, this tells whether the order is bigEndian.  null menas
     *                      unknown or not relevant.
     *
     * @return Returns a reader.
     */
    protected Reader createReader(InputStream inputStream, String encoding, Boolean isBigEndian)
    throws IOException {

        // normalize encoding name
        if (encoding == null) {
            encoding = "UTF-8";
        }

        // try to use an optimized reader
        String ENCODING = encoding.toUpperCase(Locale.ENGLISH);
        if (ENCODING.equals("UTF-8")) {
            if (DEBUG_ENCODINGS) {
                System.out.println("$$$ creating UTF8Reader");
            }
            return new UTF8Reader(inputStream, fCurrentEntity.fBufferSize, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale() );
        }
        if (ENCODING.equals("US-ASCII")) {
            if (DEBUG_ENCODINGS) {
                System.out.println("$$$ creating ASCIIReader");
            }
            return new ASCIIReader(inputStream, fCurrentEntity.fBufferSize, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
        }
        if(ENCODING.equals("ISO-10646-UCS-4")) {
            if(isBigEndian != null) {
                boolean isBE = isBigEndian.booleanValue();
                if(isBE) {
                    return new UCSReader(inputStream, UCSReader.UCS4BE);
                } else {
                    return new UCSReader(inputStream, UCSReader.UCS4LE);
                }
            } else {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                        "EncodingByteOrderUnsupported",
                        new Object[] { encoding },
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
            }
        }
        if(ENCODING.equals("ISO-10646-UCS-2")) {
            if(isBigEndian != null) { // sould never happen with this encoding...
                boolean isBE = isBigEndian.booleanValue();
                if(isBE) {
                    return new UCSReader(inputStream, UCSReader.UCS2BE);
                } else {
                    return new UCSReader(inputStream, UCSReader.UCS2LE);
                }
            } else {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                        "EncodingByteOrderUnsupported",
                        new Object[] { encoding },
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
            }
        }

        // check for valid name
        boolean validIANA = XMLChar.isValidIANAEncoding(encoding);
        boolean validJava = XMLChar.isValidJavaEncoding(encoding);
        if (!validIANA || (fAllowJavaEncodings && !validJava)) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                    "EncodingDeclInvalid",
                    new Object[] { encoding },
                    XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    // NOTE: AndyH suggested that, on failure, we use ISO Latin 1
                    //       because every byte is a valid ISO Latin 1 character.
                    //       It may not translate correctly but if we failed on
                    //       the encoding anyway, then we're expecting the content
                    //       of the document to be bad. This will just prevent an
                    //       invalid UTF-8 sequence to be detected. This is only
                    //       important when continue-after-fatal-error is turned
                    //       on. -Ac
                    encoding = "ISO-8859-1";
        }

        // try to use a Java reader
        String javaEncoding = EncodingMap.getIANA2JavaMapping(ENCODING);
        if (javaEncoding == null) {
            if(fAllowJavaEncodings) {
                javaEncoding = encoding;
            } else {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN,
                        "EncodingDeclInvalid",
                        new Object[] { encoding },
                        XMLErrorReporter.SEVERITY_FATAL_ERROR);
                        // see comment above.
                        javaEncoding = "ISO8859_1";
            }
        }
        else if (javaEncoding.equals("ASCII")) {
            if (DEBUG_ENCODINGS) {
                System.out.println("$$$ creating ASCIIReader");
            }
            return new ASCIIReader(inputStream, fBufferSize, fErrorReporter.getMessageFormatter(XMLMessageFormatter.XML_DOMAIN), fErrorReporter.getLocale());
        }

        if (DEBUG_ENCODINGS) {
            System.out.print("$$$ creating Java InputStreamReader: encoding="+javaEncoding);
            if (javaEncoding == encoding) {
                System.out.print(" (IANA encoding)");
            }
            System.out.println();
        }
        return new InputStreamReader(inputStream, javaEncoding);

    } // createReader(InputStream,String, Boolean): Reader

    /**
     * Returns the IANA encoding name that is auto-detected from
     * the bytes specified, with the endian-ness of that encoding where appropriate.
     *
     * @param b4    The first four bytes of the input.
     * @param count The number of bytes actually read.
     * @return a 2-element array:  the first element, an IANA-encoding string,
     *  the second element a Boolean which is true iff the document is big endian, false
     *  if it's little-endian, and null if the distinction isn't relevant.
     */
    protected Object[] getEncodingName(byte[] b4, int count) {

        if (count < 2) {
            return new Object[]{"UTF-8", null};
        }

        // UTF-16, with BOM
        int b0 = b4[0] & 0xFF;
        int b1 = b4[1] & 0xFF;
        if (b0 == 0xFE && b1 == 0xFF) {
            // UTF-16, big-endian
            return new Object [] {"UTF-16BE", new Boolean(true)};
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            // UTF-16, little-endian
            return new Object [] {"UTF-16LE", new Boolean(false)};
        }

        // default to UTF-8 if we don't have enough bytes to make a
        // good determination of the encoding
        if (count < 3) {
            return new Object [] {"UTF-8", null};
        }

        // UTF-8 with a BOM
        int b2 = b4[2] & 0xFF;
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return new Object [] {"UTF-8", null};
        }

        // default to UTF-8 if we don't have enough bytes to make a
        // good determination of the encoding
        if (count < 4) {
            return new Object [] {"UTF-8", null};
        }

        // other encodings
        int b3 = b4[3] & 0xFF;
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {
            // UCS-4, big endian (1234)
            return new Object [] {"ISO-10646-UCS-4", new Boolean(true)};
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, little endian (4321)
            return new Object [] {"ISO-10646-UCS-4", new Boolean(false)};
        }
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {
            // UCS-4, unusual octet order (2143)
            // REVISIT: What should this be?
            return new Object [] {"ISO-10646-UCS-4", null};
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, unusual octect order (3412)
            // REVISIT: What should this be?
            return new Object [] {"ISO-10646-UCS-4", null};
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
            // UTF-16, big-endian, no BOM
            // (or could turn out to be UCS-2...
            // REVISIT: What should this be?
            return new Object [] {"UTF-16BE", new Boolean(true)};
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
            // UTF-16, little-endian, no BOM
            // (or could turn out to be UCS-2...
            return new Object [] {"UTF-16LE", new Boolean(false)};
        }
        if (b0 == 0x4C && b1 == 0x6F && b2 == 0xA7 && b3 == 0x94) {
            // EBCDIC
            // a la xerces1, return CP037 instead of EBCDIC here
            return new Object [] {"CP037", null};
        }

        // default encoding
        return new Object [] {"UTF-8", null};

    } // getEncodingName(byte[],int):Object[]

    /**
     * xxx not removing endEntity() so that i remember that we need to implement it.
     * Ends an entity.
     *
     * @throws XNIException Thrown by entity handler to signal an error.
     */
    //
    /** Prints the contents of the buffer. */
    final void print() {
        if (DEBUG_BUFFER) {
            if (fCurrentEntity != null) {
                System.out.print('[');
                System.out.print(fCurrentEntity.count);
                System.out.print(' ');
                System.out.print(fCurrentEntity.position);
                if (fCurrentEntity.count > 0) {
                    System.out.print(" \"");
                    for (int i = 0; i < fCurrentEntity.count; i++) {
                        if (i == fCurrentEntity.position) {
                            System.out.print('^');
                        }
                        char c = fCurrentEntity.ch[i];
                        switch (c) {
                            case '\n': {
                                System.out.print("\\n");
                                break;
                            }
                            case '\r': {
                                System.out.print("\\r");
                                break;
                            }
                            case '\t': {
                                System.out.print("\\t");
                                break;
                            }
                            case '\\': {
                                System.out.print("\\\\");
                                break;
                            }
                            default: {
                                System.out.print(c);
                            }
                        }
                    }
                    if (fCurrentEntity.position == fCurrentEntity.count) {
                        System.out.print('^');
                    }
                    System.out.print('"');
                }
                System.out.print(']');
                System.out.print(" @ ");
                System.out.print(fCurrentEntity.lineNumber);
                System.out.print(',');
                System.out.print(fCurrentEntity.columnNumber);
            } else {
                System.out.print("*NO CURRENT ENTITY*");
            }
        }
    }

    /**
     * Registers the listener object and provides callback.
     * @param listener listener to which call back should be provided when scanner buffer
     * is being changed.
     */
    public void registerListener(XMLBufferListener listener) {
        if(!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
     *
     * @param loadPos Starting position from which new data is being loaded into scanner buffer.
     */
    private void invokeListeners(int loadPos){
        for(int i=0;i<listeners.size();i++){
            XMLBufferListener listener =(XMLBufferListener) listeners.get(i);
            listener.refresh(loadPos);
        }
    }

    /**
     * Skips space characters appearing immediately on the input that would
     * match non-terminal S (0x09, 0x0A, 0x0D, 0x20) before end of line
     * normalization is performed. This is useful when scanning structures
     * such as the XMLDecl and TextDecl that can only contain US-ASCII
     * characters.
     * <p>
     * <strong>Note:</strong> The characters are consumed only if they would
     * match non-terminal S before end of line normalization is performed.
     *
     * @return Returns true if at least one space character was skipped.
     *
     * @throws IOException  Thrown if i/o error occurs.
     * @throws EOFException Thrown on end of file.
     *
     * @see XMLChar#isSpace
     */
    public final boolean skipDeclSpaces() throws IOException {
        if (DEBUG_BUFFER) {
            System.out.print("(skipDeclSpaces: ");
            //XMLEntityManager.print(fCurrentEntity);
            System.out.println();
        }

        // load more characters, if needed
        if (fCurrentEntity.position == fCurrentEntity.count) {
            load(0, true);
        }

        // skip spaces
        int c = fCurrentEntity.ch[fCurrentEntity.position];
        if (XMLChar.isSpace(c)) {
            boolean external = fCurrentEntity.isExternal();
            do {
                boolean entityChanged = false;
                // handle newlines
                if (c == '\n' || (external && c == '\r')) {
                    fCurrentEntity.lineNumber++;
                    fCurrentEntity.columnNumber = 1;
                    if (fCurrentEntity.position == fCurrentEntity.count - 1) {
                        fCurrentEntity.ch[0] = (char)c;
                        entityChanged = load(1, true);
                        if (!entityChanged)
                            // the load change the position to be 1,
                            // need to restore it when entity not changed
                            fCurrentEntity.position = 0;
                    }
                    if (c == '\r' && external) {
                        // REVISIT: Does this need to be updated to fix the
                        //          #x0D ^#x0A newline normalization problem? -Ac
                        if (fCurrentEntity.ch[++fCurrentEntity.position] != '\n') {
                            fCurrentEntity.position--;
                        }
                    }
                    /*** NEWLINE NORMALIZATION ***
                     * else {
                     * if (fCurrentEntity.ch[fCurrentEntity.position + 1] == '\r'
                     * && external) {
                     * fCurrentEntity.position++;
                     * }
                     * }
                     * /***/
                } else {
                    fCurrentEntity.columnNumber++;
                }
                // load more characters, if needed
                if (!entityChanged)
                    fCurrentEntity.position++;
                if (fCurrentEntity.position == fCurrentEntity.count) {
                    load(0, true);
                }
            } while (XMLChar.isSpace(c = fCurrentEntity.ch[fCurrentEntity.position]));
            if (DEBUG_BUFFER) {
                System.out.print(")skipDeclSpaces: ");
                //  XMLEntityManager.print(fCurrentEntity);
                System.out.println(" -> true");
            }
            return true;
        }

        // no spaces were found
        if (DEBUG_BUFFER) {
            System.out.print(")skipDeclSpaces: ");
            //XMLEntityManager.print(fCurrentEntity);
            System.out.println(" -> false");
        }
        return false;

    } // skipDeclSpaces():boolean


} // class XMLEntityScanner
