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
package io.github.rosemoe.sora.widget;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.OverScroller;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Direct-access properties.
 *
 * This object saves some feature settings of editor. These features are not accessed unless the user
 * does something that requires to check the state of the feature. So we save them here by public fields
 * so that you can modify them easily and do not have to call so many methods.
 */
public class DirectAccessProps implements Serializable {

    /**
     * If set to be true, the editor will delete the whole line if the current line is empty (only tabs or spaces)
     * when the users press the DELETE key.
     *
     * Default value is {@code true}
     */
    public boolean deleteEmptyLineFast = true;

    /**
     * Delete multiple space at a time when the user press the DELETE key.
     * This only takes effect when selection is in leading spaces.
     *
     * Default Value: {@code 1}  -> The editor will always delete only 1 space.
     * Special Value: {@code -1} -> Follow tab size
     */
    public int deleteMultiSpaces = 1;

    /**
     * Set to {@code false} if you don't want the editor to go fullscreen on devices with smaller screen size.
     * Otherwise, set to {@code true}
     * <p>
     * Default value is {@code false}
     */
    public boolean allowFullscreen = false;

    /**
     * Control whether auto-completes for symbol pairs.
     *
     * Such as automatically adding a ')' when '(' is entered
     */
    public boolean symbolPairAutoCompletion = true;

    /**
     * Show auto-completion even when there is composing text set by the IME in editor.
     *
     * Note: composing text is usually a small piece of text you are typing. It is displayed with an
     * underline in editor.
     * This is useful when the user uses an input method that does not support the attitude {@link EditorInfo#TYPE_TEXT_FLAG_NO_SUGGESTIONS}.
     * When this switch is set to false, the editor will not provide auto-completion
     * when there is any composing text in editor.
     */
    public boolean autoCompletionOnComposing = true;

    /**
     * Set whether auto indent should be executed when user enters
     * a NEWLINE.
     *
     * Enabling this will automatically copy the leading spaces on this line to the new line.
     */
    public boolean autoIndent = true;

    /**
     * Define symbol pairs for any language,
     * Override language settings.
     * <p>
     * Use {@link SymbolPairMatch.Replacement#NO_REPLACEMENT} to force no completion for a character
     */
    @NonNull
    public final SymbolPairMatch overrideSymbolPairs = new SymbolPairMatch();

    /**
     * Disallow suggestions from keyboard forcibly by preventing
     * {@link android.view.inputmethod.InputConnection#setComposingText(CharSequence, int)} and
     * {@link android.view.inputmethod.InputConnection#setComposingRegion(int, int)} taking effects.
     * <p>
     * This may not be always good for all IMEs, as keyboards' strategy varies.
     */
    public boolean disallowSuggestions = false;

    /**
     * Max text length that can be extracted by {@link android.view.inputmethod.InputConnection#getExtractedText(ExtractedTextRequest, int)}
     * and other methods related to text content.
     * <p>
     * Usually you need to make it big enough so that the IME does it work for its symbol pair match (at least
     * some Chinese keyboards need it).
     * Text exceeds the limit will be cut, but editor will make sure the selection region is in the extracted text.
     * Some IMEs ignore the {@link android.view.inputmethod.ExtractedText#startOffset} and if the length exceeds this
     * limit, they may not work properly.
     * <p>
     * Set it to 0 to send no text to IME.
     */
    public int maxIPCTextLength = 500000;

    /**
     * Whether over scroll is permitted.
     * When over scroll is enabled, the user will be able to scroll out of displaying
     * bounds if the user scroll fast enough.
     * This is implemented by {@link OverScroller#fling(int, int, int, int, int, int, int, int, int, int)}
     */
    public boolean overScrollEnabled = false;

    /**
     * Allow fling scroll
     */
    public boolean scrollFling = true;

    /**
     * If the two completion requests are sent within this time, the completion will not
     * show.
     */
    public long cancelCompletionNs = 70 * 1000000;


}
