/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.xsi.participants;

import org.eclipse.lemminx.dom.DOMAttr;
import org.eclipse.lemminx.extensions.xsi.XSISchemaModel;
import org.eclipse.lemminx.services.extensions.HoverParticipantAdapter;
import org.eclipse.lemminx.services.extensions.IHoverRequest;
import org.eclipse.lsp4j.Hover;

/**
 * XSIHoverParticipant
 */
public class XSIHoverParticipant extends HoverParticipantAdapter {

	@Override
	public Hover onAttributeName(IHoverRequest request) throws Exception {
		DOMAttr attribute = (DOMAttr) request.getNode();
		return XSISchemaModel.computeHoverResponse(attribute, request);
	}

}