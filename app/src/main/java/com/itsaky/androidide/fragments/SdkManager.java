package com.itsaky.androidide.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ClipboardUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.databinding.FragmentSdkmanagerBinding;
import com.itsaky.androidide.Downloader;
import android.app.ProgressDialog;

public class SdkManager extends Fragment{
	private FragmentSdkmanagerBinding binding;
	public static final String aarch_sdk="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-aarch64.tar.xz";
	public static final String arm_sdk="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-arm.tar.xz";
	public static final String CMDLINE_TOOLS="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/cmdline-tools-all.tar.xz";
	public static String Device_Arch="";
	ArrayList<String> download_list = new ArrayList<>();

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return (binding = FragmentSdkmanagerBinding.inflate(inflater, container, false)).getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Device_Arch=System.getProperty("os.arch");
    binding.download.setOnClickListener(v->{
    ProgressDialog d = new ProgressDialog(getActivity());
	d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
download_list.add("https://cdn.discordapp.com/attachments/756840975443296370/992014909439234108/dummy.pdf");
      new Downloader(getActivity(),getActivity(),d,download_list).execute();
    });
  }
}
