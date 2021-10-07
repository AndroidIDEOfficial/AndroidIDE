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
package org.eclipse.lemminx.commons.snippets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.lemminx.utils.StringUtils;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.InsertTextFormat;
import org.eclipse.lsp4j.MarkupContent;
import org.eclipse.lsp4j.MarkupKind;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

/**
 * A registry for snippets which uses the same format than vscode snippet.
 * 
 * @author Angelo ZERR
 *
 */
public class SnippetRegistry {

	private static final Logger LOGGER = Logger.getLogger(SnippetRegistry.class.getName());

	private final List<Snippet> snippets;

	public SnippetRegistry() {
		this(null, true);
	}

	/**
	 * Snippet registry for a given language id.
	 * 
	 * @param languageId  the language id and null otherwise.
	 * @param loadDefault true if default snippets from SPI must be loaded and false
	 *                    otherwise.
	 */
	public SnippetRegistry(String languageId, boolean loadDefault) {
		snippets = new ArrayList<>();
		// Load snippets from SPI
		if (loadDefault) {
			ServiceLoader<ISnippetRegistryLoader> loaders = ServiceLoader.load(ISnippetRegistryLoader.class);
			loaders.forEach(loader -> {
				if (Objects.equals(languageId, loader.getLanguageId())) {
					try {
						loader.load(this);
					} catch (Exception e) {
						LOGGER.log(Level.SEVERE, "Error while consumming snippet loader " + loader.getClass().getName(),
								e);
					}
				}
			});
		}
	}

	/**
	 * Register the given snippet.
	 * 
	 * @param snippet the snippet to register.
	 */
	public void registerSnippet(Snippet snippet) {
		snippets.add(snippet);
	}

	/**
	 * Register the snippets from the given JSON input stream.
	 * 
	 * @param in the JSON input stream which declares snippets with vscode snippet
	 *           format.
	 * @throws IOException
	 */
	public void registerSnippets(InputStream in) throws IOException {
		registerSnippets(in, null, null);
	}

	/**
	 * Register the snippets from the given JSON stream with a context.
	 * 
	 * @param in                  the JSON input stream which declares snippets with
	 *                            vscode snippet format.
	 * @param contextDeserializer the GSON context deserializer used to create Java
	 *                            context.
	 * @throws IOException
	 */
	public void registerSnippets(InputStream in, TypeAdapter<? extends ISnippetContext<?>> contextDeserializer)
			throws IOException {
		registerSnippets(in, null, contextDeserializer);
	}

	/**
	 * Register the snippets from the given JSON stream with a context.
	 * 
	 * @param in             the JSON input stream which declares snippets with
	 *                       vscode snippet format.
	 * @param defaultContext the default context.
	 * @throws IOException
	 */
	public void registerSnippets(InputStream in, ISnippetContext<?> defaultContext) throws IOException {
		registerSnippets(in, defaultContext, null);
	}

	/**
	 * Register the snippets from the given JSON stream with a context.
	 * 
	 * @param in                  the JSON input stream which declares snippets with
	 *                            vscode snippet format.
	 * @param defaultContext      the default context.
	 * @param contextDeserializer the GSON context deserializer used to create Java
	 *                            context.
	 * @throws IOException
	 */
	public void registerSnippets(InputStream in, ISnippetContext<?> defaultContext,
			TypeAdapter<? extends ISnippetContext<?>> contextDeserializer) throws IOException {
		registerSnippets(new InputStreamReader(in, StandardCharsets.UTF_8.name()), defaultContext, contextDeserializer);
	}

	/**
	 * Register the snippets from the given JSON reader.
	 * 
	 * @param in the JSON reader which declares snippets with vscode snippet format.
	 * @throws IOException
	 */
	public void registerSnippets(Reader in) throws IOException {
		registerSnippets(in, null, null);
	}

