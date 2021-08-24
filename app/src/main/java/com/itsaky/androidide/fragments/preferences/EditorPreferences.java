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

import static com.itsaky.androidide.managers.PreferenceManager.*;
import com.itsaky.androidide.models.ConstantsBridge;
import com.google.android.material.slider.LabelFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class EditorPreferences extends BasePreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    
	public static final String KEY_EDITOR_FONT_SIZE = "idepref_editor_fontSize";
	public static final String KEY_EDITOR_PRINTABLE_CHARS = "idepref_editor_nonPrintableFlags";
	public static final String KEY_EDITOR_DRAW_HEX = "idepref_editor_drawHexColors";
	
    @Override
	public void onCreatePreferences(Bundle p1, String p2) {
		super.onCreatePreferences(p1, p2);
		if(getContext() == null) return;
		
		final PreferenceScreen screen = getPreferenceScreen();
		final Preference fontSize = new Preference(getContext());
		final Preference nonPrintable = new Preference(getContext());
		final SwitchPreference drawHex = new SwitchPreference(getContext());

		fontSize.setIcon(R.drawable.ic_text_size);
		fontSize.setKey(KEY_EDITOR_FONT_SIZE);
		fontSize.setTitle(R.string.idepref_editor_fontsize_title);
		fontSize.setSummary(R.string.idepref_editor_fontsize_summary);
		
		nonPrintable.setIcon(R.drawable.ic_drawing);
		nonPrintable.setKey(KEY_EDITOR_PRINTABLE_CHARS);
		nonPrintable.setTitle(R.string.idepref_editor_paintingflags_title);
		nonPrintable.setSummary(R.string.idepref_editor_paintingflags_summary);
		
		drawHex.setIcon(R.drawable.ic_hexadecimal);
		drawHex.setKey(KEY_EDITOR_DRAW_HEX);
		drawHex.setTitle(R.string.idepref_editor_drawhexcolors_title);
		drawHex.setSummary(R.string.idepref_editor_drawhexcolors_summary);

		screen.addPreference(fontSize);
		screen.addPreference(nonPrintable);
		screen.addPreference(drawHex);
		setPreferenceScreen(screen);
		
		fontSize.setOnPreferenceClickListener(this);
		nonPrintable.setOnPreferenceClickListener(this);
		drawHex.setOnPreferenceChangeListener(this);
		
		drawHex.setChecked(getPrefManager().getBoolean(KEY_EDITOR_DRAW_HEX, true));
	}

	@Override
	public boolean onPreferenceChange(Preference p1, Object p2) {
		if(p1.getKey().equals(KEY_EDITOR_DRAW_HEX)) {
			boolean drawHex = (boolean) p2;
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
		}
		return true;
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
		builder.create().show();
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
		builder.create().show();
	}
}
