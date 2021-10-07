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
package org.eclipse.lemminx.services.extensions.commands;

import java.util.List;

import org.eclipse.lemminx.utils.JSONUtility;
import org.eclipse.lsp4j.ExecuteCommandParams;

/**
 * Arguments utilities.
 * 
 * @author Angelo ZERR
 *
 */
public class ArgumentsUtils {

	/**
	 * Returns the object from the given index and null otherwise.
	 * 
	 * @param arguments the argument list.
	 * @param index     the index.
	 * 
	 * @return the object from the given index and null otherwise.
	 */
	public static Object getArgAt(List<Object> arguments, int index) {
		if (arguments == null || index >= arguments.size()) {
			return null;
		}
		return arguments.get(index);
	}

	/**
	 * Returns the object from the given index as a given class type and null
	 * otherwise.
	 * 
	 * @param <T>    the class type
	 * @param params the execute command parameters.
	 * @param index  the index
	 * @param clazz  the class type.
	 * @return the object from the given index as a given class type and null
	 *         otherwise.
	 * @throws UnsupportedOperationException if the object from the given index
	 *                                       cannot be retrieved from the params.
	 */
	public static <T> T getArgAt(ExecuteCommandParams params, int index, Class<T> clazz) {
		Object obj = getArgAt(params.getArguments(), index);
		if (obj == null) {
			throw new UnsupportedOperationException(String.format("Command '%s' must be called with '%s' argument(s)!",
					params.getCommand(), index + 1));
		}
		return JSONUtility.toModel(obj, clazz);
	}

}
