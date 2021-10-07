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
package org.eclipse.lemminx.extensions.generators;

import org.eclipse.lemminx.services.IXMLFullFormatter;
import org.eclipse.lemminx.settings.SharedSettings;

/**
 * File content generator API.
 *
 * @param <Source>   the source document (ex : XML)
 * @param <Settings> generator settings class.
 */
public interface IFileContentGenerator<Source, Settings extends FileContentGeneratorSettings> {

	/**
	 * Generates a file content (ex : XSD, DTD) from the given document source
	 * <code>document</code> (ex: XML) by using the given settings
	 * <code>generatorSettings</code>.
	 * 
	 * @param document          the XML document source.
	 * @param sharedSettings    the shared settings.
	 * @param generatorSettings the generator settings.
	 * @param formatter         the formatter.
	 * @return the result of the generation of the file content (ex : XSD, DTD) from
	 *         the given document source <code>document</code> (ex: XML) by using
	 *         the given settings <code>generatorSettings</code>.
	 */
	String generate(Source document, SharedSettings sharedSettings, Settings generatorSettings,
			IXMLFullFormatter formatter);

}
