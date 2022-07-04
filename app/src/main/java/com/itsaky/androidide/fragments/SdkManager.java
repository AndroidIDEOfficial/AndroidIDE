package com.itsaky.androidide.fragments;

import android.widget.CompoundButton;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import java.util.ArrayList;
import com.blankj.utilcode.util.ThreadUtils;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.itsaky.androidide.R; 
import com.blankj.utilcode.util.ClipboardUtils;
import com.itsaky.androidide.app.BaseApplication;
import com.itsaky.androidide.databinding.FragmentSdkmanagerBinding;
import com.itsaky.androidide.Downloader;
import android.app.ProgressDialog;
import com.blankj.utilcode.util.FileIOUtils;
import java.io.File;
import java.io.FileFilter;
import com.itsaky.androidide.shell.IProcessExecutor;
import com.itsaky.androidide.shell.ProcessExecutorFactory;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.InputStreamLineReader;


public class SdkManager extends Fragment implements CompoundButton.OnCheckedChangeListener{
	private FragmentSdkmanagerBinding binding;
	public static final String AARCH_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-aarch64.tar.xz";
	public static final String ARM_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-arm.tar.xz";
	public static final String CMDLINE_TOOLS="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/cmdline-tools-all.tar.xz";
	public static String Device_Arch;
	ArrayList<String> download_list = new ArrayList<>();
	private ProgressSheet progressSheet;

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
    if(Device_Arch.equals("aarch64"))
    binding.sdk32.setEnabled(false);
    else binding.sdk64.setEnabled(false);
    binding.sdk32.setOnCheckedChangeListener(this);
    binding.sdk64.setOnCheckedChangeListener(this);
    binding.buildtools.setOnCheckedChangeListener(this);
    binding.download.setOnClickListener(v->{
    ProgressDialog d = new ProgressDialog(getActivity());
	d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
  //    new Downloader(getActivity(),getActivity(),d,download_list).execute();
        try {
            final File script = createExtractScript();
            final ProcessStreamsHolder holder = new ProcessStreamsHolder();
            final IProcessExecutor executor = ProcessExecutorFactory.commonExecutor();
            
            executor.execAsync(
                    holder,
                    this::onInstallProcessExit,
                    true,
                    Environment.BUSYBOX.getAbsolutePath(),
                    "sh", // We use busybox's sh, because Environment.SHELL is not installed yet...
                    script.getAbsolutePath());

            this.output = new StringBuilder();
            final InputStreamLineReader reader =
                    new InputStreamLineReader(holder.in, this::onInstallationOutput);
            new Thread(reader).start();

        } catch (SdkManager.InstallationException e) {
            onInstallationFailed(e.exitCode);
	    
        } catch (IOException e) {
            onInstallationFailed(5);
        }
    });
  }
  private void onInstallationOutput(final String line) {
        ThreadUtils.runOnUiThread(() -> this.appendOut(line));
    }

    private void onInstallProcessExit(final int code) {
        ThreadUtils.runOnUiThread(
                () -> {
                    if (code == 0) {
                        if (getProgressSheet().isShowing()) {
                            getProgressSheet().dismiss();
                        }
                    } else {
                        onInstallationFailed(code);
                    }
                });
    }

    private void onInstallationFailed(int code) {
        if (getProgressSheet().isShowing()) {
            getProgressSheet().dismiss();
        }
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
private File createExtractScript() throws SdkManager.InstallationException{
        final StringBuilder sb = new StringBuilder();
        sb.append("cd");
        joiner(sb);
        sb.append("echo 'Installing...'");
        joiner(sb);
		File scriptPath = new File(getActivity().getFilesDir()+"/home/");
        File[] files = scriptPath.listFiles(ARCHIVE_FILTER);

        if (files == null || files.length <= 0) {
            throw new InstallationException(2);
        }

        for (File f : files) {
            if (f.getName().endsWith(".tar.xz")) {
                if(f.getName().startsWith("cmdline-tools-all")){
                    sb.append("mkdir -p $HOME/android-sdk && cd $HOME/android-sdk");
                    }
                sb.append("$BUSYBOX tar xvJf '").append(f.getAbsolutePath()).append("'");
                joiner(sb);
                sb.append("cd $HOME");
                joiner(sb);
            } else if (f.getName().endsWith(".zip")) {
                sb.append("$BUSYBOX unzip '").append(f.getAbsolutePath()).append("'");
                joiner(sb);
            }
        }

        sb.append("echo 'Cleaning unsupported flags in binaries...'");
        joiner(sb);
        String DONE = "DONE";
        sb.append("echo ").append(DONE);

        final File script = new File(getActivity().getFilesDir()+"/home/", "extract_tools.sh");
        if (!FileIOUtils.writeFileFromString(script, sb.toString())) {
            throw new InstallationException(2);
        }

        return script;
    }
	private void joiner(StringBuilder sb) {
        sb.append(" && ");
    }
	private final FileFilter ARCHIVE_FILTER =
            p1 ->
                    p1.isFile()
                            && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));
                            
    private StringBuilder output = new StringBuilder();

    private void appendOut(String line) {
        output.append(line.trim());
        output.append("\n");

        getProgressSheet().setSubMessage(line);
        
    }
    private ProgressSheet getProgressSheet() {
        return progressSheet == null
                ? progressSheet = new ProgressSheet().setMessage(getString(R.string.please_wait))
                : progressSheet;
    }
							
	private static class InstallationException extends Exception {
        private final int exitCode;

        public InstallationException(int exitCode) {
            this.exitCode = exitCode;
        }
    }
}
