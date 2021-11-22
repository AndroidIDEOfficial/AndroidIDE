/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding;
import com.itsaky.androidide.models.ConstantsBridge;

import static com.itsaky.androidide.managers.PreferenceManager.*;

public class EditorPreferences extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    
	public static final String KEY_EDITOR_FONT_SIZE = "idepref_editor_fontSize";
	public static final String KEY_EDITOR_PRINTABLE_CHARS = "idepref_editor_nonPrintableFlags";
	public static final String KEY_EDITOR_DRAW_HEX = "idepref_editor_drawHexColors";
	public static final String KEY_EDITOR_TAB_SIZE = "idepref_editor_tabSize";
    public static final String KEY_EDITOR_JAVADOC_ENABLED = "idepref_editor_javadocEnabled";
    
    @Override
	public void onCreatePreferences(Bundle p1, String p2) {
		super.onCreatePreferences(p1, p2);
		if(getContext() == null) return;
		
		final PreferenceScreen screen = getPreferenceScreen();
		final Preference fontSize = new Preference(getContext());
		final Preference nonPrintable = new Preference(getContext());
        final Preference tabSize = new Preference(getContext());
		final SwitchPreference drawHex = new SwitchPreference(getContext());
        
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

		screen.addPreference(fontSize);
		screen.addPreference(nonPrintable);
        screen.addPreference(tabSize);
		screen.addPreference(drawHex);
		setPreferenceScreen(screen);
		
		fontSize.setOnPreferenceClickListener(this);
		nonPrintable.setOnPreferenceClickListener(this);
        tabSize.setOnPreferenceClickListener(this);
		drawHex.setOnPreferenceChangeListener(this);
		
		drawHex.setChecked(getPrefManager().getBoolean(KEY_EDITOR_DRAW_HEX, true));
	}

	@Override
	public boolean onPreferenceChange(Preference p1, Object p2) {
		if(p1.getKey().equals(KEY_EDITOR_DRAW_HEX)) {
			boolean drawHex = (Boolean) p2;
			getPrefManager().putBoolean(KEY_EDITOR_DRAW_HEX, drawHex);
			ConstantsBridge.EDITORPREF_DRAW_HEX_CHANGED = true;
		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference p1) {
		final String key = p1.getKey();
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
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
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
            int index = i;
            
            // Reversing the logic applied above...
            int tabSize = (index + 1) * 2;
            if(tabSize < 2 || tabSize > 8) tabSize = 4;
            getPrefManager().putInt(KEY_EDITOR_TAB_SIZE, tabSize);
        });
       builder.setCancelable(true);
       builder.show();
    }
	
	private void showTextSizeDialog() {
		final LayoutTextSizeSliderBinding binding = LayoutTextSizeSliderBinding.inflate(LayoutInflater.from(getContext()));
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
		float size = getPrefManager().getFloat(KEY_EDITOR_FONT_SIZE, 14);
		if(size < 6 || size > 32) {
			size = 14;
			changeTextSize(binding, size);
		}
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
		ConstantsBridge.EDITORPREF_SIZE_CHANGED = true;
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
			getPrefManager().getBoolean(KEY_EDITORFLAG_WS_LEADING, true),
			getPrefManager().getBoolean(KEY_EDITORFLAG_WS_TRAILING, false),
			getPrefManager().getBoolean(KEY_EDITORFLAG_WS_INNER, true),
			getPrefManager().getBoolean(KEY_EDITORFLAG_WS_EMPTY_LINE, true),
			getPrefManager().getBoolean(KEY_EDITORFLAG_LINE_BREAK, true)
		};
		final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(), R.style.AppTheme_MaterialAlertDialog);
		builder.setTitle(R.string.idepref_editor_paintingflags_title);
		builder.setMultiChoiceItems(labels, checked, (p1, p2, p3) -> {
			DialogInterface iface = p1;
			int pos = p2;
			boolean isChecked = p3;
			
			if(pos == 0) {
				getPrefManager().putBoolean(KEY_EDITORFLAG_WS_LEADING, isChecked);
			} else if(pos == 1) {
				getPrefManager().putBoolean(KEY_EDITORFLAG_WS_TRAILING, isChecked);
			} else if(pos == 2) {
				getPrefManager().putBoolean(KEY_EDITORFLAG_WS_INNER, isChecked);
			} else if(pos == 3) {
				getPrefManager().putBoolean(KEY_EDITORFLAG_WS_EMPTY_LINE, isChecked);
			} else if(pos == 4) {
				getPrefManager().putBoolean(KEY_EDITORFLAG_LINE_BREAK, isChecked);
			}
			
			ConstantsBridge.EDITORPREF_FLAGS_CHANGED = true;
		});
		builder.setPositiveButton(android.R.string.ok, null);
		builder.setCancelable(false);
		builder.show();
	}
}
