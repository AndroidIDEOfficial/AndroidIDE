package com.itsaky.androidide.services;

import androidx.annotation.StringRes;
import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.GsonBuilder;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.sockets.SocketConnection;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.BaseGradleTasks;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.gradle.tooling.api.GradleConnection;
import com.itsaky.gradle.tooling.api.messages.ErrorMessage;
import com.itsaky.gradle.tooling.api.messages.Message;
import com.itsaky.gradle.tooling.api.messages.Method;
import com.itsaky.gradle.tooling.api.messages.ResponseMessage;
import com.itsaky.gradle.tooling.api.messages.params.BuildParams;
import com.itsaky.gradle.tooling.api.messages.params.InitializeParams;
import com.itsaky.gradle.tooling.api.model.IDEProject;
import com.itsaky.gradle.tooling.api.model.Module;
import com.itsaky.gradle.tooling.api.model.build.BuildProgressEvent;
import com.itsaky.gradle.tooling.api.model.build.BuildResult;
import com.itsaky.gradle.tooling.api.model.build.OperationDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.FinishEventDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.ProjectConfigurationDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.StartEventDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.StatusEventDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.TaskDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.TestOutDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.TransformDescriptor;
import com.itsaky.gradle.tooling.api.model.build.descriptors.WorkItemDescriptor;
import com.itsaky.gradle.tooling.api.model.build.identifiers.BinaryPluginIdentifierDescription;
import com.itsaky.gradle.tooling.api.model.build.identifiers.PluginIdentifierDescription;
import com.itsaky.gradle.tooling.api.model.build.identifiers.ScriptPluginIdentifierDescription;
import com.itsaky.gradle.tooling.api.tools.RuntimeTypeAdapterFactory;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class IDEService extends SocketConnection implements ShellServer.Callback {

	private ShellServer shell;
	private BuildListener listener;
	private File rootProject;
	private GradleTask currentTask;
    private StudioApp app;
    
    private IDEProject project;
    private Module main;

    private boolean isConnected;
    private boolean projectInitialized = false;
	private boolean isBuilding = false;
    private boolean isRunning = false;

    private long buildStart = 0;
    private int UNIVERSAL_ID = 0;
    
    public static final String SUCCESSFUL = "SUCCESSFUL";
    public static final String FAILED = "FAILED";
    
	public static final int TASK_SHOW_DEPENDENCIES       = 0;
	public static final int TASK_ASSEMBLE_DEBUG 		 = 1;
	public static final int TASK_ASSEMBLE_RELEASE 		 = 2;
	public static final int TASK_BUILD 					 = 3;
	public static final int TASK_BUNDLE 				 = 4;
	public static final int TASK_CLEAN 					 = 5;
	public static final int TASK_CLEAN_BUILD 			 = 6;
	public static final int TASK_COMPILE_JAVA 			 = 7;
	public static final int TASK_DEX 					 = 8;
	public static final int TASK_LINT 					 = 9;
	public static final int TASK_LINT_DEBUG 			 = 10;
	public static final int TASK_LINT_RELEASE			 = 11;


    private static final String START_COMMAND = "java " +
    "-cp %s" +
    " com.itsaky.gradle.tooling.api.GradleApi";

    static {
        RuntimeTypeAdapterFactory<OperationDescriptor> descriptorFactory = RuntimeTypeAdapterFactory.of(OperationDescriptor.class)
            .registerSubtype(OperationDescriptor.class, "OperationDescriptor")
            .registerSubtype(TaskDescriptor.class, "TaskDescriptor")
            .registerSubtype(FinishEventDescriptor.class, "FinishEventDescriptor")
            .registerSubtype(ProjectConfigurationDescriptor.class, "ProjectConfigurationDescriptor")
            .registerSubtype(StartEventDescriptor.class, "StartEventDescriptor")
            .registerSubtype(StatusEventDescriptor.class, "StatusEventDescriptor")
            .registerSubtype(TestOutDescriptor.class, "TestOutDescriptor")
            .registerSubtype(TransformDescriptor.class, "TransformDescriptor")
            .registerSubtype(WorkItemDescriptor.class, "WorkItemDescriptor");
        RuntimeTypeAdapterFactory<PluginIdentifierDescription> pluginFactory = RuntimeTypeAdapterFactory.of(PluginIdentifierDescription.class)
            .registerSubtype(PluginIdentifierDescription.class, "PluginIdentifierDescription")
            .registerSubtype(BinaryPluginIdentifierDescription.class, "BinaryPluginIdentifierDescription")
            .registerSubtype(ScriptPluginIdentifierDescription.class, "ScriptPluginIdentifierDescription");
        GSON = new GsonBuilder()
            .registerTypeAdapterFactory(descriptorFactory)
            .registerTypeAdapterFactory(pluginFactory)
            .create();
    }

	public IDEService(File rootProject) {
        super(GradleConnection.SERVER_HOST, GradleConnection.SERVER_PORT);
        this.app = StudioApp.getInstance();
		this.rootProject = rootProject;

        this.shell = app.newShell(this);
        this.shell.bgAppend(String.format(START_COMMAND, getClasspaths()));

        isRunning = true;
	}

    private String getClasspaths() {
        final List<File> libs = FileUtils.listFilesInDir(Environment.GRADLE_API_LIBS_DIR, true);
        StringBuilder sb = new StringBuilder();
        for (int i=0;i < libs.size();i++) {
            if (i == libs.size() - 1) {
                sb.append(libs.get(i));
            } else {
                sb.append(libs.get(i) + File.pathSeparator);
            }
        }

        return sb.toString();
    }

    @Override
    protected void onConnected() {
        isConnected = true;
        writeMessage(Method.PROJECT_INIT, new InitializeParams(Environment.GRADLE_HOME.getAbsolutePath(), rootProject.getAbsolutePath()));
    }

    @Override
    protected void onDisconnected() {
        LOG.i("Disconnected from server...");
    }

    @Override
    protected void onFailedToConnect(Throwable exception) {
        LOG.e(ex(exception));
    }

    protected void onResponse(ResponseMessage message) {
        if (message.result == null) return;
        switch (message.method) {
            case Method.API_CONNECTION_PROGRESS :
                listener.onConnectionProgressChanged(message.result.getAsString());
                break;
            case Method.PROJECT_INITIALIZED :
                project = GSON.fromJson(message.result, IDEProject.class);
                List<Module> applicationModules = project.modules.stream().filter(m -> !m.isLibrary).collect(Collectors.toList());
                if (applicationModules != null && applicationModules.size() > 0) {
                    // TODO: Handle multiple application modules
                    main = applicationModules.get(0);
                    projectInitialized = true;
                    listener.onProjectInitialized(project, main);
                } else {
                    // TODO: Notify users about no application modules
                }
                break;
            case Method.BUILD_STATUS_CHANGED :
                BuildProgressEvent progress = GSON.fromJson(message.result, BuildProgressEvent.class);
                listener.onBuildProgress(currentTask, progress);
                break;
            case Method.BUILD_STD_OUT :
                listener.stdOut(currentTask, message.result.getAsString());
                break;
            case Method.BUILD_STD_ERROR :
                listener.stdErr(currentTask, message.result.getAsString());
                break;
            case Method.BUILD_RESULT :
                isBuilding = false;
                BuildResult result = GSON.fromJson(message.result, BuildResult.class);
                int seconds = (int) (System.currentTimeMillis() - buildStart) / 1000;
                String msg = getString(com.itsaky.androidide.R.string.msg_final_build_result, result.isSuccess ? SUCCESSFUL : FAILED, currentTask.getName(), seconds);
                
                if(result.isSuccess) {
                    listener.stdSuccess(currentTask, getString(com.itsaky.androidide.R.string.msg_final_build_result_prefix));
                    listener.stdSuccess(currentTask, msg);
                    listener.onBuildSuccessful(currentTask, msg);
                } else {
                    listener.stdErr(currentTask, getString(com.itsaky.androidide.R.string.msg_final_build_result_prefix));
                    listener.stdErr(currentTask, msg);
                    listener.onBuildFailed(currentTask, msg);
                }
                currentTask = null;
                break;
            case Method.API_ERROR :
                ErrorMessage error = GSON.fromJson(message.result, ErrorMessage.class);
                listener.stdErr(currentTask, String.format("========== AndroidIDE Error ==========\nError code: %d\nError message: %s\n======================================", error.code, error.message));
                break;
        }
    }

    @Override
    protected void onResponse(String line) {
//      writeOut(line.trim() + "\n");
        onResponse(GSON.fromJson(line, ResponseMessage.class));
    }

    private BufferedOutputStream fos;

    private void writeOut(String line) {
        try {
            if (fos == null)
                fos = new BufferedOutputStream(new FileOutputStream(new File(FileUtil.getExternalStorageDir(), "ide_xlog/gradle-api-out.txt"), true));
            fos.write(line.getBytes());
            fos.flush();
        } catch (Throwable th) {}
    }

	public IDEService setListener(BuildListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public void output(CharSequence charSequence) {
//		writeOut(charSequence.toString().trim() + "\n");
	}

    public void exit() {
        if (mSocket != null) {
            CloseUtils.closeIOQuietly(mSocket);
        }
        // Delete the tmp directory. It is not needed anymore...
        FileUtils.delete(Environment.TMP_DIR);
		isRunning = false;
    }

	private String currentTime() {
		String pattern = "HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern, Locale.US);
		Date today = Calendar.getInstance().getTime();
		return df.format(today);
	}
    
    private String getString(@StringRes int id) {
        return app.getString(id);
    }
    
    private String getString(@StringRes int id, Object... format) {
        return app.getString(id, format);
    }

	private List<String> getArguments() {
		final PreferenceManager prefs = app.getPrefManager();
		final List<String> args = new ArrayList<>();
        
        args.add("--init-script");
        args.add(Environment.INIT_SCRIPT.getAbsolutePath());
        
		if (prefs.isStracktraceEnabled()) {
			args.add("--stacktrace");
		}
		if (prefs.isGradleInfoEnabled()) {
			args.add("--info");
		}
		if (prefs.isGradleDebugEnabled()) {
			args.add("--debug");
		}
		if (prefs.isGradleScanEnabled()) {
			args.add("--scan");
		}
		if (prefs.isGradleWarningEnabled()) {
			args.add("--warning-mode");
            args.add("all");
		}
        
		return args;
	}

	public void execTask(GradleTask task) {
		execTask(task, false);
	}

	public void execTask(GradleTask task, boolean stopIfRunning) {
		if (stopIfRunning && isBuilding()) {
			stopAllDaemons();
		} else if (isBuilding()) {
			return;
		}

        buildStart = System.currentTimeMillis();
		if (listener != null) {
			listener.saveFiles();
			currentTask = task;
            BuildParams params = new BuildParams(":" + main.name, task.getTasks(), getArguments());
            writeMessage(Method.PROJECT_BUILD, params);
			isBuilding = true;
			listener.prepare();
		}
	}

	public void assembleDebug(boolean installApk) {
		execTask(((AssembleDebug) BaseGradleTasks.ASSEMBLE_DEBUG).setInstallApk(installApk));
	}

	public void assembleRelease() {
		execTask(BaseGradleTasks.ASSEMBLE_RELEASE);
	}

	public void build() {
		execTask(BaseGradleTasks.BUILD);
	}

	public void bundle() {
		execTask(BaseGradleTasks.BUNDLE);
	}

	public void mergeLibAndProjectDex() {
		execTask(BaseGradleTasks.MERGE_LIB_AND_PROJECT_DEX);
	}

	public void mergeExtDex() {
		execTask(BaseGradleTasks.MERGE_EXT_DEX);
	}

	public void mergeDex() {
		execTask(BaseGradleTasks.MERGE_DEX);
	}

	public void clean() {
		execTask(BaseGradleTasks.CLEAN);
	}

	public void cleanAndRebuild() {
		execTask(BaseGradleTasks.CLEAN_BUILD);
	}

	public void stopAllDaemons() {
        app.newShell(null).bgAppend("gradle --stop");
	}

	public void lint() {
		execTask(BaseGradleTasks.LINT);
	}

	public void lintDebug() {
		execTask(BaseGradleTasks.LINT_DEBUG);
	}

	public void lintRelease() {
		execTask(BaseGradleTasks.LINT_RELEASE);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public boolean isBuilding() {
		return isBuilding;
	}

    public boolean isConnected() {
        return isConnected;
    }
    
    public boolean isProjectInitialized() {
        return projectInitialized;
    }

    protected void writeMessage(String method, Object params) {
        Message msg = new Message();
        msg.id = UNIVERSAL_ID++;
        msg.method = method;
        msg.param = GSON.toJsonTree(params);
        write(msg);
    }

	public static interface BuildListener {
        void onProjectInitialized(IDEProject project, Module main);
	    void onConnectionProgressChanged(String status);
        void onBuildProgress(GradleTask task, BuildProgressEvent event);
		void onBuildSuccessful(GradleTask task, String msg);
		void onBuildFailed(GradleTask task, String msg);
		void saveFiles();
        
        void stdOut(GradleTask task, CharSequence text);
		void stdErr(GradleTask task, CharSequence text);
        void stdSuccess(GradleTask task, CharSequence text);
		void prepare();
	}
}
