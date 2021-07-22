/**
 *
 *    Copyright 2018-2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.itsaky.androidide.javadoc;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.concat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.itsaky.androidide.javadoc.exception.DuplicateTagException;
import com.itsaky.androidide.javadoc.tags.Tag;

/**
 *
 * @author chhorz
 *
 */
public final class JavaDocParser {

	private List<Tag> tags = new ArrayList<>();
	private Map<String, String> replacements = new HashMap<>();

	public JavaDoc parse(final String javaDocString) {
		String summary = "";
		String description = "";
		List<Tag> parsedTags = new ArrayList<>();

		if (javaDocString != null && !javaDocString.isEmpty()) {
			final String rawDescription = parseDescription(javaDocString);

			summary = performReplacements(parseSummary(rawDescription));
			description = performReplacements(rawDescription);
			parsedTags = parseTags(javaDocString);
		}

		return new JavaDoc(summary, description, parsedTags);
	}

	private String parseSummary(final String description) {
		if (description.isEmpty()) {
			return "";
		} else if (description.contains("@summary")) {
			Pattern summaryPattern = Pattern.compile("\\{@summary ([^{}]+)}([\\s.,:;-])?");
			Matcher summaryMatcher = summaryPattern.matcher(description);

			if (summaryMatcher.find()) {
				return summaryMatcher.group(1);
			} else {
				return "";
			}

		} else {
			if (description.contains(".")) {
				return description.substring(0, description.indexOf('.') + 1);
			} else {
				return description;
			}
		}
	}

	private String parseDescription(final String javaDocString) {
		String[] lines = javaDocString.split("\n");

		StringBuilder stringBuilder = new StringBuilder();
		for (String line : lines) {
			if (line.trim().startsWith("@")) {
				break;
			}
			stringBuilder.append(line).append("\n");
		}
		return stringBuilder.toString().trim();
	}

	private List<Tag> parseTags(final String javaDocString) {
		List<Tag> tagList = new ArrayList<>();

		Stream<String> tagNamesStream = tags.stream()
				.map(tag -> tag.getTagName())
				.map(tag -> String.format("@%s", tag));

		String allTagNames = concat(tagNamesStream, Stream.of("[^{]@\\S+"))
				.collect(joining("|", "(?=", "|$)"));

		for (Tag tag : tags) {
			Pattern pattern = Pattern.compile(tag.createPattern(allTagNames), Pattern.DOTALL);
			Matcher matcher = pattern.matcher(javaDocString);
			
			int start = 0;
			while (matcher.find(start)) {
				Tag currentTag;
				try {
					currentTag = tag.getClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					currentTag = tag;
					e.printStackTrace();
				}

				for (String segmentName : tag.getSegmentNames()) {
					currentTag.putValue(segmentName, performReplacements(matcher.group(segmentName)));
				}
				
				tagList.add(currentTag);
				start = matcher.end();
			}
		}
		
		return tagList;
	}

	private String performReplacements(final String input) {
		String convertedString = input;
		for (Entry<String, String> replacement : replacements.entrySet()) {
			convertedString = convertedString.replaceAll(replacement.getKey(), replacement.getValue());
		}
		return convertedString.trim();
	}

	void addReplacement(final String regex, final String replacement){
		Objects.requireNonNull(regex, "The given regex must not be null!");
		Objects.requireNonNull(replacement, "The given replacement must not be null!");

		if (regex.isEmpty()) {
			throw new IllegalArgumentException("The given regex must not be empty!");
		}

		replacements.put(regex, replacement);
	}

	void addTag(final Tag tag) {
		Objects.requireNonNull(tag, "The given tag must not be null!");
		if (tags.stream().anyMatch(t -> t.getClass().equals(tag.getClass()))){
			throw new DuplicateTagException(tag);
		}
		tags.add(tag);
	}

}
