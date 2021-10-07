/*******************************************************************************
 * Copyright (c) 2020 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.dom.DOMDocument;

/**
 * XML Document validation service available to XML LS extensions
 * 
 * @author Alex Boyko
 *
 */
public interface IXMLValidationService {
	
	/**
	 * Performs XML document validation
	 * @param document the XML document
	 */
	void validate(DOMDocument document);

}
