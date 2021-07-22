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
