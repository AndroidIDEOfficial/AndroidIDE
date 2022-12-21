/*******************************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.lemminx.dom;

import com.itsaky.androidide.models.Range;

import org.eclipse.lemminx.utils.XMLPositionUtility;

/** DTDDeclParameter */
public class DTDDeclParameter implements DOMRange, TargetRange {

  private final DTDDeclNode ownerNode;

  String parameter;

  int start, end;

  public DTDDeclParameter(DTDDeclNode ownerNode, int start, int end) {
    this.ownerNode = ownerNode;
    this.start = start;
    this.end = end;
  }

  @Override
  public int getStart() {
    return start;
  }

  @Override
  public int getEnd() {
    return end;
  }

  @Override
  public DOMDocument getOwnerDocument() {
    return getOwnerNode().getOwnerDocument();
  }

  public DOMDocumentType getOwnerDocType() {
    return getOwnerNode().getOwnerDocType();
  }

  public DTDDeclNode getOwnerNode() {
    return ownerNode;
  }

  public String getParameter() {
    if (parameter == null) {
      parameter = getOwnerDocType().getSubstring(start, end);
    }
    return parameter;
  }

  /**
   * Will get the parameter with the first and last character removed
   *
   * <p>Can be used to remove the quotations from a URL value...
   */
  public String getParameterWithoutFirstAndLastChar() {
    if (parameter == null) {
      parameter = getOwnerDocType().getSubstring(start + 1, end - 1);
    }
    return parameter;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DTDDeclParameter)) {
      return false;
    }
    DTDDeclParameter temp = (DTDDeclParameter) obj;
    return start == temp.start && end == temp.end;
  }

  @Override
  public Range getTargetRange() {
    return XMLPositionUtility.createRange(this);
  }

  @Override
  public String getTargetURI() {
    return getOwnerDocument().getDocumentURI();
  }
}
