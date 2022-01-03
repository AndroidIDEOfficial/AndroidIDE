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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itsaky.androidide.R;
import com.itsaky.androidide.fragments.SimpleOutputFragment;
import com.itsaky.androidide.fragments.DiagnosticsListFragment;
import com.itsaky.androidide.fragments.IDELogFragment;
import com.itsaky.androidide.fragments.LogViewFragment;
import com.itsaky.androidide.fragments.SearchResultFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditorBottomSheetTabAdapter extends FragmentStateAdapter {
    
    private final List<Tab> fragments;
    
    public EditorBottomSheetTabAdapter (@NonNull FragmentActivity fragmentActivity) {
        super (fragmentActivity);
        
        this.fragments = new ArrayList<> ();
        this.fragments.add (new Tab (fragmentActivity.getString (R.string.build_output), new SimpleOutputFragment ()));
        this.fragments.add (new Tab (fragmentActivity.getString (R.string.app_logs), new LogViewFragment ()));
        this.fragments.add (new Tab (fragmentActivity.getString (R.string.ide_logs), new IDELogFragment ()));
        this.fragments.add (new Tab (fragmentActivity.getString (R.string.view_diags), new DiagnosticsListFragment ()));
        this.fragments.add (new Tab (fragmentActivity.getString (R.string.view_search_results), new SearchResultFragment ()));
    }
    
    @NonNull
    @Override
    public Fragment createFragment (int position) {
        return this.fragments.get (position).fragment;
    }
    
    @Override
    public int getItemCount () {
        return fragments.size ();
    }
    
    public String getTitle (int position) {
        return fragments.get (position).title;
    }
    
    @NonNull
    public SimpleOutputFragment getBuildOutputFragment () {
        return Objects.requireNonNull (findFragmentByClass (SimpleOutputFragment.class));
    }
    
    @NonNull
    public LogViewFragment getLogFragment () {
        return Objects.requireNonNull (findFragmentByClass (LogViewFragment.class));
    }
    
    @NonNull
    public DiagnosticsListFragment getDiagnosticsFragment () {
        return Objects.requireNonNull (findFragmentByClass (DiagnosticsListFragment.class));
    }
    
    @NonNull
    public SearchResultFragment getSearchResultFragment () {
        return Objects.requireNonNull (findFragmentByClass (SearchResultFragment.class));
    }
    
    @Nullable
    private <T extends Fragment> T findFragmentByClass (Class<T> clazz) {
        for (final var tab : this.fragments) {
            if (tab.fragment.getClass () == clazz) {
                return (T) tab.fragment;
            }
        }
        return null;
    }
    
    public <T extends Fragment> int findIndexOfFragmentByClass (Class<T> tClass) {
        for (int i = 0; i < this.fragments.size (); i++) {
            final var tab = this.fragments.get (i);
            if (tab.fragment.getClass () == tClass) {
                return i;
            }
        }
        
        return -1;
    }
    
    static class Tab {
        
        public Tab (String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }
        
        String title;
        Fragment fragment;
    }
}
