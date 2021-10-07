/**
 *  Copyright (c) 2020 Red Hat, Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Red Hat Inc. - initial API and implementation
 */
package org.eclipse.lemminx.extensions.entities.participants;

import java.util.Collection;
import java.util.List;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMDocumentType;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.dom.DTDEntityDecl;
import org.eclipse.lemminx.dom.TargetRange;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.services.extensions.AbstractDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionRequest;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lemminx.utils.XMLPositionUtility.EntityReferenceRange;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;

/**
 * Entities definition used in a text node (ex : &amp;).
 *
 */
public class EntitiesDefinitionParticipant extends AbstractDefinitionParticipant {

	@Override
	protected boolean match(DOMDocument document) {
		return true;
	}

	@Override
	protected void doFindDefinition(IDefinitionRequest request, List<LocationLink> locations,
			CancelChecker cancelChecker) {
		DOMNode node = request.getNode();
		if (!node.isText()) {
			return;
		}
		// Definition is done in a text node, check if it's a entity reference
		DOMDocument document = request.getXMLDocument();
		int offset = request.getOffset();
		EntityReferenceRange entityRange = XMLPositionUtility.selectEntityReference(offset, document);
		if (entityRange != null) {
			String entityName = entityRange.getName();
			Range range = entityRange.getRange();
			searchInLocalEntities(entityName, range, document, locations, cancelChecker);
			searchInExternalEntities(entityName, range, document, locations, request, cancelChecker);
		}
	}

	/**
	 * Search the given entity name in the local entities.
	 * 
	 * @param document      the DOM document.
	 * @param entityName    the entity name.
	 * @param entityRange   the entity range.
	 * @param locations     the location links
	 * @param cancelChecker the cancel checker.
	 */
	private static void searchInLocalEntities(String entityName, Range entityRange, DOMDocument document,
			List<LocationLink> locations, CancelChecker cancelChecker) {
		DOMDocumentType docType = document.getDoctype();
		if (docType == null) {
			return;
		}
		cancelChecker.checkCanceled();
		// Loop for entities declared in the DOCTYPE of the document
		NamedNodeMap entities = docType.getEntities();
		for (int i = 0; i < entities.getLength(); i++) {
			cancelChecker.checkCanceled();
			DTDEntityDecl entity = (DTDEntityDecl) entities.item(i);
			fillEntityLocation(entity, entityName, entityRange, locations);
		}
	}

	/**
	 * Search the given entity name in the external entities.
	 * 
	 * @param document      the DOM document.
	 * @param entityName    the entity name.
	 * @param entityRange   the entity range.
	 * @param locations     the location links
	 * @param request       the definition request.
	 * @param cancelChecker the cancel checker.
	 */
	private static void searchInExternalEntities(String entityName, Range entityRange, DOMDocument document,
			List<LocationLink> locations, IDefinitionRequest request, CancelChecker cancelChecker) {
		ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
		Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(document, null, false);
		for (CMDocument cmDocument : cmDocuments) {
			List<Entity> entities = cmDocument.getEntities();
			for (Entity entity : entities) {
				fillEntityLocation((DTDEntityDecl) entity, entityName, entityRange, locations);
			}
		}
	}

	private static void fillEntityLocation(DTDEntityDecl entity, String entityName, Range entityRange,
			List<LocationLink> locations) {
		if (entityName.equals(entity.getName())) {
			TargetRange name = entity.getNameParameter();
			locations.add(XMLPositionUtility.createLocationLink(entityRange, name));
		}
	}

}
