/*
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
 */
package com.itsaky.androidide.fragments.preferences;

import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_AUTO_SAVE;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_DRAW_HEX;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_LINE_BREAK;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_PASSWORD;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_WS_EMPTY_LINE;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_WS_INNER;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_WS_LEADING;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FLAG_WS_TRAILING;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FONT_LIGATURES;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_FONT_SIZE;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_PRINTABLE_CHARS;
import static com.itsaky.androidide.managers.PreferenceManager.KEY_EDITOR_TAB_SIZE;
import static com.itsaky.lsp.java.models.JavaServerSettings.KEY_COMPLETIONS_MATCH_LOWER;
import static com.itsaky.lsp.java.models.JavaServerSettings.KEY_JAVA_PREF_GOOGLE_CODE_STYLE;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ConstantsBridge;
import com.itsaky.androidide.utils.DialogUtils;

public class EditorPreferences extends BasePreferenceFragment
    implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

  @Override
  public void onCreatePreferences(Bundle p1, String p2) {
    super.onCreatePreferences(p1, p2);

    if (getContext() == null) {
      return;
    }

    final var screen = getPreferenceScreen();
    final var commonCategory = new PreferenceCategory(getContext());
    final var javaCategory = new PreferenceCategory(getContext());

    final var fontSize = new Preference(getContext());
    final var nonPrintable = new Preference(getContext());
    final var tabSize = new Preference(getContext());
    final var drawHex = new SwitchPreference(getContext());
    final var wordWrap = new SwitchPreference(getContext());
    final var magnifier = new SwitchPreference(getContext());
    final var fontLigatures = new SwitchPreference(getContext());
    final var autoSave = new SwitchPreference(getContext());
    final var useGoogleCodeStyle = new SwitchPreference(getContext());
    final var visiblePasswordFlag = new SwitchPreference(getContext());

    final var completionsMatchLower = new SwitchPreference(getContext());

    screen.addPreference(commonCategory);
    screen.addPreference(javaCategory);

    fontSize.setIcon(R.drawable.ic_text_size);
    fontSize.setKey(KEY_EDITOR_FONT_SIZE);
    fontSize.setTitle(R.string.idepref_editor_fontsize_title);
    fontSize.setSummary(R.string.idepref_editor_fontsize_summary);

    wordWrap.setIcon(R.drawable.ic_wrap_text);
    wordWrap.setKey(PreferenceManager.KEY_EDITOR_WORD_WRAP);
    wordWrap.setTitle(R.string.idepref_editor_word_wrap_title);
    wordWrap.setSummary(R.string.idepref_editor_word_wrap_summary);

    magnifier.setIcon(R.drawable.ic_loupe);
    magnifier.setKey(PreferenceManager.KEY_EDITOR_USE_MAGNIFER);
    magnifier.setTitle(R.string.idepref_editor_use_magnifier_title);
    magnifier.setSummary(R.string.idepref_editor_use_magnifier_summary);

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

    fontLigatures.setIcon(R.drawable.ic_font_ligatures);
    fontLigatures.setKey(KEY_EDITOR_FONT_LIGATURES);
    fontLigatures.setTitle(getString(R.string.idepref_editor_ligatures_title));
    fontLigatures.setSummary(getString(R.string.idepref_editor_ligatures_summary));

    autoSave.setIcon(R.drawable.ic_save);
    autoSave.setKey(KEY_EDITOR_AUTO_SAVE);
    autoSave.setTitle(getString(R.string.idepref_editor_autoSave_title));
    autoSave.setSummary(getString(R.string.idepref_editor_autoSave_summary));

    completionsMatchLower.setKey(KEY_COMPLETIONS_MATCH_LOWER);
    completionsMatchLower.setIcon(R.drawable.ic_text_lower);
    completionsMatchLower.setTitle(getString(R.string.idepref_java_matchLower_title));
    completionsMatchLower.setSummary(getString(R.string.idepref_java_matchLower_summary));

    useGoogleCodeStyle.setIcon(R.drawable.ic_format_code);
    useGoogleCodeStyle.setKey(KEY_JAVA_PREF_GOOGLE_CODE_STYLE);
    useGoogleCodeStyle.setTitle(getString(R.string.idepref_java_useGoogleStyle_title));
    useGoogleCodeStyle.setSummary(getString(R.string.idepref_java_useGoogleStyle_summary));

    visiblePasswordFlag.setIcon(R.drawable.ic_password_input);
    visiblePasswordFlag.setKey(KEY_EDITOR_FLAG_PASSWORD);
    visiblePasswordFlag.setTitle(getString(R.string.idepref_visiblePassword_title));
    visiblePasswordFlag.setSummary(getString(R.string.idepref_visiblePassword_summary));

    commonCategory.setTitle(getString(R.string.idepref_editor_category_common));
    commonCategory.addPreference(fontSize);
    commonCategory.addPreference(fontLigatures);
    commonCategory.addPreference(tabSize);
    commonCategory.addPreference(wordWrap);
    commonCategory.addPreference(magnifier);
    commonCategory.addPreference(completionsMatchLower);
    commonCategory.addPreference(visiblePasswordFlag);
    commonCategory.addPreference(nonPrintable);
    commonCategory.addPreference(drawHex);
    commonCategory.addPreference(autoSave);

    javaCategory.setTitle(getString(R.string.idepref_editor_category_java));
    javaCategory.addPreference(useGoogleCodeStyle);

    setPreferenceScreen(screen);

    fontSize.setOnPreferenceClickListener(this);
    fontLigatures.setOnPreferenceChangeListener(this);
    nonPrintable.setOnPreferenceClickListener(this);
    tabSize.setOnPreferenceClickListener(this);
    drawHex.setOnPreferenceChangeListener(this);
    autoSave.setOnPreferenceChangeListener(this);
    completionsMatchLower.setOnPreferenceChangeListener(this);
    useGoogleCodeStyle.setOnPreferenceChangeListener(this);
    visiblePasswordFlag.setOnPreferenceChangeListener(this);
    wordWrap.setOnPreferenceChangeListener(this);
    magnifier.setOnPreferenceChangeListener(this);

    fontLigatures.setChecked(getPrefManager().getBoolean(KEY_EDITOR_FONT_LIGATURES, true));
    drawHex.setChecked(getPrefManager().getBoolean(KEY_EDITOR_DRAW_HEX, true));
    autoSave.setChecked(getPrefManager().getBoolean(KEY_EDITOR_AUTO_SAVE, false));
    completionsMatchLower.setChecked(
        getPrefManager().getBoolean(KEY_COMPLETIONS_MATCH_LOWER, false));
    useGoogleCodeStyle.setChecked(
        getPrefManager().getBoolean(KEY_JAVA_PREF_GOOGLE_CODE_STYLE, false));
    visiblePasswordFlag.setChecked(getPrefManager().getBoolean(KEY_EDITOR_FLAG_PASSWORD, true));
    wordWrap.setChecked(getPrefManager().getBoolean(PreferenceManager.KEY_EDITOR_WORD_WRAP, false));
    magnifier.setChecked(
        getPrefManager().getBoolean(PreferenceManager.KEY_EDITOR_USE_MAGNIFER, true));
  }

  @Override
  public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
    final boolean value = (boolean) newValue;

    switch (preference.getKey()) {
      case KEY_EDITOR_FLAG_PASSWORD:
        ConstantsBridge.EDITOR_PREF_VISIBLE_PASSWORD_CHANGED = true;
        break;
      case KEY_EDITOR_DRAW_HEX:
        ConstantsBridge.EDITOR_PREF_DRAW_HEX_CHANGED = true;
        break;
      case KEY_EDITOR_FONT_LIGATURES:
        ConstantsBridge.EDITOR_PREF_LIGATURES_CHANGED = true;
        break;
      case PreferenceManager.KEY_EDITOR_WORD_WRAP:
        ConstantsBridge.EDITOR_PREF_WORD_WRAP_CHANGED = true;
        break;
      case PreferenceManager.KEY_EDITOR_USE_MAGNIFER:
        ConstantsBridge.EDITOR_PREF_USE_MAGNIFIER_CHANGED = true;
        break;
    }

    getPrefManager().putBoolean(preference.getKey(), value);

    return true;
  }

  @Override
  public boolean onPreferenceClick(@NonNull Preference preference) {
    final String key = preference.getKey();
    switch (key) {
      case KEY_EDITOR_FONT_SIZE:
        showTextSizeDialog();
        break;
      case KEY_EDITOR_PRINTABLE_CHARS:
        showPrintableCharsDialog();
        break;
      case KEY_EDITOR_TAB_SIZE:
        showTabSizeDialog();
        break;
    }
    return true;
  }

  private void showTabSizeDialog() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    final String[] sizes = new String[] {"2", "4", "6", "8"};

    int current = (getPrefManager().getEditorTabSize() / 2) - 1;
    if (current < 0 || current >= sizes.length) {
      current = 1;
    }

    builder.setTitle(R.string.title_tab_size);
    builder.setSingleChoiceItems(
        sizes,
        current,
        (d, i) -> {
          d.dismiss();

          int tabSize = (i + 1) * 2;
          if (tabSize < 2 || tabSize > 8) {
            tabSize = 4;
          }
          getPrefManager().putInt(KEY_EDITOR_TAB_SIZE, tabSize);
        });
    builder.setCancelable(true);
    builder.show();
  }

  private void showTextSizeDialog() {
    final LayoutTextSizeSliderBinding binding =
        LayoutTextSizeSliderBinding.inflate(LayoutInflater.from(getContext()));
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    float size = getPrefManager().getFloat(KEY_EDITOR_FONT_SIZE, 14);
    if (size < 6 || size > 32) {
      size = 14;
    }
    changeTextSize(binding, size);

    binding.slider.setLabelFormatter(p1 -> String.valueOf((int) p1));
    builder.setTitle(R.string.title_change_text_size);
    builder.setMessage(R.string.msg_editor_font_size);
    builder.setView(binding.getRoot());
    builder.setPositiveButton(
        android.R.string.ok,
        (p1, p2) -> {
          p1.dismiss();
          float s = binding.slider.getValue();
          changeTextSize(binding, s);
        });
    builder.setNeutralButton(R.string.reset, (p1, p2) -> changeTextSize(binding, 14));
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
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    builder.setTitle(R.string.idepref_editor_paintingflags_title);
    builder.setMultiChoiceItems(
        labels,
        checked,
        (dialog, which, isChecked) -> {
          if (which == 0) {
            getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_LEADING, isChecked);
          } else if (which == 1) {
            getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_TRAILING, isChecked);
          } else if (which == 2) {
            getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_INNER, isChecked);
          } else if (which == 3) {
            getPrefManager().putBoolean(KEY_EDITOR_FLAG_WS_EMPTY_LINE, isChecked);
          } else if (which == 4) {
            getPrefManager().putBoolean(KEY_EDITOR_FLAG_LINE_BREAK, isChecked);
          }

          ConstantsBridge.EDITOR_PREF_FLAGS_CHANGED = true;
        });
    builder.setPositiveButton(android.R.string.ok, null);
    builder.setCancelable(false);
    builder.show();
  }
}
