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

package com.itsaky.androidide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.ActivitySplashBinding;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.DialogUtils;

public class SplashActivity extends StudioActivity {
  private static final Handler mHandler = new Handler();
  private final Runnable mRunnable =
      () -> {
        ConstantsBridge.SPLASH_TO_MAIN = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
      };
  private ActivitySplashBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding.splashText.setText(getString(R.string.msg_checking_storage_permissions));
  }

  @Override
  protected void onStorageAlreadyGranted() {
    super.onStorageAlreadyGranted();
    proceed();
  }

  @Override
  protected void onStorageGranted() {
    super.onStorageGranted();
    proceed();
  }

  @Override
  protected View bindLayout() {
    binding = ActivitySplashBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  private void proceed() {
    if (!StudioApp.isAbiSupported()) {
      final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(this);
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
    mHandler.removeCallbacks(mRunnable);
    mHandler.postDelayed(mRunnable, 1000);
  }
}
