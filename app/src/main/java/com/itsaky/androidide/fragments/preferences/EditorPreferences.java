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

import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.AUTO_SAVE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.COMPLETIONS_MATCH_LOWER;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.DRAW_HEX;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FLAG_PASSWORD;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FONT_LIGATURES;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.FONT_SIZE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.GOOGLE_CODE_STYLE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.PRINTABLE_CHARS;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.TAB_SIZE;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.USE_ICU;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.USE_MAGNIFER;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.WORD_WRAP;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getAutoSave;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getCompletionsMatchLower;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawEmptyLineWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawHex;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawInnerWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawLeadingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawLineBreak;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getDrawTrailingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getFontLigatures;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getFontSize;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getGoogleCodeStyle;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getTabSize;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getUseIcu;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getUseMagnifier;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getVisiblePasswordFlag;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.getWordwrap;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setDrawEmptyLineWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setDrawInnerWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setDrawLeadingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setDrawLineBreak;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setDrawTrailingWs;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setFontSize;
import static com.itsaky.androidide.models.prefs.EditorPreferencesKt.setTabSize;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.itsaky.androidide.R;
import com.itsaky.androidide.databinding.LayoutTextSizeSliderBinding;
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
    final var useIcu = new SwitchPreference(getContext());

    final var completionsMatchLower = new SwitchPreference(getContext());

    screen.addPreference(commonCategory);
    screen.addPreference(javaCategory);

    fontSize.setIcon(R.drawable.ic_text_size);
    fontSize.setKey(FONT_SIZE);
    fontSize.setTitle(R.string.idepref_editor_fontsize_title);
    fontSize.setSummary(R.string.idepref_editor_fontsize_summary);

    wordWrap.setIcon(R.drawable.ic_wrap_text);
    wordWrap.setKey(WORD_WRAP);
    wordWrap.setTitle(R.string.idepref_editor_word_wrap_title);
    wordWrap.setSummary(R.string.idepref_editor_word_wrap_summary);

    magnifier.setIcon(R.drawable.ic_loupe);
    magnifier.setKey(USE_MAGNIFER);
    magnifier.setTitle(R.string.idepref_editor_use_magnifier_title);
    magnifier.setSummary(R.string.idepref_editor_use_magnifier_summary);

    nonPrintable.setIcon(R.drawable.ic_drawing);
    nonPrintable.setKey(PRINTABLE_CHARS);
    nonPrintable.setTitle(R.string.idepref_editor_paintingflags_title);
    nonPrintable.setSummary(R.string.idepref_editor_paintingflags_summary);

    tabSize.setIcon(R.drawable.ic_tab);
    tabSize.setKey(TAB_SIZE);
    tabSize.setTitle(R.string.title_tab_size);
    tabSize.setSummary(R.string.msg_tab_size);

    drawHex.setIcon(R.drawable.ic_hexadecimal);
    drawHex.setKey(DRAW_HEX);
    drawHex.setTitle(R.string.idepref_editor_drawhexcolors_title);
    drawHex.setSummary(R.string.idepref_editor_drawhexcolors_summary);

    fontLigatures.setIcon(R.drawable.ic_font_ligatures);
    fontLigatures.setKey(FONT_LIGATURES);
    fontLigatures.setTitle(getString(R.string.idepref_editor_ligatures_title));
    fontLigatures.setSummary(getString(R.string.idepref_editor_ligatures_summary));

    autoSave.setIcon(R.drawable.ic_save);
    autoSave.setKey(AUTO_SAVE);
    autoSave.setTitle(getString(R.string.idepref_editor_autoSave_title));
    autoSave.setSummary(getString(R.string.idepref_editor_autoSave_summary));

    completionsMatchLower.setKey(COMPLETIONS_MATCH_LOWER);
    completionsMatchLower.setIcon(R.drawable.ic_text_lower);
    completionsMatchLower.setTitle(getString(R.string.idepref_java_matchLower_title));
    completionsMatchLower.setSummary(getString(R.string.idepref_java_matchLower_summary));

    useGoogleCodeStyle.setIcon(R.drawable.ic_format_code);
    useGoogleCodeStyle.setKey(GOOGLE_CODE_STYLE);
    useGoogleCodeStyle.setTitle(getString(R.string.idepref_java_useGoogleStyle_title));
    useGoogleCodeStyle.setSummary(getString(R.string.idepref_java_useGoogleStyle_summary));

    visiblePasswordFlag.setIcon(R.drawable.ic_password_input);
    visiblePasswordFlag.setKey(FLAG_PASSWORD);
    visiblePasswordFlag.setTitle(getString(R.string.idepref_visiblePassword_title));
    visiblePasswordFlag.setSummary(getString(R.string.idepref_visiblePassword_summary));

    useIcu.setIcon(R.drawable.ic_expand_selection);
    useIcu.setKey(USE_ICU);
    useIcu.setTitle(getString(R.string.idepref_useIcu_title));
    useIcu.setSummary(getString(R.string.idepref_useIcu_summary));

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
    commonCategory.addPreference(useIcu);

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
    useIcu.setOnPreferenceChangeListener(this);

    fontLigatures.setChecked(getFontLigatures());
    drawHex.setChecked(getDrawHex());
    autoSave.setChecked(getAutoSave());
    completionsMatchLower.setChecked(getCompletionsMatchLower());
    useGoogleCodeStyle.setChecked(getGoogleCodeStyle());
    visiblePasswordFlag.setChecked(getVisiblePasswordFlag());
    wordWrap.setChecked(getWordwrap());
    magnifier.setChecked(getUseMagnifier());
    useIcu.setChecked(getUseIcu());
  }

  @Override
  public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
    final boolean value = (boolean) newValue;
    getPrefManager().putBoolean(preference.getKey(), value);
    return true;
  }

  @Override
  public boolean onPreferenceClick(@NonNull Preference preference) {
    final String key = preference.getKey();
    switch (key) {
      case FONT_SIZE:
        showTextSizeDialog();
        break;
      case PRINTABLE_CHARS:
        showPrintableCharsDialog();
        break;
      case TAB_SIZE:
        showTabSizeDialog();
        break;
    }
    return true;
  }

  private void showTabSizeDialog() {
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    final String[] sizes = new String[] {"2", "4", "6", "8"};

    int current = (getTabSize() / 2) - 1;
    if (current < 0 || current >= sizes.length) {
      current = 1;
    }

    builder.setTitle(R.string.title_tab_size);
    builder.setSingleChoiceItems(sizes, current, this::changeTabSize);
    builder.setCancelable(true);
    builder.show();
  }

  private void changeTabSize(final DialogInterface dialog, final int position) {
    dialog.dismiss();

    int tabSize = (position + 1) * 2;
    if (tabSize < 2 || tabSize > 8) {
      tabSize = 4;
    }
    setTabSize(tabSize);
  }

  private void showTextSizeDialog() {
    final LayoutTextSizeSliderBinding binding =
        LayoutTextSizeSliderBinding.inflate(LayoutInflater.from(getContext()));
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    float size = getFontSize();
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
    setFontSize(size);
    binding.slider.setValue(size);
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
      getDrawLeadingWs(),
      getDrawTrailingWs(),
      getDrawInnerWs(),
      getDrawEmptyLineWs(),
      getDrawLineBreak()
    };
    final MaterialAlertDialogBuilder builder = DialogUtils.newMaterialDialogBuilder(getContext());
    builder.setTitle(R.string.idepref_editor_paintingflags_title);
    builder.setMultiChoiceItems(
        labels, checked, (dialog, which, draw) -> changePaintingFlag(which, draw));
    builder.setPositiveButton(android.R.string.ok, null);
    builder.setCancelable(false);
    builder.show();
  }

  private void changePaintingFlag(final int which, final boolean draw) {
    if (which == 0) {
      setDrawLeadingWs(draw);
    } else if (which == 1) {
      setDrawTrailingWs(draw);
    } else if (which == 2) {
      setDrawInnerWs(draw);
    } else if (which == 3) {
      setDrawEmptyLineWs(draw);
    } else if (which == 4) {
      setDrawLineBreak(draw);
    }
  }
}
