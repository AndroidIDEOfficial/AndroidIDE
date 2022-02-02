/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.lang.analysis;

import io.github.rosemoe.sora.lang.styling.Styles;

/**
 * A {@link StyleReceiver} receives spans and other styles from analyzers.
 *
 * The implementations of the class must make sure its code can be safely run. For example, update
 * UI by posting its actions to UI thread, but not here.
 *
 * Also, the implementations of the class should pay attention to concurrent invocations due not to
 * corrupt the information it maintains.
 */
public interface StyleReceiver {

    /**
     * Send the styles to the receiver. You can call it in any thread.
     * The implementation of this should make sure that concurrent invocations to it are safe.
     */
    void setStyles(AnalyzeManager sourceManager, Styles styles);

}
