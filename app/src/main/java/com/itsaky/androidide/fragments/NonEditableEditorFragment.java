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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.itsaky.androidide.databinding.FragmentNonEditableEditorBinding;
import com.itsaky.androidide.syntax.colorschemes.SchemeAndroidIDE;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.views.editor.IDEEditor;
import io.github.rosemoe.sora.lang.EmptyLanguage;

public abstract class NonEditableEditorFragment extends Fragment {

    private static final Logger LOG = Logger.newInstance ("NonEditableEditorFragment");
    private FragmentNonEditableEditorBinding binding;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        this.binding = FragmentNonEditableEditorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final var editor = binding.getRoot();
        editor.setEditable(false);
        editor.setDividerWidth(0);
        editor.setEditorLanguage(new EmptyLanguage());
        editor.setWordwrap(false);
        editor.setUndoEnabled(false);
        editor.setTypefaceLineNumber(TypefaceUtils.jetbrainsMono());
        editor.setTypefaceText(TypefaceUtils.jetbrainsMono());
        editor.setTextSize(12);
        editor.setColorScheme(new SchemeAndroidIDE());
    }

    @Nullable
    public IDEEditor getEditor() {
        if (binding == null) {
            return null;
        }
        return binding.editor;
    }
}
