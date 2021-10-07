/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.extensions.xsd.participants;

import static org.eclipse.lemminx.utils.StringUtils.getString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xerces.xni.XMLLocator;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.s4s_elt_invalid_content_3CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.src_import_1_2CodeAction;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IXMLErrorCode;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Range;

/**
 * XSD error code.
 *
 * @see https://wiki.xmldation.com/Support/Validator
 *
 * All error code types and messages can be found in the Xerces library
 * https://github.com/apache/xerces2-j/blob/trunk/src/org/apache/xerces/impl/msg/XMLSchemaMessages.properties
 *
 */
public enum XSDErrorCode implements IXMLErrorCode {

	cos_all_limited_2("cos-all-limited.2"),
	ct_props_correct_3("ct-props-correct.3"),
	p_props_correct_2_1("p-props-correct.2.1"),
	s4s_elt_invalid_content_1("s4s-elt-invalid-content.1"), //
	s4s_elt_must_match_1("s4s-elt-must-match.1"), //
	s4s_elt_must_match_2("s4s-elt-must-match.2"),
	s4s_att_must_appear("s4s-att-must-appear"), //
	s4s_elt_invalid_content_2("s4s-elt-invalid-content.2"), //
	s4s_att_not_allowed("s4s-att-not-allowed"), //
	s4s_att_invalid_value("s4s-att-invalid-value"), //
	s4s_elt_character("s4s-elt-character"), //
	s4s_elt_invalid_content_3("s4s-elt-invalid-content.3"), //
	sch_props_correct_2("sch-props-correct.2"),
	schema_reference_4("schema_reference.4"), //
	src_ct_1("src-ct.1"),
	src_import_1_2("src-import.1.2"),
	src_element_3("src-element.3"),
	src_resolve_4_2("src-resolve.4.2"), //
	src_resolve("src-resolve"), src_element_2_1("src-element.2.1"),
	EmptyTargetNamespace("EmptyTargetNamespace"),
	src_import_3_1("src-import.3.1"),
	src_import_3_2("src-import.3.2");

	private final String code;

	private XSDErrorCode() {
		this(null);
	}

	private XSDErrorCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		if (code == null) {
			return name();
		}
		return code;
	}

	private final static Map<String, XSDErrorCode> codes;

	static {
		codes = new HashMap<>();
		for (XSDErrorCode errorCode : values()) {
			codes.put(errorCode.getCode(), errorCode);
		}
	}

	public static XSDErrorCode get(String name) {
		return codes.get(name);
	}

	/**
	 * Create the LSP range from the SAX error.
	 *
	 * @param characterOffset
	 * @param key
	 * @param arguments
	 * @param document
	 * @return the LSP range from the SAX error.
	 */
	public static Range toLSPRange(XMLLocator location, XSDErrorCode code, Object[] arguments, DOMDocument document) {
		int offset = location.getCharacterOffset() - 1;
		// adjust positions
		switch (code) {
		case cos_all_limited_2: {
			String nameValue = getString(arguments[1]);
			DOMNode parent = document.findNodeAt(offset);
			List<DOMNode> children = parent.getChildrenWithAttributeValue("name", nameValue);

			if (children.isEmpty()) {
				return XMLPositionUtility.selectStartTagName(offset, document);
			}

			offset = children.get(0).getStart() + 1;
			return XMLPositionUtility.selectAttributeValueAt("maxOccurs", offset, document);
		}
		case ct_props_correct_3: {
			String attrValue = getString(arguments[0]);
			if (attrValue.charAt(0) == ':') {
				attrValue = attrValue.substring(1);
			}
			return XMLPositionUtility.selectAttributeValueFromGivenValue(attrValue, offset, document);
		}
		case p_props_correct_2_1:
			return XMLPositionUtility.selectAttributeFromGivenNameAt("minOccurs", offset, document);
		case s4s_elt_invalid_content_1:
		case s4s_elt_must_match_1:
		case s4s_elt_must_match_2:
		case s4s_att_must_appear:
		case s4s_elt_invalid_content_2:
		case s4s_elt_invalid_content_3:
		case src_element_2_1:
		case src_element_3:
		case src_import_1_2:
			return XMLPositionUtility.selectStartTagName(offset, document);
		case s4s_att_not_allowed: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeNameFromGivenNameAt(attrName, offset, document);
		}
		case s4s_att_invalid_value: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeValueAt(attrName, offset, document);
		}
		case s4s_elt_character:
			return XMLPositionUtility.selectContent(offset, document);
		case sch_props_correct_2: {
			String argument = getString(arguments[0]);
			String attrName = argument.substring(argument.indexOf(",") + 1);
			return XMLPositionUtility.selectAttributeValueFromGivenValue(attrName, offset, document);
		}
		case src_ct_1:
			return XMLPositionUtility.selectAttributeValueAt("base", offset, document);
		case src_resolve_4_2: {
			String attrValue = getString(arguments[2]);
			return XMLPositionUtility.selectAttributeValueByGivenValueAt(attrValue, offset, document);
		}
		case src_resolve: {
			String attrValue = getString(arguments[0]);
			return XMLPositionUtility.selectAttributeValueByGivenValueAt(attrValue, offset, document);
		}
		case schema_reference_4: {
			return XMLPositionUtility.selectAttributeValueAt(XSDUtils.SCHEMA_LOCATION_ATTR, offset, document);
		}
		case EmptyTargetNamespace :
			return XMLPositionUtility.selectAttributeValueAt(XSDUtils.TARGET_NAMESPACE_ATTR, offset, document);
		case src_import_3_1: {
			// If the imported file of `schemaLocation` contains at least a doctype and 'xs:schema' with at least one `xs:element`,
			// then the `xs:import` line will be highlighted, otherwise the `xs:schema` line will be highlighted
			DOMNode elementHighlighted = document.findNodeAt(offset);
			if (elementHighlighted.getNodeName().equals(XSDUtils.XS_SCHEMA_TAG)) { // `xs:schema` line is highlighted
				return XMLPositionUtility.selectChildNodeAttributeValueFromGivenNameAt(XSDUtils.XS_IMPORT_TAG, XSDUtils.NAMESPACE_ATTR, offset, document);
			} else { // `xs:import` line is highlighted
				return XMLPositionUtility.selectAttributeValueAt(XSDUtils.NAMESPACE_ATTR, offset, document);
			}
		}
		case src_import_3_2:
			return XMLPositionUtility.selectChildNodeAttributeValueFromGivenNameAt(XSDUtils.XS_IMPORT_TAG, XSDUtils.SCHEMA_LOCATION_ATTR, offset, document);
		}

		return null;
	}

	public static void registerCodeActionParticipants(Map<String, ICodeActionParticipant> codeActions) {
		codeActions.put(s4s_elt_invalid_content_3.getCode(), new s4s_elt_invalid_content_3CodeAction());
		codeActions.put(src_import_1_2.getCode(), new src_import_1_2CodeAction());
	}
}
