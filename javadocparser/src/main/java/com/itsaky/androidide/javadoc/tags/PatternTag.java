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
package com.itsaky.androidide.javadoc.tags;

/**
 * A PatternTag can be used to define a custom {@link java.util.regex.Pattern}. For this free kind of tag, more methods have to be implemented.
 *
 * @author chhorz
 *
 */
public abstract class PatternTag extends Tag {

	private String name;
	private String pattern;

	public PatternTag(final String name, final String pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public String getTagName() {
		return name == null ? "" : name;
	}

	@Override
	public String createPattern(final String allTagNames) {
		return "@" + name + pattern + "\\s*" + allTagNames;
	}

	@Override
	public String toString() {
		return String.format("Tag [name=%s, pattern=%s]", name, pattern);
	}

}
