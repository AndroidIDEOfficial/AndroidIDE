/************************************************************************************
 * This file is part of AndroidIDE.
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
package com.itsaky.androidide.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.itsaky.androidide.utils.ILogger;

public abstract class IDEActivity extends AppCompatActivity {
  public static final int REQCODE_STORAGE = 1009;
  protected static ILogger LOG = ILogger.newInstance("StudioActivity");
  
  public void loadFragment(Fragment fragment, int id) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(id, fragment);
    transaction.commit();
  }

  public IDEApplication getApp() {
    return (IDEApplication) getApplication();
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    preSetContentLayout();
    setContentView(bindLayout());
  
    if (!isStoragePermissionGranted()) {
      requestStorage();
      return;
    }

    if (isStoragePermissionGranted()) {
      onStorageAlreadyGranted();
    } else {
      onStorageDenied();
    }
  }

  public boolean isStoragePermissionGranted() {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED;
  }

  protected void requestStorage() {
    if (isStoragePermissionGranted()) {
      onStorageGranted();
      return;
    }
    ActivityCompat.requestPermissions(
        this,
        new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
        },
        REQCODE_STORAGE);
  }

  protected void onStorageGranted() {}

  @Override
  public void onRequestPermissionsResult(
      int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQCODE_STORAGE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        onStorageGranted();
      else onStorageDenied();
    }
  }

  protected void onStorageAlreadyGranted() {}

  protected void onStorageDenied() {}

  protected void preSetContentLayout() {}
  
  protected abstract View bindLayout();
}
