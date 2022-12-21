/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itsaky.androidide.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.TooltipCompat;
import androidx.core.util.Pair;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.itsaky.androidide.R;
import com.itsaky.androidide.app.IDEActivity;
import com.itsaky.androidide.databinding.ActivityAboutBinding;
import com.itsaky.androidide.databinding.LayoutAboutItemsBinding;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends IDEActivity {

  public static final String JDK_SOURCE = "https://github.com/itsaky/OpenJDK-Android";
  private ActivityAboutBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding.items.footerText.setText(getFooter());

    setupTranslations();

    OssLicensesMenuActivity.setActivityTitle(getString(R.string.oss_license_title));

    LayoutAboutItemsBinding items = binding.items;
    TooltipCompat.setTooltipText(items.discuss, getString(R.string.discussions_on_telegram));
    TooltipCompat.setTooltipText(items.github, getString(R.string.title_github));
    TooltipCompat.setTooltipText(items.email, getString(R.string.about_option_email));
    TooltipCompat.setTooltipText(items.website, getString(R.string.about_option_website));
    TooltipCompat.setTooltipText(items.licenses, getString(R.string.oss_license_title));
    items.github.setOnClickListener(v -> getApp().openGitHub());
    items.discuss.setOnClickListener(v -> getApp().openTelegramGroup());
    items.email.setOnClickListener(v -> getApp().emailUs());
    items.website.setOnClickListener(v -> getApp().openWebsite());
    items.licenses.setOnClickListener(
        v -> startActivity(new Intent(this, OssLicensesMenuActivity.class)));
  }

  @Override
  protected View bindLayout() {
    binding = ActivityAboutBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  private void setupTranslations() {
    final var translations = createTranslationsList();
    final var sb = new StringBuilder();

    for (var translation : translations) {
      final var author = translation.first;
      final var language = translation.second;

      sb.append(author);
      sb.append(" - ");
      sb.append(language);
      sb.append("\n");
    }

    binding.items.translations.setText(sb);
  }

  /**
   * Create the list of translations. The list items are pairs. The first item of the pair is the
   * name of the language. The second item is the name of the person who made the translations.
   *
   * <p>If you're adding translations, make sure you add an item to this list. You can choose to
   * display the language in a localized version. For example : Language: हिन्दी - < name of author
   * >
   *
   * <p>Prefer using unicode escapes for language names. For example, हिन्दी can be written as :
   * Language: \u0939\u093f\u0928\u094d\u0926\u0940 - < name of author >
   *
   * @return The translations list.
   */
  private List<Pair<String, String>> createTranslationsList() {
    final var list = new ArrayList<Pair<String, String>>();

    list.add(Pair.create("Bahasa Indonesia", "Fitrah Nuno Syahbani"));
    list.add(Pair.create("\u4e2d\u6587", "Rosemoe and @mikofe")); // 中文
    list.add(Pair.create("Deutsch", "Marvin Stelter"));
    list.add(Pair.create("\u0939\u093f\u0928\u094d\u0926\u0940", "Premjit Chowdhury")); // हिन्दी
    list.add(Pair.create("Russian", "Smooth-E"));
    list.add(Pair.create("French", "Se-Lyan"));
    list.add(Pair.create("Portuguese-Brazilian", "Frederick'XS"));
    return list;
  }

  private String getFooter() {
    final String arch = android.os.Build.SUPPORTED_ABIS[0];
    try {
      final String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
      return getString(R.string.about_footer, version, arch);
    } catch (Throwable th) {
      return getString(R.string.about_footer_alternate, arch);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }
}
