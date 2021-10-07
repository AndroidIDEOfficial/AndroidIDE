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
package org.eclipse.lemminx.extensions.contentmodel.participants;

import static org.eclipse.lemminx.utils.StringUtils.getString;

import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.URI.MalformedURIException;
import org.apache.xerces.xni.XMLLocator;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMElement;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.dom.NoNamespaceSchemaLocation;
import org.eclipse.lemminx.dom.SchemaLocation;
import org.eclipse.lemminx.dom.SchemaLocationHint;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.TargetNamespace_1CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.TargetNamespace_2CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_attribute_3CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_complex_type_2_1CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_complex_type_2_3CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_complex_type_2_4_aCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_complex_type_3_2_2CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_complex_type_4CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_enumeration_validCodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.cvc_type_3_1_1CodeAction;
import org.eclipse.lemminx.extensions.contentmodel.participants.codeactions.schema_reference_4CodeAction;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;
import org.eclipse.lemminx.services.extensions.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IXMLErrorCode;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ResourceOperationKind;

/**
 * XML Schema error code.
 *
 * @see https://wiki.xmldation.com/Support/Validator
 *
 */
public enum XMLSchemaErrorCode implements IXMLErrorCode {
	cvc_complex_type_2_3("cvc-complex-type.2.3"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-3
	cvc_complex_type_2_2("cvc-complex-type.2.2"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-2
	cvc_complex_type_2_1("cvc-complex-type.2.1"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-1
	cvc_complex_type_2_4_a("cvc-complex-type.2.4.a"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-4-a
	cvc_complex_type_2_4_b("cvc-complex-type.2.4.b"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-4-b
	cvc_complex_type_2_4_c("cvc-complex-type.2.4.c"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-4-c
	cvc_complex_type_2_4_d("cvc-complex-type.2.4.d"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-4-d
	cvc_complex_type_2_4_f("cvc-complex-type.2.4.f"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-2-4-f
	cvc_complex_type_3_1("cvc-complex-type.3.1"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-3-1
	cvc_complex_type_3_2_2("cvc-complex-type.3.2.2"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-3-2-2
	cvc_complex_type_4("cvc-complex-type.4"), // https://wiki.xmldation.com/Support/Validator/cvc-complex-type-4
	cvc_datatype_valid_1_2_1("cvc-datatype-valid.1.2.1"), // https://wiki.xmldation.com/Support/Validator/cvc-datatype-valid-1-2-1
	cvc_datatype_valid_1_2_3("cvc-datatype-valid.1.2.3"), // https://wiki.xmldation.com/Support/Validator/cvc-datatype-valid-1-2-3
	cvc_elt_1_a("cvc-elt.1.a"), // https://wiki.xmldation.com/Support/Validator/cvc-elt-1
	cvc_elt_3_1("cvc-elt.3.1"), // https://wiki.xmldation.com/Support/Validator/cvc-elt-3-1
	cvc_elt_3_2_1("cvc-elt.3.2.1"), // https://wiki.xmldation.com/Support/Validator/cvc-elt-3-2-1
	cvc_elt_4_2("cvc-elt.4.2"), // https://wiki.xmldation.com/Support/Validator/cvc-elt-4-2
	cvc_pattern_valid("cvc-pattern-valid"), // https://wiki.xmldation.com/Support/Validator/cvc-pattern-valid
	cvc_type_3_1_1("cvc-type.3.1.1"), // https://wiki.xmldation.com/Support/Validator/cvc-type-3-1-1
	cvc_type_3_1_2("cvc-type.3.1.2"), // https://wiki.xmldation.com/Support/Validator/cvc-type-3-1-2
	cvc_type_3_1_3("cvc-type.3.1.3"), // https://wiki.xmldation.com/Support/Validator/cvc-type-3-1-3,
	cvc_attribute_3("cvc-attribute.3"), // https://wiki.xmldation.com/Support/Validator/cvc-attribute-3
	cvc_enumeration_valid("cvc-enumeration-valid"), // https://wiki.xmldation.com/Support/Validator/cvc-enumeration-valid
	cvc_maxlength_valid("cvc-maxLength-valid"), // https://wiki.xmldation.com/Support/validator/cvc-maxlength-valid
	cvc_minlength_valid("cvc-minLength-valid"), // https://wiki.xmldation.com/Support/validator/cvc-minlength-valid
	cvc_maxExclusive_valid("cvc-maxExclusive-valid"), // https://wiki.xmldation.com/Support/validator/cvc-maxexclusive-valid
	cvc_maxInclusive_valid("cvc-maxInclusive-valid"), // https://wiki.xmldation.com/Support/validator/cvc-maxinclusive-valid
	cvc_minExclusive_valid("cvc-minExclusive-valid"), // https://wiki.xmldation.com/Support/validator/cvc-minexclusive-valid
	cvc_minInclusive_valid("cvc-minInclusive-valid"), // https://wiki.xmldation.com/Support/validator/cvc-mininclusive-valid
	TargetNamespace_1("TargetNamespace.1"), //
	TargetNamespace_2("TargetNamespace.2"), //
	SchemaLocation("SchemaLocation"), //
	schema_reference_4("schema_reference.4"), //
	src_element_3("src-element.3");

	private final String code;

	private XMLSchemaErrorCode() {
		this(null);
	}

	private XMLSchemaErrorCode(String code) {
		this.code = code;
	}

	@Override
	public String getCode() {
		if (code == null) {
			return name();
		}
		return code;
	}

	@Override
	public String toString() {
		return getCode();
	}

	private final static Map<String, XMLSchemaErrorCode> codes;

	static {
		codes = new HashMap<>();
		for (XMLSchemaErrorCode errorCode : values()) {
			codes.put(errorCode.getCode(), errorCode);
		}
	}

	public static XMLSchemaErrorCode get(String name) {
		return codes.get(name);
	}

	/**
	 * Create the LSP range from the SAX error.
	 *
	 * @param location
	 * @param key
	 * @param arguments
	 * @param document
	 * @return the LSP range from the SAX error.
	 */
	public static Range toLSPRange(XMLLocator location, XMLSchemaErrorCode code, Object[] arguments,
			DOMDocument document) {
		int offset = location.getCharacterOffset() - 1;
		// adjust positions
		switch (code) {
		case cvc_complex_type_2_3:
			return XMLPositionUtility.selectFirstNonWhitespaceText(offset, document);
		case cvc_complex_type_2_2:
		case cvc_complex_type_2_4_a:
		case cvc_complex_type_2_4_b:
		case cvc_complex_type_2_4_c:
		case cvc_complex_type_2_4_d:
		case cvc_complex_type_2_4_f:
		case cvc_elt_1_a:
		case cvc_complex_type_4:
		case src_element_3:
			return XMLPositionUtility.selectStartTagName(offset, document);
		case cvc_complex_type_3_2_2: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeNameFromGivenNameAt(attrName, offset, document);
		}
		case cvc_elt_3_1: {
			String namespaceAntAttrName = getString(arguments[1]); // http://www.w3.org/2001/XMLSchema-instance,nil
			String attrName = namespaceAntAttrName;
			int index = namespaceAntAttrName.indexOf(",");
			if (index != -1) {
				String namespaceURI = namespaceAntAttrName.substring(0, index);
				String prefix = document.getDocumentElement().getPrefix(namespaceURI);
				attrName = namespaceAntAttrName.substring(index + 1, namespaceAntAttrName.length());
				if (prefix != null && !prefix.isEmpty()) {
					attrName = prefix + ":" + attrName;
				}
			}
			return XMLPositionUtility.selectAttributeFromGivenNameAt(attrName, offset, document);
		}
		case cvc_pattern_valid: {
			String value = getString(arguments[0]);
			Range result = XMLPositionUtility.selectAttributeValueByGivenValueAt(value, offset, document);
			if (result != null) {
				return result;
			}
			return XMLPositionUtility.selectTrimmedText(offset, document);
		}
		case SchemaLocation: { // xml xsi:schemaLocation
			SchemaLocation schemaLocation = document.getSchemaLocation();
			DOMRange locationRange = schemaLocation.getAttr().getNodeAttrValue();
			return locationRange != null ? XMLPositionUtility.createRange(locationRange) : null;
		}

		case schema_reference_4: {
			String grammarURI = arguments.length == 1 ? (String) arguments[0] : null;
			if (DOMUtils.isXSD(document)) {
				//
				// This error code occurs in XSD document when an XSD file path is invalid in
				// the following
				// attributes:
				// 1. xsd:import/@schemaLocation
				// 2. xsd:include/@schemaLocation
				//
				// search grammar uri from xs:include/@schemaLocation or
				// xs:import/@schemaLocation which reference the grammar URI
				DOMAttr schemaLocationAttr = XSDUtils.findSchemaLocationAttrByURI(document, grammarURI);
				if (schemaLocationAttr != null) {
					return XMLPositionUtility.selectAttributeValue(schemaLocationAttr);
				}
			} else {
				//
				// This error code occurs when an XSD file path is invalid in the following
				// attributes:
				// 1. xml-model href
				// 2. xsi:schemaLocation
				// 3. xsi:noNamespaceSchemaLocation
				//
				// Check if location comes from a xml-model/@href
				DOMRange locationRange = XMLModelUtils.getHrefNode(document, grammarURI);
				if (locationRange == null) {
					NoNamespaceSchemaLocation noNamespaceSchemaLocation = document.getNoNamespaceSchemaLocation();
					if (noNamespaceSchemaLocation != null) {
						locationRange = noNamespaceSchemaLocation.getAttr().getNodeAttrValue();
					} else {
						SchemaLocation schemaLocation = document.getSchemaLocation();
						if (schemaLocation != null) {
							String invalidSchemaPath = arguments[0] instanceof String ? (String) arguments[0] : null;

							if (invalidSchemaPath != null) {
								for (SchemaLocationHint locHintRange : schemaLocation.getSchemaLocationHints()) {
									String expandedHint = getResolvedLocation(document.getDocumentURI(),
											locHintRange.getHint());
									if (invalidSchemaPath.equals(expandedHint)) {
										return XMLPositionUtility.createRange(locHintRange);
									}
								}
							}
							// Highlight entire attribute if finding the location hint fails
							locationRange = schemaLocation.getAttr().getNodeAttrValue();
						}
					}
				}
				return locationRange != null ? XMLPositionUtility.createRange(locationRange) : null;
			}
		}
		case cvc_attribute_3:
		case cvc_complex_type_3_1:
		case cvc_elt_4_2: {
			String attrName = getString(arguments[1]);
			return XMLPositionUtility.selectAttributeValueAt(attrName, offset, document);
		}
		case cvc_type_3_1_1:
			return XMLPositionUtility.selectAllAttributes(offset, document);
		case cvc_complex_type_2_1:
		case cvc_elt_3_2_1:
			return XMLPositionUtility.selectContent(offset, document);
		case cvc_type_3_1_3:
		case cvc_datatype_valid_1_2_1:
		case cvc_datatype_valid_1_2_3:
		case cvc_enumeration_valid:
		case cvc_maxlength_valid:
		case cvc_minlength_valid:
		case cvc_maxExclusive_valid:
		case cvc_maxInclusive_valid:
		case cvc_minExclusive_valid:
		case cvc_minInclusive_valid: {
			// this error can occur for attribute value or text
			// Try for attribute value
			String attrValue = getString(arguments[0]);
			Range range = XMLPositionUtility.selectAttributeValueFromGivenValue(attrValue, offset, document);
			if (range != null) {
				return range;
			} else {
				// Try with text
				DOMElement element = (DOMElement) document.findNodeAt(offset);
				if (element != null && element.isEmpty()) {
					return XMLPositionUtility.selectStartTagName(element);
				} else if (DOMUtils.containsTextOnly(element)) {
					return XMLPositionUtility.selectTrimmedText(offset, document);
				} else {
					return XMLPositionUtility.selectFirstChild(offset, document);
				}
			}
		}
		case cvc_type_3_1_2:
			return XMLPositionUtility.selectStartTagName(offset, document);
		case TargetNamespace_1:
			return XMLPositionUtility.selectRootAttributeValue(DOMAttr.XMLNS_ATTR, document);
		case TargetNamespace_2:
			return XMLPositionUtility.selectRootStartTag(document);
		default:
		}
		return null;
	}

	public static void registerCodeActionParticipants(Map<String, ICodeActionParticipant> codeActions,
			SharedSettings sharedSettings) {
		codeActions.put(cvc_complex_type_2_4_a.getCode(), new cvc_complex_type_2_4_aCodeAction());
		codeActions.put(cvc_complex_type_2_4_c.getCode(), new cvc_complex_type_2_4_aCodeAction());
		codeActions.put(cvc_complex_type_2_3.getCode(), new cvc_complex_type_2_3CodeAction());
		codeActions.put(cvc_complex_type_4.getCode(), new cvc_complex_type_4CodeAction());
		codeActions.put(cvc_type_3_1_1.getCode(), new cvc_type_3_1_1CodeAction());
		codeActions.put(cvc_attribute_3.getCode(), new cvc_attribute_3CodeAction());
		codeActions.put(cvc_complex_type_3_2_2.getCode(), new cvc_complex_type_3_2_2CodeAction());
		codeActions.put(cvc_enumeration_valid.getCode(), new cvc_enumeration_validCodeAction());
		codeActions.put(cvc_complex_type_2_1.getCode(), new cvc_complex_type_2_1CodeAction());
		codeActions.put(TargetNamespace_1.getCode(), new TargetNamespace_1CodeAction());
		codeActions.put(TargetNamespace_2.getCode(), new TargetNamespace_2CodeAction());
		if (sharedSettings != null
				&& sharedSettings.getWorkspaceSettings().isResourceOperationSupported(ResourceOperationKind.Create)) {
			codeActions.put(schema_reference_4.getCode(), new schema_reference_4CodeAction());
		}
	}

	/**
	 * Returns the expanded system location
	 *
	 * @return the expanded system location
	 */
	private static String getResolvedLocation(String documentURI, String location) {
		if (location == null) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}
}
