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
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 */

package jaxp.xml.stream.events;

import jaxp.xml.stream.Location;
import jaxp.xml.stream.XMLEventReader;
import jaxp.xml.stream.XMLStreamConstants;
import jaxp.xml.stream.XMLStreamException;

import java.io.Writer;
import jaxp.xml.namespace.QName;

/**
 * This is the base event interface for handling markup events.
 * Events are value objects that are used to communicate the
 * XML 1.0 InfoSet to the Application.  Events may be cached
 * and referenced after the parse has completed.
 *
 * @version 1.0
 * @author Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 * @see XMLEventReader
 * @see Characters
 * @see ProcessingInstruction
 * @see StartElement
 * @see EndElement
 * @see StartDocument
 * @see EndDocument
 * @see EntityReference
 * @see EntityDeclaration
 * @see NotationDeclaration
 * @since 1.6
 */
public interface XMLEvent extends XMLStreamConstants {

  /**
   * Returns an integer code for this event.
   * @see #START_ELEMENT
   * @see #END_ELEMENT
   * @see #CHARACTERS
   * @see #ATTRIBUTE
   * @see #NAMESPACE
   * @see #PROCESSING_INSTRUCTION
   * @see #COMMENT
   * @see #START_DOCUMENT
   * @see #END_DOCUMENT
   * @see #DTD
   */
  public int getEventType();

  /**
   * Return the location of this event.  The Location
   * returned from this method is non-volatile and
   * will retain its information.
   * @see Location
   */
  Location getLocation();

  /**
   * A utility function to check if this event is a StartElement.
   * @see StartElement
   */
  public boolean isStartElement();

  /**
   * A utility function to check if this event is an Attribute.
   * @see Attribute
   */
  public boolean isAttribute();

  /**
   * A utility function to check if this event is a Namespace.
   * @see Namespace
   */
  public boolean isNamespace();


  /**
   * A utility function to check if this event is a EndElement.
   * @see EndElement
   */
  public boolean isEndElement();

  /**
   * A utility function to check if this event is an EntityReference.
   * @see EntityReference
   */
  public boolean isEntityReference();

  /**
   * A utility function to check if this event is a ProcessingInstruction.
   * @see ProcessingInstruction
   */
  public boolean isProcessingInstruction();

  /**
   * A utility function to check if this event is Characters.
   * @see Characters
   */
  public boolean isCharacters();

  /**
   * A utility function to check if this event is a StartDocument.
   * @see StartDocument
   */
  public boolean isStartDocument();

  /**
   * A utility function to check if this event is an EndDocument.
   * @see EndDocument
   */
  public boolean isEndDocument();

  /**
   * Returns this event as a start element event, may result in
   * a class cast exception if this event is not a start element.
   */
  public StartElement asStartElement();

  /**
   * Returns this event as an end  element event, may result in
   * a class cast exception if this event is not a end element.
   */
  public EndElement asEndElement();

  /**
   * Returns this event as Characters, may result in
   * a class cast exception if this event is not Characters.
   */
  public Characters asCharacters();

  /**
   * This method is provided for implementations to provide
   * optional type information about the associated event.
   * It is optional and will return null if no information
   * is available.
   */
  public QName getSchemaType();

  /**
   * This method will write the XMLEvent as per the XML 1.0 specification as Unicode characters.
   * No indentation or whitespace should be outputted.
   *
   * Any user defined event type SHALL have this method
   * called when being written to on an output stream.
   * Built in Event types MUST implement this method,
   * but implementations MAY choose not call these methods
   * for optimizations reasons when writing out built in
   * Events to an output stream.
   * The output generated MUST be equivalent in terms of the
   * infoset expressed.
   *
   * @param writer The writer that will output the data
   * @throws XMLStreamException if there is a fatal error writing the event
   */
  public void writeAsEncodedUnicode(Writer writer)
    throws XMLStreamException;

}
