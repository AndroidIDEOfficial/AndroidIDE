/**
 *  Copyright (c) 2018 Angelo ZERR
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.lemminx.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import org.eclipse.lemminx.utils.platform.Platform;

/**
 * Files utilities.
 *
 */
public class FilesUtils {

	private static final Logger LOGGER = Logger.getLogger(FilesUtils.class.getName());

	public static final String FILE_SCHEME = "file://";
	public static final String LEMMINX_WORKDIR_KEY = "lemminx.workdir";
	private static String cachePathSetting = null;

	private static Pattern uriSchemePattern = Pattern.compile("^([a-zA-Z\\-]+:\\/\\/).*");
	private static Pattern endFilePattern = Pattern.compile(".*[\\\\\\/]\\.[\\S]+");

	public static String getCachePathSetting() {
		return cachePathSetting;
	}

	public static void setCachePathSetting(String cachePathSetting) {
		if (StringUtils.isEmpty(cachePathSetting)) {
			FilesUtils.cachePathSetting = null;
		} else {
			FilesUtils.cachePathSetting = cachePathSetting;
		}
		resetDeployPath();
	}

	private FilesUtils() {
	}

	public static Supplier<Path> DEPLOYED_BASE_PATH;

	static {
		resetDeployPath();
	}

	/** Public for test purposes */
	public static void resetDeployPath() {
		DEPLOYED_BASE_PATH = Suppliers.memoize(() -> getDeployedBasePath());
	}

	/**
	 * Given a file path as a string, will normalize it and return the normalized
	 * string if valid, or null if not.
	 *
	 * The '~' home symbol will be converted into the actual home path. Slashes will
	 * be corrected depending on the OS.
	 */
	public static String normalizePath(String pathString) {
		if (pathString != null && !pathString.isEmpty()) {
			if (pathString.indexOf("~") == 0) {
				pathString = System.getProperty("user.home") + (pathString.length() > 1 ? pathString.substring(1) : "");
			}
			pathString = pathString.replace("/", File.separator);
			pathString = pathString.replace("\\", File.separator);
			Path p = Paths.get(pathString);
			pathString = p.normalize().toString();
			return pathString;
		}
		return null;
	}

	private static Path getDeployedBasePath() {
		String dir = System.getProperty(LEMMINX_WORKDIR_KEY);
		if (dir != null) {
			return Paths.get(dir);
		}
		if (cachePathSetting != null && !cachePathSetting.isEmpty()) {
			return Paths.get(cachePathSetting);
		}
		dir = System.getProperty("user.home");
		if (dir == null) {
			dir = System.getProperty("user.dir");
		}
		if (dir == null) {
			dir = "";
		}
		return getPath(dir).resolve(".lemminx");
	}

	/**
	 * Returns the deployed path from the given <code>path</code>.
	 *
	 * @param path the path
	 * @return the deployed path from the given <code>path</code>.
	 * @throws IOException
	 */
	public static Path getDeployedPath(Path path) throws IOException {
		return DEPLOYED_BASE_PATH.get().resolve(path);
	}

	/**
	 * Save the given input stream <code>in</code> in the give out file
	 * <code>outFile</code>
	 *
	 * @param in      the input stream
	 * @param outFile the output file
	 * @throws IOException
	 */
	public static void saveToFile(InputStream in, Path outFile) throws IOException {
		saveToFile(IOUtils.convertStreamToString(in), outFile);
	}

	/**
	 * Save the given String <code>content</code> in the give out file
	 * <code>outFile</code>
	 *
	 * @param content the string content
	 * @param outFile the output file
	 * @throws IOException
	 */
	public static void saveToFile(String content, Path outFile) throws IOException {
		if (!Files.exists(outFile.getParent())) {
			Files.createDirectories(outFile.getParent());
		}
		try (Writer writer = Files.newBufferedWriter(outFile, StandardCharsets.UTF_8)) {
			writer.write(content);
		}
		// Make sure it's not executable
		outFile.toFile().setExecutable(false);
	}

	public static int getOffsetAfterScheme(String uri) {
		Matcher m = uriSchemePattern.matcher(uri);

		if (m.matches()) {
			return m.group(1).length();
		}

		return -1;
	}

	/**
	 * Returns the slash ("/" or "\") that is used by the given string. If no slash
	 * is given "/" is returned by default.
	 *
	 * @param text
	 * @return
	 */
	public static String getFilePathSlash(String text) {
		if (text.contains("\\")) {
			return "\\";
		}
		return "/";
	}

	/**
	 * Ensures there is no slash before a drive letter, and forces use of '\'
	 *
	 * @param pathString
	 * @return
	 */
	public static String convertToWindowsPath(String pathString) {
		String pathSlash = getFilePathSlash(pathString);
		if (pathString.startsWith(pathSlash)) {
			if (pathString.length() > 3) {
				char letter = pathString.charAt(1);
				char colon = pathString.charAt(2);
				if (Character.isLetter(letter) && ':' == colon) {
					pathString = pathString.substring(1);
				}
			}
		}
		return pathString.replace("/", "\\");
	}

	public static boolean pathEndsWithFile(String pathString) {
		Matcher m = endFilePattern.matcher(pathString);
		return m.matches();
	}

	public static boolean isIncludedInDeployedPath(Path resourceCachePath) {
		return resourceCachePath.normalize().startsWith(DEPLOYED_BASE_PATH.get());
	}

	/**
	 * Remove the file:// scheme from the given file URI.
	 *
	 * @param fileURI the file URI.
	 *
	 * @return the file URI without file scheme.
	 */
	public static String removeFileScheme(String fileURI) {
		return removeFileScheme(fileURI, false);
	}

	private static String removeFileScheme(String fileURI, boolean removeLastSlash) {
		int index = fileURI.indexOf(FILE_SCHEME);
		if (index != -1) {
			index = index + FILE_SCHEME.length() - 1;
			if (removeLastSlash && index + 1 < fileURI.length() && fileURI.charAt(index + 1) == '/') {
				index++;
			}
			fileURI = fileURI.substring(index + 1, fileURI.length());
		}
		return fileURI;
	}

	/**
	 * Returns the IO Path from the given uri. This URI can use several syntaxes
	 * like:
	 *
	 * <ul>
	 * <li>file:///C:/folder (Windows OS), file://home (Linux OS)</li>
	 * <li>a%20b/folder (folder with spaces)</li>
	 * </ul>
	 *
	 * @param uri the URI
	 *
	 * @return the IO Path from the given uri.
	 */
	public static Path getPath(String uri) {
		// Remove file://
		uri = removeFileScheme(uri, Platform.isWindows);
		try {
			// replace "%20" with " ", "%3A" with ":", etc
			uri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// Do nothing
		}
		return Paths.get(uri);
	}

	/**
	 * Replace spaces with "%20".
	 *
	 * @param path the path.
	 *
	 * @return the path with replaced spaces.
	 */
	public static String encodePath(String path) {
		return path.replace(" ", "%20");
	}
}
