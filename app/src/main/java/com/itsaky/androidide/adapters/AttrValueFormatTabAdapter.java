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
package com.itsaky.androidide.adapters;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.itsaky.androidide.fragments.attr.BaseValueEditorFragment;
import com.itsaky.androidide.models.XMLAttribute;
import com.itsaky.androidide.utils.ILogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Adapter for value format tabs of attribute editor in UI Designer.
 *
 * @author Akash Yadav
 */
@SuppressWarnings("deprecation")
public class AttrValueFormatTabAdapter extends FragmentStatePagerAdapter {

  private static final ILogger LOG = ILogger.newInstance("AttrValueEditorTabAdapter");
  private final List<Fragment> mFragments = new ArrayList<>();
  private final List<String> mTitles = new ArrayList<>();
  private final XMLAttribute attribute;

  public AttrValueFormatTabAdapter(@NonNull FragmentManager manager, XMLAttribute attribute) {
    super(manager);
    Objects.requireNonNull(attribute);

    this.attribute = attribute;
  }

  public void addFragment(final Class<? extends BaseValueEditorFragment> fragment, String title) {
    Objects.requireNonNull(fragment);
    Objects.requireNonNull(title);
    if (TextUtils.isEmpty(title.trim())) {
      throw new IllegalArgumentException("Invalid tab title");
    }

    mTitles.add(capitalize(title));
    mFragments.add(createFragment(fragment, title));
  }

  @NonNull
  private String capitalize(String title) {
    title = title.trim();

    final var sb = new StringBuilder();
    for (int i = 0; i < title.length(); i++) {
      var ch = title.charAt(i);
      if (i == 0) {
        ch = Character.toUpperCase(ch);
      } else {
        ch = Character.toLowerCase(ch);
      }

      sb.append(ch);
    }

    return sb.toString();
  }

  @NonNull
  public Fragment createFragment(
      Class<? extends BaseValueEditorFragment> clazz, @NonNull final String name) {
    try {
      final var frag = clazz.newInstance();
      final var args = new Bundle();
      args.putParcelable(BaseValueEditorFragment.KEY_ATTR, attribute);
      args.putString(BaseValueEditorFragment.KEY_NAME, name);
      frag.setArguments(args);

      return frag;
    } catch (Throwable th) {
      LOG.error("Unable to create value editor fragment", th);
      throw new RuntimeException(th);
    }
  }

  public void removeAll() {
    mFragments.clear();
    mTitles.clear();
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    return mFragments.get(position);
  }

  @Override
  public int getCount() {
    return mFragments.size();
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mTitles.get(position);
  }
}
