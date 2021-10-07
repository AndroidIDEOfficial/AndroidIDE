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
package org.eclipse.lemminx.settings;

/**
 * SchemaDocumentationType enum
 * 
 * Defines enums managing the which XSD element content to display
 * as documentation
 * 
 * {@link SchemaDocumentationType#documentation} XSD documentation element
 * {@link SchemaDocumentationType#appinfo} XSD appinfo element
 * {@link SchemaDocumentationType#all} XSD documentation and appinfo elements
 * {@link SchemaDocumentationType#none} none
 */
public enum SchemaDocumentationType {
	documentation,
	appinfo,
	all,
	none
}