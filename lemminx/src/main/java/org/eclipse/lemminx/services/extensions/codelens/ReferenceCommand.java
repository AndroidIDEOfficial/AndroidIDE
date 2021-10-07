/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services.extensions.codelens;

import java.util.Arrays;

import org.eclipse.lemminx.client.ClientCommands;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.Position;

/**
 * References command for CodeLens.
 * 
 * @author Angelo ZERR
 *
 */
public class ReferenceCommand extends Command {

	private transient int nbReferences = 1;

	public ReferenceCommand(String uri, Position position, boolean supportedByClient) {
		super(computeTitle(1), supportedByClient ? ClientCommands.SHOW_REFERENCES : "");
		super.setArguments(Arrays.asList(uri, position));
	}

	public void increment() {
		nbReferences++;
		super.setTitle(computeTitle(nbReferences));
	}

	private static String computeTitle(int nbReferences) {
		if (nbReferences == 1) {
			return nbReferences + " reference";
		}
		return nbReferences + " references";
	}

}