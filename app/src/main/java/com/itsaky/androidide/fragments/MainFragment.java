package com.itsaky.androidide.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutCreateProjectBinding;
import com.itsaky.androidide.databinding.LayoutCreateProjectContentBinding;
import com.itsaky.androidide.databinding.LayoutMainBinding;
import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.views.transition.ArcMotionPlus;
import com.itsaky.toaster.Toaster;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import com.itsaky.androidide.utils.ProjectWriter;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.ProjectCreatorCallable;

public class MainFragment extends BaseFragment implements OnClickListener, ProjectWriterCallback
{
	private LayoutMainBinding binding;
	private LayoutCreateProjectBinding createBinding;
	private LayoutCreateProjectContentBinding createLayoutBinding;
	
	private OnCreateProjectListener createProjectListener;
	private ProgressSheet mWritingProgressSheet;
	
	private ArrayList<ProjectTemplate> mTemplates = new ArrayList<ProjectTemplate>();
	private OnCreateProjectVisibilityListener visibilityChangeListener;
	
	public static final String TAG = "MainFragment";
	private int currentTemplateIndex = 0;

	public MainFragment setCreateProjectListener(OnCreateProjectListener createProjectListener) {
		this.createProjectListener = createProjectListener;
		return this;
	}
	
	public MainFragment setOnCreateProjectVisibilityListener(OnCreateProjectVisibilityListener visibilityChangeListener) {
		this.visibilityChangeListener = visibilityChangeListener;
		return this;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = LayoutMainBinding.inflate(inflater, container, false);
		createBinding = binding.createLayout;
		createLayoutBinding = createBinding.createProjectContent;
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(final View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		binding.createNew.setOnClickListener(this);
		createBinding.createprojectClose.setOnClickListener(this);
		createLayoutBinding.createprojectCreate.setOnClickListener(this);
		createLayoutBinding.previousCard.setOnClickListener(this);
		createLayoutBinding.nextCard.setOnClickListener(this);
		binding.createNewCard.setVisibility(View.GONE);
		
		currentTemplateIndex = 0;
		createTemplates();
		createProgressSheet();
		showTemplate(currentTemplateIndex);
	}

	@Override
	public void onClick(View p1) {
		if(p1.getId() == binding.createNew.getId())
			showCreateProject();
		else if(p1.getId() == createBinding.createprojectClose.getId())
			hideCreateProject();
		else if(p1.getId() == createLayoutBinding.nextCard.getId()) {
			currentTemplateIndex++;
			if(currentTemplateIndex >= mTemplates.size()) currentTemplateIndex = 0;
			showTemplate(currentTemplateIndex);
		} else if(p1.getId() == createLayoutBinding.previousCard.getId()) {
			currentTemplateIndex--;
			if(currentTemplateIndex < 0) currentTemplateIndex = mTemplates.size() - 1;
			showTemplate(currentTemplateIndex);
		} else if(p1.getId() == createLayoutBinding.createprojectCreate.getId())
			createNewProject();
	}

	@Override
	public void beforeBegin() {
		if(mWritingProgressSheet == null)
			createProgressSheet();
			
		setMessage(R.string.msg_begin_project_write);
	}

	@Override
	public void onProcessTask(String taskName) {
		setMessage(taskName);
	}

	@Override
	public void onSuccess() {
		if(mWritingProgressSheet != null)
			createProgressSheet();
			
		if(mWritingProgressSheet.isShowing())
			mWritingProgressSheet.dismiss();
		getStudioActivity().getApp().toast(R.string.project_created_successfully, Toaster.Type.SUCCESS);
		if(createProjectListener != null)
			createProjectListener.onNewProjectCreated();
	}
	
	@Override
	public void onFailed(String reason) {
		if(mWritingProgressSheet != null)
			createProgressSheet();

		if(mWritingProgressSheet.isShowing())
			mWritingProgressSheet.dismiss();
		getStudioActivity().getApp().toast(reason, Toaster.Type.ERROR);
	}
	
	private void showTemplate(int index) {
		if(index < 0 || index >= mTemplates.size()) return;
		final ProjectTemplate template = mTemplates.get(index);
		createLayoutBinding.createprojectTemplateLabel.setText(template.getName());
		createLayoutBinding.createprojectTemplateDescription.setText(template.getDescription());
		createLayoutBinding.createprojectTemplateImage.setImageResource(template.getImageId());
	}
	
	/**
	 * Must be called in/after onViewCreated
	 */
	private void createTemplates() {
		mTemplates = new ArrayList<>();

		ProjectTemplate 
			empty = new ProjectTemplate()
				.setId(0)
				.setName(getContext(), R.string.template_empty)
				.setDescription(getContext(), R.string.template_description_empty)
				.setImageId(R.drawable.template_empty),
			basic = new ProjectTemplate()
				.setId(1)
				.setName(getContext(), R.string.template_basic)
				.setDescription(getContext(), R.string.template_description_basic)
				.setImageId(R.drawable.template_basic),
			drawer = new ProjectTemplate()
				.setId(2)
				.setName(getContext(), R.string.template_navigation_drawer)		
				.setDescription(getContext(), R.string.template_description_navigation_drawer)
				.setImageId(R.drawable.template_navigation_drawer);
//			navigation = new ProjectTemplate()
//				.setId(3)
//				.setName(getContext(), R.string.template_bottom_navigation)
//				.setDescription(getContext(), R.string.template_description_bottom_navigation)
//				.setImageId(R.drawable.template_bottom_navigation),
//			kotlin = new ProjectTemplate()
//				.setId(4)
//				.setName(getContext(), R.string.template_basic_kotlin)
//				.setDescription(getContext(), R.string.template_description_kotlin)
//				.setImageId(R.drawable.template_kotlin);

		mTemplates.add(empty);
		mTemplates.add(basic);
		mTemplates.add(drawer);
//		mTemplates.add(navigation);
//		mTemplates.add(kotlin);
	}
	
	private void createProgressSheet() {
		mWritingProgressSheet = new ProgressSheet();
	}
	
	private void setMessage(int msg) {
		if(getContext() != null) {
			setMessage(getContext().getString(msg));
		}
	}
	
	private void setMessage(String msg) {
		mWritingProgressSheet.setMessage(msg);
	}

	private void createNewProject() {
		final String appName = createLayoutBinding.createprojectTextAppName.getEditText().getText().toString().trim();
		final String packageName = createLayoutBinding.createprojectTextPackageName.getEditText().getText().toString().trim();
		final int minSdk = getMinSdk();
		final int targetSdk = getTargetSdk();
		
		if(!isValid(appName, packageName, minSdk, targetSdk)) {
			getStudioActivity().getApp().toast(R.string.invalid_values, Toaster.Type.ERROR);
			return;
		}
			
		createProject(appName, packageName, minSdk, targetSdk);
	}

	private void createProject(String appName, String packageName, int minSdk, int targetSdk) {
		new TaskExecutor().executeAsync(new ProjectCreatorCallable(mTemplates.get(currentTemplateIndex), new NewProjectDetails(appName, packageName, minSdk, targetSdk), this), r -> {});
	}

	private boolean isValid(String appName, String packageName, int minSdk, int targetSdk)
	{
		return appName != null && appName.length() > 0
		&& packageName != null && packageName.length() > 0
		&& minSdk > 1 		   && minSdk < 99
		&& targetSdk > 1 	   && minSdk < 99;
	}

	private int getMinSdk()
	{
		try
		{
			return Integer.parseInt(createLayoutBinding.createprojectTextMinSdk.getEditText().getText().toString());
		} catch (Exception e)
		{ getStudioActivity().getApp().toast(e.getMessage(), Toaster.Type.ERROR); }
		return -1;
	}
	
	private int getTargetSdk()
	{
		try
		{
			return Integer.parseInt(createLayoutBinding.createprojectTextTargetSdk.getEditText().getText().toString());
		} catch (Exception e)
		{ getStudioActivity().getApp().toast(e.getMessage(), Toaster.Type.ERROR); }
		return -1;
	}
	
	private void showCreateProject()
	{
		ChangeBounds bounds = new ChangeBounds();
		bounds.setPathMotion(new ArcMotionPlus().setArcAngle(90));
		bounds.setDuration(300);
		bounds.addListener(new OnEndTransitionListener(() -> {
			final int x = (int) binding.createNew.getX() + (binding.createNew.getWidth() / 2);
			final int y = (int) binding.createNew.getY() + (binding.createNew.getHeight() / 2);
			binding.createNewCard.setVisibility(View.VISIBLE);
			int startRadius = binding.createNew.getWidth() / 2;
			int endRadius = Math.max(binding.getRoot().getWidth(), binding.getRoot().getHeight());
			Animator anim = ViewAnimationUtils.createCircularReveal(binding.createNewCard, x, y, startRadius, endRadius);
			anim.setDuration(600);
			anim.start();
			if(visibilityChangeListener != null)
				visibilityChangeListener.onVisibilityChanged(true);
			return null;
		}));
		TransitionManager.beginDelayedTransition(binding.getRoot(), bounds);
		
		ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) binding.createNew.getLayoutParams();
		p.topToTop = p.PARENT_ID;
		p.rightToRight = p.PARENT_ID;
		p.leftToLeft = p.UNSET;
		binding.createNew.setLayoutParams(p);
	}
	
