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
package org.eclipse.lemminx.dom;

import org.w3c.dom.CDATASection;

/**
 * A CData section node.
 *
 */
public class DOMCDATASection extends DOMText implements CDATASection {

	int startContent;
	int endContent;

	public DOMCDATASection(int start, int end) {
		super(start, end);
	}

	@Override
	public int getStartContent() {
		return startContent;
	}

	@Override
	public int getEndContent() {
		return endContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.w3c.dom.Text#getNodeType()
	 */
	@Override
	public short getNodeType() {
		return DOMNode.CDATA_SECTION_NODE;
	}
}
