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

package jaxp.xml.stream;

import jaxp.xml.stream.events.Attribute;
import jaxp.xml.stream.events.Characters;
import jaxp.xml.stream.events.Comment;
import jaxp.xml.stream.events.EndDocument;
import jaxp.xml.stream.events.EndElement;
import jaxp.xml.stream.events.EntityReference;
import jaxp.xml.stream.events.Namespace;
import jaxp.xml.stream.events.NotationDeclaration;
import jaxp.xml.stream.events.ProcessingInstruction;
import jaxp.xml.stream.events.StartDocument;
import jaxp.xml.stream.events.StartElement;

/**
 * This interface declares the constants used in this API.
 * Numbers in the range 0 to 256 are reserved for the specification,
 * user defined events must use event codes outside that range.
 *
 * @since 1.6
 */

public interface XMLStreamConstants {
  /**
   * Indicates an event is a start element
   * @see StartElement
   */
  public static final int START_ELEMENT=1;
  /**
   * Indicates an event is an end element
   * @see EndElement
   */
  public static final int END_ELEMENT=2;
  /**
   * Indicates an event is a processing instruction
   * @see ProcessingInstruction
   */
  public static final int PROCESSING_INSTRUCTION=3;

  /**
   * Indicates an event is characters
   * @see Characters
   */
  public static final int CHARACTERS=4;

  /**
   * Indicates an event is a comment
   * @see Comment
   */
  public static final int COMMENT=5;

  /**
   * The characters are white space
   * (see [XML], 2.10 "White Space Handling").
   * Events are only reported as SPACE if they are ignorable white
   * space.  Otherwise they are reported as CHARACTERS.
   * @see Characters
   */
  public static final int SPACE=6;

  /**
   * Indicates an event is a start document
   * @see StartDocument
   */
  public static final int START_DOCUMENT=7;

  /**
   * Indicates an event is an end document
   * @see EndDocument
   */
  public static final int END_DOCUMENT=8;

  /**
   * Indicates an event is an entity reference
   * @see EntityReference
   */
  public static final int ENTITY_REFERENCE=9;

  /**
   * Indicates an event is an attribute
   * @see Attribute
   */
  public static final int ATTRIBUTE=10;

  /**
   * Indicates an event is a DTD
   * @see jaxp.xml.stream.events.DTD
   */
  public static final int DTD=11;

  /**
   * Indicates an event is a CDATA section
   * @see Characters
   */
  public static final int CDATA=12;

  /**
   * Indicates the event is a namespace declaration
   *
   * @see Namespace
   */
  public static final int NAMESPACE=13;

  /**
   * Indicates a Notation
   * @see NotationDeclaration
   */
  public static final int NOTATION_DECLARATION=14;

  /**
   * Indicates a Entity Declaration
   * @see NotationDeclaration
   */
  public static final int ENTITY_DECLARATION=15;
}
