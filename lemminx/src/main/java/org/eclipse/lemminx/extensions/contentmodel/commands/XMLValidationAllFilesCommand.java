/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.commands;

import java.util.Collection;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.services.IXMLDocumentProvider;
import org.eclipse.lemminx.services.IXMLValidationService;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService.IDelegateCommandHandler;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML Command "xml.validation.all.files" to revalidate all opened XML files
 * which means:
 * 
 * <ul>
 * <li>clear the Xerces grammar pool (used by the Xerces validation) and the
 * content model documents cache (used by the XML completion/hover based on the
 * grammar)</li>
 * <li>trigger the validation for the all opened XML files</li>
 * </ul>
 * 
 * @author Angelo ZERR
 *
 */
public class XMLValidationAllFilesCommand implements IDelegateCommandHandler {

	public static final String COMMAND_ID = "xml.validation.all.files";

	private final ContentModelManager contentModelManager;

	private final IXMLDocumentProvider documentProvider;

	private final IXMLValidationService validationService;

	public XMLValidationAllFilesCommand(ContentModelManager contentModelManager, IXMLDocumentProvider documentProvider,
			IXMLValidationService validationService) {
		this.contentModelManager = contentModelManager;
		this.documentProvider = documentProvider;
		this.validationService = validationService;
	}

	@Override
	public Object executeCommand(ExecuteCommandParams params, SharedSettings sharedSettings,
			CancelChecker cancelChecker) throws Exception {
		// 1. clear the Xerces grammar pool
		// (used by the Xerces validation) and the content model documents cache (used
		// by the XML completion/hover based on the grammar)
		contentModelManager.evictCache();
		// 2. trigger the validation for the all opened XML files
		Collection<DOMDocument> all = documentProvider.getAllDocuments();
		for (DOMDocument document : all) {
			validationService.validate(document);
		}
		return null;
	}

}