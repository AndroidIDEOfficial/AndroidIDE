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
 *  - Nikolas Komonen <nkomonen@redhat.com> - additions for CDATA and Processing Instruction
 */
package org.eclipse.lemminx.dom.parser;

/**
 * XML Scanner state.
 *
 */
public enum ScannerState {
	WithinContent, AfterOpeningStartTag, AfterOpeningEndTag, WithinProlog, WithinTag, WithinEndTag,
	WithinComment, AfterAttributeName, BeforeAttributeValue, WithinCDATA, AfterClosingCDATATag, StartCDATATag,
	AfterPrologOpen, PrologOrPI, WithinPI,

	// DTD
	DTDWithinDoctype, DTDAfterDoctypeName, DTDAfterDoctypePUBLIC, DTDAfterDoctypeSYSTEM,
	DTDAfterDoctypePublicId,DTDWithinContent, DTDWithinElement, DTDWithinAttlist, DTDWithinEntity, 
	DTDElementAfterName, DTDElementWithinContent, DTDAfterAttlistName, DTDAfterAttlistElementName, 
	DTDAfterAttlistAttributeName, DTDAfterAttlistAttributeType, DTDAfterEntityName, DTDUnrecognizedParameters, 
	DTDWithinNotation, DTDAfterNotationName, DTDAfterNotationPUBLIC, DTDAfterNotationSYSTEM, 
	DTDAfterNotationPublicId, DTDAfterEntityPUBLIC, DTDAfterEntitySYSTEM, DoctypeUnrecognizedParameters;

}