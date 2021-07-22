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

import java.util.List;

/**
 * Base class for all internal Javadoc tags.
 *
 * @author chhorz
 *
 */
public abstract class Tag {

	/**
	 *
	 *
	 * @return the Javadoc tag name
	 */
	public abstract String getTagName();

	/**
	 *
	 * @return all names of segments that can be read
	 */
	public abstract List<String> getSegmentNames();

	/**
	 *
	 * @param segmentName	the segment name for which the value should be saved
	 * @param value	the value that was parsed for the segment
	 */
	public abstract void putValue(final String segmentName, final String value);

	/**
	 *
	 * @param allTagNames	the list of all patterns will be calculated on the fly and describes the end of a sinlge tag
	 * @return	the complete pattern string for this tag
	 */
	public abstract String createPattern(final String allTagNames);

}
