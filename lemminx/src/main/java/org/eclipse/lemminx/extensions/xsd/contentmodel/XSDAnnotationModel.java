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

import static org.eclipse.lemminx.dom.parser.Constants.DOCUMENTATION_CONTENT;
import static org.eclipse.lemminx.utils.StringUtils.isEmpty;
import static org.eclipse.lemminx.utils.StringUtils.normalizeSpace;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xerces.impl.dv.ValidatedInfo;
import org.apache.xerces.xs.XSAnnotation;
import org.apache.xerces.xs.XSMultiValueFacet;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.datatypes.ObjectList;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Extract xs:document & xs:appinfo from the xs:annotation.
 *
 */
class XSDAnnotationModel {

	private final List<String> appInfo;

	private final List<String> documentation;

	private XSDAnnotationModel() {
		this.appInfo = new ArrayList<>();
		this.documentation = new ArrayList<>();
	}

	/**
	 * Returns content from appinfo element(s)
	 * 
	 * @return content from appinfo element(s)
	 */
	public List<String> getAppInfo() {
		return appInfo;
	}

	/**
	 * Returns content from documentation elements(s)
	 * 
	 * @return content from documentation elements(s)
	 */
	public List<String> getDocumentation() {
		return documentation;
	}

	/**
	 * Returns documentation content from the provided collection of annotations
	 * 
	 * @param annotations the collection of annotations
	 * @return documentation content from the provided collection of annotations
	 */
	public static List<String> getDocumentation(XSObjectList annotations) {
		return getDocumentation(annotations, null);
	}

