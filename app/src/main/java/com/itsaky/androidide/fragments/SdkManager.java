package com.itsaky.androidide.fragments;

import static com.itsaky.androidide.utils.Environment.BIN_DIR;
import static com.itsaky.androidide.utils.Environment.DEFAULT_HOME;
import static com.itsaky.androidide.utils.Environment.PREFIX;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.TerminalActivity;
import com.itsaky.androidide.databinding.FragmentSdkmanagerBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.shell.IProcessExecutor;
import com.itsaky.androidide.shell.IProcessExitListener;
import com.itsaky.androidide.shell.ProcessExecutorFactory;
import com.itsaky.androidide.shell.ProcessStreamsHolder;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.InputStreamLineReader;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.utils.SdkHelper;
import com.itsaky.androidide.TerminalActivity;
import com.itsaky.androidide.utils.BootstrapInstaller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SdkManager extends Fragment implements CompoundButton.OnCheckedChangeListener{
	private FragmentSdkmanagerBinding binding;
  public static final String TAG="SDK Manager";
	ArrayList<String> download_list = new ArrayList<>();
	//All Urls
	private ArrayList<HashMap<String, Object>> All_URLS = new ArrayList<>();
	//Device Specific Urls
	Map<String,String> Device_Url=new HashMap<>();
	private ProgressSheet progressSheet;
	final StringBuilder sb = new StringBuilder();
	private boolean install_jdk=false;
  private final FileFilter ARCHIVE_FILTER = p1 -> p1.isFile() && (p1.getName().endsWith(".tar.xz") || p1.getName().endsWith(".zip"));

  private StringBuilder output = new StringBuilder();

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
    if (!checkBootstrapInstalled()) {
      showDialogInstallBootStrap();
    }
    else {
    processLinks();
    String Device_Arch = System.getProperty("os.arch");
      binding.deviceType.setText("Your Device Type :" + Device_Arch);
      if (Device_Arch.equals("aarch64"))
        binding.sdk32.setEnabled(false);
      else {
      binding.sdk64.setEnabled(false);
      binding.ndk.setEnabled(false);
      }
      binding.sdk32.setOnCheckedChangeListener(this);
      binding.sdk64.setOnCheckedChangeListener(this);
      binding.cmdTools.setOnCheckedChangeListener(this);
      binding.buildTools.setOnCheckedChangeListener(this);
      binding.platformTools.setOnCheckedChangeListener(this);
      binding.ndk.setOnCheckedChangeListener(this);
      binding.jdk17.setOnCheckedChangeListener(this);
      binding.download.setOnClickListener(v -> download_tools());
      binding.install.setOnClickListener(v -> installIools());
    }
  }
  public void processLinks(){
    if(FileUtils.isFileExists(DEFAULT_HOME+"manifest.json")){
		String urls = FileIOUtils.readFile2String(DEFAULT_HOME+"/manifest.json");
		All_URLS = new Gson().fromJson(urls,new TypeToken<
                                                                ArrayList<
                                                                        HashMap<
                                                                                String,
                                                                                Object>>>() {}.getType());
                                                                                
        Device_Url = SdkHelper.getLinks(All_URLS);
		}
	else {
		new Thread(()->{
			try {
		HttpURLConnection connection = (HttpURLConnection) new URL("https://raw.githubusercontent.com/dead8309/BuildTools/main/data.json").openConnection();
		InputStream inputStream = connection.getInputStream();
		String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
	requireActivity().runOnUiThread(()->{
			FileIOUtils.writeFileFromString(DEFAULT_HOME+"/manifest.json",text);
			All_URLS = new Gson().fromJson(text,new TypeToken<
                                                                ArrayList<
                                                                        HashMap<
                                                                                String,
                                                                                Object>>>() {}.getType());
                                                                                
        Device_Url = SdkHelper.getLinks(All_URLS);
		});
		
		} catch (MalformedURLException e){}
		catch (IOException e){}
		}).start();
		}
  }

  private void showDialogInstallBootStrap() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(requireActivity());
    builder.setTitle(R.string.title_warning);
    TextView view = new TextView(requireActivity());
    view.setPadding(10, 10, 10, 10);
    view.setText(getString(R.string.msg_require_install_bootstrap_packages));
    view.setMovementMethod(LinkMovementMethod.getInstance());
    builder.setView(view);
    builder.setCancelable(false);
    builder.setPositiveButton(android.R.string.ok, (d, w) -> installBootStrap());
    builder.show();
  }

  private void download_tools() {
    showProgress();
      File download_script = new File(DEFAULT_HOME, "download_tools.sh");
      StringBuilder dlScript = new StringBuilder();
      download_list.forEach(link -> {
        dlScript.append("$BUSYBOX wget ").append(link).append("\n");
      });
      dlScript.append("echo 'Finished Downloading Tools'");
      FileIOUtils.writeFileFromString(download_script, dlScript.toString());
      ExecBash(download_script,this::onDownloadComplete);
    }
  public void ExecBash(File script, IProcessExitListener iProcessExitListener)
  {
    try{
    final ProcessStreamsHolder holder = new ProcessStreamsHolder();
    final IProcessExecutor executor = ProcessExecutorFactory.commonExecutor();

    executor.execAsync(
        holder,
        iProcessExitListener,
        true,
        Environment.BUSYBOX.getAbsolutePath(),
        "sh",
        script.getAbsolutePath());

    this.output = new StringBuilder();
    final InputStreamLineReader reader =
        new InputStreamLineReader(holder.in, this::onInstallationOutput);
    new Thread(reader).start();

  } catch (IOException e) {
    onFailed();
  }
  }

  public void installIools(){
    showProgress();
    try {
      final File script = createExtractScript();
      ExecBash(script,this::onProcessExit);

    } catch (InstallationException e) {
      onFailed();
    }

  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public void onCheckedChanged(CompoundButton cbuttton, boolean isChecked) {
    switch (cbuttton.getId()){
      case R.id.sdk32:handleCheck(isChecked,SdkHelper.SDK);
      break;
      case R.id.sdk64: handleCheck(isChecked,SdkHelper.SDK);
      break;
      case R.id.cmdTools: handleCheck(isChecked,SdkHelper.CMDLINE_TOOLS);
      break;
      case R.id.jdk17: install_jdk= isChecked;
      break;
      case R.id.buildTools : handleCheck(isChecked,SdkHelper.BUILD_TOOLS);
      break;
      case R.id.platformTools : handleCheck(isChecked,SdkHelper.PLATFORM_TOOLS);
      break;
      case R.id.ndk : handleCheck(isChecked,SdkHelper.NDK);
                      handleCheck(isChecked,SdkHelper.CMAKE);
      break;
    }
  }
  
  public void handleCheck(boolean check, String link){
    if (check) {
      download_list.add(Device_Url.get(link));
    } else {
      download_list.remove(Device_Url.get(link));
    }
  }
  private void onInstallationOutput(final String line) {
        ThreadUtils.runOnUiThread(() -> this.appendOut(line));
    }

    private void onProcessExit(final int code) {
        ThreadUtils.runOnUiThread(
                () -> {
                    if (code == 0) {
                        if (getProgressSheet().isShowing()) {
                            getProgressSheet().dismiss();
                        }
                      FileUtil.deleteFile(DEFAULT_HOME+"/install_tools.sh");
                      MaterialAlertDialogBuilder m = DialogUtils.newMaterialDialogBuilder(requireActivity());
                      m.setTitle("Tools Installed successfully");
                      m.setNeutralButton(android.R.string.ok,(d,w)->d.cancel());
                      m.show();
                    } else {
                        onFailed();
                    }
                });
    }
  private void onDownloadComplete(final int code) {
    ThreadUtils.runOnUiThread(
        () -> {
          if (code == 0) {
            if (getProgressSheet().isShowing()) {
              getProgressSheet().dismiss();
            }
            FileUtil.deleteFile(DEFAULT_HOME+"/download_tools.sh");
            MaterialAlertDialogBuilder m = DialogUtils.newMaterialDialogBuilder(requireActivity());
            m.setTitle("Download Finished");
            m.setMessage("Tools are downloaded successfully, Install them ?\nYou can also install Tools later by clicking on Install button");
            m.setPositiveButton(android.R.string.ok,(d, w)->installIools());
            m.setNegativeButton(android.R.string.cancel,(d,w)->d.cancel());
            m.show();
          } else {
            onFailed();
          }
        });
  }

    private void onFailed() {
        if (getProgressSheet().isShowing()) {
            getProgressSheet().dismiss();
        }
    }
   private void showProgress() {
       getProgressSheet().setCancelable(false);
        getProgressSheet().setShowShadow(false);
        getProgressSheet()
                .setSubMessageEnabled(true)
                .setWelcomeTextEnabled(true)
                .show(requireActivity().getSupportFragmentManager(), "progress_sheet");
    } 

private File createExtractScript() throws SdkManager.InstallationException{
	if(install_jdk){
      sb.append(SdkHelper.setupJDK());
	}
	File scriptPath = new File(DEFAULT_HOME);
    File[] files = scriptPath.listFiles(ARCHIVE_FILTER);

        if (files == null || files.length <= 0) {
            getProgressSheet().setMessage("No Zips Files Found Skipping Extraction");
        }
        else {
          for (File f : files) {
          sb.append(SdkHelper.setupZip(f));
          }
        }
        sb.append(SdkHelper.postInstall());

        final File script = new File(DEFAULT_HOME, "install_tools.sh");
        if (!FileIOUtils.writeFileFromString(script, sb.toString())) {
            throw new InstallationException(2);
        }

        return script;
    }


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


  private boolean checkBootstrapInstalled() {
    final var bash = new File(BIN_DIR, "bash");
    return ((PREFIX.exists()
        && PREFIX.isDirectory()
        && bash.exists()
        && bash.isFile()
        && bash.canExecute()));
  }
  private void installBootStrap() {
      // Show the progress sheet
      final var progress = new ProgressSheet();
      progress.setShowShadow(false);
      progress.setSubMessageEnabled(true);
      progress.setShowTitle(false);
      progress.setMessage(getString(R.string.please_wait));
      progress.setSubMessage(getString(R.string.msg_reading_bootstrap));
      progress.setCancelable(false);
      progress.show(getParentFragmentManager(), "extract_bootstrap_progress");
      // Install bootstrap asynchronously
      final var future = BootstrapInstaller.doInstall(
              this, message -> requireActivity().runOnUiThread(() -> progress.setSubMessage(message)));

      future.whenComplete(
          (voidResult, throwable) -> {

            requireActivity().runOnUiThread(
                () -> {
                  progress.dismissAllowingStateLoss();

                  if (future.isCompletedExceptionally() || throwable != null) {
                    //Future has been completed exceptionally
                    TerminalActivity.showInstallationError(throwable);
                    return;
                  }
progress.dismiss();
                });
          });
    }
}

