/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.xerces.xmlmodel;

import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.parser.XMLComponent;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

/**
 * XML model validator API.
 *
 */
public interface XMLModelValidator extends XMLComponent, XMLDocumentFilter{

	void setLocator(XMLLocator locator);
	
	void setHref(String href);

}
