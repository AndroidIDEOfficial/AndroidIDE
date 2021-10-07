/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.extensions.contentmodel.commands;

import org.eclipse.lemminx.services.extensions.commands.ArgumentsUtils;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService.IDelegateCommandHandler;
import org.eclipse.lemminx.settings.PathPatternMatcher;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML Command "xml.check.file.pattern" whose first argument is a file pattern,
 * and whose second argument is some file URI. The command returns true when
 * the given pattern matches against the given file URI and false otherwise.
 */
public class CheckFilePatternCommand implements IDelegateCommandHandler {

	public static final String COMMAND_ID = "xml.check.file.pattern";

	@Override
	public Object executeCommand(ExecuteCommandParams params, SharedSettings sharedSettings,
			CancelChecker cancelChecker) throws Exception {
		String pattern = ArgumentsUtils.getArgAt(params, 0, String.class);
		String currentUri = ArgumentsUtils.getArgAt(params, 1, String.class);
		PathPatternMatcher matcher = new PathPatternMatcher();
		matcher.setPattern(pattern);
		return matcher.matches(currentUri);
	}
}
