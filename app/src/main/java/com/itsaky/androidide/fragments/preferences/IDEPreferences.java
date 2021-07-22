package com.itsaky.androidide.fragments.preferences;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import org.json.JSONArray;
import org.json.JSONObject;
import com.blankj.utilcode.util.SizeUtils;

public class IDEPreferences extends BasePreferenceFragment {
	
	private AppearancePreferences mAppearancePrefs;
	private BuildPreferences mBuildPreferences;
	private EditorPreferences mEditorPreferences;
	
	private String CHANGELOG = null;
	
	public static final String KEY_APPEARANCE = "idepref_appearance";
	public static final String KEY_EDITOR = "idepref_editor";
	public static final String KEY_BUILD = "idepref_build";
	public static final String KEY_TELEGRAM = "idepref_telegram";
	public static final String KEY_ISSUES = "idepref_issues";
	public static final String KEY_CHANGELOG = "idepref_changelog";
	
	@Override
	public void onCreatePreferences(Bundle savedState, String rootKey) {
		super.onCreatePreferences(savedState, rootKey);
		if(getContext() == null) return;
		
		final PreferenceScreen screen = getPreferenceScreen();
		final Preference appearance = new Preference(getContext());
		final Preference editor = new Preference(getContext());
		final Preference build = new Preference(getContext());
		final Preference telegram = new Preference(getContext());
		final Preference issueTracker = new Preference(getContext());
		final Preference changelog = new Preference(getContext());
		
		appearance.setKey(KEY_APPEARANCE);
		appearance.setIconSpaceReserved(false);
		appearance.setFragment(getAppearanceFrag().getClass().getName());
		appearance.setTitle(R.string.idepref_appearance_title);
		appearance.setSummary(R.string.idepref_appearance_summary);
		
		editor.setKey(KEY_EDITOR);
		editor.setIconSpaceReserved(false);
		editor.setFragment(getEditorFrag().getClass().getName());
		editor.setTitle(R.string.idepref_editor_title);
		editor.setSummary(R.string.idepref_editor_summary);
		
		build.setKey(KEY_BUILD);
		build.setIconSpaceReserved(false);
		build.setFragment(getBuildFrag().getClass().getName());
		build.setTitle(R.string.idepref_build_title);
		build.setSummary(R.string.idepref_build_summary);
		
		issueTracker.setKey(KEY_ISSUES);
		issueTracker.setIconSpaceReserved(false);
		issueTracker.setTitle(R.string.user_suggestions);
		issueTracker.setSummary(R.string.issue_tracker_summary);
		
		telegram.setKey(KEY_TELEGRAM);
		telegram.setIconSpaceReserved(false);
		telegram.setTitle(R.string.discussions_on_telegram);
		telegram.setSummary(R.string.discussions_on_telegram_summary);
		
		changelog.setKey(KEY_CHANGELOG);
		changelog.setIconSpaceReserved(false);
		changelog.setTitle(R.string.pref_changelog);
		changelog.setSummary(R.string.pref_changelog_summary);
		
		screen.addPreference(editor);
		screen.addPreference(build);
//		screen.addPreference(issueTracker);
		screen.addPreference(telegram);
		screen.addPreference(changelog);
		
		final Preference.OnPreferenceClickListener listener = getListener();
		issueTracker.setOnPreferenceClickListener(listener);
		telegram.setOnPreferenceClickListener(listener);
		changelog.setOnPreferenceClickListener(listener);
	}
	
	private AppearancePreferences getAppearanceFrag() {
		return mAppearancePrefs == null ? mAppearancePrefs = new AppearancePreferences() : mAppearancePrefs;
	}
	
	private EditorPreferences getEditorFrag() {
		return mEditorPreferences == null ? mEditorPreferences = new EditorPreferences() : mEditorPreferences;
	}
	
	private BuildPreferences getBuildFrag() {
		return mBuildPreferences == null ? mBuildPreferences = new BuildPreferences() : mBuildPreferences;
	}
	
	private void showChangelog() {
		final int dp8 = SizeUtils.dp2px(8);
		final int dp16 = SizeUtils.dp2px(16);
		final HorizontalScrollView scroll = new HorizontalScrollView(getContext());
		scroll.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
		scroll.setFillViewport(true);
		scroll.setPadding(dp16, dp8, dp16, dp8);
		scroll.setPaddingRelative(dp16, dp8, dp16, dp8);
		
		final TextView text = new TextView(getContext());
		text.setText(CHANGELOG == null ? CHANGELOG = changelogAsString() : CHANGELOG);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		text.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryTextColor));
		scroll.removeAllViews();
		scroll.addView(text, new ViewGroup.LayoutParams(-1, -1));
		
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.pref_changelog);
		builder.setView(scroll);
		builder.setPositiveButton(android.R.string.ok, null);
		builder.create().show();
	}
	
	private String changelogAsString() {
		try {
			final SpannableStringBuilder sb = new SpannableStringBuilder();
			JSONArray arr = new JSONArray(ResourceUtils.readAssets2String("changelog.json"));
			for(int i =0;i<arr.length();i++) {
				JSONObject version = arr.getJSONObject(i);
				sb.append("v".concat(version.getString("version")), new StyleSpan(Typeface.BOLD), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				JSONArray changes = version.getJSONArray("changes");
				for(int j=0;j<changes.length();j++) {
					sb.append("\n    ");
					sb.append("\u2022 ".concat(changes.getString(j)));
				}
				sb.append("\n\n");
			}
			return sb.toString();
		} catch (Throwable th) {
			return getString(R.string.msg_failed_get_changelog);
		}
	}
	
	private Preference.OnPreferenceClickListener  getListener() {
		return new Preference.OnPreferenceClickListener(){

			@Override
			public boolean onPreferenceClick(Preference p1) {
				final String key = p1.getKey();
				if(key.equals(KEY_CHANGELOG)) {
					showChangelog();
				} else if(key.equals(KEY_TELEGRAM)) {
					StudioApp.getInstance().openTelegramGroup();
				} else if(key.equals(KEY_ISSUES)) {
					StudioApp.getInstance().openIssueTracker();
				}
				return true;
			}
		};
	}
}
