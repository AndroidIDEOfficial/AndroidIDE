/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.participants;

import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;

/**
 * Provides an interface to find related info for a given error
 */
public interface IRelatedInfoFinder {

	/**
	 * Returns a list of related information
	 *
	 * @param offset   The LemMinX reported error start offset
	 * @param errorKey The error key
	 * @param document The document
	 * @return a list of related information
	 */
	List<DiagnosticRelatedInformation> findRelatedInformation(
			int offset,
			String errorKey,
			DOMDocument document);

}