	private void hideCreateProject()
	{
		int x = (int) binding.createNew.getX() + (binding.createNew.getWidth() / 2);
		int y = (int) binding.createNew.getY() + (binding.createNew.getHeight() / 2);
		int startRadius = Math.max(binding.getRoot().getWidth(), binding.getRoot().getHeight());
		int endRadius = binding.createNew.getWidth() / 2;
		Animator anim = ViewAnimationUtils.createCircularReveal(binding.createNewCard, x, y, startRadius, endRadius);
		anim.setDuration(500);
		anim.addListener(new AnimatorListenerAdapter()
		{
			@Override
			public void onAnimationEnd(Animator anim)
			{
				binding.createNewCard.setVisibility(View.GONE);
				if(visibilityChangeListener != null)
					visibilityChangeListener.onVisibilityChanged(false);
					
				ChangeBounds bounds = new ChangeBounds();
				bounds.setPathMotion(new ArcMotionPlus().setArcAngle(90));
				bounds.setDuration(300);
				TransitionManager.beginDelayedTransition(binding.getRoot(), bounds);
				
				ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams) binding.createNew.getLayoutParams();
				p.topToTop = p.UNSET;
				p.topToBottom = binding.greetingText.getId();
				p.rightToRight = p.PARENT_ID;
				p.leftToLeft = p.PARENT_ID;
				binding.createNew.setLayoutParams(p);
			}
		});
		anim.start();
	}
	
	private final class OnEndTransitionListener implements Transition.TransitionListener {
		
		Callable<Void> c;
		public OnEndTransitionListener(Callable<Void> c) {
			this.c = c;
		}

		@Override
		public void onTransitionStart(Transition p1) {
		}

		@Override
		public void onTransitionEnd(Transition p1) {
			try {
				c.call();
			} catch (Exception e) {}
		}

		@Override
		public void onTransitionCancel(Transition p1) {
		}

		@Override
		public void onTransitionPause(Transition p1) {
		}

		@Override
		public void onTransitionResume(Transition p1) {
		}
	}
	
	public interface OnCreateProjectVisibilityListener {
		public void onVisibilityChanged(boolean isVisible);
	}
	
	public static interface OnCreateProjectListener {
		public void onNewProjectCreated();
	}
}
