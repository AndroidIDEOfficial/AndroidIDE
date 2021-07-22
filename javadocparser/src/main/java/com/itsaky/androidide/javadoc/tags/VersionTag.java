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
 * https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html#CHDCHBAE
 *
 * @author chhorz
 *
 */
public class VersionTag extends StructuredTag {

	private static final String TAG_NAME = "version";

	private static final String VERSION_TEXT = "versionText";

	public VersionTag() {
		super(TAG_NAME, VERSION_TEXT);
	}

	public String getVersionText() {
		return getValues().get(VERSION_TEXT);
	}

}
