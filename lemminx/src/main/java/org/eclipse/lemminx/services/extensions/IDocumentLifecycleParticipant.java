/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lemminx.dom.DOMDocument;

/**
 * Document LifecycleService participant API.
 * 
 * @since 0.18.0
 */
public interface IDocumentLifecycleParticipant {

	/**
	 * Handler called when an XML document is opened.
	 * 
	 * @param document the DOM document
	 */
	void didOpen(DOMDocument document);

	/**
	 * Handler called when an XML document is changed.
	 * 
	 * @param document the DOM document
	 */
	void didChange(DOMDocument document);

	/**
	 * Handler called when an XML document is saved.
	 * 
	 * @param document the DOM document
	 */
	void didSave(DOMDocument document);

	/**
	 * Handler called when an XML document is closed.
	 * 
	 * @param document the DOM document
	 */
	void didClose(DOMDocument document);

}
