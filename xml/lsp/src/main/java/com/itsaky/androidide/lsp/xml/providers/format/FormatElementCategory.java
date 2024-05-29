/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*******************************************************************************
* Copyright (c) 2022 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package com.itsaky.androidide.lsp.xml.providers.format;

/**
 * Format element catagory.
 * 
 * @author Angelo ZERR
 * 
 * @see https://www.oxygenxml.com/doc/versions/24.0/ug-editorEclipse/topics/format-and-indent-xml.html
 */
public enum FormatElementCategory {

	/**
	 * In the Ignore space category, all whitespace is considered insignificant.
	 * This generally applies to content that consists only of elements nested
	 * inside other elements, with no text content.
	 */
	IgnoreSpace,

	/**
	 * In the normalize space category, a single whitespace character between
	 * character strings is considered significant and all other spaces are
	 * considered insignificant. Therefore, all consecutive whitespaces will be
	 * replaced with a single space. This generally applies to elements that contain
	 * text content only.
	 */
	NormalizeSpace,

	/**
	 * In the mixed content category, a single whitespace between text characters is
	 * considered significant and all other spaces are considered insignificant.
	 */
	MixedContent,

	/**
	 * In the preserve space category, all whitespace in the element is regarded as
	 * significant. No changes are made to the spaces in elements in this category.
	 * However, child elements may be in another category, and may be treated
	 * differently.
	 * 
	 * Attribute values are always in the preserve space category. The spaces
	 * between attributes in an element tag are always in the default space
	 * category.
	 * 
	 */
	PreserveSpace
}
