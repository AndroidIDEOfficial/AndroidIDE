package com.itsaky.androidide.fragments;

import androidx.fragment.app.Fragment;
import com.itsaky.androidide.app.StudioActivity;

public class BaseFragment extends Fragment
{
	protected StudioActivity getStudioActivity() {
		return (StudioActivity) getActivity();
	}
}
