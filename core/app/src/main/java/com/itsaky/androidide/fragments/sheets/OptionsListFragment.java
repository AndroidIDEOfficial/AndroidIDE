/*
 * This file is part of AndroidIDE.
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
 */

package com.itsaky.androidide.fragments.sheets;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.SizeUtils;
import com.itsaky.androidide.adapters.OptionsSheetAdapter;
import com.itsaky.androidide.events.FileContextMenuItemClickEvent;
import com.itsaky.androidide.models.SheetOption;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class OptionsListFragment extends BaseBottomSheetFragment {

  private final List<SheetOption> mOptions = new ArrayList<>();
  protected boolean dismissOnItemClick = true;
  private RecyclerView mList;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState
  ) {
    final var dp8 = SizeUtils.dp2px(8);
    final var dp16 = dp8 * 2;
    mList = new RecyclerView(requireContext());
    mList.setLayoutParams(new LayoutParams(-1, -1));
    mList.setPaddingRelative(dp16, dp8, dp16, dp8);
    return mList;
  }

  @Override
  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mList.setLayoutManager(new LinearLayoutManager(getContext()));
    mList.setAdapter(new OptionsSheetAdapter(mOptions, option -> {
      if (dismissOnItemClick) {
        dismiss();
      }
      final var event = new FileContextMenuItemClickEvent(option);
      event.put(Context.class, requireContext());
      EventBus.getDefault().post(event);
    }));
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
