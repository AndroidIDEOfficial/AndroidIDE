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

import java.util.stream.Stream;

import com.itsaky.androidide.javadoc.tags.*;

/**
 *
 * @author chhorz
 *
 */
public class JavaDocParserBuilder {

	private static final String BASE_INLINE_PATTERN = "\\{@%s ([^\\{\\}]+)\\}([\\s.,:;-])?";
	private static final String INLINE_SUMMARY_PATTERN = String.format(BASE_INLINE_PATTERN, "summary");
	private static final String INLINE_CODE_PATTERN = String.format(BASE_INLINE_PATTERN, "code");
	private static final String INLINE_LINK_PATTERN = String.format(BASE_INLINE_PATTERN, "link");
	private static final String INLINE_LINKPLAIN_PATTERN = String.format(BASE_INLINE_PATTERN, "linkplain");
	private static final String INLINE_LITERAL_PATTERN = String.format(BASE_INLINE_PATTERN, "literal");
	private static final String INLINE_VALUE_PATTERN = String.format(BASE_INLINE_PATTERN, "value");

	private JavaDocParser javaDocParser;

	private JavaDocParserBuilder() {
		this.javaDocParser = new JavaDocParser();

		Stream.of(new AuthorTag(),
			     new CategoryTag(),
			     new DeprecatedTag(),
			     new ExceptionTag(),
			     new ParamTag(),
			     new ReturnTag(),
			     new SeeTag(),
			     new SinceTag(),
			     new ThrowsTag(),
			     new VersionTag())
			.forEach(javaDocParser::addTag);
	}

	public static JavaDocParserBuilder withBasicTags() {
		return new JavaDocParserBuilder();
	}

	public <T extends Tag> JavaDocParserBuilder withCustomTag(final T tag) {
		javaDocParser.addTag(tag);
		return this;
	}

	public JavaDocParserBuilder withOutputType(final OutputType outputType) {
		javaDocParser.addReplacement(INLINE_SUMMARY_PATTERN, "$1$2");
		if (OutputType.ASCIIDOC.equals(outputType) || OutputType.MARKDOWN.equals(outputType)) {
			javaDocParser.addReplacement(INLINE_CODE_PATTERN, "`$1`$2");
			javaDocParser.addReplacement(INLINE_LINK_PATTERN, "$1$2");
			javaDocParser.addReplacement(INLINE_LINKPLAIN_PATTERN, "$1$2");
			javaDocParser.addReplacement(INLINE_LITERAL_PATTERN, "_$1_$2");
			javaDocParser.addReplacement(INLINE_VALUE_PATTERN, "`$1`$2");
		} else if (OutputType.HTML.equals(outputType)) {
			javaDocParser.addReplacement(INLINE_CODE_PATTERN, "<code>$1</code>$2");
			javaDocParser.addReplacement(INLINE_LINK_PATTERN, "$1$2");
			javaDocParser.addReplacement(INLINE_LINKPLAIN_PATTERN, "$1$2");
			javaDocParser.addReplacement(INLINE_LITERAL_PATTERN, "<i>$1</i>$2");
			javaDocParser.addReplacement(INLINE_VALUE_PATTERN, "<pre>$1</pre>$2");
		}
		return this;
	}

	public JavaDocParserBuilder withReplacement(final String regex, final String replacement) {
		javaDocParser.addReplacement(regex, replacement);
		return this;
	}

	public JavaDocParser build() {
		return javaDocParser;
	}

}
