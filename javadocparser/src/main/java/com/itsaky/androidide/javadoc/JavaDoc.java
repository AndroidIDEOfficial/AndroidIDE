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

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

import com.itsaky.androidide.javadoc.tags.Tag;

/**
 * This Javadoc object holds the parsed information.
 *
 * @author chhorz
 *
 */
public class JavaDoc {

	private String summary;
	private String description;
	private List<Tag> tags;

	public JavaDoc(final String summary, final String description, final List<Tag> tags) {
		this.summary = summary;
		this.description = description;
		this.tags = tags;
	}

	/**
	 * The summary is either defined by the new tag {(at)summary ...} or the first sentence of the textual description.
	 *
	 * @return the summary of the textual description
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * The Javadoc description is the whole text until the first tag.
	 *
	 * @return the complete textual description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This method returns all parsed {@link Tag}s
	 *
	 * @return a list of all tags
	 */
	public List<Tag> getTags() {
		return tags == null ? Collections.emptyList() : tags;
	}

	/**
	 * This method returns a list of all tags from the given class.
	 *
	 * @param tagClass    a class to filter the output
	 * @return a list of tags from the given class
	 */
	public <T extends Tag> List<T> getTags(final Class<T> tagClass) {
		return tags == null ? Collections.emptyList() : tags.stream()
				.filter(tagClass::isInstance)
				.map(tagClass::cast)
				.collect(toList());
	}

	@Override
	public String toString() {
		return String.format("JavaDoc [summary=%s, description=%s, tags=%s]", summary, description, tags);
	}

}
