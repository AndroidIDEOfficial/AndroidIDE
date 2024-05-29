/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.lemminx.commons;

/**
 * Indicates the attempt to access a non-existing position. The attempt has been
 * performed on a text store such as a document or string.
 * <p>
 * This class is not intended to be serialized.
 * </p>
 * 
 *  This class is a copy/paste of org.eclipse.jface.text.BadLocationException.
 */
public class BadLocationException extends Exception {

	/**
	 * Serial version UID for this class.
	 * <p>
	 * Note: This class is not intended to be serialized.
	 * </p>
	 * 
	 * @since 3.1
	 */
	private static final long serialVersionUID = 3257281452776370224L;

	/**
	 * Creates a new bad location exception.
	 */
	public BadLocationException() {
		super();
	}

	/**
	 * Creates a new bad location exception.
	 *
	 * @param message the exception message
	 */
	public BadLocationException(String message) {
		super(message);
	}
}
