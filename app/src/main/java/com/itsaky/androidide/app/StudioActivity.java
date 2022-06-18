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
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.itsaky.androidide.R;
import com.itsaky.androidide.utils.ILogger;

public abstract class StudioActivity extends AppCompatActivity {
  private boolean toRequestStorage = true;

  public static final String TAG = "StudioActivity";
  public static final int REQCODE_STORAGE = 1009;

  protected static ILogger LOG = ILogger.newInstance("StudioActivity");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(bindLayout());
    onSetContentView();

    if (toRequestStorage && !isStoragePermissionGranted()) {
      requestStorage();
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

  protected void doNotRequestStorage() {
    this.toRequestStorage = false;
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

  public void loadFrament(Fragment frag, View view) {
    loadFragment(frag, view.getId());
  }

  public void loadFragment(Fragment fragment, int id) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(id, fragment);
    transaction.commit();
  }

  public StudioApp getApp() {
    return (StudioApp) getApplication();
  }

  public TypedValue getTypedValueForAttr(@AttrRes int attrRes) {
    TypedValue typedValue = new TypedValue();
    getTheme().resolveAttribute(attrRes, typedValue, true);
    return typedValue;
  }

  public int getColorPrimary() {
    return getTypedValueForAttr(R.attr.colorPrimary).data;
  }

  public int getColorPrimaryDark() {
    return getTypedValueForAttr(R.attr.colorPrimaryDark).data;
  }

  public int getColorAccent() {
    return getTypedValueForAttr(R.attr.colorAccent).data;
  }

  public int dpToPx(int dp) {
    return (int)
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
  }

  @Override
  public void onRequestPermissionsResult(
      int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQCODE_STORAGE) {
      if (grantResults != null
          && grantResults.length > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED) onStorageGranted();
      else onStorageDenied();
    }
  }

  protected void onStorageAlreadyGranted() {}

  protected void onStorageGranted() {}

  protected void onStorageDenied() {}

  protected void onSetContentView() {}

  protected abstract View bindLayout();
}
