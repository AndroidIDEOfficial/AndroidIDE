package com.itsaky.androidide.tasks.git;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.app.Activity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.net.URL;
import com.itsaky.androidide.utils.ILogger;
import io.reactivex.Observer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import com.itsaky.androidide.utils.Environment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.util.concurrent.Callable;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.preferences.R;

public class CloneGitTask {
	Activity activity;
	AlertDialog alertDialog;
	private static final String TAG = "CloneGitTask";
	TextInputLayout textInputLayout;
	URL uRL;
	View view;
	AlertDialog progressDialog;
        private Disposable mDisposable;
        
	public CloneGitTask(Activity mActivity) {
		activity = mActivity;
	}

	public void cloneRepo() {
		//custom layout
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.layout_dialog_text_input, null);

		textInputLayout = view.findViewById(R.id.name);
		textInputLayout.setHint(R.string.repository_url);
		//materialdialog
		MaterialAlertDialogBuilder materialAlertDialogBuilder = DialogUtils.newMaterialDialogBuilder(activity);
		materialAlertDialogBuilder.setView(view);
		materialAlertDialogBuilder.setTitle(R.string.clone_git_repository);
		materialAlertDialogBuilder.setCancelable(true);
		materialAlertDialogBuilder.setPositiveButton(R.string.clone, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dia, int which) {

				//url
				String urlRepo = String.valueOf(textInputLayout.getEditText().getText());

				//materialprogress dialog
				LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.git_dialog_progress, null);

				MaterialAlertDialogBuilder materialAlertDialogBuilder = DialogUtils.newMaterialDialogBuilder(activity);
				materialAlertDialogBuilder.setTitle(activity.getString(R.string.cloning_repository));
				materialAlertDialogBuilder.setMessage(urlRepo);
				materialAlertDialogBuilder.setCancelable(false);
				materialAlertDialogBuilder.setView(view);
				
				materialAlertDialogBuilder.setPositiveButton(R.string.hide, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dia, int which) {
						progressDialog.dismiss();
					}
				});
				materialAlertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dia, int which) {
						mDisposable.dispose();
					}
				});
				progressDialog = materialAlertDialogBuilder.create();
				progressDialog.show();

				//createObservable
				createObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
						.subscribe(new Observer<String>() {
							@Override
							public void onSubscribe(Disposable disposable) {			
                                                                mDisposable = disposable;
							}

							@Override
							public void onNext(String value) {

							}

							@Override
							public void onError(Throwable e) {
								progressDialog.dismiss();
								alertDialog.dismiss();

								MaterialAlertDialogBuilder materialAlertDialogBuilder = DialogUtils.newMaterialDialogBuilder(
										activity);
								materialAlertDialogBuilder.setTitle(R.string.git_clone_failed);

								//if file exists
								if (e.toString().contains("org.eclipse.jgit.api.errors.JGitInternalException")) {
									materialAlertDialogBuilder
											.setMessage(e.toString().substring(e.toString().lastIndexOf(":") + 2));
								}
								//if missing url
								else if (e.toString().contains("java.net.MalformedURLException: no protocol")) {
									materialAlertDialogBuilder.setMessage(
											e.toString().substring(e.toString().lastIndexOf(":") + 2) + " "
													+ activity.getString(R.string.not_found));
								} else {
									//if new error found	
									materialAlertDialogBuilder.setMessage(e.toString());
								}
								materialAlertDialogBuilder.setCancelable(true);
								materialAlertDialogBuilder.setPositiveButton(android.R.string.ok, null);
								materialAlertDialogBuilder.show();
							}

							@Override
							public void onComplete() {	
								progressDialog.dismiss();

								MaterialAlertDialogBuilder materialAlertDialogBuilder = DialogUtils.newMaterialDialogBuilder(
										activity);
								materialAlertDialogBuilder.setTitle(R.string.git_clone_successful);
								materialAlertDialogBuilder
										.setMessage(urlRepo + " " + activity.getString(R.string.successfully_cloned));
								materialAlertDialogBuilder.setCancelable(true);
								materialAlertDialogBuilder.setPositiveButton(android.R.string.ok, null);
								materialAlertDialogBuilder.show();
							}
						});
			}
		});
		materialAlertDialogBuilder.setNegativeButton(android.R.string.cancel, null);
		alertDialog = materialAlertDialogBuilder.show();
	}

	public String run(String url) throws IOException {

		try {
			String urlRepo = String.valueOf(textInputLayout.getEditText().getText());
			uRL = new URL(urlRepo.toString());
			String fileName = uRL.getFile();

			//clone repo
			Git.cloneRepository().setURI(uRL + ".git")
					.setDirectory(Paths
							.get(Environment.PROJECTS_DIR.getAbsolutePath() + fileName.substring(fileName.lastIndexOf('/') + 0))
							.toFile())
					.call();

		} catch (GitAPIException e) {
			System.out.println("Exception occurred while cloning repo");			
                        e.printStackTrace();
		}
		return url;
	}

	private Observable<String> createObservable() {

		//Could use fromCallable
		return Observable.defer(new Callable<ObservableSource<? extends String>>() {
			@Override
			public ObservableSource<? extends String> call() throws Exception {
				return Observable.just(run(TAG));
			}
		});

	}
}
