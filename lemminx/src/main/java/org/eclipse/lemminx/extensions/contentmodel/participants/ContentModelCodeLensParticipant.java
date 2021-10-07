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

package org.eclipse.lemminx.extensions.contentmodel.participants;

import static org.eclipse.lemminx.client.ClientCommands.OPEN_BINDING_WIZARD;
import static org.eclipse.lemminx.client.ClientCommands.OPEN_URI;
import static org.eclipse.lemminx.extensions.contentmodel.commands.CheckBoundGrammarCommand.canBindWithGrammar;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.lemminx.client.CodeLensKind;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMRange;
import org.eclipse.lemminx.extensions.contentmodel.commands.AssociateGrammarCommand;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.extensions.contentmodel.model.ReferencedGrammarInfo;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensParticipant;
import org.eclipse.lemminx.services.extensions.codelens.ICodeLensRequest;
import org.eclipse.lemminx.utils.DOMUtils;
import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lemminx.utils.XMLPositionUtility;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * At first this participant is enabled only when LSP client can support the
 * client command "xml.open.binding.wizard" to open the wizard to bind a XML to
 * a grammar/schema.
 *
 * In this case, when XML file is not associated to a grammar/schema (DTD, XSD),
 * this class generates [Bind to grammar/schema...] CodeLens on the root of the
 * DOM Document:
 *
 * On client side, click on this Codelens should open a wizard:
 *
 * <ul>
 * <li>page1 : display a combo to select the binding type ("standard",
 * "xml-model").</li>
 * <li>page2: open a file dialog to select the grammar/schema (XSD/DTD) to
 * bind.</li>
 * <li>the finish wizard should consume the "xml.associate.grammar.insert"
 * command {@link AssociateGrammarCommand} to generate the proper syntax for
 * binding with following parameters:
 * <ul>
 * <li>the document uri.</li>
 * <li>the selected grammar/schema file uri.</li>
 * <li>the binding type.</li>
 * </ul>
 * In other words the "xml.associate.grammar.insert" returns the
 * {@link TextDocumentEdit} which must be applied on the LSP client side.
 * </p>
 *
 */
public class ContentModelCodeLensParticipant implements ICodeLensParticipant {

	private final ContentModelManager contentModelManager;

	public ContentModelCodeLensParticipant(ContentModelManager contentModelManager) {
		this.contentModelManager = contentModelManager;
	}

	@Override
	public void doCodeLens(ICodeLensRequest request, List<CodeLens> lenses, CancelChecker cancelChecker) {
		// List of referenced schema/grammar
		createReferencedGrammarLenses(request, lenses);
		// "Bind to grammar/schema..."
		createBindToGrammarSchemaLenses(request, lenses);
	}

	private void createReferencedGrammarLenses(ICodeLensRequest request, List<CodeLens> lenses) {
		DOMDocument document = request.getDocument();
		if (!document.hasGrammar()) {
			return;
		}

		// The DOM document is bound with a schema/grammar, display the referenced
		// grammars as Codelens.
		boolean canSupportOpenUri = canSupportOpenUri(request);
		Range range = XMLPositionUtility.createRange((DOMRange) document.getFirstChild());
		range.setEnd(range.getStart());
		Set<ReferencedGrammarInfo> referencedGrammarInfos = contentModelManager.getReferencedGrammarInfos(document);
		for (ReferencedGrammarInfo info : referencedGrammarInfos) {
			lenses.add(createReferencedGrammarLens(info, range, canSupportOpenUri));
		}
	}

	private static boolean canSupportOpenUri(ICodeLensRequest request) {
		return request.isSupportedByClient(CodeLensKind.OpenUri);
	}

	private CodeLens createReferencedGrammarLens(ReferencedGrammarInfo info, Range range, boolean canSupportOpenUri) {
		String uri = info.getIdentifierURI();
		StringBuilder title = new StringBuilder(uri != null ? uri : "");
		String bindingKind = ReferencedGrammarInfo.getBindingKindAndResolvedBy(info);
		if (!StringUtils.isEmpty(bindingKind)) {
			title.append(" (");
			title.append(bindingKind);
			title.append(")");
		}

		String grammarURI = info.isInCache() ? info.getGrammarCacheInfo().getCachedResolvedUri() : null;
		if (grammarURI == null) {
			grammarURI = info.getResolvedURIInfo().getResolvedURI();
		}
		Command command = new Command(title.toString(), canSupportOpenUri ? OPEN_URI : "",
				canSupportOpenUri ? Arrays.asList(grammarURI) : null);
		return new CodeLens(range, command, null);
	}

	private static void createBindToGrammarSchemaLenses(ICodeLensRequest request, List<CodeLens> lenses) {
		if (!canSupportAssociation(request)) {
			return;
		}

		// The LSP client can support Association, when DOM document is not bound to a
		// grammar, [Bind to grammar/schema...] CodeLens appears:

		// [Bind to grammar/schema...]
		// <foo />

		DOMDocument document = request.getDocument();
		if (!canBindWithGrammar(document)) {
			return;
		}
		String documentURI = document.getDocumentURI();
		Range range = XMLPositionUtility.selectRootStartTag(document);

		lenses.add(createAssociateLens(documentURI, "Bind to grammar/schema...", range));
	}

	private static boolean canSupportAssociation(ICodeLensRequest request) {
		if (!request.isSupportedByClient(CodeLensKind.Association)) {
			return false;
		}
		String uri = request.getDocument().getDocumentURI();
		return !DOMUtils.isXSD(uri) && !DOMUtils.isDTD(uri);
	}

	private static CodeLens createAssociateLens(String documentURI, String title, Range range) {
		Command command = new Command(title, OPEN_BINDING_WIZARD, Arrays.asList(documentURI));
		return new CodeLens(range, command, null);
	}

}
