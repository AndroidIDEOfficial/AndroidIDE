package com.itsaky.androidide.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.blankj.utilcode.util.FileUtils;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.gradle.Artifact;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.tasks.GradleTask;
import com.itsaky.androidide.tasks.gradle.BaseGradleTasks;
import com.itsaky.androidide.tasks.gradle.build.AssembleDebug;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.PreferenceManager;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public class IDEService extends Service implements ShellServer.Callback {
	
	private static IDEService instance;
	private ShellServer shell;
	private BuildListener listener;
	private File module;
	private GradleTask currentTask;
	
	private List<String> dependencies;
	
	private boolean isBuilding = false;
	private boolean outputtingJars = false;
	private boolean recreateShellOnDone = false;
	
	private static boolean isRunning = false;
	
	private final String RUN_TASK = "> Task";
	private final String STARTING_DAEMON = "Starting a ";
	private final String BUILD_SUCCESS = "BUILD SUCCESSFUL";
	private final String BUILD_FAILED = "BUILD FAILED";
	private final String SHOW_DEPENDENCIES_START = ">> DEPENDENCIES START <<";
	private final String SHOW_DEPENDENCIES_END = ">> DEPENDENCIES END <<";
	
	public static final String ACTION_CREATED = "BUILD_SERVICE_CREATED";
	
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
	
	
	public IDEService() {}
	
	public IDEService(String module) {
		this(new File(module));
	}
	
	public IDEService(File module) {
		this.module = module;
	}

	public void onGradleUpdated() {
		if(shell == null) {
			createShell();
			return;
		}
		if(!isBuilding()) {
			createShell();
		} else {
			recreateShellOnDone = true;
		}
	}
	
	public static IDEService getInstance() {
		return instance;
	}

	public IDEService setListener(BuildListener listener) {
		this.listener = listener;
		return this;
	}
	
	public IDEService setModule(String path) {
		return setModule(new File(path));
	}
	
	public IDEService setModule(File module) {
		this.module = module;
		this.shell.append(String.format("cd \"%s\"", module.getAbsolutePath()));
		return this;
	}
	
	@Override
	public void output(CharSequence charSequence) {
		if(listener == null || charSequence == null) return;
		final String line = charSequence
								.toString()
								.replace(":showDependenciesDebug", ":startCompletionServices")
								.trim();
		
		boolean shouldOutput = true;
		if(line.contains(SHOW_DEPENDENCIES_START)) {
			shouldOutput = false;
			dependencies = new ArrayList<>();
			outputtingJars = true;
			if(listener != null)
				listener.onRunTask("> Task :app:startCompletionServices", currentTask);
			return;
		} else if(line.contains(SHOW_DEPENDENCIES_END)) {
			shouldOutput = false;
			outputtingJars = false;
			if(listener != null
			&& dependencies != null) {
				
				listener.onGetDependencies(dependencies);
			}
			StudioApp.getInstance().getPrefManager().putBoolean(PreferenceManager.KEY_IS_FIRST_PROJECT_BUILD, false);
			return;
		}
		
		if(outputtingJars) {
			
			JSONObject o = asObjectOrNull(line);
			try {
				if(o.has("type")) {
					String type = o.getString("type");
					if(type.equals("ANDROID_JAR")) {
						shouldOutput = false;
						Environment.setBootClasspath(new File(o.getString("requested")));
					}
				} else if(o.has("file")) {
					shouldOutput = false;
					String jar = o.getString("file");
					
					dependencies.add(jar);
				}
			} catch (Throwable th) {}
		}
		if(shouldOutput) {
			String text = line.replace(StudioApp.getInstance().getRootDir().getAbsolutePath(), "ANDROIDIDE_HOME");
			listener.appendOutput(currentTask, text);
		}
		if(charSequence.toString().contains(RUN_TASK)) {
			listener.onRunTask(charSequence.toString().trim(), currentTask);
		} else
		if(charSequence.toString().startsWith(STARTING_DAEMON)) {
			listener.onStartingGradleDaemon(currentTask);
		} else
		if(charSequence.toString().contains(BUILD_SUCCESS)) {
			isBuilding = false;
			outputtingJars = false;
			if(recreateShellOnDone)
				createShell();
			if(currentTask != null && currentTask.getTaskID() != TASK_SHOW_DEPENDENCIES)
				listener.onBuildSuccessful(charSequence.toString().trim(), currentTask);
		} else
		if(charSequence.toString().contains(BUILD_FAILED)) {
			isBuilding = false;
			outputtingJars = false;
			if(recreateShellOnDone)
				createShell();
			if(currentTask != null) {
				if(currentTask.getTaskID() == TASK_SHOW_DEPENDENCIES) 
					listener.onGetDependenciesFailed();
				else listener.onBuildFailed(line.trim(), currentTask);
			}
		}
	}

	private ArrayList<Artifact> dependenciesMapAsList(HashMap<String, String> dependencies) {
		ArrayList<Artifact> result = new ArrayList<>();
		for(Map.Entry<String, String> entry : dependencies.entrySet()) {
			result.add(Artifact.from(String.format("%s:%s", entry.getKey(), entry.getValue())));
		}
		return result;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.instance = this;
		createShell();
		sendBroadcast(new Intent().setAction(ACTION_CREATED));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isRunning = true;
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
        FileUtils.delete(Environment.TMP_DIR);
		isRunning = false;
	}
	
	@Override
	public IBinder onBind(Intent p1) {
		return null;
	}
	
	private void createShell() {
		if(shell != null)
			shell.exit();
		shell = StudioApp.getInstance().newShell(this);
		recreateShellOnDone = false;
	}
	
	private JSONObject asObjectOrNull(String line) {
		try {
			if(line == null) return null;
			line = line.trim();
			if(line.length() <= 0) return null;
			return new JSONObject(line);
		} catch (Throwable th) {
			return null;
		}
	}
	
	private String currentTime() {
		String pattern = "HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern, Locale.US);
		Date today = Calendar.getInstance().getTime();
		return df.format(today);
	}
	
	private String processCommands(String cmd) {
		final PreferenceManager prefs = StudioApp.getInstance().getPrefManager();
		final StringBuilder sb = new StringBuilder();
		sb.append("gradle ");
		sb.append(cmd);
		if(prefs.isStracktraceEnabled()) {
			sb.append(" --stacktrace");
		}
		if(prefs.isGradleInfoEnabled()) {
			sb.append(" --info");
		}
		if(prefs.isGradleDebugEnabled()) {
			sb.append(" --debug");
		}
		if(prefs.isGradleScanEnabled()) {
			sb.append(" --scan");
		}
		if(prefs.isGradleWarningEnabled()) {
			sb.append(" --warning-mode all");
		}
		return sb.toString();
	}
	
	public void execTask(GradleTask task) {
		execTask(task, false);
	}
	
	public void execTask(GradleTask task, boolean stopIfRunning) {
		if(stopIfRunning && isBuilding()) {
			stopAllDaemons();
		} else if(isBuilding()) {
			return;
		}
		if(listener != null) {
			listener.saveFiles();
			currentTask = task;
			listener.appendOutput(task, getString(R.string.msg_task_begin, currentTime(), task.getName()));
			shell.bgAppend(String.format("cd \"%s\"", module.getAbsolutePath()));
			shell.bgAppend(processCommands(task.getCommands()));
			isBuilding = true;
			listener.showPreparing();
		}
	}
	
	public void showDependencies() {
		execTask(BaseGradleTasks.SHOW_DEPENDENCIES);
	}
	
	public void recreateServices() {
		execTask(BaseGradleTasks.SHOW_DEPENDENCIES, true);
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
		if(isBuilding()) {
			forceStopDaemons();
		} else {
			shell.bgAppend("gradle --stop");
			currentTask = null;
			if(listener != null) {
				listener.appendOutput(null, getString(R.string.msg_daemons_stopped));
			}
		}
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
	
	public void forceStopDaemons() {
		if(listener != null) {
			final long s = System.currentTimeMillis();
			listener.appendOutput(null, getString(R.string.msg_daemons_force_stopping));
			// Wait for a second! Just to let the user read the message!
			while(System.currentTimeMillis() - s <= 1000) {}
		}
		isBuilding = false;
		createShell();
		stopAllDaemons();
	}
	
	public static boolean isRunning() {
		return isRunning;
	}
	
	public boolean isBuilding() {
		return isBuilding;
	}
	
	public String typeString(int type) {
		switch (type) {
			case TASK_SHOW_DEPENDENCIES:
				return "";
			case TASK_ASSEMBLE_DEBUG:
				return getResources().getString(R.string.build_debug);
			case TASK_ASSEMBLE_RELEASE:
				return getResources().getString(R.string.build_release);
			case TASK_BUILD:
				return getResources().getString(R.string.build);
			case TASK_CLEAN_BUILD:
				return getResources().getString(R.string.clean_amp_build);
			case TASK_CLEAN:
				return getResources().getString(R.string.clean_project);
			case TASK_BUNDLE :
				return getResources().getString(R.string.create_aab);
			case TASK_DEX:
				return getResources().getString(R.string.compiling);
			default: return "";
		}
	}
	
	public static interface BuildListener {
		public void onStartingGradleDaemon(GradleTask task);
		public void onRunTask(String taskName, GradleTask task);
		public void onBuildSuccessful(String msg, GradleTask task);
		public void onBuildFailed(String msg, GradleTask task);
		public void onGetDependencies(List<String> dependencies);
		public void onGetDependenciesFailed();
		public void saveFiles();
		
		public void appendOutput(GradleTask task, CharSequence text);
		public void showPreparing();
	}
}
