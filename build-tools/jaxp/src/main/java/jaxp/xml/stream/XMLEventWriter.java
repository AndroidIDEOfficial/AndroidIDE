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

package jaxp.xml.stream;

import jaxp.xml.stream.events.Characters;
import jaxp.xml.stream.events.EndElement;
import jaxp.xml.stream.events.ProcessingInstruction;
import jaxp.xml.stream.events.StartElement;
import jaxp.xml.stream.events.XMLEvent;
import jaxp.xml.stream.util.XMLEventConsumer;

import jaxp.xml.namespace.NamespaceContext;

/**
 *
 * This is the top level interface for writing XML documents.
 *
 * Instances of this interface are not required to validate the
 * form of the XML.
 *
 * @version 1.0
 * @author Copyright (c) 2009 by Oracle Corporation. All Rights Reserved.
 * @see XMLEventReader
 * @see XMLEvent
 * @see Characters
 * @see ProcessingInstruction
 * @see StartElement
 * @see EndElement
 * @since 1.6
 */
public interface XMLEventWriter extends XMLEventConsumer {

  /**
   * Writes any cached events to the underlying output mechanism
   * @throws XMLStreamException
   */
  public void flush() throws XMLStreamException;

  /**
   * Frees any resources associated with this stream
   * @throws XMLStreamException
   */
  public void close() throws XMLStreamException;

  /**
   * Add an event to the output stream
   * Adding a START_ELEMENT will open a new namespace scope that
   * will be closed when the corresponding END_ELEMENT is written.
   * <table border="2" rules="all" cellpadding="4">
   *   <thead>
   *     <tr>
   *       <th align="center" colspan="2">
   *         Required and optional fields for events added to the writer
   *       </th>
   *     </tr>
   *   </thead>
   *   <tbody>
   *     <tr>
   *       <th>Event Type</th>
   *       <th>Required Fields</th>
   *       <th>Optional Fields</th>
   *       <th>Required Behavior</th>
   *     </tr>
   *     <tr>
   *       <td> START_ELEMENT  </td>
   *       <td> QName name </td>
   *       <td> namespaces , attributes </td>
   *       <td> A START_ELEMENT will be written by writing the name,
   *       namespaces, and attributes of the event in XML 1.0 valid
   *       syntax for START_ELEMENTs.
   *       The name is written by looking up the prefix for
   *       the namespace uri.  The writer can be configured to
   *       respect prefixes of QNames.  If the writer is respecting
   *       prefixes it must use the prefix set on the QName.  The
   *       default behavior is to lookup the value for the prefix
   *       on the EventWriter's internal namespace context.
   *       Each attribute (if any)
   *       is written using the behavior specified in the attribute
   *       section of this table.  Each namespace (if any) is written
   *       using the behavior specified in the namespace section of this
   *       table.
   *       </td>
   *     </tr>
   *     <tr>
   *       <td> END_ELEMENT  </td>
   *       <td> Qname name  </td>
   *       <td> None </td>
   *       <td> A well formed END_ELEMENT tag is written.
   *       The name is written by looking up the prefix for
   *       the namespace uri.  The writer can be configured to
   *       respect prefixes of QNames.  If the writer is respecting
   *       prefixes it must use the prefix set on the QName.  The
   *       default behavior is to lookup the value for the prefix
   *       on the EventWriter's internal namespace context.
   *       If the END_ELEMENT name does not match the START_ELEMENT
   *       name an XMLStreamException is thrown.
   *       </td>
   *     </tr>
   *     <tr>
   *       <td> ATTRIBUTE  </td>
   *       <td> QName name , String value </td>
   *       <td> QName type </td>
   *       <td> An attribute is written using the same algorithm
   *            to find the lexical form as used in START_ELEMENT.
   *            The default is to use double quotes to wrap attribute
   *            values and to escape any double quotes found in the
   *            value.  The type value is ignored.
   *       </td>
   *     </tr>
   *     <tr>
   *       <td> NAMESPACE  </td>
   *       <td> String prefix, String namespaceURI,
   *            boolean isDefaultNamespaceDeclaration
   *      </td>
   *       <td> None  </td>
   *       <td> A namespace declaration is written.  If the
   *            namespace is a default namespace declaration
   *            (isDefault is true) then xmlns="$namespaceURI"
   *            is written and the prefix is optional.  If
   *            isDefault is false, the prefix must be declared
   *            and the writer must prepend xmlns to the prefix
   *            and write out a standard prefix declaration.
   *      </td>
   *     </tr>
   *     <tr>
   *       <td> PROCESSING_INSTRUCTION  </td>
   *       <td>   None</td>
   *       <td>   String target, String data</td>
   *       <td>   The data does not need to be present and may be
   *              null.  Target is required and many not be null.
   *              The writer
   *              will write data section
   *              directly after the target,
   *              enclosed in appropriate XML 1.0 syntax
   *      </td>
   *     </tr>
   *     <tr>
   *       <td> COMMENT  </td>
   *       <td> None  </td>
   *       <td> String comment  </td>
   *       <td> If the comment is present (not null) it is written, otherwise an
   *            an empty comment is written
   *      </td>
   *     </tr>
   *     <tr>
   *       <td> START_DOCUMENT  </td>
   *       <td> None  </td>
   *       <td> String encoding , boolean standalone, String version  </td>
   *       <td> A START_DOCUMENT event is not required to be written to the
   *             stream.  If present the attributes are written inside
   *             the appropriate XML declaration syntax
   *      </td>
   *     </tr>
   *     <tr>
   *       <td> END_DOCUMENT  </td>
   *       <td> None </td>
   *       <td> None  </td>
   *       <td> Nothing is written to the output  </td>
   *     </tr>
   *     <tr>
   *       <td> DTD  </td>
   *       <td> String DocumentTypeDefinition  </td>
   *       <td> None  </td>
   *       <td> The DocumentTypeDefinition is written to the output  </td>
   *     </tr>
   *   </tbody>
   * </table>
   * @param event the event to be added
   * @throws XMLStreamException
   */
  public void add(XMLEvent event) throws XMLStreamException;

