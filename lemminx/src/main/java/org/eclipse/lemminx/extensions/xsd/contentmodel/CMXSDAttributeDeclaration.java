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
package org.eclipse.lemminx.extensions.xsd.contentmodel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSValue;
import org.eclipse.lemminx.extensions.contentmodel.model.CMAttributeDeclaration;
import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;
import org.eclipse.lemminx.settings.SchemaDocumentationType;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.MarkupKind;

/**
 * XSD attribute declaration implementation.
 *
 */
public class CMXSDAttributeDeclaration implements CMAttributeDeclaration {

	private final XSAttributeUse attributeUse;

	private Map<String, String> valuesDocumentation;

	private String attrDocumentation;

	private SchemaDocumentationType docStrategy;

	public CMXSDAttributeDeclaration(XSAttributeUse attributeUse) {
		this.attributeUse = attributeUse;
	}

	@Override
	public String getName() {
		return getAttrDeclaration().getName();
	}

	@Override
	public String getDefaultValue() {
		XSValue xsValue = attributeUse.getValueConstraintValue();
		if (xsValue == null) {
			if (CMXSDDocument.isBooleanType(getAttrDeclaration().getTypeDefinition())) {
				return "false";
			}
		}
		return xsValue != null ? xsValue.getNormalizedValue().toString() : null;
	}

	@Override
	public String getAttributeNameDocumentation(ISharedSettingsRequest request) {
		SchemaDocumentationType currStrategy = request.getSharedSettings().getPreferences()
				.getShowSchemaDocumentationType();
		if (this.docStrategy != currStrategy) {
			clearDocumentation();
		} else if (this.attrDocumentation != null) {
			return this.attrDocumentation;
		}
		this.docStrategy = currStrategy;
		// Try get xs:annotation from the element declaration or type
		XSObjectList annotations = getAttributeNameAnnotations();
		boolean markdownSupported = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
		this.attrDocumentation = new XSDDocumentation(annotations, docStrategy, !markdownSupported)
				.getFormattedDocumentation(markdownSupported);
		return this.attrDocumentation;
	}

	/**
	 * Returns list of xs:annotation from the attribute declaration or type
	 * declaration.
	 * 
	 * @return list of xs:annotation from the attribute declaration or type
	 *         declaration.
	 */
	private XSObjectList getAttributeNameAnnotations() {
		// Try get xs:annotation from the attribute declaration
		XSAttributeDeclaration attributeDeclaration = getAttrDeclaration();
		XSObjectList annotation = attributeDeclaration.getAnnotations();
		if (annotation != null && annotation.getLength() > 0) {
			return annotation;
		}
		// Try get xs:annotation from the type of attribute declaration
		XSSimpleTypeDefinition typeDefinition = attributeDeclaration.getTypeDefinition();
		return typeDefinition != null ? typeDefinition.getAnnotations() : null;
	}

	@Override
	public String getAttributeValueDocumentation(String value, ISharedSettingsRequest request) {
		SchemaDocumentationType currStrategy = request.getSharedSettings().getPreferences()
				.getShowSchemaDocumentationType();
		if (this.docStrategy != currStrategy) {
			clearDocumentation();
		}
		valuesDocumentation = null;
		this.docStrategy = currStrategy;
		if (valuesDocumentation == null) {
			valuesDocumentation = createValuesDocumentation(request);
		}
		return valuesDocumentation.get(value);
	}

	private Map<String, String> createValuesDocumentation(ISharedSettingsRequest request) {
		boolean markdownSupported = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
		Map<String, String> valuesDocumentation = new HashMap<>();
		// loop for each enumeration values and update the values documentation map with
		// documentation
		getEnumerationValues().forEach(value -> {
			String documentation = null;
			XSObjectList annotations = getAttributeValueAnnotations(value);
			if (annotations != null) {
				documentation = new XSDDocumentation(annotations, value, docStrategy, !markdownSupported)
						.getFormattedDocumentation(markdownSupported);
			}
			if (StringUtils.isBlank(documentation)) {
				// The documentation is blank or not defined, try to get the documentation from
				// the attribute
				// name
				documentation = getAttributeNameDocumentation(request);
			}
			valuesDocumentation.put(value, documentation);
		});
		return valuesDocumentation;
	}

	/**
	 * Returns list of xs:annotation from the attribute declaration or type
	 * declaration.
	 * 
	 * Indicated by:
	 * https://msdn.microsoft.com/en-us/library/ms256143(v=vs.110).aspx xs:attribute
	 * tags have content of either an xs:annotation or xs:simpleType
	 * 
	 * @param value
	 * 
	 * @return list of xs:annotation from the attribute declaration or type
	 *         declaration.
	 */
	private XSObjectList getAttributeValueAnnotations(String value) {
		// Try get xs:annotation from the xs:enumeration declaration
		XSAttributeDeclaration attributeDeclaration = getAttrDeclaration();
		XSSimpleTypeDefinition simpleTypeDefinition = attributeDeclaration.getTypeDefinition();
		XSObjectList annotation = CMXSDDocument.getEnumerationAnnotations(simpleTypeDefinition, value);
		if (annotation != null && annotation.getLength() > 0) {
			return annotation;
		}
		// There was no specific documentation for the value, so use the general
		// attribute documentation
		return getAttributeNameAnnotations();
	}

	private void clearDocumentation() {
		this.valuesDocumentation = null;
		this.attrDocumentation = null;
	}

	@Override
	public boolean isRequired() {
		return attributeUse.getRequired();
	}

	XSAttributeDeclaration getAttrDeclaration() {
		return attributeUse.getAttrDeclaration();
	}

	@Override
	public Collection<String> getEnumerationValues() {
		XSAttributeDeclaration attributeDeclaration = getAttrDeclaration();
		if (attributeDeclaration != null) {
			XSSimpleTypeDefinition typeDefinition = attributeDeclaration.getTypeDefinition();
			return CMXSDDocument.getEnumerationValues(typeDefinition);
		}
		return Collections.emptyList();
	}
}
