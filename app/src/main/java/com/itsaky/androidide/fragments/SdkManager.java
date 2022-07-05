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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;


public class SdkManager extends Fragment implements CompoundButton.OnCheckedChangeListener{
	private FragmentSdkmanagerBinding binding;
  public static final String TAG="SDK Manager";
  //Todo add links for ndk & ndk install method , buildtools link
	public static final String AARCH_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-aarch64.tar.xz";
	public static final String ARM_SDK="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/android-sdk-33.0.1-arm.tar.xz";
	public static final String CMDLINE_TOOLS="https://github.com/itsaky/androidide-build-tools/releases/download/v33.0.1/cmdline-tools-all.tar.xz";
	public static String Device_Arch;
	ArrayList<String> download_list = new ArrayList<>();
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
      Device_Arch = System.getProperty("os.arch");
      binding.deviceType.setText("Your Device Type :" + Device_Arch);
      if (Device_Arch.equals("aarch64"))
        binding.sdk32.setEnabled(false);
      else binding.sdk64.setEnabled(false);
      binding.sdk32.setOnCheckedChangeListener(this);
      binding.sdk64.setOnCheckedChangeListener(this);
      binding.cmdTools.setOnCheckedChangeListener(this);
      binding.jdk17.setOnCheckedChangeListener(this);
      binding.download.setOnClickListener(v -> download_tools());
      binding.install.setOnClickListener(v -> installIools());
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
    builder.setPositiveButton(android.R.string.ok, (d, w) -> openTerminal());
    builder.show();
  }

  private void download_tools() {
    showProgress();
      File download_script = new File(DEFAULT_HOME, "download_tools.sh");
      StringBuilder dlScript = new StringBuilder();
      download_list.forEach(link -> {
        dlScript.append("$BUSYBOX wget ").append(link);
        joiner(dlScript);
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
      case R.id.sdk32:handleCheck(isChecked,ARM_SDK);
      break;
      case R.id.sdk64: handleCheck(isChecked,AARCH_SDK);
      break;
      case R.id.cmdTools: handleCheck(isChecked,CMDLINE_TOOLS);
      break;
      case R.id.jdk17: install_jdk= isChecked;
      break;
    }
  }
  public void handleCheck(boolean check, String link){
    if (check) {
      download_list.add(link);
    } else {
      download_list.remove(link);
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
        sb.append("cd");
        joiner(sb);
	if(install_jdk){
      sb.append("pkg install -y openjdk-17 && echo 'JAVA_HOME=/data/data/com.itsaky.androidide/files/usr/opt/openjdk' > $SYSROOT/etc/ide-environment.properties");
    joiner(sb);
	}
	File scriptPath = new File(DEFAULT_HOME);
        File[] files = scriptPath.listFiles(ARCHIVE_FILTER);

        if (files == null || files.length <= 0) {
            DialogUtils.newMaterialDialogBuilder(requireActivity())
                .setTitle("No Zips Found")
                .setMessage("No Downloaded zips found skipping extraction")
                .setNeutralButton(android.R.string.ok,(d,w)->d.cancel())
                .create()
                .show();
        }
        else {
          for (File f : files) {
            if (f.getName().endsWith(".tar.xz")) {
              if (f.getName().startsWith("cmdline-tools") || f.getName().startsWith("build-tools") || f.getName().startsWith("platform-tools")) {
                sb.append("mkdir -p $HOME/android-sdk");
                joiner(sb);
                sb.append("$BUSYBOX tar xvJf ").append("$HOME/").append(f.getName()).append(" -C $HOME/android-sdk");
              } else {
                sb.append("$BUSYBOX tar xvJf ").append("$HOME/").append(f.getName());
                joiner(sb);
                sb.append("cd $HOME");
              }
              joiner(sb);
            } else if (f.getName().endsWith(".zip")) {
              sb.append("$BUSYBOX unzip ").append(f.getAbsolutePath());
              joiner(sb);
            }
          }
        }

        sb.append("echo 'Running Post Install Process'");
        joiner(sb);
        sb.append("rm -rf *.tar* && rm -rf *.zip");
        joiner(sb);
        String DONE = "DONE";sb.append("echo ").append(DONE);
        joiner(sb);

        final File script = new File(DEFAULT_HOME, "install_tools.sh");
        if (!FileIOUtils.writeFileFromString(script, sb.toString())) {
            throw new InstallationException(2);
        }

        return script;
    }
	private void joiner(StringBuilder sb) {
        sb.append("\n");
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
  private void openTerminal() {
    requireActivity().startActivity(new Intent(requireActivity(), TerminalActivity.class));
  }
}
