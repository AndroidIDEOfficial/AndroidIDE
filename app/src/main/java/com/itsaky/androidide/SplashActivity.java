package com.itsaky.androidide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivitySplashBinding;
import com.itsaky.androidide.models.ConstantsBridge;                               

public class SplashActivity extends StudioActivity
{
	private ActivitySplashBinding binding;
	
    private static final Handler mHandler = new Handler();
    private final Runnable mRunnable = new Runnable(){

        @Override
        public void run() {
            ConstantsBridge.SPLASH_TO_MAIN = true;
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

	@Override
	protected View bindLayout() {
		binding = ActivitySplashBinding.inflate(getLayoutInflater());
		return binding.getRoot();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		binding.splashText.setText(getString(R.string.msg_checking_storage_permissions));
    }

	@Override
	protected void onStorageGranted()
	{
		super.onStorageGranted();
		proceed();
	}

	@Override
	protected void onStorageAlreadyGranted()
	{
		super.onStorageAlreadyGranted();
		proceed();
	}

	private void proceed()
	{
		if(false && !getApp().is64Bit()) {
			final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.AppTheme_MaterialAlertDialog);
			builder.setTitle(R.string.title_device_not_supported);
			builder.setMessage(R.string.msg_device_not_supported);
			builder.setCancelable(false);
			builder.setPositiveButton(android.R.string.ok, (p1, p2) -> finishAffinity());
			builder.create().show();
		} else {
			binding.splashText.setText(getString(R.string.msg_storage_granted));
			if (getApp().isFrameworkInstalled()) {
				goToMain();
			} else {
				startActivity(new Intent(this, DownloadActivity.class));
				finish();
			}
		}
	}

    private void goToMain() {
		getApp().checkAndUpdateGradle();
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 1000);
    }
}
