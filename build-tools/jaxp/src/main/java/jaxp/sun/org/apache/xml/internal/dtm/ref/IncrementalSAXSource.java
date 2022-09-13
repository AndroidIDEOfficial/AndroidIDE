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
 * $Id: IncrementalSAXSource.java,v 1.2.4.1 2005/09/15 08:15:06 suresh_emailid Exp $
 */

package jaxp.sun.org.apache.xml.internal.dtm.ref;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** <p>IncrementalSAXSource is an API that delivers a small number of
 * SAX events each time a request is made from a "controller"
 * coroutine.  See IncrementalSAXFilter and IncrementalSAXFilter_Xerces
 * for examples.
 *
 * Note that interaction is via the deliverMoreNodes
 * method, and therefore coroutine support is not exposed
 * here.</p>
 * */
public interface IncrementalSAXSource
{
  // ------------------------------------------------------------------
  // SAX Output API
  // ------------------------------------------------------------------

  /** Register a SAX-style content handler for us to output to
   */
  public void setContentHandler(ContentHandler handler);

  /**  Register a SAX-style lexical handler for us to output to
   */
  public void setLexicalHandler(org.xml.sax.ext.LexicalHandler handler);

  /**  Register a SAX-style DTD handler for us to output to
   */
  public void setDTDHandler(org.xml.sax.DTDHandler handler);

  // ------------------------------------------------------------------
  // Command Input API
  // ------------------------------------------------------------------

  /** deliverMoreNodes() is a simple API which tells the thread in which the
   * IncrementalSAXSource is running to deliver more events (true),
   * or stop delivering events and close out its input (false).
   *
   * This is intended to be called from one of our partner coroutines,
   * and serves to encapsulate the coroutine communication protocol.
   *
   * @param parsemore If true, tells the incremental SAX stream to deliver
   * another chunk of events. If false, finishes out the stream.
   *
   * @return Boolean.TRUE if the IncrementalSAXSource believes more data
   * may be available for further parsing. Boolean.FALSE if parsing
   * ran to completion, or was ended by deliverMoreNodes(false).
   * */
  public Object deliverMoreNodes (boolean parsemore);

  // ------------------------------------------------------------------
  // Parse Thread Convenience API
  // ------------------------------------------------------------------

  /** Launch an XMLReader's parsing operation, feeding events to this
   * IncrementalSAXSource. In some implementations, this may launch a
   * thread which runs the previously supplied XMLReader's parse() operation.
   * In others, it may do other forms of initialization.
   *
   * @throws SAXException is parse thread is already in progress
   * or parsing can not be started.
   * */
  public void startParse(InputSource source) throws SAXException;

} // class IncrementalSAXSource
