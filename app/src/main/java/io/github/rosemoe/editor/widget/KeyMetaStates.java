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
package io.github.rosemoe.editor.widget;

import android.text.Editable;
import android.view.KeyEvent;

/**
 * Handles key events such SHIFT
 * @author Rosemoe
 */
public class KeyMetaStates extends android.text.method.MetaKeyKeyListener {

    private CodeEditor editor;
    /**
     * Dummy text used for Android original APIs
     */
    private Editable dest = Editable.Factory.getInstance().newEditable("");

    public KeyMetaStates(CodeEditor editor) {
        this.editor = editor;
    }

    public void onKeyDown(KeyEvent event) {
        super.onKeyDown(editor, dest, event.getKeyCode(), event);
    }

    public void onKeyUp(KeyEvent event) {
        super.onKeyUp(editor, dest, event.getKeyCode(), event);
    }

    public boolean isShiftPressed() {
        return getMetaState(dest, META_SHIFT_ON) != 0;
    }

    public void adjust() {
        adjustMetaAfterKeypress(dest);
    }

    public void clearMetaStates(int states) {
        clearMetaKeyState(editor, dest, states);
    }

}
