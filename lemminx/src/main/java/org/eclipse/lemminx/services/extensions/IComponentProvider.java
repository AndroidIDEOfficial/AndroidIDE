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

import org.eclipse.lemminx.services.XMLLanguageService;

/**
 * Component provider API to register utilities class for a given XML language
 * service {@link XMLLanguageService}
 *
 */
public interface IComponentProvider {

	/**
	 * Returns the component class instance from the given class and null otherwise.
	 * 
	 * @param clazz class of the component.
	 * @return the component class instance from the given class and null otherwise.
	 */
	<T> T getComponent(@SuppressWarnings("rawtypes") Class clazz);
}