	/**
	 * Register the snippets from the given JSON reader with a context.
	 * 
	 * @param in                  the JSON reader which declares snippets with
	 *                            vscode snippet format.
	 * @param contextDeserializer the GSON context deserializer used to create Java
	 *                            context.
	 * @throws IOException
	 */
	public void registerSnippets(Reader in, TypeAdapter<? extends ISnippetContext<?>> contextDeserializer)
			throws IOException {
		registerSnippets(in, null, contextDeserializer);
	}

	/**
	 * Register the snippets from the given JSON stream with a context.
	 * 
	 * @param in             the JSON reader which declares snippets with vscode
	 *                       snippet format.
	 * @param defaultContext the default context.
	 * @throws IOException
	 */
	public void registerSnippets(Reader in, ISnippetContext<?> defaultContext) throws IOException {
		registerSnippets(in, defaultContext, null);
	}

	/**
	 * Register the snippets from the given JSON stream with a context.
	 * 
	 * @param in                  the JSON reader which declares snippets with
	 *                            vscode snippet format.
	 * @param defaultContext      the default context.
	 * @param contextDeserializer the GSON context deserializer used to create Java
	 *                            context.
	 * @throws IOException
	 */
	public void registerSnippets(Reader in, ISnippetContext<?> defaultContext,
			TypeAdapter<? extends ISnippetContext<?>> contextDeserializer) throws IOException {
		JsonReader reader = new JsonReader(in);
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			Snippet snippet = createSnippet(reader, contextDeserializer);
			if (snippet.getDescription() == null) {
				snippet.setDescription(name);
			}
			if (snippet.getContext() == null) {
				snippet.setContext(defaultContext);
			}
			registerSnippet(snippet);
		}
		reader.endObject();
	}

	private static Snippet createSnippet(JsonReader reader,
			TypeAdapter<? extends ISnippetContext<?>> contextDeserializer) throws JsonIOException, JsonSyntaxException {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Snippet.class, new SnippetDeserializer(contextDeserializer));
		return builder.create().fromJson(reader, Snippet.class);
	}

	/**
	 * Returns all snippets.
	 * 
	 * @return all snippets.
	 */
	public List<Snippet> getSnippets() {
		return snippets;
	}

	/**
	 * Returns the snippet completion items according to the context filter.
	 * 
	 * @param replaceRange       the replace range.
	 * @param lineDelimiter      the line delimiter.
	 * @param canSupportMarkdown true if markdown is supported to generate
	 *                           documentation and false otherwise.
	 * @param contextFilter      the context filter.
	 * @return the snippet completion items according to the context filter.
	 */
	public List<CompletionItem> getCompletionItems(Range replaceRange, String lineDelimiter, boolean canSupportMarkdown,
			boolean snippetsSupported, BiPredicate<ISnippetContext<?>, Map<String, String>> contextFilter,
			ISuffixPositionProvider suffixProvider) {
		if (replaceRange == null) {
			return Collections.emptyList();
		}
		Map<String, String> model = new HashMap<>();
		return getSnippets().stream().filter(snippet -> {
			return snippet.match(contextFilter, model);
		}).map(snippet -> {
			CompletionItem item = new CompletionItem();
			item.setLabel(snippet.getLabel());
			String insertText = getInsertText(snippet, model, !snippetsSupported, lineDelimiter);
			item.setKind(CompletionItemKind.Snippet);
			item.setDocumentation(
					Either.forRight(createDocumentation(snippet, model, canSupportMarkdown, lineDelimiter)));
			String prefix = snippet.getPrefixes().get(0);
			item.setFilterText(prefix);
			item.setDetail(snippet.getDescription());
			Range range = replaceRange;
			if (!StringUtils.isEmpty(snippet.getSuffix()) && suffixProvider != null) {
				Position end = suffixProvider.findSuffixPosition(snippet.getSuffix());
				if (end != null) {
					range = new Range(replaceRange.getStart(), end);
				}
			}
			item.setTextEdit(Either.forLeft(new TextEdit(range, insertText)));
			item.setInsertTextFormat(InsertTextFormat.Snippet);
			item.setSortText(snippet.getSortText());
			return item;

		}).collect(Collectors.toList());
	}

	private static MarkupContent createDocumentation(Snippet snippet, Map<String, String> model,
			boolean canSupportMarkdown, String lineDelimiter) {
		StringBuilder doc = new StringBuilder();
		if (canSupportMarkdown) {
			doc.append(System.lineSeparator());
			doc.append("```");
			String scope = snippet.getScope();
			if (scope != null) {
				doc.append(scope);
			}
			doc.append(System.lineSeparator());
		}
		String insertText = getInsertText(snippet, model, true, lineDelimiter);
		doc.append(insertText);
		if (canSupportMarkdown) {
			doc.append(System.lineSeparator());
			doc.append("```");
			doc.append(System.lineSeparator());
		}
		return new MarkupContent(canSupportMarkdown ? MarkupKind.MARKDOWN : MarkupKind.PLAINTEXT, doc.toString());
	}

	private static String getInsertText(Snippet snippet, Map<String, String> model, boolean replace,
			String lineDelimiter) {
		StringBuilder text = new StringBuilder();
		int i = 0;
		List<String> body = snippet.getBody();
		if (body != null) {
			for (String bodyLine : body) {
				if (i > 0) {
					text.append(lineDelimiter);
				}
				bodyLine = merge(bodyLine, model, replace);
				text.append(bodyLine);
				i++;
			}
		}
		return text.toString();
	}

	private static String merge(String line, Map<String, String> model, boolean replace) {
		return replace(line, 0, model, replace, null);
	}

	private static String replace(String line, int offset, Map<String, String> model, boolean replace,
			StringBuilder newLine) {
		int dollarIndex = line.indexOf("$", offset);
		if (dollarIndex == -1 || dollarIndex == line.length() - 1) {
			if (newLine == null) {
				return line;
			}
			newLine.append(line, offset, line.length());
			return newLine.toString();
		}
		if (newLine == null) {
			newLine = new StringBuilder();
		}
		char next = line.charAt(dollarIndex + 1);
		if (Character.isDigit(next)) {
			if (replace) {
				newLine.append(line, offset, dollarIndex);
			}
			int lastDigitOffset = dollarIndex + 1;
			while (Character.isDigit(line.charAt(lastDigitOffset))) {
				lastDigitOffset++;
			}
			if (!replace) {
				newLine.append(line, offset, lastDigitOffset);
			}
			return replace(line, lastDigitOffset, model, replace, newLine);
		} else if (next == '{') {
			int startExpr = dollarIndex;
			int endExpr = line.indexOf("}", startExpr);
			if (endExpr == -1) {
				// Should never occur
				return line;
			}
			newLine.append(line, offset, startExpr);
			// Parameter
			int startParam = startExpr + 2;
			int endParam = endExpr;
			boolean startsWithNumber = true;
			boolean onlyNumber = true;
			for (int i = startParam; i < endParam; i++) {
				char ch = line.charAt(i);
				if (Character.isDigit(ch)) {
					startsWithNumber = true;
				} else {
					onlyNumber = false;
					if (ch == ':') {

						if (startsWithNumber) {
							startParam = i + 1;
						}
						break;
					} else if (ch == '|') {
						if (startsWithNumber) {
							startParam = i + 1;
							int index = line.indexOf(',', startExpr);
							if (index != -1) {
								endParam = index;
							}
						}
						break;
					} else {
						break;
					}
				}
			}
			String paramName = line.substring(startParam, endParam);
			if (model.containsKey(paramName)) {
				paramName = model.get(paramName);
			} else if (!replace) {
				paramName = line.substring(startExpr, endExpr + 1);
			}
			if (!(replace && onlyNumber)) {
				newLine.append(paramName);
			}
			return replace(line, endExpr + 1, model, replace, newLine);
		}
		return line;
	}

}