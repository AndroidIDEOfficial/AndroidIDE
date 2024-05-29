/**
 * Copyright (c) 2018 Angelo ZERR. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.dom;

import static org.eclipse.lemminx.dom.DOMAttr.XMLNS_ATTR;
import static org.eclipse.lemminx.dom.DOMAttr.XMLNS_NO_DEFAULT_ATTR;

import org.eclipse.lemminx.utils.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** An Element node. */
public class DOMElement extends DOMNode implements org.w3c.dom.Element {

  String tag;
  boolean selfClosed;

  // DomElement.start == startTagOpenOffset
  int startTagOpenOffset = NULL_VALUE; // |<root>
  int startTagCloseOffset = NULL_VALUE; // <root |>

  int endTagOpenOffset = NULL_VALUE; // <root> |</root >
  int endTagCloseOffset = NULL_VALUE; // <root> </root |>
  // DomElement.end = <root> </root>| , is always scanner.getTokenEnd()

  public DOMElement(int start, int end) {
    super(start, end);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Node#getNodeType()
   */
  @Override
  public short getNodeType() {
    return DOMNode.ELEMENT_NODE;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Node#getNodeName()
   */
  @Override
  public String getNodeName() {
    return getTagName();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Element#getTagName()
   */
  @Override
  public String getTagName() {
    return tag;
  }

  /**
   * Returns true if the DOM element have a tag name and false otherwise (ex : '<' or '</').
   *
   * @return true if the DOM element have a tag name and false otherwise (ex : '<' or '</').
   */
  public boolean hasTagName() {
    return tag != null;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Node#getLocalName()
   */
  @Override
  public String getLocalName() {
    String name = getTagName();
    if (name == null) {
      return null;
    }
    int index = name.indexOf(":"); // $NON-NLS-1$
    if (index != -1) {
      name = name.substring(index + 1);
    }
    return name;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Node#getPrefix()
   */
  @Override
  public String getPrefix() {
    String name = getTagName();
    if (name == null) {
      return null;
    }
    String prefix = null;
    int index = name.indexOf(":"); // $NON-NLS-1$
    if (index != -1) {
      prefix = name.substring(0, index);
    }
    return prefix;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.w3c.dom.Node#getNamespaceURI()
   */
  @Override
  public String getNamespaceURI() {
    String prefix = getPrefix();
    // Try to get xmlns attribute from the element
    return getNamespaceURI(prefix);
  }

  /**
   * Returns the namespace URI for the given prefix and null otherwise.
   *
   * @param prefix the prefix.
   * @return the namespace URI for the given prefix and null otherwise.
   */
  public String getNamespaceURI(String prefix) {
    String namespaceURI = getNamespaceURI(prefix, this);
    if (namespaceURI != null) {
      return namespaceURI;
    }
    // try to get the namespace from the parent element
    DOMNode parent = getParentNode();
    while (parent != null) {
      if (parent.getNodeType() == DOMNode.ELEMENT_NODE) {
        DOMElement parentElement = ((DOMElement) parent);
        namespaceURI = getNamespaceURI(prefix, parentElement);
        if (namespaceURI != null) {
          return namespaceURI;
        }
      }
      parent = parent.getParentNode();
    }
    return null;
  }

  /**
   * Returns the namespace URI from the given prefix declared in the given element and null
   * otherwise.
   *
   * @param prefix the prefix
   * @param element the DOM element
   * @return the namespace URI from the given prefix declared in the given element and null
   *     otherwise.
   */
  private static String getNamespaceURI(String prefix, DOMElement element) {
    boolean hasPrefix = !StringUtils.isEmpty(prefix);
    return hasPrefix
        ? element.getAttribute(XMLNS_NO_DEFAULT_ATTR + prefix)
        : element.getAttribute(XMLNS_ATTR);
  }

  public Collection<String> getAllPrefixes() {
    if (hasAttributes()) {
      Collection<String> prefixes = new ArrayList<>();
      for (DOMAttr attr : getAttributeNodes()) {
        if (attr.isNoDefaultXmlns()) {
          prefixes.add(attr.extractPrefixFromXmlns());
        }
      }
      return prefixes;
    }
    return Collections.emptyList();
  }

  /**
   * Returns the xmlns prefix from the given namespace URI and null otherwise.
   *
   * @param namespaceURI the namespace
   * @return the xmlns prefix from the given namespace URI and null otherwise.
   */
  public String getPrefix(String namespaceURI) {
    if (namespaceURI == null) {
      return null;
    }
    if (hasAttributes()) {
      for (DOMAttr attr : getAttributeNodes()) {
        String prefix = attr.getPrefixIfMatchesURI(namespaceURI);
        if (prefix != null) {
          return prefix;
        }
      }
    }
    // try to get the prefix in the parent element
    DOMNode parent = getParentNode();
    while (parent != null) {
      if (parent.getNodeType() == DOMNode.ELEMENT_NODE) {
        DOMElement parentElement = ((DOMElement) parent);
        String prefix = parentElement.getPrefix(namespaceURI);
        if (prefix != null) {
          return prefix;
        }
      }
      parent = parent.getParentNode();
    }
    return null;
  }

  public boolean isDocumentElement() {
    return this.equals(getOwnerDocument().getDocumentElement());
  }

  public boolean isSelfClosed() {
    return selfClosed;
  }

  /**
   * Will traverse backwards from the start offset returning an offset of the given character if
   * it's found before another character. Whitespace is ignored.
   *
   * <p>Returns null if the character is not found.
   *
   * <p>The initial value for the start offset is not included. So have the offset 1 position after
   * the character you want to start at.
   */
  public Integer endsWith(char c, int startOffset) {
    String text = this.getOwnerDocument().getText();
    if (startOffset > text.length() || startOffset < 0) {
      return null;
    }
    startOffset--;
    while (startOffset >= 0) {
      char current = text.charAt(startOffset);
      if (Character.isWhitespace(current)) {
        startOffset--;
        continue;
      }
      if (current != c) {
        return null;
      }
      return startOffset;
    }
    return null;
  }

  /**
   * Returns true if the given tag is the same tag of this element and false otherwise.
   *
   * @param tag tag element
   * @return true if the given tag is the same tag of this element and false otherwise.
   */
  public boolean isSameTag(String tag) {
    return Objects.equals(this.tag, tag);
  }

  public boolean isInStartTag(int offset) {
    if (startTagOpenOffset == NULL_VALUE || startTagCloseOffset == NULL_VALUE) {
      // case <|
      return true;
    }
    if (offset > startTagOpenOffset && offset <= startTagCloseOffset) {
      // case <bean | >
      return true;
    }
    return false;
  }

  public boolean isInEndTag(int offset) {
    return isInEndTag(offset, false);
  }

  public boolean isInEndTag(int offset, boolean afterBackSlash) {
    if (endTagOpenOffset == NULL_VALUE) {
      // case >|
      return false;
    }
    if (offset > endTagOpenOffset + (afterBackSlash ? 1 : 0) && offset < getEnd()) {
      // case </bean | >
      return true;
    }
    return false;
  }

  public boolean isInInsideStartEndTag(int offset) {
    return offset > startTagCloseOffset && offset <= endTagOpenOffset;
  }

  /**
   * Returns the start tag open offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   *
   * @return the start tag open offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   */
  public int getStartTagOpenOffset() {
    return startTagOpenOffset;
  }

  /**
   * Returns the start tag close offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   *
   * @return the start tag close offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   */
  public int getStartTagCloseOffset() {
    return startTagCloseOffset;
  }

  /**
   * Returns the end tag open offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   *
   * @return the end tag open offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   */
  public int getEndTagOpenOffset() {
    return endTagOpenOffset;
  }

  /**
   * Returns the end tag close offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   *
   * @return the end tag close offset and {@link DOMNode#NULL_VALUE} if it doesn't exist.
   */
  public int getEndTagCloseOffset() {
    return endTagCloseOffset;
  }

  /**
   * Returns the offset before the close of start tag name. <code>
   * 	<foo |></foo>
   *  <foo |/>
   * </code>
   *
   * @return the offset before the close of start tag name.
   */
  public int getOffsetBeforeCloseOfStartTag() {
    if (isSelfClosed()) {
      return getEnd() - 2;
    }
    return getStartTagCloseOffset();
  }

  /**
   * Returns true if has a start tag.
   *
   * <p>In our source-oriented DOM, a lone end tag will cause a node to be created in the tree,
   * unlike well-formed-only DOMs.
   *
   * @return true if has a start tag.
   */
  public boolean hasStartTag() {
    return getStartTagOpenOffset() != NULL_VALUE;
  }

  /**
   * Returns true if has an end tag.
   *
   * <p>In our source-oriented DOM, sometimes Elements are "ended", even without an explicit end tag
   * in the source.
   *
   * @return true if has an end tag.
   */
  public boolean hasEndTag() {
    return getEndTagOpenOffset() != NULL_VALUE;
  }

  /** If '>' exists in <root> */
  public boolean isStartTagClosed() {
    return getStartTagCloseOffset() != NULL_VALUE;
  }

  /** If '>' exists in </root> */
  public boolean isEndTagClosed() {
    return getEndTagCloseOffset() != NULL_VALUE;
  }

  /**
   * Returns true if the given element is an orphan end tag (which has no start tag, eg: </a>) and
   * false otherwise.
   *
   * @return true if the given element is an orphan end tag (which has no start tag, eg: </a>) and
   *     false otherwise.
   */
  public boolean isOrphanEndTag() {
    return hasEndTag() && !hasStartTag();
  }

  /**
   * Returns true if the given element is an orphan end tag (which has no start tag, eg: </a>) of
   * the given tag name and false otherwise.
   *
   * @param tagName the end tag name.
   * @return true if the given element is an orphan end tag (which has no start tag, eg: </a>) of
   *     the given tag name and false otherwise.
   */
  public boolean isOrphanEndTagOf(String tagName) {
    return isSameTag(tagName) && isOrphanEndTag();
  }

  /**
   * Returns the offset at which the given unclosed start tag should be closed with an angle bracket
   *
   * @returns the offset at which the given unclosed start tag should be closed with an angle
   *     bracket
   */
  public int getUnclosedStartTagCloseOffset() {
    String documentText = getOwnerDocument().getText();
    int i = getStart() + 1;
    for (;
        i < documentText.length() && documentText.charAt(i) != '/' && documentText.charAt(i) != '<';
        i++) {}
    if (i < documentText.length() && documentText.charAt(i) == '/') {
      return i + 1;
    }
    i--;
    for (; i > 0 && Character.isWhitespace(documentText.charAt(i)); i--) {}
    return i + 1;
  }

  @Override
  public DOMElement getOrphanEndElement(int offset, String tagName) {
    if (getEnd() <= offset) {
      // <employee />|
      // <employee /> |
      // <employee></employee> |
      // check if next sibling node is an element like <\tagName>
      return super.getOrphanEndElement(offset, tagName);
    }
    if (isSameTag(tagName) && isInStartTag(offset)) {
      // <employe|e></employee>
      return hasEndTag() ? this : null;
    }
    // search if it exists an end tag
    List<DOMNode> children = getChildren();
    for (DOMNode child : children) {
      if (child.isElement()) {
        DOMElement childElement = (DOMElement) child;
        if (childElement.isOrphanEndTagOf(tagName)) {
          return childElement;
        }
      }
    }
    return null;
  }

  /**
   * Returns true if element has a closing end tag (eg: <a> </a>) and false otherwise (eg: <a>
   * </b>).
   *
   * @return true if element has a closing end tag (eg: <a> </a>) and false otherwise (eg: <a>
   *     </b>).
   */
  @Override
  public boolean isClosed() {
    return super.isClosed();
  }

  @Override
  public String getAttributeNS(String arg0, String arg1) throws DOMException {
    return null;
  }

  @Override
  public DOMAttr getAttributeNode(String name) {
    return super.getAttributeNode(name);
  }

  @Override
  public DOMAttr getAttributeNodeNS(String arg0, String arg1) throws DOMException {
    return null;
  }

  @Override
  public NodeList getElementsByTagName(String arg0) {
    return null;
  }

  @Override
  public NodeList getElementsByTagNameNS(String arg0, String arg1) throws DOMException {
    return null;
  }

  @Override
  public TypeInfo getSchemaTypeInfo() {
    return null;
  }

  @Override
  public boolean hasAttributeNS(String arg0, String arg1) throws DOMException {
    return false;
  }

  @Override
  public void removeAttribute(String arg0) throws DOMException {}

  @Override
  public void removeAttributeNS(String arg0, String arg1) throws DOMException {}

  @Override
  public DOMAttr removeAttributeNode(org.w3c.dom.Attr arg0) throws DOMException {
    return null;
  }

  @Override
  public void setAttributeNS(String arg0, String arg1, String arg2) throws DOMException {}

  @Override
  public DOMAttr setAttributeNode(org.w3c.dom.Attr arg0) throws DOMException {
    return null;
  }

  @Override
  public DOMAttr setAttributeNodeNS(org.w3c.dom.Attr arg0) throws DOMException {
    return null;
  }

  @Override
  public void setIdAttribute(String arg0, boolean arg1) throws DOMException {}

  @Override
  public void setIdAttributeNS(String arg0, String arg1, boolean arg2) throws DOMException {}

  @Override
  public void setIdAttributeNode(org.w3c.dom.Attr arg0, boolean arg1) throws DOMException {}

  /**
   * Returns true if the element is empty and false otherwise.
   *
   * @return true if the element is empty and false otherwise.
   */
  public boolean isEmpty() {
    if (!hasChildNodes()) {
      return true;
    }
    for (DOMNode child : getChildren()) {
      if (child.isText()) {
        DOMText text = (DOMText) child;
        if (!text.isElementContentWhitespace()) {
          return false;
        }
      } else {
        return false;
      }
    }
    return true;
  }

  public int getOffsetAfterStartTag() {
    if (hasTagName()) {
      return getStartTagOpenOffset() + 1;
    }
    return getStartTagOpenOffset() + getTagName().length() + 1;
  }
}
