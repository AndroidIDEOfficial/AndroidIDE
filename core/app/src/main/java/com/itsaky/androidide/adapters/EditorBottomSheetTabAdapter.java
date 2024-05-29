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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.itsaky.androidide.fragments.DiagnosticsListFragment;
import com.itsaky.androidide.fragments.SearchResultFragment;
import com.itsaky.androidide.fragments.output.AppLogFragment;
import com.itsaky.androidide.fragments.output.BuildOutputFragment;
import com.itsaky.androidide.fragments.output.IDELogFragment;
import com.itsaky.androidide.resources.R;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorBottomSheetTabAdapter extends FragmentStateAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(EditorBottomSheetTabAdapter.class);
  private final List<Tab> fragments;

  public EditorBottomSheetTabAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);

    var index = -1;
    this.fragments = new ArrayList<>();
    this.fragments.add(
        new Tab(
            fragmentActivity.getString(R.string.build_output),
            BuildOutputFragment.class,
            ++index));
    this.fragments.add(
        new Tab(fragmentActivity.getString(R.string.app_logs), AppLogFragment.class, ++index));
    this.fragments.add(
        new Tab(fragmentActivity.getString(R.string.ide_logs), IDELogFragment.class, ++index));
    this.fragments.add(
        new Tab(
            fragmentActivity.getString(R.string.view_diags),
            DiagnosticsListFragment.class,
            ++index));
    this.fragments.add(
        new Tab(
            fragmentActivity.getString(R.string.view_search_results),
            SearchResultFragment.class,
            ++index));
  }

  public Fragment getFragmentAtIndex(int index) {
    return getFragmentById(getItemId(index));
  }

  @Nullable
  public Fragment getFragmentById(long itemId) {
    final var fragments = getFragments();
    if (fragments != null) {
      return fragments.get(itemId);
    }

    return null;
  }

  @Nullable
  private LongSparseArray<Fragment> getFragments() {
    try {
      final var field = FragmentStateAdapter.class.getDeclaredField("mFragments");
      field.setAccessible(true);
      return (LongSparseArray<Fragment>) field.get(this);
    } catch (Throwable th) {
      LOG.error("Unable to reflect fragment list from adapter.");
    }

    return null;
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    try {
      final var tab = fragments.get(position);
      final var klass = Class.forName(tab.name).asSubclass(Fragment.class);
      final var constructor = klass.getDeclaredConstructor();
      constructor.setAccessible(true);
      return constructor.newInstance();
    } catch (Throwable th) {
      throw new RuntimeException("Unable to create fragment", th);
    }
  }

  @Override
  public int getItemCount() {
    return fragments.size();
  }

  public String getTitle(int position) {
    return fragments.get(position).title;
  }

  @Nullable
  public BuildOutputFragment getBuildOutputFragment() {
    return findFragmentByClass(BuildOutputFragment.class);
  }

  @Nullable
  private <T extends Fragment> T findFragmentByClass(Class<T> clazz) {
    final var name = clazz.getName();
    for (final var tab : this.fragments) {
      if (tab.name.equals(name)) {
        return (T) getFragmentById(tab.itemId);
      }
    }

    return null;
  }

  @Nullable
  public AppLogFragment getLogFragment() {
    return findFragmentByClass(AppLogFragment.class);
  }

  @Nullable
  public DiagnosticsListFragment getDiagnosticsFragment() {
    return findFragmentByClass(DiagnosticsListFragment.class);
  }

  @Nullable
  public SearchResultFragment getSearchResultFragment() {
    return findFragmentByClass(SearchResultFragment.class);
  }

  public <T extends Fragment> int findIndexOfFragmentByClass(@NonNull Class<T> tClass) {
    final var name = tClass.getName();
    for (int i = 0; i < this.fragments.size(); i++) {
      final var tab = this.fragments.get(i);
      if (tab.name.equals(name)) {
        return i;
      }
    }

    return -1;
  }

  static class Tab {

    final String title;
    final String name;
    final long itemId;

    public Tab(String title, @NonNull Class<? extends Fragment> fragment, long id) {
      this.title = title;
      this.name = fragment.getName();
      this.itemId = id;
    }
  }
}
