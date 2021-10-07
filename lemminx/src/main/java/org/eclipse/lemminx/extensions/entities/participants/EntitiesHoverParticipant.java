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
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils.EntityOriginType;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils.PredefinedEntity;
import org.eclipse.lemminx.services.extensions.HoverParticipantAdapter;
import org.eclipse.lemminx.services.extensions.IHoverRequest;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lemminx.utils.XMLPositionUtility.EntityReferenceRange;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Range;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;

/**
 * Entities hover used in a text node (ex : &amp;).
 *
 */
public class EntitiesHoverParticipant extends HoverParticipantAdapter {

	@Override
	public Hover onText(IHoverRequest request) throws Exception {
		DOMNode node = request.getNode();
		if (!node.isText()) {
			return null;
		}
		// Hover is done in a text node, check if it's a entity reference
		DOMDocument document = request.getXMLDocument();
		int offset = request.getOffset();
		EntityReferenceRange entityRange = XMLPositionUtility.selectEntityReference(offset, document);
		if (entityRange == null) {
			return null;
		}
		// The hovered text follows the entity reference syntax (ex : &amp;)
		String entityName = entityRange.getName();
		Range range = entityRange.getRange();
		// Try to find the entity
		MarkupContent entityContents = searchInEntities(entityName, range, document, request);
		if (entityContents != null) {
			return new Hover(entityContents, range);
		}
		return null;
	}

	/**
	 * Returns the markup content of the given entity name in the predefined, local
	 * and external entities and null otherwise.
	 * 
	 * @param entityName  the entity name to search.
	 * @param entityRange the hovered range.
	 * @param document    the DOM document
	 * @param request     the hover request.
	 * @return the markup content of the given entity name in the predefined, local
	 *         and external entities and null otherwise.
	 */
	private static MarkupContent searchInEntities(String entityName, Range entityRange, DOMDocument document,
			IHoverRequest request) {
		MarkupContent entityContents = searchInPredefinedEntities(entityName, entityRange, document, request);
		if (entityContents != null) {
			return entityContents;
		}
		entityContents = searchInLocalEntities(entityName, entityRange, document, request);
		if (entityContents != null) {
			return entityContents;
		}
		return searchInExternalEntities(entityName, entityRange, document, request);
	}

	/**
	 * Returns the markup content of the given entity name in the predefined
	 * entities and null otherwise.
	 * 
	 * @param entityName  the entity name to search.
	 * @param entityRange the hovered range.
	 * @param document    the DOM document
	 * @param request     the hover request.
	 * @return the markup content of the given entity name in the predefined
	 *         entities and null otherwise.
	 */
	private static MarkupContent searchInPredefinedEntities(String entityName, Range entityRange, DOMDocument document,
			IHoverRequest request) {
		PredefinedEntity[] entities = PredefinedEntity.values();
		for (PredefinedEntity entity : entities) {
			if (entityName.equals(entity.getName())) {
				boolean markdown = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
				return EntitiesDocumentationUtils.getDocumentation(entity.getName(), entity.getValue(),
						EntityOriginType.PREDEFINED, markdown);
			}
		}
		return null;
	}

	/**
	 * Returns the markup content of the given entity name in the local entities and
	 * null otherwise.
	 * 
	 * @param entityName  the entity name to search.
	 * @param entityRange the hovered range.
	 * @param document    the DOM document
	 * @param request     the hover request.
	 * @return the markup content of the given entity name in the local entities and
	 *         null otherwise.
	 */
	private static MarkupContent searchInLocalEntities(String entityName, Range entityRange, DOMDocument document,
			IHoverRequest request) {
		DOMDocumentType docType = document.getDoctype();
		if (docType == null) {
			return null;
		}
		// Loop for entities declared in the DOCTYPE of the document
		NamedNodeMap entities = docType.getEntities();
		for (int i = 0; i < entities.getLength(); i++) {
			DTDEntityDecl entity = (DTDEntityDecl) entities.item(i);
			if (entityName.equals(entity.getName())) {
				boolean markdown = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
				return EntitiesDocumentationUtils.getDocumentation(entity, EntityOriginType.LOCAL, markdown);
			}
		}
		return null;
	}

	/**
	 * Returns the markup content of the given entity name in the external entities
	 * and null otherwise.
	 * 
	 * @param entityName  the entity name to search.
	 * @param entityRange the hovered range.
	 * @param document    the DOM document
	 * @param request     the hover request.
	 * @return the markup content of the given entity name in the external entities
	 *         and null otherwise.
	 */
	private static MarkupContent searchInExternalEntities(String entityName, Range entityRange, DOMDocument document,
			IHoverRequest request) {
		ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
		Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(document, null, false);
		for (CMDocument cmDocument : cmDocuments) {
			List<Entity> entities = cmDocument.getEntities();
			for (Entity ent : entities) {
				DTDEntityDecl entity = (DTDEntityDecl) ent;
				if (entityName.equals(entity.getName())) {
					boolean markdown = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
					return EntitiesDocumentationUtils.getDocumentation(entity, EntityOriginType.EXTERNAL, markdown);
				}
			}
		}
		return null;
	}

}
