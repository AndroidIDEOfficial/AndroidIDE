/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/

package com.itsaky.androidide.fragments.sheets;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.adapters.OptionsSheetAdapter;
import com.itsaky.androidide.models.SheetOption;

import java.util.ArrayList;
import java.util.List;

public class OptionsListFragment extends BaseBottomSheetFragment {

    private RecyclerView mList;
    private OnOptionsClickListener listener;
    private final List<SheetOption> mOptions = new ArrayList<>();

    protected boolean dismissOnItemClick = true;

    @Override
    protected void bind(LinearLayout container) {
        mList = new RecyclerView(getContext());
        container.removeAllViews();
        container.addView(mList, new LinearLayout.LayoutParams(-1, -1));
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setAdapter(
                new OptionsSheetAdapter(
                        mOptions,
                        __ -> {
                            if (dismissOnItemClick) dismiss();
                            if (listener != null) listener.onOptionsClick(__);
                        }));
    }

    public OptionsListFragment setOnOptionsClickListener(OnOptionsClickListener listener) {
        this.listener = listener;
        return this;
    }

    public OptionsListFragment addOption(SheetOption option) {
        if (!mOptions.contains(option)) {
            mOptions.add(option);
        }

        return this;
    }

    public OptionsListFragment removeOption(int optionIndex) {
        return removeOption(mOptions.get(optionIndex));
    }

    public OptionsListFragment removeOption(SheetOption option) {
        mOptions.remove(option);
        return this;
    }

    public OptionsListFragment setDismissOnItemClick(boolean dissmiss) {
        this.dismissOnItemClick = dissmiss;
        return this;
    }

    @Override
    protected String getTitle() {
        return getString(com.itsaky.androidide.R.string.file_options);
    }

    public static interface OnOptionsClickListener {
        void onOptionsClick(SheetOption option);
    }
}
