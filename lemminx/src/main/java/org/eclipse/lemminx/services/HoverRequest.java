/**
 *  Copyright (c) 2018 Angelo ZERR
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
package org.eclipse.lemminx.services;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.dom.DOMNode;
import org.eclipse.lemminx.services.extensions.IHoverRequest;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lemminx.settings.XMLHoverSettings;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

/**
 * Hover request implementation.
 *
 */
class HoverRequest extends AbstractPositionRequest implements IHoverRequest {

	private final SharedSettings settings;

	private Range hoverRange;

	private boolean open;

	public HoverRequest(DOMDocument xmlDocument, Position position, SharedSettings settings,
			XMLExtensionsRegistry extensionsRegistry) throws BadLocationException {
		super(xmlDocument, position, extensionsRegistry);
		this.settings = settings;
	}

	@Override
	protected DOMNode findNodeAt(DOMDocument xmlDocument, int offset) {
		DOMNode node = xmlDocument.findNodeAt(offset);
		if (node != null && node.isElement()) {
			DOMAttr attr = DOMNode.findAttrAt(node, offset);
			if (attr != null) {
				return attr;
			}
		}
		return node;
	}

	@Override
	public Range getHoverRange() {
		return hoverRange;
	}

	public void setHoverRange(Range hoverRange) {
		this.hoverRange = hoverRange;
	}

	@Override
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public boolean canSupportMarkupKind(String kind) {
		XMLHoverSettings hoverSettings = settings.getHoverSettings();
		return settings != null && hoverSettings.getCapabilities() != null
				&& hoverSettings.getCapabilities().getContentFormat() != null
				&& hoverSettings.getCapabilities().getContentFormat().contains(kind);
	}

	@Override
	public SharedSettings getSharedSettings() {
		return settings;
	}
}
