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

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.adapters.OptionsSheetAdapter;
import com.itsaky.androidide.events.FileContextMenuItemClickEvent;
import com.itsaky.androidide.models.SheetOption;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class OptionsListFragment extends BaseBottomSheetFragment {

  private final List<SheetOption> mOptions = new ArrayList<>();
  protected boolean dismissOnItemClick = true;
  private RecyclerView mList;

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mList.setLayoutManager(new LinearLayoutManager(getContext()));
    mList.setAdapter(
        new OptionsSheetAdapter(
            mOptions,
            option -> {
              if (dismissOnItemClick) dismiss();
              final var event = new FileContextMenuItemClickEvent(option);
              event.put(Context.class, requireContext());
              EventBus.getDefault().post(event);
            }));
  }

  @Override
  protected String getTitle() {
    return getString(com.itsaky.androidide.resources.R.string.file_options);
  }

  @Override
  protected void bind(@NonNull LinearLayout container) {
    mList = new RecyclerView(requireContext());
    container.removeAllViews();
    container.addView(mList, new LinearLayout.LayoutParams(-1, -1));
  }

  public void addOption(SheetOption option) {
    if (!mOptions.contains(option)) {
      mOptions.add(option);
    }
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
}
