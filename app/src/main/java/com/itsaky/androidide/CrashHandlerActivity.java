/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide;

import static com.blankj.utilcode.util.AppUtils.getAppVersionCode;
import static com.blankj.utilcode.util.AppUtils.getAppVersionName;
import static com.blankj.utilcode.util.DeviceUtils.getManufacturer;
import static com.blankj.utilcode.util.DeviceUtils.getModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ClipboardUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityCrashHandlerBinding;

public class CrashHandlerActivity extends StudioActivity {
    
    private ActivityCrashHandlerBinding binding;
    
    public static final String REPORT_ACTION = "com.itsaky.androidide.REPORT_CRASH";
    public static final String TRACE_KEY = "crash_trace";
    
    @Override
    protected View bindLayout () {
        binding = ActivityCrashHandlerBinding.inflate (getLayoutInflater ());
        return binding.getRoot ();
    }
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding.closeButton.setOnClickListener (v -> finishAffinity ());
        
        final var extra = getIntent ().getExtras ();
        if (extra == null) {
            binding.closeButton.performClick ();
            return;
        }
        
        final var report = buildReportText (extra.getString (TRACE_KEY, "Unable to get logs."));
        binding.logText.setText (report);
        binding.reportButton.setOnClickListener (v -> reportTrace (report));
    }
    
    private void reportTrace (String report) {
        ClipboardUtils.copyText ("AndroidIDE CrashLog", report);
        final var url = BaseApplication.GITHUB_URL.concat ("/issues");
        final var intent = new Intent ();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData (Uri.parse (url));
        intent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity (intent);
    }
    
    @NonNull
    private String buildReportText (String trace) {
        return "AndroidIDE crash report\n" +
                "Manufacturer: " +
                getManufacturer () +
                "\n" +
                "Device: " +
                getModel () +
                "\n" +
                "App version: " +
                getAppVersionName () +
                " (" +
                getAppVersionCode () +
                ")\n\n Stacktrace: \n" +
                trace;
    }
}