  /**
   * Adds an entire stream to an output stream,
   * calls next() on the inputStream argument until hasNext() returns false
   * This should be treated as a convenience method that will
   * perform the following loop over all the events in an
   * event reader and call add on each event.
   *
   * @param reader the event stream to add to the output
   * @throws XMLStreamException
   */

  public void add(XMLEventReader reader) throws XMLStreamException;

  /**
   * Gets the prefix the uri is bound to
   * @param uri the uri to look up
   * @throws XMLStreamException
   */
  public String getPrefix(String uri) throws XMLStreamException;

  /**
   * Sets the prefix the uri is bound to.  This prefix is bound
   * in the scope of the current START_ELEMENT / END_ELEMENT pair.
   * If this method is called before a START_ELEMENT has been written
   * the prefix is bound in the root scope.
   * @param prefix the prefix to bind to the uri
   * @param uri the uri to bind to the prefix
   * @throws XMLStreamException
   */
  public void setPrefix(String prefix, String uri) throws XMLStreamException;

  /**
   * Binds a URI to the default namespace
   * This URI is bound
   * in the scope of the current START_ELEMENT / END_ELEMENT pair.
   * If this method is called before a START_ELEMENT has been written
   * the uri is bound in the root scope.
   * @param uri the uri to bind to the default namespace
   * @throws XMLStreamException
   */
  public void setDefaultNamespace(String uri) throws XMLStreamException;

  /**
   * Sets the current namespace context for prefix and uri bindings.
   * This context becomes the root namespace context for writing and
   * will replace the current root namespace context.  Subsequent calls
   * to setPrefix and setDefaultNamespace will bind namespaces using
   * the context passed to the method as the root context for resolving
   * namespaces.
   * @param context the namespace context to use for this writer
   * @throws XMLStreamException
   */
  public void setNamespaceContext(NamespaceContext context)
    throws XMLStreamException;

  /**
   * Returns the current namespace context.
   * @return the current namespace context
   */
  public NamespaceContext getNamespaceContext();


}
