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

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itsaky.androidide.R;
import com.itsaky.androidide.adapters.AttrListAdapter;
import com.itsaky.androidide.databinding.LayoutAddAttrSheetBinding;
import com.itsaky.attrinfo.models.Attr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A bottom sheet dialog for showing a simple list of text icon pair.
 *
 * @author Akash Yadav
 * @see com.itsaky.androidide.models.IconTextListItem
 */
public class AttributeListSheet extends BottomSheetDialogFragment {

  private LayoutAddAttrSheetBinding binding;
  private Consumer<Attr> clickConsumer;
  private List<Attr> mItems;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_SheetAboveKeyboard);
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    this.binding = LayoutAddAttrSheetBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    binding.searchBar.setOnQueryTextListener(
        new SearchView.OnQueryTextListener() {

          @Override
          public boolean onQueryTextSubmit(String query) {
            return filterAttributes(query.trim());
          }

          @Override
          public boolean onQueryTextChange(String newText) {
            return filterAttributes(newText.trim());
          }
        });
    binding.attrList.setAdapter(new AttrListAdapter(clickConsumer, mItems));
  }

  private boolean filterAttributes(@NonNull String query) {
    if (mItems == null || TextUtils.isEmpty(query)) {
      update();
      return true;
    }

    final var filtered =
        this.mItems.stream()
            .filter(
                attr -> attr.name.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)))
            .collect(Collectors.toList());

    update(filtered);
    return true;
  }

  public void setItems(@Nullable List<Attr> items) {
    if (items == null) {
      items = new ArrayList<>();
    }

    mItems = items;
    update();
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    //noinspection unchecked
    this.clickConsumer = (Consumer<Attr>) getParentFragment();
  }

  public void update() {
    update(mItems);
  }

  public void update(List<Attr> items) {
    if (binding != null) {
      binding.attrList.setAdapter(new AttrListAdapter(clickConsumer, items));
    }
  }
}
