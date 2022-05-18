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
package com.itsaky.androidide;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import androidx.appcompat.widget.TooltipCompat;
import androidx.core.util.Pair;

import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityAboutBinding;
import com.itsaky.androidide.databinding.LayoutAboutItemsBinding;
import com.itsaky.androidide.models.License;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends StudioActivity {

  public static final String JDK_SOURCE = "https://github.com/itsaky/OpenJDK-Android";
  private ActivityAboutBinding binding;

  @Override
  protected View bindLayout() {
    binding = ActivityAboutBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding.items.footerText.setText(getFooter());

    setupTranslations();
    setupLicenses();

    LayoutAboutItemsBinding items = binding.items;
    TooltipCompat.setTooltipText(items.discuss, getString(R.string.discussions_on_telegram));
    TooltipCompat.setTooltipText(items.github, getString(R.string.title_github));
    TooltipCompat.setTooltipText(items.email, getString(R.string.about_option_email));
    TooltipCompat.setTooltipText(items.website, getString(R.string.about_option_website));
    items.github.setOnClickListener(v -> getApp().openGitHub());
    items.discuss.setOnClickListener(v -> getApp().openTelegramGroup());
    items.email.setOnClickListener(v -> getApp().emailUs());
    items.website.setOnClickListener(v -> getApp().openWebsite());
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

  private void setupLicenses() {
    final var licenses = new ArrayList<License>();
    licenses.add(
        new License(
            "AndroidX Libraries", "Apache License 2.0", "https://github.com/androidx/androidx"));
    licenses.add(
        new License("Gradle Build Tool", "Apache License 2.0", "https://github.com/gradle/gradle"));
    licenses.add(new License("Gson", "Apache License 2.0", "https://github.com/google/gson"));
    licenses.add(
        new License(
            "Material Components for Android",
            "Apache License 2.0",
            "https://github.com/material-components/material-components-android"));
    licenses.add(
        new License("ANTLR4 Runtime", "BSD 3-clause License", "https://github.com/antlr/antlr4"));
    licenses.add(
        new License("Glide", "BSD, part MIT and Apache 2.0", "https://github.com/bumptech/glide"));
    licenses.add(
        new License(
            "Android UtilCode", "Apache License 2.0", "https://github.com/Blankj/AndroidUtilCode"));
    licenses.add(
        new License(
            "UnicornFilePicker",
            "Apache License 2.0",
            "https://github.com/abhishekti7/UnicornFilePicker"));
    licenses.add(
        new License("QuickAction", "Apache License 2.0", "https://github.com/piruin/quickaction"));
    licenses.add(
        new License("XmlToJson", "Apache License 2.0", "https://github.com/smart-fun/XmlToJson"));
    licenses.add(
        new License(
            "AndroidTreeView",
            "Apache License 2.0",
            "https://github.com/bmelnychuk/AndroidTreeView"));
    licenses.add(
        new License(
            "CodeEditor v0.5.2", "Apache License 2.0", "https://github.com/Rosemoe/CodeEditor"));
    licenses.add(
        new License("Guava-Android", "Apache License 2.0", "https://github.com/google/guava"));
    licenses.add(new License("Jsoup", "MIT License", "https://github.com/jhy/jsoup"));
    licenses.add(
        new License("Termux [Terminal Emulator]", "GPL 3", "https://github.com/termux/termux-app"));
    licenses.add(
        new License("JavaPoet", "Apache License 2.0", "https://github.com/square/JavaPoet"));
    licenses.add(
        new License("Zip4j", "Apache License 2.0", "https://github.com/srikanth-lingala/zip4j"));

    StringBuilder sb = new StringBuilder();
    for (License license : licenses) {
      sb.append("\u2022 ");
      sb.append(license.name);
      sb.append("<br>");
      sb.append(getString(R.string.msg_about_licensed_under));
      sb.append(license.license);
      sb.append("<br><a href=\"");
      sb.append(license.url);
      sb.append("\">");
      sb.append(license.url);
      sb.append("</a>");
      sb.append("<br><br>");
    }
    sb.append(getString(R.string.license_jdk));
    sb.append(String.format("<br><a href=\"%1$s\">%1$s</a>", JDK_SOURCE));
    sb.append("<br><br>");
    binding.items.licenses.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY));
    binding.items.licenses.setMovementMethod(LinkMovementMethod.getInstance());
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
}
