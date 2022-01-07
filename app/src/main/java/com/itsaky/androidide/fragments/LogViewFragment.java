/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;
import com.itsaky.androidide.language.logs.LogLanguageImpl;
import com.itsaky.androidide.models.LogLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.rosemoe.editor.interfaces.EditorLanguage;

public class LogViewFragment extends NonEditableEditorFragment {
    
    private final EditorLanguage language = getLanguage ();
    private final List<LogLine> unsavedLines = new ArrayList<> ();
    
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        Objects.requireNonNull (getEditor ()).setEditorLanguage (language);
        
        if (!unsavedLines.isEmpty ()) {
            for (var line : unsavedLines) {
                applyToLanguage (line);
                getEditor ().getText ().append (line.toString ().trim () + "\n");
            }
            unsavedLines.clear ();
        }
    }
    
    public void appendLog (LogLine line) {
        if (getEditor () == null) {
            unsavedLines.add (line);
            return;
        }
    
        applyToLanguage (line);
    
        final var lineString = line.toString ();
        final var msg = lineString.endsWith ("\n") ? lineString : lineString + "\n";
        ThreadUtils.runOnUiThread (() -> getEditor ().getText ().append (msg));
    }
    
    private void applyToLanguage (LogLine line) {
        if (language instanceof LogLanguageImpl) {
            ((LogLanguageImpl)this.language).addLine (line);
        }
    }
    
    protected EditorLanguage getLanguage () {
        return new LogLanguageImpl ();
    }
}
