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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

/**
 * DTDNode
 */
public class DTDDeclNode extends DOMNode {

	/**
	 * This class is the base for all declaration nodes for DTD's.
	 * 
	 * It can also be used to represent an undefined tag, meaning it is not any of:
	 * ELEMENT, ATTLIST, ENTITY, or NOTATION
	 */

	public DTDDeclParameter unrecognized; // holds all content after parsing goes wrong in a DTD declaration (ENTITY,
											// ATTLIST, ...).
	public DTDDeclParameter declType; // represents the actual name of the decl eg: ENTITY, ATTLIST, ...

	private List<DTDDeclParameter> parameters;
	private DTDDeclParameter name;

	public DTDDeclNode(int start, int end) {
		super(start, end);
	}

	public String getName() {
		DTDDeclParameter name = getNameParameter();
		return name != null ? name.getParameter() : null;
	}

	public DTDDeclParameter getNameParameter() {
		return name;
	}

	protected DTDDeclParameter getParameterAtIndex(int index) {
		return parameters != null && parameters.size() > index ? parameters.get(index) : null;
	}

	public void setName(int start, int end) {
		name = addNewParameter(start, end);
	}

	public boolean isInNameParameter(int offset) {
		DTDDeclParameter name = getNameParameter();
		return DOMNode.isIncluded(name, offset);
	}

	public DOMDocumentType getOwnerDocType() {
		Node node = parent;
		while (node != null) {
			if (node.getNodeType() == Node.DOCUMENT_TYPE_NODE) {
				return (DOMDocumentType) node;
			}
			node = node.getParentNode();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.dom.Node#getNodeName()
	 */
	@Override
	public String getNodeName() {
		return getName();
	}

	@Override
	public short getNodeType() {
		return DOMNode.DTD_DECL_NODE;
	}

	public String getUnrecognized() {
		return unrecognized.getParameter();
	}

	public void setUnrecognized(int start, int end) {
		unrecognized = addNewParameter(start, end);
	}

	public DTDDeclParameter addNewParameter(int start, int end) {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		DTDDeclParameter parameter = new DTDDeclParameter(this, start, end);
		parameters.add(parameter);
		this.end = end; // updates end position of the node.
		return parameter;
	}

	public void updateLastParameterEnd(int end) {
		if (parameters != null && parameters.size() > 0) {
			DTDDeclParameter last = parameters.get(parameters.size() - 1);
			last.end = end;
			this.end = end;
		}
	}

	public List<DTDDeclParameter> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		return parameters;
	}

	public void setDeclType(int start, int end) {
		declType = new DTDDeclParameter(this, start, end);
	}

	public String getDeclType() {
		if (declType != null) {
			return declType.getParameter();
		}
		return null;
	}

	/**
	 * Returns the parameter name which references a DTD element declaration
	 * (<!ELEMENT) at the given offset and null otherwise.
	 * 
	 * <p>
	 * <!ATTLIST element-name ... will return position of 'element-name' which
	 * references the <!ELEMENT element-name
	 * </p>
	 * 
	 * @param offset the offset
	 * @return the parameter name which references a DTD element declaration
	 *         (<!ELEMENT) at the given offset and null otherwise.
	 */
	public DTDDeclParameter getReferencedElementNameAt(int offset) {
		if (isInNameParameter(offset)) {
			return getNameParameter();
		}
		return null;
	}
}