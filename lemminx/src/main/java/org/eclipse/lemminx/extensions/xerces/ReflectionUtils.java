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
package org.eclipse.lemminx.extensions.xerces;

import java.lang.reflect.Field;

/**
 * Java reflection utilities. In LemMinx context (with LemMinx binary context),
 * Java reflection should be forbidden. But to fix some Xerces bugs and improve
 * some Xerces feature, Java reflection is required to get access to some field
 * which are private. It's one reason why this {@link ReflectionUtils} is in the
 * xerces package and not in the org.eclipse.lemminx.utils package.
 *
 */
public class ReflectionUtils {

	/**
	 * Returns the field value of the given instance and field name.
	 * 
	 * @param <T>      the field value type.
	 * @param instance the instance.
	 * @param name     the field name.
	 * @return the field value of the given instance and field name.
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> T getFieldValue(Object instance, String name)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = getField(instance.getClass(), name);
		f.setAccessible(true);
		return (T) f.get(instance);
	}

	private static Field getField(Class<? extends Object> clazz, String name) throws NoSuchFieldException {
		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				return getField(superclass, name);
			} else {
				throw e;
			}
		}
	}

}
