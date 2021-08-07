package com.itsaky.androidide;

import android.os.Bundle;
import android.view.View;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityAboutBinding;
import com.itsaky.androidide.fragments.sheets.OptionsListFragment;
import com.itsaky.androidide.models.License;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.LicenseReader;
import java.util.List;

public class AboutActivity extends StudioActivity {

    private ActivityAboutBinding binding;
    
    @Override
    protected View bindLayout() {
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding.items.footerText.setText(getFooter());
        
        new TaskExecutor().executeAsync(new LicenseReader(this), __ -> {
            setupLicenses(__);
        });
    }
    
    private void setupLicenses(List<License> licenses) {
        if(licenses == null || licenses.size() <= 0) {
            binding.items.licenses.setText(getString(R.string.msg_about_no_licenses));
        } else {
            StringBuilder sb = new StringBuilder();
            for(License license : licenses) {
                sb.append("\u2022 ");
                sb.append(license.name);
                sb.append("\n");
                sb.append(getString(R.string.msg_about_licensed_under, license.license));
                sb.append("\n");
                sb.append(getString(R.string.msg_about_license_source, license.url));
                sb.append("\n\n");
            }
            binding.items.licenses.setText(sb);
        }
    }
    
    private String getFooter() {
        final String arch = android.os.Build.SUPPORTED_ABIS[0];
        try {
            final String version = getPackageManager().getPackageInfo("com.itsaky.androidide", 0).versionName;
            return getString(R.string.about_footer, version, arch);
        } catch (Throwable th) {
            return getString(R.string.about_footer_alternate, arch);
        }
    }
}
