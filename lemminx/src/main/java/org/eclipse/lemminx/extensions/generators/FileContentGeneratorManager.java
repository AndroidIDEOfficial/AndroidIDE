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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.lemminx.extensions.generators.xml2dtd.DTDGeneratorSettings;
import org.eclipse.lemminx.extensions.generators.xml2dtd.XML2DTDGenerator;
import org.eclipse.lemminx.extensions.generators.xml2xsd.XML2XMLSchemaGenerator;
import org.eclipse.lemminx.extensions.generators.xml2xsd.XMLSchemaGeneratorSettings;
import org.eclipse.lemminx.services.IXMLFullFormatter;
import org.eclipse.lemminx.settings.SharedSettings;

/**
 * Generic file generator.
 * 
 * <p>
 * By default the manager is able to generate :
 * 
 * <ul>
 * <li>XML Schema from a given XML.</li>
 * <li>DTD from a given XML.</li>
 * </ul>
 * </p>
 */
public class FileContentGeneratorManager {

	private final Map<Class<? extends FileContentGeneratorSettings>, IFileContentGenerator<?, ?>> generators;

	private final IXMLFullFormatter formatter;

	public FileContentGeneratorManager(IXMLFullFormatter formatter) {
		this.generators = new HashMap<>();
		this.formatter = formatter;
		registerDefaultGenerators();
	}

	/**
	 * Register default generators.
	 */
	private void registerDefaultGenerators() {
		registerGenerator(new XML2DTDGenerator(), DTDGeneratorSettings.class);
		registerGenerator(new XML2XMLSchemaGenerator(), XMLSchemaGeneratorSettings.class);
	}

	/**
	 * Register the given generator by using settings class as key generator.
	 * 
	 * @param generator    the generator.
	 * @param generatorKey the key of the generator identified by the settings
	 *                     class.
	 */
	public void registerGenerator(IFileContentGenerator<?, ?> generator,
			Class<? extends FileContentGeneratorSettings> generatorKey) {
		generators.put(generatorKey, generator);
	}

	/**
	 * Generates a file content (ex : XSD, DTD) from the given document source
	 * <code>document</code> (ex: XML) by using the given settings
	 * <code>generatorSettings</code>.
	 * 
	 * @param document          the document source (ex : XML).
	 * @param sharedSettings    the shared settings.
	 * @param generatorSettings the generator settings.
	 * @param formatter         the formatter.
	 * @return the result of the generation of the file content (ex : XSD, DTD) from
	 *         the given document source <code>document</code> (ex: XML) by using
	 *         the given settings <code>generatorSettings</code>.
	 */
	public <Source, Settings extends FileContentGeneratorSettings> String generate(Source document,
			SharedSettings sharedSettings, Settings generatorSettings) {
		// Get the generator by the generator settings class key
		@SuppressWarnings("unchecked")
		IFileContentGenerator<Source, Settings> generator = (IFileContentGenerator<Source, Settings>) generators
				.get(generatorSettings.getClass());
		// process the generator
		return generator.generate(document, sharedSettings, generatorSettings, this.formatter);
	}
}
