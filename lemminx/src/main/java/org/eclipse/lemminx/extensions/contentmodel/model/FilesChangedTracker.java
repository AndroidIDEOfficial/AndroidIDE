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
package org.eclipse.lemminx.extensions.contentmodel.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File changed tracker.
 * 
 * @author Angelo ZERR
 *
 */
public class FilesChangedTracker {

	private static final Logger LOGGER = Logger.getLogger(FilesChangedTracker.class.getName());

	private static class FileChangedTracker {

		private final Path file;
		private FileTime lastModified;

		public FileChangedTracker(Path file) {
			this.file = file;
			if (Files.exists(file)) {
				try {
					lastModified = Files.getLastModifiedTime(file);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Get last modified time failed", e);
				}
			}
		}

		public boolean isDirty() {
			try {
				if (!Files.exists(file)) {
					// This case occurs when user delete the XML Schema / DTD file
					return true;
				}
				FileTime currentLastModified = Files.getLastModifiedTime(file);
				if (!currentLastModified.equals(lastModified)) {
					lastModified = currentLastModified;
					return true;
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Get last modified time failed", e);
				return true;
			}
			return false;
		}

	}

	private final List<FileChangedTracker> files;

	public FilesChangedTracker() {
		files = new ArrayList<>();
	}

	/**
	 * Add file URI to track
	 * 
	 * @param fileURI
	 */
	public void addFileURI(String fileURI) {
		try {
			addFileURI(new URI(fileURI));
		} catch (URISyntaxException e) {
			LOGGER.log(Level.SEVERE, "Add file URI to track failed", e);
		}
	}

	/**
	 * Add file URI to track
	 * 
	 * @param fileURI
	 */
	public void addFileURI(URI fileURI) {
		files.add(new FileChangedTracker(Paths.get(fileURI)));
	}

	/**
	 * Returns true if one file has changed and false otherwise.
	 * 
	 * @return true if one file has changed and false otherwise.
	 */
	public boolean isDirty() {
		for (FileChangedTracker dirtyFile : files) {
			if (dirtyFile.isDirty()) {
				return true;
			}
		}
		return false;
	}

}
