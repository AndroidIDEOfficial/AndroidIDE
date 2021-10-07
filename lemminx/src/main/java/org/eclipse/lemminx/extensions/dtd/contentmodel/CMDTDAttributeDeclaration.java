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
package org.eclipse.lemminx.extensions.dtd.contentmodel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.xerces.impl.dtd.XMLAttributeDecl;
import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.eclipse.lemminx.extensions.contentmodel.model.CMAttributeDeclaration;
import org.eclipse.lemminx.services.extensions.ISharedSettingsRequest;

/**
 * DTD attribute declaration.
 *
 */
public class CMDTDAttributeDeclaration extends XMLAttributeDecl implements CMAttributeDeclaration {

	private String documentation;
	private CMDTDElementDeclaration elementDecl;

	public CMDTDAttributeDeclaration(CMDTDElementDeclaration elementDecl) {
		this.elementDecl = elementDecl;
	}

	@Override
	public String getName() {
		return super.name.localpart;
	}

	@Override
	public String getDefaultValue() {
		return super.simpleType.defaultValue;
	}

	@Override
	public Collection<String> getEnumerationValues() {
		String[] values = super.simpleType.enumeration;
		if (values == null) {
			return Collections.emptyList();
		}
		return Arrays.asList(values);
	}

	@Override
	public String getAttributeNameDocumentation(ISharedSettingsRequest request) {
		if (documentation != null) {
			return documentation;
		}
		documentation = elementDecl.getDocumentation(getName());
		return documentation;
	}

	@Override
	public boolean isRequired() {
		return super.simpleType.defaultType == XMLSimpleType.DEFAULT_TYPE_REQUIRED;
	}

	@Override
	public String getAttributeValueDocumentation(String value, ISharedSettingsRequest request) {
		return null;
	}

}
