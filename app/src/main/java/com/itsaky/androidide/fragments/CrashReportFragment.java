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
package com.itsaky.androidide.fragments;

import static com.blankj.utilcode.util.AppUtils.getAppVersionCode;
import static com.blankj.utilcode.util.AppUtils.getAppVersionName;
import static com.blankj.utilcode.util.DeviceUtils.getManufacturer;
import static com.blankj.utilcode.util.DeviceUtils.getModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ClipboardUtils;
import com.itsaky.androidide.resources.R;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.databinding.LayoutCrashReportBinding;

public class CrashReportFragment extends Fragment {

  public static final String KEY_TITLE = "crash_title";
  public static final String KEY_MESSAGE = "crash_message";
  public static final String KEY_TRACE = "crash_trace";
  public static final String KEY_CLOSE_APP_ON_CLICK = "close_on_app_click";
  private LayoutCrashReportBinding binding;
  private boolean closeAppOnClick = true;

  @NonNull
  public static CrashReportFragment newInstance(@NonNull final String trace) {
    return CrashReportFragment.newInstance(null, null, trace, true);
  }

  @NonNull
  public static CrashReportFragment newInstance(
      @Nullable final String title,
      @Nullable final String message,
      @NonNull final String trace,
      boolean closeAppOnClick) {
    final var frag = new CrashReportFragment();
    final var args = new Bundle();

    args.putString(KEY_TRACE, trace);
    args.putBoolean(KEY_CLOSE_APP_ON_CLICK, closeAppOnClick);

    if (title != null) {
      args.putString(KEY_TITLE, title);
    }

    if (message != null) {
      args.putString(KEY_MESSAGE, message);
    }

    frag.setArguments(args);
    return frag;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return (binding = LayoutCrashReportBinding.inflate(inflater, container, false)).getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    final var args = requireArguments();
    this.closeAppOnClick = args.getBoolean(KEY_CLOSE_APP_ON_CLICK);
    var title = getString(R.string.msg_ide_crashed);
    var message = getString(R.string.msg_report_crash);
    var trace = "";
    if (args.containsKey(KEY_TITLE)) {
      title = args.getString(KEY_TITLE);
    }

    if (args.containsKey(KEY_MESSAGE)) {
      message = args.getString(KEY_MESSAGE);
    }

    if (args.containsKey(KEY_TRACE)) {
      trace = args.getString(KEY_TRACE);
      trace = buildReportText(trace);
    } else {
      trace = "No stack strace was provided for the report";
    }

    binding.crashTitle.setText(title);
    binding.crashSubtitle.setText(message);
    binding.logText.setText(trace);

    final var report = trace;
    binding.closeButton.setOnClickListener(
        v -> {
          if (closeAppOnClick) {
            requireActivity().finishAffinity();
          } else {
            requireActivity().finish();
          }
        });
    binding.reportButton.setOnClickListener(v -> reportTrace(report));
  }

  private void reportTrace(String report) {
    ClipboardUtils.copyText("AndroidIDE CrashLog", report);

    final var url = BaseApplication.GITHUB_URL.concat("/issues");
    final var intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);
  }

  @NonNull
  private String buildReportText(String trace) {
    return "AndroidIDE crash report\n"
        + "Manufacturer: "
        + getManufacturer()
        + "\n"
        + "Device: "
        + getModel()
        + "\n"
        + "ABI: "
        + BaseApplication.getArch()
        + "\n"
        + "SDK version: "
        + Build.VERSION.SDK_INT
        + "\n"
        + "App version: "
        + getAppVersionName()
        + " ("
        + getAppVersionCode()
        + ")\n\n Stacktrace: \n"
        + trace;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }
}
