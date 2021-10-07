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
package org.eclipse.lemminx.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.commons.TextDocument;
import org.eclipse.lemminx.dom.parser.Scanner;
import org.eclipse.lemminx.dom.parser.TokenType;
import org.eclipse.lemminx.dom.parser.XMLScanner;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.settings.XMLFoldingSettings;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeKind;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

/**
 * XML folding support.
 *
 */
class XMLFoldings {
	private static Logger LOGGER = Logger.getLogger(XMLFoldings.class.getName());
	private final XMLExtensionsRegistry extensionsRegistry;

	private static final Pattern REGION_PATTERN = Pattern.compile("\\s*#(region\\b)|(endregion\\b)");

	public XMLFoldings(XMLExtensionsRegistry extensionsRegistry) {
		this.extensionsRegistry = extensionsRegistry;
	}

	class TagInfo {

		public final int startLine;

		public final String tagName;

		public TagInfo(int startLine, String tagName) {
			this.startLine = startLine;
			this.tagName = tagName;
		}
	}

	public List<FoldingRange> getFoldingRanges(TextDocument document, XMLFoldingSettings context,
			CancelChecker cancelChecker) {
		Scanner scanner = XMLScanner.createScanner(document.getText());
		TokenType token = scanner.scan();
		List<FoldingRange> ranges = new ArrayList<>();

		List<TagInfo> stack = new ArrayList<>();
		String lastTagName = null;
		int prevStart = -1;

		try {
			while (token != TokenType.EOS) {
				cancelChecker.checkCanceled();
				switch (token) {
				case StartTag: {
					String tagName = scanner.getTokenText();
					int startLine = document.positionAt(scanner.getTokenOffset()).getLine();
					stack.add(new TagInfo(startLine, tagName));
					lastTagName = tagName;
					break;
				}
				case EndTag: {
					lastTagName = scanner.getTokenText();
					break;
				}
				case StartTagClose:
					if (lastTagName != null /* || !isEmptyElement(lastTagName) */) {
						break;
					}
					// fallthrough
				case EndTagClose:
				case StartTagSelfClose: {
					int i = stack.size() - 1;
					while (i >= 0 && !stack.get(i).tagName.equals(lastTagName)) {
						i--;
					}
					if (i >= 0) {
						TagInfo stackElement = stack.get(i);
						// remove obsolete entries ()
						int j = stack.size() - 1;
						while (j >= i) {
							stack.remove(j--);
						}
						int line = document.positionAt(scanner.getTokenOffset()).getLine();
						int startLine = stackElement.startLine;
						int endLine = line - 1;
						if (endLine > startLine && prevStart != startLine) {
							prevStart = addRange(new FoldingRange(startLine, endLine), ranges);
						}
					}
					break;
				}
				case Comment: {
					int startLine = document.positionAt(scanner.getTokenOffset()).getLine();
					String text = scanner.getTokenText();
					Matcher m = REGION_PATTERN.matcher(text);
					if (m.find()) {
						if ("#region".equals(m.group().trim())) { // start pattern match
							stack.add(new TagInfo(startLine, "")); // empty tagName marks region
						} else {
							int i = stack.size() - 1;
							while (i >= 0 && stack.get(i).tagName != null && !stack.get(i).tagName.isEmpty()) {
								i--;
							}
							if (i >= 0) {
								TagInfo stackElement = stack.get(i);
								// remove obsolete entries ()
								int j = stack.size() - 1;
								while (j >= i) {
									stack.remove(j--);
								}
								int endLine = startLine;
								startLine = stackElement.startLine;
								if (endLine > startLine && prevStart != startLine) {
									FoldingRange range = new FoldingRange(startLine, endLine);
									range.setKind(FoldingRangeKind.Region);
									prevStart = addRange(range, ranges);
								}
							}
						}
					} else {
						int endLine = document.positionAt(scanner.getTokenOffset() + scanner.getTokenLength())
								.getLine();
						if (startLine < endLine) {
							FoldingRange range = new FoldingRange(startLine, endLine);
							range.setKind(FoldingRangeKind.Comment);
							prevStart = addRange(range, ranges);
						}
					}
					break;
				}
				default:
				}
				token = scanner.scan();
			}

			int rangeLimit = context != null && context.getRangeLimit() != null ? context.getRangeLimit()
					: Integer.MAX_VALUE;
			if (ranges.size() > rangeLimit) {
				ranges = limitRanges(ranges, rangeLimit);
			}
		} catch (BadLocationException e) {
			LOGGER.log(Level.SEVERE, "Foldings received a BadLocation while scanning the document", e);
		} catch (StackOverflowError e) {
			// This exception occurs with large file, why?
			// For the moment we catch it.
			LOGGER.log(Level.SEVERE, "Foldings received a StackOverflowError while scanning the document", e);
		}
		return ranges;
	}

	private static int addRange(FoldingRange range, List<FoldingRange> ranges) {
		ranges.add(range);
		return range.getStartLine();
	}

	private static List<FoldingRange> limitRanges(List<FoldingRange> ranges, int rangeLimit) {
		Collections.sort(ranges, (r1, r2) -> {
			int diff = r1.getStartLine() - r2.getStartLine();
			if (diff == 0) {
				diff = r1.getEndLine() - r2.getEndLine();
			}
			return diff;
		});

		// compute each range's nesting level in 'nestingLevels'.
		// count the number of ranges for each level in 'nestingLevelCounts'
		FoldingRange top = null;
		List<FoldingRange> previous = new ArrayList<>();
		Map<Integer, Integer> nestingLevels = new HashMap<>();
		Map<Integer, Integer> nestingLevelCounts = new LinkedHashMap<>();

		// compute nesting levels and sanitize
		for (int i = 0; i < ranges.size(); i++) {
			FoldingRange entry = ranges.get(i);
			if (top == null) {
				top = entry;
				setNestingLevel(i, 0, nestingLevels, nestingLevelCounts);
			} else {
				if (entry.getStartLine() > top.getStartLine()) {
					if (entry.getEndLine() <= top.getEndLine()) {
						previous.add(top);
						top = entry;
						setNestingLevel(i, previous.size(), nestingLevels, nestingLevelCounts);
					} else if (entry.getStartLine() > top.getEndLine()) {
						do {
							top = previous.remove(previous.size() - 1);
						} while (top != null && entry.getStartLine() > top.getEndLine());
						if (top != null) {
							previous.add(top);
						}
						top = entry;
						setNestingLevel(i, previous.size(), nestingLevels, nestingLevelCounts);
					}
				}
			}
		}
		int entries = 0;
		int maxLevel = 0;
		for (Entry<Integer, Integer> entry : nestingLevelCounts.entrySet()) {
			Integer n = entry.getValue();
			if (n + entries > rangeLimit) {
				maxLevel = entry.getKey();
				break;
			}
			entries += n;
		}

		List<FoldingRange> result = new ArrayList<>();
		for (int i = 0; i < ranges.size(); i++) {
			Integer level = nestingLevels.get(i);
			if (level != null) {
				if (level < maxLevel || (level == maxLevel && entries++ < rangeLimit)) {
					result.add(ranges.get(i));
				}
			}
		}
		return result;
	}

	private static void setNestingLevel(int index, int level, Map<Integer, Integer> nestingLevels,
			Map<Integer, Integer> nestingLevelCounts) {
		nestingLevels.put(index, level);
		if (level < 30) {
			nestingLevelCounts.put(level,
					(nestingLevelCounts.containsKey(level) ? nestingLevelCounts.get(level) : 0) + 1);
		}
	};

}