	/**
	 * Returns documentation content from the provided collection of annotations
	 * 
	 * @param annotations the collection of attribute value annotations
	 * @param value       the attribute value to find documentation content for
	 * @return documentation content from the provided collection of annotations
	 */
	public static List<String> getDocumentation(XSObjectList annotations, String value) {
		if (annotations == null) {
			return Collections.emptyList();
		}
		List<String> result = new ArrayList<>();
		for (Object object : annotations) {
			XSAnnotation annotation = getXSAnnotation((XSObject) object, value);
			XSDAnnotationModel annotationModel = XSDAnnotationModel.load(annotation);
			if (annotationModel != null) {
				List<String> documentation = annotationModel.getDocumentation();
				if (documentation.size() > 0) {
					result.addAll(documentation);
				} else {
					String annotationString = annotation.getAnnotationString();
					if (!isEmpty(annotationString)) {
						String docFromPattern = getDocumentation(annotationString);
						if (!isEmpty(docFromPattern)) {
							result.add(docFromPattern);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Returns appinfo content from the provided collection of annotations
	 * 
	 * @param annotations the collection of annotations
	 * @return appinfo content from the provided collection of annotations
	 */
	public static List<String> getAppInfo(XSObjectList annotations) {
		return getAppInfo(annotations, null);
	}

	/**
	 * Returns appinfo content from the provided collection of annotations
	 * 
	 * @param annotations the collection of attribute value annotations
	 * @param value       the attribute value to find appinfo content for
	 * @return appinfo content from the provided collection of annotations
	 */
	public static List<String> getAppInfo(XSObjectList annotations, String value) {
		if (annotations == null) {
			return Collections.emptyList();
		}
		List<String> appinfo = new ArrayList<>();
		for (Object object : annotations) {
			XSAnnotation annotation = getXSAnnotation((XSObject) object, value);
			XSDAnnotationModel annotationModel = XSDAnnotationModel.load(annotation);
			if (annotationModel != null) {
				appinfo.addAll(annotationModel.getAppInfo());
			}
		}
		return appinfo;
	}

	/**
	 * Returns the prefix (ie. xs) from the provided collection of annotations
	 * 
	 * @param annotations the collection of annotations
	 * @return the prefix (ie. xs) from the provided collection of annotations
	 */
	public static String getPrefix(XSObjectList annotations) {
		return getPrefix(annotations, null);
	}

	/**
	 * Returns the prefix (ie. xs) from the provided collection of annotations
	 * 
	 * Prerequisite: <code>value</code> should be provided if <code>annotations</code>
	 * is a collection of attribute value annotations
	 * 
	 * @param annotations the collection of annotations
	 * @param value       the attribute value
	 * @return the prefix (ie. xs) from the provided collection of annotations
	 */
	public static String getPrefix(XSObjectList annotations, String value) {
		if (annotations == null) {
			return "";
		}
		for (Object object : annotations) {
			XSAnnotation annotation = getXSAnnotation((XSObject) object, value);
			String content = annotation.getAnnotationString();
			int index = content.indexOf(":");
			if (index != -1) {
				return content.substring(1, index);
			}
		}
		return "";
	}


	public static XSDAnnotationModel load(XSAnnotation annotation) {
		try {
			SAXParserFactory factory = DOMUtils.newSAXParserFactory();
			SAXParser saxParser = factory.newSAXParser();
			XSAnnotationHandler handler = new XSAnnotationHandler();
			saxParser.parse(new InputSource(new StringReader(annotation.getAnnotationString())), handler);
			return handler.getModel();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns the <code>XSAnnotation</code> instance for the 
	 * provided <code>annotation</code>
	 * 
	 * If <code>value</code> is provided, the <code>XSAnnotation</code> for
	 * an attribute value will be searched for
	 * 
	 * If not provided, the <code>XSAnnotation</code> for
	 * an attribute or element will be searched for
	 * 
	 * @param annotation the annotation object 
	 * @param value      the attribute value
	 * @return the <code>XSAnnotation</code> instance for the 
	 * provided <code>annotation</code>
	 */
	private static XSAnnotation getXSAnnotation(XSObject annotation, String value) {
		if (annotation instanceof XSMultiValueFacet && value != null) {
			XSMultiValueFacet multiValueFacet = (XSMultiValueFacet) annotation;
				ObjectList enumerationValues = multiValueFacet.getEnumerationValues();
				XSObjectList annotationValues = multiValueFacet.getAnnotations();
				for (int i = 0; i < enumerationValues.getLength(); i++) {
					Object enumValue = enumerationValues.get(i);

					// Assuming always ValidatedInfo
					String enumString = ((ValidatedInfo) enumValue).stringValue();

					if (value.equals(enumString)) {
						return (XSAnnotation) annotationValues.get(i);
					}
				}
		} else if (annotation instanceof XSAnnotation) {
			return  (XSAnnotation) annotation;
		}
		return null;
	}

	private static class XSAnnotationHandler extends DefaultHandler {

		private static final String APPINFO_ELEMENT = "appinfo";
		private static final String DOCUMENTATION_ELEMENT = "documentation";

		private StringBuilder current;
		private final XSDAnnotationModel model;

		public XSAnnotationHandler() {
			model = new XSDAnnotationModel();
		}

		public XSDAnnotationModel getModel() {
			return model;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (qName.endsWith(DOCUMENTATION_ELEMENT) || qName.endsWith(APPINFO_ELEMENT)) {
				current = new StringBuilder();
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (current != null) {
				if (qName.endsWith(APPINFO_ELEMENT)) {
					addIfNonEmptyString(model.appInfo, normalizeSpace(current.toString()));
				} else if (qName.endsWith(DOCUMENTATION_ELEMENT)) {
					addIfNonEmptyString(model.documentation, normalizeSpace(current.toString()));
				}
				current = null;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (current != null) {
				current.append(ch, start, length);
			}
			super.characters(ch, start, length);
		}

		private static void addIfNonEmptyString(List<String> list, String str) {
			if (!StringUtils.isEmpty(str)) {
				list.add(str);
			}
		}

	}

	public static String getDocumentation(String xml) {
		Matcher m = DOCUMENTATION_CONTENT.matcher(xml);
		if(m.find()) {
			return m.group(1);
		}
		return null;
	}

}
