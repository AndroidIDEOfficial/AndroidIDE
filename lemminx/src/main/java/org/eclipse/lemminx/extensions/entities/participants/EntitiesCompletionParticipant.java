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
import org.eclipse.lemminx.dom.DTDEntityDecl;
import org.eclipse.lemminx.extensions.contentmodel.model.CMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils.EntityOriginType;
import org.eclipse.lemminx.extensions.entities.EntitiesDocumentationUtils.PredefinedEntity;
import org.eclipse.lemminx.services.extensions.CompletionParticipantAdapter;
import org.eclipse.lemminx.services.extensions.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.ICompletionResponse;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lemminx.utils.XMLPositionUtility.EntityReferenceRange;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;

/**
 * Entities completion used in a text node (ex : &amp;).
 *
 */
public class EntitiesCompletionParticipant extends CompletionParticipantAdapter {

	@Override
	public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
		EntityReferenceRange entityRange = XMLPositionUtility.selectEntityReference(request.getOffset(),
				request.getXMLDocument(), false);
		if (entityRange == null) {
			return;
		}
		Range range = entityRange.getRange();
		// There is the '&' character before the offset where completion was triggered
		boolean markdown = request.canSupportMarkupKind(MarkupKind.MARKDOWN);
		DOMDocument document = request.getXMLDocument();
		collectLocalEntityProposals(document, range, markdown, response);
		collectExternalEntityProposals(document, range, markdown, request, response);
		collectPredefinedEntityProposals(range, markdown, response);
	}

	/**
	 * Collect local entities declared in the DOCTYPE.
	 * 
	 * @param document    the DOM document.
	 * @param entityRange the entity range.
	 * @param markdown    true if the documentation can be formatted as markdown and
	 *                    false otherwise.
	 * @param response    the completion response.
	 */
	private static void collectLocalEntityProposals(DOMDocument document, Range entityRange, boolean markdown,
			ICompletionResponse response) {
		DOMDocumentType docType = document.getDoctype();
		if (docType == null) {
			return;
		}
		NamedNodeMap entities = docType.getEntities();
		for (int i = 0; i < entities.getLength(); i++) {
			Entity entity = (Entity) entities.item(i);
			if (entity.getNodeName() != null) {
				// provide completion for the locally declared entity
				MarkupContent documentation = EntitiesDocumentationUtils.getDocumentation((DTDEntityDecl) entity,
						EntityOriginType.LOCAL, markdown);
				fillCompletion(entity.getNodeName(), documentation, entityRange, response);
			}
		}
	}

	/**
	 * Collect external entities.
	 * 
	 * @param document    the DOM document.
	 * @param entityRange the entity range.
	 * @param markdown    true if the documentation can be formatted as markdown and
	 *                    false otherwise.
	 * @param request     the completion request.
	 * @param response    the completion response.
	 */
	private static void collectExternalEntityProposals(DOMDocument document, Range entityRange, boolean markdown,
			ICompletionRequest request, ICompletionResponse response) {
		ContentModelManager contentModelManager = request.getComponent(ContentModelManager.class);
		Collection<CMDocument> cmDocuments = contentModelManager.findCMDocument(document, null, false);
		for (CMDocument cmDocument : cmDocuments) {
			List<Entity> entities = cmDocument.getEntities();
			for (Entity entity : entities) {
				if (entity.getNodeName() != null) {
					// provide completion for the external declared entity
					MarkupContent documentation = EntitiesDocumentationUtils.getDocumentation((DTDEntityDecl) entity,
							EntityOriginType.EXTERNAL, markdown);
					fillCompletion(entity.getNodeName(), documentation, entityRange, response);
				}
			}
		}
	}

	/**
	 * Collect predefined entities.
	 * 
	 * @param entityRange the entity range.
	 * @param markdown    true if the documentation can be formatted as markdown and
	 *                    false otherwise.
	 * @param response    the completion response.
	 * 
	 * @see https://www.w3.org/TR/xml/#sec-predefined-ent
	 */
	private void collectPredefinedEntityProposals(Range entityRange, boolean markdown, ICompletionResponse response) {
		PredefinedEntity[] entities = PredefinedEntity.values();
		for (PredefinedEntity entity : entities) {
			MarkupContent documentation = EntitiesDocumentationUtils.getDocumentation(entity.getName(),
					entity.getValue(), EntityOriginType.PREDEFINED, markdown);
			fillCompletion(entity.getName(), documentation, entityRange, response);
		}
	}

	private static void fillCompletion(String name, MarkupContent documentation, Range entityRange,
			ICompletionResponse response) {
		String entityName = "&" + name + ";";
		CompletionItem item = new CompletionItem();
		item.setLabel(entityName);
		item.setKind(CompletionItemKind.Keyword);
		item.setInsertTextFormat(InsertTextFormat.PlainText);
		String insertText = entityName;
		item.setFilterText(insertText);
		item.setTextEdit(Either.forLeft(new TextEdit(entityRange, insertText)));
		item.setDocumentation(documentation);
		response.addCompletionItem(item);
	}

}
