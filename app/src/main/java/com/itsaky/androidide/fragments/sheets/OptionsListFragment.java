package com.itsaky.androidide.fragments.sheets;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itsaky.androidide.adapters.OptionsSheetAdapter;
import com.itsaky.androidide.models.SheetOption;
import java.util.ArrayList;
import java.util.List;

public class OptionsListFragment extends BaseBottomSheetFragment {
	
	private RecyclerView mList;
	private OnOptionsClickListener listener;
	private final List<SheetOption> mOptions = new ArrayList<>();
	
	@Override
	protected void bind(LinearLayout container) {
		mList = new RecyclerView(getContext());
		container.removeAllViews();
		container.addView(mList, new LinearLayout.LayoutParams(-1, -1));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mList.setLayoutManager(new LinearLayoutManager(getContext()));
		mList.setAdapter(new OptionsSheetAdapter(mOptions, __ -> { dismiss(); if(listener != null) listener.onOptionsClick(__);}));
	}
	
	public OptionsListFragment setOnOptionsClickListener(OnOptionsClickListener listener) {
		this.listener = listener;
		return this;
	}
	
	public OptionsListFragment addOption(SheetOption option) {
		if(!mOptions.contains(option)) {
			mOptions.add(option);
		}
		
		return this;
	}
	
	public OptionsListFragment removeOption(int optionIndex) {
		return removeOption(mOptions.get(optionIndex));
	}
	
	public OptionsListFragment removeOption(SheetOption option) {
		mOptions.remove(option);
		return this;
	}

	@Override
	protected String getTitle() {
		return getString(com.itsaky.androidide.R.string.file_options);
	}
	
	public static interface OnOptionsClickListener {
		void onOptionsClick(SheetOption option);
	}
}
