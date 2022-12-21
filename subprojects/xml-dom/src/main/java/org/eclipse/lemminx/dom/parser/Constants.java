/**
 *  Copyright (c) 2018 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v2.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/Microsoft/vscode-html-languageservice
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.lemminx.dom.parser;

import java.util.regex.Pattern;

public class Constants {

	public final static int _EXL = "!".codePointAt(0);
	public final static int _MIN = "-".codePointAt(0);
	public final static int _UDS = "_".codePointAt(0);
	public final static int _DDT = ":".codePointAt(0);
	public final static int _DOT = ".".codePointAt(0);
	public final static int _LAN = "<".codePointAt(0);
	public final static int _RAN = ">".codePointAt(0);
	public final static int _FSL = "/".codePointAt(0);
	public final static int _EQS = "=".codePointAt(0);
	public final static int _CMA = ",".codePointAt(0);
	public final static int _DQO = "\"".codePointAt(0);
	public final static int _SQO = "\"".codePointAt(0);
	public final static int _SIQ = "\'".codePointAt(0);
	public final static int _NWL = "\n".codePointAt(0);
	public final static int _CAR = "\r".codePointAt(0);
	public final static int _LFD = "\f".codePointAt(0);
	public final static int _WSP = " ".codePointAt(0);
	public final static int _TAB = "\t".codePointAt(0);
	public final static int _OSB = "[".codePointAt(0);
	public final static int _CSB = "]".codePointAt(0);
	public final static int _ORB = "(".codePointAt(0);
	public final static int _CRB = ")".codePointAt(0);
	public final static int _OCB = "{".codePointAt(0);
	public final static int _CCB = "}".codePointAt(0);
	public final static int _CVL = "C".codePointAt(0);
	public final static int _DVL = "D".codePointAt(0);
	public final static int _AVL = "A".codePointAt(0);
	public final static int _TVL = "T".codePointAt(0);
	public final static int _OVL = "O".codePointAt(0);
	public final static int _YVL = "Y".codePointAt(0);
	public final static int _PVL = "P".codePointAt(0);
	public final static int _EVL = "E".codePointAt(0);
	public final static int _LVL = "L".codePointAt(0);
	public final static int _MVL = "M".codePointAt(0);
	public final static int _NVL = "N".codePointAt(0);
	public final static int _IVL = "I".codePointAt(0);
	public final static int _SVL = "S".codePointAt(0);
	public final static int _QMA = "?".codePointAt(0);
	public final static int _XVL = "x".codePointAt(0);
	public final static int _mVL = "m".codePointAt(0);
	public final static int _lVL = "l".codePointAt(0);
	public final static int _PCT = "%".codePointAt(0);
	public final static int _AST = "*".codePointAt(0);
	public final static int _PLS = "+".codePointAt(0);

	public static final Pattern ENTITY_NAME_REGEX = Pattern.compile("");

	public static final Pattern ELEMENT_NAME_REGEX = Pattern.compile("^[_:\\w][_:\\w-.\\d]*");

	public static final Pattern ATTRIBUTE_NAME_REGEX = Pattern.compile("^[^\\s\\?\"'<>/=\\x00-\\x0F\\x7F\\x80-\\x9F]*");

	public static final Pattern ATTRIBUTE_VALUE_REGEX = Pattern.compile("^(\"[^\"]*\"?)|(\'[^\']*\'?)");

	public static final Pattern URL_VALUE_REGEX = Pattern.compile("^(\"[^<>\"]*\")|(\'[^<>\']*\')");

	public static final Pattern PROLOG_NAME_OPTIONS = Pattern.compile("^(xml)[\\s<>?]?");

	public static final Pattern PI_TAG_NAME = Pattern.compile("^[a-zA-Z0-9]+");

	// Add coming processing instructions that are defined to have attributes as
	// content
	public static final Pattern PI_WITH_VARIABLES = Pattern.compile("^(xml-stylesheet)[\\s<>?]?");

	public static final Pattern DOCTYPE_KIND_OPTIONS = Pattern.compile("^(PUBLIC|SYSTEM)([\\s<>\"'])");

	public static final Pattern DTD_ELEMENT_CATEGORY = Pattern.compile("^(EMPTY|ANY)([\\s<>\"'])");

	public static final Pattern DTD_ELEMENT_CONTENT = Pattern.compile("^(\\((([^\\s,]+,)*[^\\s,]+)\\))|\\(\\)");

	public static final Pattern DTD_PCDATA = Pattern.compile("^#PCDATA");

	public static final Pattern DTD_ATTLIST_ATTRIBUTE_TYPE = Pattern
			.compile("^(CDATA|IDREFS|IDREF|ID|NMTOKENS|NMTOKEN|ENTITIES|ENTITY|NOTATION|xml:|\\(.*\\))([\\s<>\"'])");

	public static final Pattern DTD_ATTLIST_ATTRIBUTE_VALUE = Pattern
			.compile("^(#REQUIRED|#IMPLIED|\".*\"|#FIXED \".*\")([\\s<>\"'])");

	public static final Pattern DTD_ENTITY_VALUE = Pattern.compile("^\".*\"");

	public static final Pattern DOCTYPE_NAME = Pattern.compile("^[_:\\w][_:\\w-.\\d]*");

	public static final Pattern DOCUMENTATION_CONTENT = Pattern.compile(".*<[\\S]+:?documentation[^\\>]*>(.*)<\\/[\\S]+:?documentation[\\s]*>.*");

}
