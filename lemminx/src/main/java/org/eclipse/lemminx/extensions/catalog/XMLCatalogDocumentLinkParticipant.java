/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/

package org.eclipse.lemminx.extensions.catalog;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xerces.impl.XMLEntityManager;
import org.apache.xerces.util.URI.MalformedURIException;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.utils.FilesUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.DocumentLink;

/**
 * Document links that are specific to catalogs
 */
public class XMLCatalogDocumentLinkParticipant implements IDocumentLinkParticipant {

	private static Logger LOGGER = Logger.getLogger(XMLCatalogDocumentLinkParticipant.class.getName());

	@Override
	public void findDocumentLinks(DOMDocument document, List<DocumentLink> links) {
		for (CatalogEntry catalogEntry : CatalogUtils.getCatalogEntries(document)) {
			DocumentLink docLink = createDocumentLinkFromCatalogEntry(document, catalogEntry);
			if (docLink != null) {
				links.add(docLink);
			}
		}
	}

	/**
	 * Return a DocumentLink for the given CatalogEntry or null if this fails
	 *
	 * @param document     The document that the attribute belongs to
	 * @param catalogEntry The CatalogEntry that should be turned into a
	 *                     DocumentLink
	 * @return a document link that links the CatalogEntry to the external document
	 *         it references or null otherwise
	 */
	private static DocumentLink createDocumentLinkFromCatalogEntry(DOMDocument document, CatalogEntry catalogEntry) {
		try {
			String path = getResolvedLocation(FilesUtils.removeFileScheme(document.getDocumentURI()),
					catalogEntry.getResolvedURI());
			if (path != null && catalogEntry.getLinkRange() != null) {
				return XMLPositionUtility.createDocumentLink(catalogEntry.getLinkRange(), path, true);
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Creation of document link failed", e);
		}
		return null;
	}

	/**
	 * Returns the expanded system location
	 *
	 * @return the expanded system location
	 */
	private static String getResolvedLocation(String documentURI, String location) {
		if (StringUtils.isBlank(location)) {
			return null;
		}
		try {
			return XMLEntityManager.expandSystemId(location, documentURI, false);
		} catch (MalformedURIException e) {
			return location;
		}
	}

}