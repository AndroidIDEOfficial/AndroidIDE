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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;

/**
 * Finds related info for any error code
 */
public class AggregateRelatedInfoFinder implements IRelatedInfoFinder {

	private static IRelatedInfoFinder[] RELATED_INFO_FINDERS = {
		new XMLSyntaxRelatedInfoFinder()
	};

	private static AggregateRelatedInfoFinder INSTANCE = null;

	private AggregateRelatedInfoFinder() {}

	public static AggregateRelatedInfoFinder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AggregateRelatedInfoFinder();
		}
		return INSTANCE;
	}

	@Override
	public List<DiagnosticRelatedInformation> findRelatedInformation(
			int offset,
			String errorKey,
			DOMDocument document) {
		List<DiagnosticRelatedInformation> relatedInfo = new ArrayList<>();
		for (IRelatedInfoFinder relatedInfoFinder : RELATED_INFO_FINDERS) {
			relatedInfo.addAll(relatedInfoFinder.findRelatedInformation(
				offset,
				errorKey,
				document
			));
		}
		return relatedInfo;
	}

}
