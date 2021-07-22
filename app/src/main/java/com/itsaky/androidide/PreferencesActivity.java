package com.itsaky.androidide;

import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityPreferencesBinding;
import com.itsaky.androidide.fragments.preferences.AppearancePreferences;
import com.itsaky.androidide.fragments.preferences.BuildPreferences;
import com.itsaky.androidide.fragments.preferences.EditorPreferences;
import com.itsaky.androidide.fragments.preferences.IDEPreferences;

public class PreferencesActivity extends StudioActivity {
	
	private ActivityPreferencesBinding binding;
	private IDEPreferences mPref;
	
	@Override
	protected View bindLayout() {
		binding = ActivityPreferencesBinding.inflate(getLayoutInflater());
		return binding.getRoot();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setSupportActionBar(binding.toolbar);
		getSupportActionBar().setTitle(R.string.ide_preferences);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		loadFragment(getPrefsFragment());
	}
	
	private IDEPreferences getPrefsFragment() {
		return mPref == null ? mPref = new IDEPreferences() : mPref;
	}
	
	private void loadFragment(Fragment frag) {
		super.loadFragment(frag, binding.fragmentContainer.getId());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
