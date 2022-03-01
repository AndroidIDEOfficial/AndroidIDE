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

package com.itsaky.androidide.fragments.sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.adapters.AttrListAdapter;
import com.itsaky.attrinfo.models.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * A bottom sheet dialog for showing a simple list of
 * text icon pair.
 *
 * @author Akash Yadav
 * @see com.itsaky.androidide.models.IconTextListItem
 */
public class AttributeListSheet extends BottomSheetDialogFragment {
    
    private RecyclerView mList;
    private List<Attr> mItems;
    
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mList = new RecyclerView (inflater.getContext ());
        mList.setLayoutManager (new LinearLayoutManager (inflater.getContext ()));
        mList.setLayoutParams (new ViewGroup.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        
        return mList;
    }
    
    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        mList.setAdapter (new AttrListAdapter (mItems));
    }
    
    public void setItems (@Nullable List<Attr> items) {
        if (items == null) {
            items = new ArrayList<> ();
        }
        
        mItems = items;
        update ();
    }
    
    public void update () {
        if (mList != null) {
            mList.setAdapter (new AttrListAdapter (mItems));
        }
    }
}
