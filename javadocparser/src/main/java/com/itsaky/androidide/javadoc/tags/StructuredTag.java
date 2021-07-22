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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author chhorz
 *
 */
public abstract class StructuredTag extends Tag {

	private String name;
	private List<String> segmentNames;
	private TreeMap<String, String> values = new TreeMap<>();

	public StructuredTag(final String name, final String... segmentNames) {
		this.name = name;
		this.segmentNames = Arrays.asList(segmentNames);
		for (String segmentName : segmentNames) {
			values.put(segmentName, "");
		}
	}

	public String getTagName() {
		return name == null ? "" : name;
	}

	public List<String> getSegmentNames() {
		return segmentNames == null ? Collections.emptyList() : segmentNames;
	}

	public Map<String, String> getValues() {
		return values == null ? new HashMap<String, String>() : values;
	}

	public void putValue(final String segmentName, final String segmentValue) {
		if (values.containsKey(segmentName)) {
			values.put(segmentName, segmentValue != null ? segmentValue : "");
		}
	}

	public String createPattern(final String allTagNames) {
		StringBuilder sb = new StringBuilder();
		for (String segmentName : getSegmentNames()) {
			sb.append("\\s+?(?<" + segmentName + ">.+?)");
		}
		return "@" + name + sb.toString() + "\\s*" + allTagNames;
	}

	@Override
	public String toString() {
		return String.format("Tag [name=%s, values=%s]", name, values);
	}

}
