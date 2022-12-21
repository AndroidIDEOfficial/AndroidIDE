/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.dom;

import java.util.ArrayList;
import java.util.List;

/**
 * DTD Attribute List declaration <!ATTLIST
 * 
 * @see https://www.w3.org/TR/REC-xml/#attdecls
 *
 */
public class DTDAttlistDecl extends DTDDeclNode {

	/**
	 * Format:
	 * 
	 * <!ATTLIST element-name attribute-name attribute-type "attribute-value>""
	 * 
	 * or
	 * 
	 * <pre>
	 * <!ATTLIST element-name 
	 * 			 attribute-name1 attribute-type1 "attribute-value1"
	 * 			 attribute-name2 attribute-type2 "attribute-value2"
	 * 			 ..
	 * </pre>
	 * 
	 * >
	 */

	public DTDDeclParameter attributeName;
	public DTDDeclParameter attributeType;
	public DTDDeclParameter attributeValue;

	private List<DTDAttlistDecl> internalChildren; // Holds all additional internal attlist declaractions

	public DTDAttlistDecl(int start, int end) {
		super(start, end);
		setDeclType(start + 2, start + 9);
	}

	@Override
	public String getNodeName() {
		return getAttributeName();
	}

	/**
	 * Returns the element name
	 * 
	 * @return the element name
	 */
	public String getElementName() {
		return getName();
	}

	/**
	 * Returns the attribute name
	 * 
	 * @return the attribute name
	 */
	public String getAttributeName() {
		return attributeName != null ? attributeName.getParameter() : null;
	}

	public void setAttributeName(int start, int end) {
		attributeName = addNewParameter(start, end);
	}

	public String getAttributeType() {
		return attributeType != null ? attributeType.getParameter() : null;
	}

	public void setAttributeType(int start, int end) {
		attributeType = addNewParameter(start, end);
	}

	public String getAttributeValue() {
		return attributeValue != null ? attributeValue.getParameter() : null;
	}

	public void setAttributeValue(int start, int end) {
		attributeValue = addNewParameter(start, end);
	}

	@Override
	public short getNodeType() {
		return DOMNode.DTD_ATT_LIST_NODE;
	}

	/**
	 * Add another internal attlist declaration to the list of children.
	 * 
	 * An ATTLIST decl can internally declare multiple declarations, see top of
	 * file. This will add another one to its list of additional declarations.
	 */
	void addAdditionalAttDecl(DTDAttlistDecl child) {
		if (internalChildren == null) {
			internalChildren = new ArrayList<>();
		}
		internalChildren.add(child);
	}

	public List<DTDAttlistDecl> getInternalChildren() {
		return internalChildren;
	}

	/**
	 * Returns true if this node's parent is the Doctype node.
	 * 
	 * 
	 * This is used because an Attlist declaration can have multiple attribute
	 * declarations within a tag that are each represented by this class.
	 */
	public boolean isRootAttlist() {
		return this.parent.isDoctype();
	}

}
