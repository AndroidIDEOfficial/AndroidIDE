package com.itsaky.androidide;

import abhishekti7.unicorn.filepicker.UnicornFilePicker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.adapters.MainPagerAdapter;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityMainBinding;
import com.itsaky.androidide.fragments.MainFragment;
import com.itsaky.androidide.fragments.ProjectsFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.views.VerticalViewPager;
import com.itsaky.toaster.Toaster;
import java.io.File;
import java.util.ArrayList;
import com.itsaky.androidide.utils.Environment;

public class MainActivity extends StudioActivity
{
	private ActivityMainBinding binding;
	
	private MainFragment mMainFragment;
	private ProjectsFragment mProjectsFragment;
	private MainPagerAdapter mPagerAdapter;
	
	private int currentItem = 0;
	
    public static final String TAG = "MainActivity";

	@Override
	protected View bindLayout() {
		binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager())
							.addFragment(getMainFragment())
							.addFragment(getProjectsFragment());
		binding.mainViewPager.setAdapter(mPagerAdapter);
		binding.mainMoveDown.hide();
		binding.mainMoveDown.setOnClickListener(v -> showFirst());
		binding.settings.setOnClickListener(v -> gotoSettings());
		binding.mainViewPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener(){

				@Override
				public void onPageScrolled(int p1, float p2, int p3) {
					float elevation = SizeUtils.dp2px(8) * (currentItem == 0 ? p2 : -p2);
					binding.activitymainTextView1.setElevation(elevation);
				}

				@Override
				public void onPageSelected(int p1) {
					currentItem = p1;
					if(p1 == 1)
						binding.mainMoveDown.show();
					else binding.mainMoveDown.hide();
				}

				@Override
				public void onPageScrollStateChanged(int p1) {
				}
			});
    }
	
	private void gotoSettings() {
		startActivity(new Intent(this, PreferencesActivity.class));
	}
	
	private void showFirst() {
		if(binding.mainViewPager.getCurrentItem() != 0)
			binding.mainViewPager.setCurrentItem(0, true);
	}

	@Override
	protected void onStorageGranted()
	{
		getProjectsFragment().loadProjects();
	}

	@Override
	protected void onStorageDenied()
	{
		getApp().toast(R.string.msg_storage_denied, Toaster.Type.ERROR);
		finishAffinity();
	}
	
	private MainFragment getMainFragment() {
		return mMainFragment == null ? mMainFragment = new MainFragment().setCreateProjectListener(() -> getProjectsFragment().loadProjects()).setOnCreateProjectVisibilityListener((isVisible) -> binding.mainViewPager.setScrollEnabled(!isVisible)) : mMainFragment;
	}
	
	private ProjectsFragment getProjectsFragment() {
		return mProjectsFragment == null ? mProjectsFragment = new ProjectsFragment() : mProjectsFragment;
	}
}
