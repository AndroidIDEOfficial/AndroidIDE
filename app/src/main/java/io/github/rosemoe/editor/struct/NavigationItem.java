/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.struct;

/**
 * Model for code navigation
 *
 * @author Rose
 */
@SuppressWarnings("CanBeFinal")
public class NavigationItem {

    /**
     * The line position
     */
    public int line;

    /**
     * The description
     */
    public String label;

    /**
     * Create a new navigation
     *
     * @param line  The line position
     * @param label The description
     */
    public NavigationItem(int line, String label) {
        this.line = line;
        this.label = label;
    }

}
