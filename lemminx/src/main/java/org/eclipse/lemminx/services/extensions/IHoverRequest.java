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
package org.eclipse.lemminx.services.extensions;

import org.eclipse.lsp4j.Range;

/**
 * Hover request API.
 *
 */
public interface IHoverRequest extends IPositionRequest, ISharedSettingsRequest {

	/**
	 * Returns the hover range and null otherwise.
	 * 
	 * @return the hover range and null otherwise.
	 */
	Range getHoverRange();

	/**
	 * Returns true if hovered tag is opened and false otherwise.
	 * 
	 * @return true if hovered tag is opened and false otherwise.
	 */
	boolean isOpen();
}
