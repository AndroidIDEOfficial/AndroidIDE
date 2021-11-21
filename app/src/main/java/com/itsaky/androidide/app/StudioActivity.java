package com.itsaky.androidide.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
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
import com.itsaky.androidide.services.build.IDEService;
import com.itsaky.androidide.utils.Logger;

public abstract class StudioActivity extends AppCompatActivity {
	private boolean toRequestStorage = true;

	public static final String TAG = "StudioActivity";
	public static final int REQCODE_STORAGE = 1009;
    
    protected static Logger LOG = Logger.instance("StudioActivity");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(bindLayout());
		onSetContentView();
		if (toRequestStorage && !isStoragePermissionGranted())
			requestStorage();
		else if (isStoragePermissionGranted())
			onStorageAlreadyGranted();
    }

	public boolean isStoragePermissionGranted() {
		return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED 
			&& ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
	}

	protected void doNotRequestStorage() {
		this.toRequestStorage = false;
	}

	protected void requestStorage() {
		if (isStoragePermissionGranted()) { onStorageGranted(); return; }
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQCODE_STORAGE);
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
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQCODE_STORAGE) {
			if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				onStorageGranted();
			else onStorageDenied();
		}
	}

	protected void onStorageAlreadyGranted() {}
	protected void onStorageGranted() {}
	protected void onStorageDenied() {}
	protected void onSetContentView() {}
	
	protected abstract View bindLayout();
}
