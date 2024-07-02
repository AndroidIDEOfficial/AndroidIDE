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

/*
 * Copyright (c) 2018 Angelo ZERR All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * <p>SPDX-License-Identifier: EPL-2.0
 *
 * <p>Contributors: Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */

package com.itsaky.androidide.lsp.xml.utils;

import com.itsaky.androidide.lsp.xml.models.XMLServerSettings;

import org.eclipse.lemminx.dom.builder.BaseXmlFormattingOptions;
import org.eclipse.lemminx.dom.builder.XmlBuilder;

/**
 * XML content builder utilities.
 */
public class XMLBuilder extends XmlBuilder {

  public XMLBuilder(String whitespacesIndent, String lineDelimiter) {
    this(whitespacesIndent, lineDelimiter, XMLServerSettings.INSTANCE.getFormattingOptions());
  }

  public XMLBuilder(String whitespacesIndent, String lineDelimiter, BaseXmlFormattingOptions settings
  ) {
    super(whitespacesIndent, lineDelimiter, settings);
  }
}
