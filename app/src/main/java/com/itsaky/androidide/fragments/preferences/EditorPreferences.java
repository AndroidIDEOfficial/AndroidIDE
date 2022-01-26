/************************************************************************************
 * This file is part of AndroidIDE.
 * 
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.androidide.fragments.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.models.PrefBasedJavaServerSettings;
import com.itsaky.androidide.utils.DialogUtils;

import static com.itsaky.androidide.managers.PreferenceManager.*;
import static com.itsaky.androidide.models.PrefBasedJavaServerSettings.*;

public class EditorPreferences extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    
    @Override
	public void onCreatePreferences(Bundle p1, String p2) {
		super.onCreatePreferences(p1, p2);
		if(getContext() == null) return;
		
		final var screen = getPreferenceScreen();
		final var commonCategory = new PreferenceCategory (getContext ());
		final var javaCategory = new PreferenceCategory (getContext ());
		
		final var fontSize = new Preference(getContext());
		final var nonPrintable = new Preference(getContext());
        final var tabSize = new Preference(getContext());
		final var drawHex = new SwitchPreference(getContext());
		
		final var javaMatchLower = new SwitchPreference (getContext ());
		
		screen.addPreference (commonCategory);
		screen.addPreference (javaCategory);
        
		fontSize.setIcon(R.drawable.ic_text_size);
		fontSize.setKey(KEY_EDITOR_FONT_SIZE);
		fontSize.setTitle(R.string.idepref_editor_fontsize_title);
		fontSize.setSummary(R.string.idepref_editor_fontsize_summary);
		
		nonPrintable.setIcon(R.drawable.ic_drawing);
		nonPrintable.setKey(KEY_EDITOR_PRINTABLE_CHARS);
		nonPrintable.setTitle(R.string.idepref_editor_paintingflags_title);
		nonPrintable.setSummary(R.string.idepref_editor_paintingflags_summary);
        
        tabSize.setIcon(R.drawable.ic_tab);
        tabSize.setKey(KEY_EDITOR_TAB_SIZE);
        tabSize.setTitle(R.string.title_tab_size);
        tabSize.setSummary(R.string.msg_tab_size);
		
		drawHex.setIcon(R.drawable.ic_hexadecimal);
		drawHex.setKey(KEY_EDITOR_DRAW_HEX);
		drawHex.setTitle(R.string.idepref_editor_drawhexcolors_title);
		drawHex.setSummary(R.string.idepref_editor_drawhexcolors_summary);
		
		javaMatchLower.setKey (KEY_JAVA_PREF_MATCH_LOWER);
		javaMatchLower.setIcon (R.drawable.ic_text_lower);
		javaMatchLower.setTitle (getString(R.string.idepref_java_matchLower_title));
		javaMatchLower.setSummary (getString(R.string.idepref_java_matchLower_summary));
		
		commonCategory.setTitle (getString(R.string.idepref_editor_category_common));
		commonCategory.addPreference(fontSize);
		commonCategory.addPreference(nonPrintable);
		commonCategory.addPreference(tabSize);
		commonCategory.addPreference(drawHex);
		
		javaCategory.setTitle (getString(R.string.idepref_editor_category_java));
		javaCategory.addPreference (javaMatchLower);
		
		setPreferenceScreen(screen);
		
		fontSize.setOnPreferenceClickListener(this);
		nonPrintable.setOnPreferenceClickListener(this);
        tabSize.setOnPreferenceClickListener(this);
		drawHex.setOnPreferenceChangeListener(this);
		javaMatchLower.setOnPreferenceChangeListener (this);
		
		drawHex.setChecked(getPrefManager().getBoolean(KEY_EDITOR_DRAW_HEX, true));
		javaMatchLower.setChecked (getPrefManager ().getBoolean (KEY_JAVA_PREF_MATCH_LOWER, false));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
    	final boolean value = (boolean) newValue;
		if(preference.getKey().equals(KEY_EDITOR_DRAW_HEX)) {
			getPrefManager().putBoolean(KEY_EDITOR_DRAW_HEX, value);
			ConstantsBridge.EDITOR_PREF_DRAW_HEX_CHANGED = true;
		} else if (preference.getKey ().equals (KEY_JAVA_PREF_MATCH_LOWER)) {
			getPrefManager ().putBoolean (KEY_JAVA_PREF_MATCH_LOWER, value);
			final var javaServer = StudioApp.getInstance ().getJavaLanguageServer ();
			javaServer.applySettings (PrefBasedJavaServerSettings.getInstance ());
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		final String key = preference.getKey();
		if(key.equals(KEY_EDITOR_FONT_SIZE)) {
			showTextSizeDialog();
		} else if(key.equals(KEY_EDITOR_PRINTABLE_CHARS)) {
			showPrintableCharsDialog();
		} else if(key.equals(KEY_EDITOR_TAB_SIZE)) {
            showTabSizeDialog();
        }
		return true;
	}

    private void showTabSizeDialog() {
        final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder (getContext ());
        final String[] sizes = new String[]{"2", "4", "6", "8"};
        
        // We apply simple maths,
        // assuming that tab size options have a difference of 2
        // And the array is sorted
        // Like, 2, 4, 6, 8, 10, etc
        // 
        // If current tab size if 4, 4/2 = 2 and 2 - 1 = 1, so option at index 1 will be selected which is current tab size (4)!
        int current = (getPrefManager().getEditorTabSize() / 2) - 1;
        if(current < 0 || current >= sizes.length) current = 1;
        builder.setTitle(R.string.title_tab_size);
        builder.setSingleChoiceItems(sizes, current, (d, i) -> {
            d.dismiss();
            
			// Reversing the logic applied above...
            int tabSize = (i + 1) * 2;
            if(tabSize < 2 || tabSize > 8) tabSize = 4;
            getPrefManager().putInt(KEY_EDITOR_TAB_SIZE, tabSize);
        });
       builder.setCancelable(true);
       builder.show();
    }
	
	private void showTextSizeDialog() {
		final LayoutTextSizeSliderBinding binding = LayoutTextSizeSliderBinding.inflate(LayoutInflater.from(getContext()));
		final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder (getContext ());
		float size = getPrefManager().getFloat(KEY_EDITOR_FONT_SIZE, 14);
		if(size < 6 || size > 32) {
			size = 14;
		}
		changeTextSize(binding, size);
		
		binding.slider.setLabelFormatter(p1 -> String.valueOf((int) p1));
		builder.setTitle(R.string.title_change_text_size);
		builder.setMessage(R.string.msg_editor_font_size);
		builder.setView(binding.getRoot());
		builder.setPositiveButton(android.R.string.ok, (p1, p2) -> {
			p1.dismiss();
			float s = binding.slider.getValue();
			changeTextSize(binding, s);
		});
		builder.setNeutralButton(R.string.reset, (p1, p2) -> {
			changeTextSize(binding, 14);
		});
		builder.setCancelable(false);
		builder.show();
	}
	
	private void changeTextSize(LayoutTextSizeSliderBinding binding, float size) {
		getPrefManager().putFloat(KEY_EDITOR_FONT_SIZE, size);
		binding.slider.setValue(size);
		ConstantsBridge.EDITOR_PREF_SIZE_CHANGED = true;
	}
	
	private void showPrintableCharsDialog() {
		String[] labels = {
			"Leading whitespaces",
			"Trailing whitespaces",
			"Inner whitespaces",
			"Whitespaces in empty lines",
			"Line breaks"
		};
		final boolean[] checked = {
			getPrefManager().getBoolean(KEY_EDITOR_FLAG_WS_LEADING, true),
			getPrefManager().getBoolean(KEY_EDITOR_FLAG_WS_TRAILING, false),
			getPrefManager().getBoolean(KEY_EDITOR_FLAG_WS_INNER, true),
			getPrefManager().getBoolean(KEY_EDITOR_FLAG_WS_EMPTY_LINE, true),
			getPrefManager().getBoolean(KEY_EDITOR_FLAG_LINE_BREAK, true)
		};
		final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder (getContext ());
		builder.setTitle(R.string.idepref_editor_paintingflags_title);
		builder.setMultiChoiceItems(labels, checked, (dialog, which, isChecked) -> {
			
			if(which == 0) {
				getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_LEADING, isChecked);
			} else if(which == 1) {
				getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_TRAILING, isChecked);
			} else if(which == 2) {
				getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_INNER, isChecked);
			} else if(which == 3) {
				getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_EMPTY_LINE, isChecked);
			} else if(which == 4) {
				getPrefManager().putBoolean(KEY_EDITOR_FLAG_LINE_BREAK, isChecked);
			}
			
			ConstantsBridge.EDITOR_PREF_FLAGS_CHANGED = true;
		});
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setCancelable(false);
		builder.show();
	}
}