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


package com.itsaky.androidide.adapters;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class MainPagerAdapter extends FragmentPagerAdapter
{
	private ArrayList<Fragment> mFragmentList;

	public MainPagerAdapter(FragmentManager manager)
	{
		super(manager);
		mFragmentList = new ArrayList<>();
	}

	@Override
	public Fragment getItem(int p1)
	{
		return mFragmentList != null && p1 < mFragmentList.size() ? mFragmentList.get(p1) : null;
	}

	@Override
	public int getCount()
	{
		return mFragmentList != null ? mFragmentList.size() : 0;
	}
	
	public MainPagerAdapter addFragment(Fragment frag)
	{
		getFragments().add(frag);
		return this;
	}
	
	public ArrayList<Fragment> getFragments()
	{
		return mFragmentList == null ? mFragmentList = new ArrayList<>() : mFragmentList;
	}
}
