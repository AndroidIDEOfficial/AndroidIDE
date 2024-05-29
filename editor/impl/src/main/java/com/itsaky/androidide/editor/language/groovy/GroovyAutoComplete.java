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
package com.itsaky.androidide.editor.language.groovy;

import android.os.Bundle;
import com.blankj.utilcode.util.StringUtils;
import com.itsaky.androidide.lsp.models.CompletionItem;
import com.itsaky.androidide.lsp.models.CompletionItemKind;
import com.itsaky.androidide.lsp.models.InsertTextFormat;
import io.github.rosemoe.sora.lang.completion.CompletionHelper;
import io.github.rosemoe.sora.lang.completion.CompletionPublisher;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.util.MyCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroovyAutoComplete {

  private static final List<String> ANDROIDX_ARTIFACTS = createArtifacts();
  private static final List<String> CONFIGURATIONS = createConfigs();
  private static final List<String> OTHERS = createOtherCompletions();

  private static List<String> createArtifacts() {
    var artifacts = new ArrayList<String>();
    artifacts.add("androidx.arch.core:core-common");
    artifacts.add("androidx.arch.core:core");
    artifacts.add("androidx.arch.core:core-testing");
    artifacts.add("androidx.arch.core:core-runtime");
    artifacts.add("androidx.lifecycle:lifecycle-common");
    artifacts.add("androidx.lifecycle:lifecycle-common-java8");
    artifacts.add("androidx.lifecycle:lifecycle-compiler");
    artifacts.add("androidx.lifecycle:lifecycle-extensions");
    artifacts.add("androidx.lifecycle:lifecycle-livedata");
    artifacts.add("androidx.lifecycle:lifecycle-livedata-core");
    artifacts.add("androidx.lifecycle:lifecycle-reactivestreams");
    artifacts.add("androidx.lifecycle:lifecycle-runtime");
    artifacts.add("androidx.lifecycle:lifecycle-viewmodel");
    artifacts.add("androidx.paging:paging-common");
    artifacts.add("androidx.paging:paging-runtime");
    artifacts.add("androidx.paging:paging-rxjava2");
    artifacts.add("androidx.room:room-common");
    artifacts.add("androidx.room:room-compiler");
    artifacts.add("androidx.room:room-guava");
    artifacts.add("androidx.room:room-migration");
    artifacts.add("androidx.room:room-runtime");
    artifacts.add("androidx.room:room-rxjava2");
    artifacts.add("androidx.room:room-testing");
    artifacts.add("androidx.sqlite:sqlite");
    artifacts.add("androidx.sqlite:sqlite-framework");
    artifacts.add("androidx.constraintlayout:constraintlayout");
    artifacts.add("androidx.constraintlayout:constraintlayout-solver");
    artifacts.add("androidx.test.espresso.idling:idling-concurrent");
    artifacts.add("androidx.test.espresso.idling:idling-net");
    artifacts.add("androidx.test.espresso:espresso-accessibility");
    artifacts.add("androidx.test.espresso:espresso-contrib");
    artifacts.add("androidx.test.espresso:espresso-core");
    artifacts.add("androidx.test.espresso:espresso-idling-resource");
    artifacts.add("androidx.test.espresso:espresso-intents");
    artifacts.add("androidx.test.espresso:espresso-remote");
    artifacts.add("androidx.test.espresso:espresso-web");
    artifacts.add("androidx.test.jank:janktesthelper");
    artifacts.add("androidx.test:test-services");
    artifacts.add("androidx.test.uiautomator:uiautomator");
    artifacts.add("androidx.test:monitor");
    artifacts.add("androidx.test:orchestrator");
    artifacts.add("androidx.test:rules");
    artifacts.add("androidx.test:runner");
    artifacts.add("androidx.vectordrawable:vectordrawable-animated");
    artifacts.add("androidx.appcompat:appcompat");
    artifacts.add("androidx.asynclayoutinflater:asynclayoutinflater");
    artifacts.add("androidx.car:car");
    artifacts.add("androidx.cardview:cardview");
    artifacts.add("androidx.collection:collection");
    artifacts.add("androidx.coordinatorlayout:coordinatorlayout");
    artifacts.add("androidx.cursoradapter:cursoradapter");
    artifacts.add("androidx.browser:browser");
    artifacts.add("androidx.customview:customview");
    artifacts.add("com.google.android.material:material");
    artifacts.add("androidx.documentfile:documentfile");
    artifacts.add("androidx.drawerlayout:drawerlayout");
    artifacts.add("androidx.exifinterface:exifinterface");
    artifacts.add("androidx.gridlayout:gridlayout");
    artifacts.add("androidx.heifwriter:heifwriter");
    artifacts.add("androidx.interpolator:interpolator");
    artifacts.add("androidx.leanback:leanback");
    artifacts.add("androidx.loader:loader");
    artifacts.add("androidx.localbroadcastmanager:localbroadcastmanager");
    artifacts.add("androidx.media2:media2");
    artifacts.add("androidx.media2:media2-exoplayer");
    artifacts.add("androidx.mediarouter:mediarouter");
    artifacts.add("androidx.multidex:multidex");
    artifacts.add("androidx.multidex:multidex-instrumentation");
    artifacts.add("androidx.palette:palette");
    artifacts.add("androidx.percentlayout:percentlayout");
    artifacts.add("androidx.leanback:leanback-preference");
    artifacts.add("androidx.legacy:legacy-preference-v14");
    artifacts.add("androidx.preference:preference");
    artifacts.add("androidx.print:print");
    artifacts.add("androidx.recommendation:recommendation");
    artifacts.add("androidx.recyclerview:recyclerview-selection");
    artifacts.add("androidx.recyclerview:recyclerview");
    artifacts.add("androidx.slice:slice-builders");
    artifacts.add("androidx.slice:slice-core");
    artifacts.add("androidx.slice:slice-view");
    artifacts.add("androidx.slidingpanelayout:slidingpanelayout");
    artifacts.add("androidx.annotation:annotation");
    artifacts.add("androidx.core:core");
    artifacts.add("androidx.contentpager:contentpager");
    artifacts.add("androidx.legacy:legacy-support-core-ui");
    artifacts.add("androidx.legacy:legacy-support-core-utils");
    artifacts.add("androidx.dynamicanimation:dynamicanimation");
    artifacts.add("androidx.emoji:emoji");
    artifacts.add("androidx.emoji:emoji-appcompat");
    artifacts.add("androidx.emoji:emoji-bundled");
    artifacts.add("androidx.fragment:fragment");
    artifacts.add("androidx.media:media");
    artifacts.add("androidx.tvprovider:tvprovider");
    artifacts.add("androidx.legacy:legacy-support-v13");
    artifacts.add("androidx.legacy:legacy-support-v4");
    artifacts.add("androidx.vectordrawable:vectordrawable");
    artifacts.add("androidx.swiperefreshlayout:swiperefreshlayout");
    artifacts.add("androidx.textclassifier:textclassifier");
    artifacts.add("androidx.transition:transition");
    artifacts.add("androidx.versionedparcelable:versionedparcelable");
    artifacts.add("androidx.viewpager:viewpager");
    artifacts.add("androidx.wear:wear");
    artifacts.add("androidx.webkit:webkit");

    artifacts.add("fileTree(dir: 'libs', include: ['*.jar'])");
    return artifacts;
  }

  private static List<String> createConfigs() {
    var configs = new ArrayList<String>();
    configs.add("androidApis");
    configs.add("androidTestAnnotationProcessor");
    configs.add("androidTestApi");
    configs.add("androidTestApk");
    configs.add("androidTestCompile");
    configs.add("androidTestCompileOnly");
    configs.add("androidTestDebugAnnotationProcessor");
    configs.add("androidTestDebugApi");
    configs.add("androidTestDebugApk");
    configs.add("androidTestDebugCompile");
    configs.add("androidTestDebugCompileOnly");
    configs.add("androidTestDebugImplementation");
    configs.add("androidTestDebugProvided");
    configs.add("androidTestDebugRuntimeOnly");
    configs.add("androidTestDebugWearApp");
    configs.add("androidTestImplementation");
    configs.add("androidTestProvided");
    configs.add("androidTestRuntimeOnly");
    configs.add("androidTestUtil");
    configs.add("androidTestWearApp");
    configs.add("annotationProcessor");
    configs.add("api");
    configs.add("apk");
    configs.add("archives");
    configs.add("compile");
    configs.add("compileOnly");
    configs.add("coreLibraryDesugaring");
    configs.add("debugAnnotationProcessor");
    configs.add("debugApi");
    configs.add("debugApk");
    configs.add("debugCompile");
    configs.add("debugCompileOnly");
    configs.add("debugImplementation");
    configs.add("debugProvided");
    configs.add("debugRuntimeOnly");
    configs.add("debugWearApp");
    configs.add("default");
    configs.add("implementation");
    configs.add("implementation platform()");
    configs.add("lintChecks");
    configs.add("lintClassPath");
    configs.add("lintPublish");
    configs.add("provided");
    configs.add("releaseAnnotationProcessor");
    configs.add("releaseApi");
    configs.add("releaseApk");
    configs.add("releaseCompile");
    configs.add("releaseCompileOnly");
    configs.add("releaseImplementation");
    configs.add("releaseProvided");
    configs.add("releaseRuntimeOnly");
    configs.add("releaseWearApp");
    configs.add("runtimeOnly");
    configs.add("testAnnotationProcessor");
    configs.add("testApi");
    configs.add("testApk");
    configs.add("testCompile");
    configs.add("testCompileOnly");
    configs.add("testDebugAnnotationProcessor");
    configs.add("testDebugApi");
    configs.add("testDebugApk");
    configs.add("testDebugCompile");
    configs.add("testDebugCompileOnly");
    configs.add("testDebugImplementation");
    configs.add("testDebugProvided");
    configs.add("testDebugRuntimeOnly");
    configs.add("testDebugWearApp");
    configs.add("testImplementation");
    configs.add("testProvided");
    configs.add("testReleaseAnnotationProcessor");
    configs.add("testReleaseApi");
    configs.add("testReleaseApk");
    configs.add("testReleaseCompile");
    configs.add("testReleaseCompileOnly");
    configs.add("testReleaseImplementation");
    configs.add("testReleaseProvided");
    configs.add("testReleaseRuntimeOnly");
    configs.add("testReleaseWearApp");
    configs.add("testRuntimeOnly");
    configs.add("testWearApp");
    configs.add("wearApp");
    return configs;
  }

  private static List<String> createOtherCompletions() {
    var others = new ArrayList<String>();

    // Common words that user may use
    others.add("android");
    others.add("applicationVariants");
    others.add("compileSdkVersion");
    others.add("buildToolsVersion");
    others.add("defaultConfig");
    others.add("applicationId");
    others.add("minSdkVersion");
    others.add("targetSdkVersion");
    others.add("versionCode");
    others.add("versionName");
    others.add("multiDexEnabled");
    others.add("vectorDrawables.useSupportLibrary");
    others.add("compileOptions");
    others.add("coreLibraryDesugaringEnabled");
    others.add("sourceCompatibility");
    others.add("targetCompatibility");
    others.add("JavaVersion.VERSION_1_8");
    others.add("JavaVersion.VERSION_1_7");

    // Lint options
    others.add("lintOptions");
    others.add("quiet");
    others.add("abortOnError");
    others.add("checkReleaseBuilds");
    others.add("ignoreWarnings");
    others.add("checkAllWarnings");
    others.add("warningsAsErrors");
    others.add("disable");
    others.add("enable");
    others.add("check");
    others.add("noLines");
    others.add("showAll");
    others.add("explainIssues");
    others.add("lintConfig");
    others.add("textReport");
    others.add("textOutput");
    others.add("xmlReport");
    others.add("xmlOutput");
    others.add("htmlReport");
    others.add("htmlOutput");
    others.add("fatal");
    others.add("error");
    others.add("warning");
    others.add("ignore");
    others.add("informational");
    others.add("baseline");
    others.add("checkTestSources");
    others.add("ignoreTestSources");
    others.add("checkGeneratedSources");
    others.add("checkDependencies");

    // buildFeatures options
    others.add("buildFeatures");
    others.add("aidl");
    others.add("buildConfig");
    others.add("compose");
    others.add("prefab");
    others.add("renderScript");
    others.add("resValues");
    others.add("shaders");
    others.add("viewBinding");

    // Signing configs and all
    others.add("signingConfigs");
    others.add("debug");
    others.add("storeFile");
    others.add("storePassword");
    others.add("keyAlias");
    others.add("keyPassword");
    others.add("release");
    others.add("storeFile");
    others.add("storePassword");
    others.add("keyAlias");
    others.add("keyPassword");
    others.add("buildTypes");
    others.add("debug");
    others.add("minifyEnabled");
    others.add("proguardFiles");
    others.add("signingConfig");
    others.add("stringfog.enable");
    others.add("release");
    others.add("minifyEnabled");
    others.add("proguardFiles");
    others.add("signingConfig");
    others.add("stringfog.enable");
    others.add("packagingOptions");
    others.add("exclude");

    // Maybe used in root project's build.gradle
    others.add("buildscript");
    others.add("ext");
    others.add("project.ext");
    others.add("repositories");
    others.add("maven");
    others.add("url");
    others.add("google()");
    others.add("mavenLocal()");
    others.add("mavenCentral()");
    others.add("jcenter()");
    others.add("classpath");
    others.add("allprojects");
    others.add("subprojects");
    return others;
  }

  public void complete(
    ContentReference content,
    CharPosition position,
    CompletionPublisher publisher,
    Bundle extraArguments) {
    publisher.setUpdateThreshold(0);
    final var prefix =
      CompletionHelper.computePrefix(content, position,
        c -> MyCharacter.isJavaIdentifierPart(c) || c == '.');
    if (StringUtils.isTrimEmpty(prefix)) {
      return;
    }
    for (String artifact : ANDROIDX_ARTIFACTS) {
      if (!artifact.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT))) {
        continue;
      }
      final var completionItem = createCompletionItem(artifact);
      completionItem.setMatchLevel(CompletionItem.matchLevel(artifact, prefix));
      publisher.addItem(completionItem);
    }

    for (String config : CONFIGURATIONS) {
      if (!config.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT))) {
        continue;
      }
      final var completionItem = createCompletionItem(config);
      completionItem.setMatchLevel(CompletionItem.matchLevel(config, prefix));
      publisher.addItem(completionItem);
    }

    for (String other : OTHERS) {
      if (!other.toLowerCase(Locale.ROOT).startsWith(prefix.toLowerCase(Locale.ROOT))) {
        continue;
      }
      final var completionItem = createCompletionItem(other);
      completionItem.setMatchLevel(CompletionItem.matchLevel(other, prefix));
      publisher.addItem(completionItem);
    }
  }

  private CompletionItem createCompletionItem(String itemLabel) {
    CompletionItem item = new CompletionItem();
    item.setIdeLabel(itemLabel);
    item.setDetail("");
    item.setInsertText(itemLabel);
    item.setIdeSortText("0" + itemLabel);
    item.setInsertTextFormat(InsertTextFormat.PLAIN_TEXT);
    item.setCompletionKind(CompletionItemKind.NONE);
    return item;
  }
}
