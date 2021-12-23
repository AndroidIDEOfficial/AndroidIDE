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

package com.itsaky.androidide.fragments.attreditors;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.itsaky.androidide.databinding.LayoutDimensionEditorBinding;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.attrinfo.models.Attr;

public class DimensionAttrEditor extends IAttrEditorFragment {
    
    private LayoutDimensionEditorBinding binding;
    
    @Override
    protected IAttrEditorFragment newInstance (XMLAttribute attribute) {
        final var fragment = new DimensionAttrEditor ();
        Bundle args = new Bundle ();
        args.putParcelable (ATTRIBUTE_KEY, attribute);
        fragment.setArguments (args);
        return fragment;
    }
    
    @Override
    protected int[] getSupportedFormats () {
        // TODO Should we consider Attr.ENUM as well? Maybe for match_parent and wrap_content.
        return new int[] { Attr.DIMENSION };
    }
    
    @Override
    protected View createEditorView (LayoutInflater inflater, ViewGroup parent) {
        this.binding = LayoutDimensionEditorBinding.inflate (inflater, parent, false);
        return this.binding.getRoot ();
    }
    
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
    }
}
