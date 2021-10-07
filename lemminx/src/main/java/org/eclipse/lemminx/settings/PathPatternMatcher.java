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
package org.eclipse.lemminx.settings;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.Objects;

public class PathPatternMatcher {

	private transient PathMatcher pathMatcher;
	private String pattern;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.pathMatcher = null;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public boolean matches(String uri) {
		try {
			return matches(new URI(uri));
		} catch (Exception e) {
			return false;
		}
	}

	public boolean matches(URI uri) {
		if (pattern.length() < 1) {
			return false;
		}
		if (pathMatcher == null) {
			char c = pattern.charAt(0);
			String glob = pattern;
			if (c != '*' && c != '?' && c != '/') {
				// in case of pattern like this pattern="myFile*.xml", we must add '**/' before
				glob = "**/" + glob;
			}
			pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
		}
		try {
			return pathMatcher.matches(Paths.get(uri));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}

		if(obj instanceof PathPatternMatcher) {
			PathPatternMatcher other = (PathPatternMatcher) obj;
			if(!Objects.equals(pathMatcher, other.getPathMatcher())) {
				return false;
			}
			if(!Objects.equals(pattern, other.getPattern())) {
				return false;
			}
			return true;
		}
		return false;
	}

}