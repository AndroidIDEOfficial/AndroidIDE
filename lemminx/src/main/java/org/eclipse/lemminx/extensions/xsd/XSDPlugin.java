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
package org.eclipse.lemminx.extensions.xsd;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelProvider;
import org.eclipse.lemminx.extensions.xsd.contentmodel.CMXSDContentModelProvider;
import org.eclipse.lemminx.extensions.xsd.participants.XSDCodeLensParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDCompletionParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDDefinitionParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDDocumentLinkParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDHighlightingParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDReferenceParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.XSDRenameParticipant;
import org.eclipse.lemminx.extensions.xsd.participants.diagnostics.XSDDiagnosticsParticipant;
import org.eclipse.lemminx.services.extensions.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.IDefinitionParticipant;
import org.eclipse.lemminx.services.extensions.IDocumentLinkParticipant;
import org.eclipse.lemminx.services.extensions.IHighlightingParticipant;
import org.eclipse.lemminx.services.extensions.IReferenceParticipant;
import org.eclipse.lemminx.services.extensions.IRenameParticipant;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lsp4j.InitializeParams;

/**
 * XSD plugin.
 */
public class XSDPlugin implements IXMLExtension {

	private final ICompletionParticipant completionParticipant;

	private final IDefinitionParticipant definitionParticipant;

	private final IDiagnosticsParticipant diagnosticsParticipant;

	private final IReferenceParticipant referenceParticipant;
	private final ICodeLensParticipant codeLensParticipant;
	private final IHighlightingParticipant highlightingParticipant;
	private final IRenameParticipant renameParticipant;
	private final IDocumentLinkParticipant documentLinkParticipant;
	private XSDURIResolverExtension uiResolver;

	private ContentModelManager contentModelManager;

	public XSDPlugin() {
		completionParticipant = new XSDCompletionParticipant();
		definitionParticipant = new XSDDefinitionParticipant();
		diagnosticsParticipant = new XSDDiagnosticsParticipant(this);
		referenceParticipant = new XSDReferenceParticipant();
		codeLensParticipant = new XSDCodeLensParticipant();
		highlightingParticipant = new XSDHighlightingParticipant();
		renameParticipant = new XSDRenameParticipant();
		documentLinkParticipant = new XSDDocumentLinkParticipant();
	}

	@Override
	public void doSave(ISaveContext context) {
		String documentURI = context.getUri();
		DOMDocument document = context.getDocument(documentURI);
		if (DOMUtils.isXSD(document)) {
			context.collectDocumentToValidate(d -> {
				DOMDocument xml = context.getDocument(d.getDocumentURI());
				return contentModelManager.dependsOnGrammar(xml, context.getUri());
			});
		}
	}

	@Override
	public void start(InitializeParams params, XMLExtensionsRegistry registry) {
		// Register resolver
		uiResolver = new XSDURIResolverExtension(registry.getDocumentProvider());
		registry.getResolverExtensionManager().registerResolver(uiResolver);
		// register XSD content model provider
		ContentModelProvider modelProvider = new CMXSDContentModelProvider(registry.getResolverExtensionManager());
		contentModelManager = registry.getComponent(ContentModelManager.class);
		contentModelManager.registerModelProvider(modelProvider);
		// register completion, diagnostic participant
		registry.registerCompletionParticipant(completionParticipant);
		registry.registerDefinitionParticipant(definitionParticipant);
		registry.registerDiagnosticsParticipant(diagnosticsParticipant);
		registry.registerReferenceParticipant(referenceParticipant);
		registry.registerCodeLensParticipant(codeLensParticipant);
		registry.registerHighlightingParticipant(highlightingParticipant);
		registry.registerRenameParticipant(renameParticipant);
		registry.registerDocumentLinkParticipant(documentLinkParticipant);
	}

	@Override
	public void stop(XMLExtensionsRegistry registry) {
		registry.getResolverExtensionManager().unregisterResolver(uiResolver);
		registry.unregisterCompletionParticipant(completionParticipant);
		registry.unregisterDefinitionParticipant(definitionParticipant);
		registry.unregisterDiagnosticsParticipant(diagnosticsParticipant);
		registry.unregisterReferenceParticipant(referenceParticipant);
		registry.unregisterCodeLensParticipant(codeLensParticipant);
		registry.unregisterHighlightingParticipant(highlightingParticipant);
		registry.unregisterRenameParticipant(renameParticipant);
		registry.unregisterDocumentLinkParticipant(documentLinkParticipant);
	}
	
	public ContentModelManager getContentModelManager() {
		return contentModelManager;
	}
}
