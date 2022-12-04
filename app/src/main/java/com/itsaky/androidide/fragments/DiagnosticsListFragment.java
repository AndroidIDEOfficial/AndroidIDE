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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itsaky.androidide.adapters.DiagnosticsAdapter;
import com.itsaky.androidide.ui.EmptyView;

import java.util.ArrayList;

public class DiagnosticsListFragment extends Fragment {

  private RecyclerView list;
  private EmptyView emptyView;

  private DiagnosticsAdapter unsavedAdapter;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent,
      @Nullable Bundle savedInstanceState) {
    final var container = new LinearLayout(inflater.getContext());
    container.setOrientation(LinearLayout.VERTICAL);
    container.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));

    this.list = new RecyclerView(inflater.getContext());
    this.emptyView = new EmptyView(inflater.getContext());

    container.addView(list, new LinearLayout.LayoutParams(-1, -1));
    container.addView(emptyView, new LinearLayout.LayoutParams(-1, -1));

    return container;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    list.setLayoutManager(new LinearLayoutManager(list.getContext()));
    list.setAdapter(
        unsavedAdapter != null ? unsavedAdapter : new DiagnosticsAdapter(new ArrayList<>(), null));
    unsavedAdapter = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unsavedAdapter = null;
    list = null;
    emptyView = null;
  }

  public void setAdapter(@NonNull DiagnosticsAdapter adapter) {
    if (list != null) {
      list.setAdapter(adapter);
    } else {
      unsavedAdapter = adapter;
    }
  }

  public void handleResultVisibility(boolean error) {
    if (emptyView != null && list != null) {
      emptyView.setVisibility(error ? View.VISIBLE : View.GONE);
      list.setVisibility(error ? View.GONE : View.VISIBLE);
    }
  }
}
