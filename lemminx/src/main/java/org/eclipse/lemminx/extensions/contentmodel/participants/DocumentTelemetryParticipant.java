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

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.model.ContentModelManager;
import org.eclipse.lemminx.services.extensions.IDocumentLifecycleParticipant;
import org.eclipse.lemminx.telemetry.TelemetryManager;

public class DocumentTelemetryParticipant implements IDocumentLifecycleParticipant {

	private TelemetryManager telemetryManager;

	private ContentModelManager contentManager;

	public DocumentTelemetryParticipant(TelemetryManager telemetryManager, ContentModelManager contentManager) {
		this.telemetryManager = telemetryManager;
		this.contentManager = contentManager;
	}

	@Override
	public void didOpen(DOMDocument document) {
		telemetryManager.onDidOpen(document, contentManager);
	}

	@Override
	public void didChange(DOMDocument document) {}

	@Override
	public void didSave(DOMDocument document) {}

	@Override
	public void didClose(DOMDocument document) {}

}
