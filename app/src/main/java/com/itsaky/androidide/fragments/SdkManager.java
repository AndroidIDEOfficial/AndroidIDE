package com.itsaky.androidide.fragments;

import android.widget.CompoundButton;
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

public class SdkManager extends Fragment implements CompoundButton.OnCheckedChangeListener{
	private FragmentSdkmanagerBinding binding;
	public static final String AARCH_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-aarch64.tar.xz";
	public static final String ARM_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-arm.tar.xz";
	public static final String CMDLINE_TOOLS="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/cmdline-tools-all.tar.xz";
	public static String Device_Arch;
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
    binding.deviceType.setText("Your Device Type :"+Device_Arch);
    binding.sdk32.setOnCheckedChangeListener(this);
    binding.sdk64.setOnCheckedChangeListener(this);
    binding.buildtools.setOnCheckedChangeListener(this);
    binding.download.setOnClickListener(v->{
    ProgressDialog d = new ProgressDialog(getActivity());
	d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
      new Downloader(getActivity(),getActivity(),d,download_list).execute();
    });
  }
  @Override
	public void onCheckedChanged(CompoundButton cbuttton, boolean isChecked) {
		if(cbuttton.getId() == binding.sdk32.getId()){
		if(isChecked)
		download_list.add(ARM_SDK);
		else download_list.remove(ARM_SDK);
		}
		else if(cbuttton.getId() == binding.sdk64.getId()){
                if(isChecked)
		download_list.add(AARCH_SDK);
                else download_list.remove(AARCH_SDK);               
	        }
		else if(cbuttton.getId() == binding.buildtools.getId()){
                if(isChecked)
	        download_list.add(CMDLINE_TOOLS);
                else download_list.remove(CMDLINE_TOOLS);
	        }
        }
}
