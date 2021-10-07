/*******************************************************************************
* Copyright (c) 2021 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.utils.platform;

import java.util.ResourceBundle;

/**
 * LemMinX version information
 *
 * The information is read from git
 */
public class Version {

	private final String versionNumber;

	private final String shortCommitId;

	private final String commitMessage;

	private final String branch;

	public static final String MAIN_BRANCH = "master";

	public Version() {
		versionNumber = "AndroidIDE v1.0.3-alpha";
		shortCommitId = "NOT_APPLICABLE";
		commitMessage = "LemMinX for AndroidIDE";
		branch = "main";
	}

	/**
	 * Returns the version number of LemMinX.
	 *
	 * @return the version number of LemMinX
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Returns the git short commit id.
	 *
	 * eg. 4f2ed3d
	 *
	 * @return the git short commit id
	 */
	public String getShortCommitId() {
		return shortCommitId;
	}

	/**
	 * Returns the first line of the git commit message.
	 *
	 * @return the first line of the git commit message
	 */
	public String getCommitMessage() {
		return commitMessage;
	}

	/**
	 * Returns the git branch.
	 *
	 * @return the git branch
	 */
	public String getBranch() {
		return branch;
	}

}
