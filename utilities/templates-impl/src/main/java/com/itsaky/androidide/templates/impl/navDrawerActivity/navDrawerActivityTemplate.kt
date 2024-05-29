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

package com.itsaky.androidide.templates.impl.navDrawerActivity

import com.itsaky.androidide.templates.Language.Kotlin
import com.itsaky.androidide.templates.base.AndroidModuleTemplateBuilder
import com.itsaky.androidide.templates.base.models.Dependency
import com.itsaky.androidide.templates.base.modules.android.defaultAppModule
import com.itsaky.androidide.templates.base.util.AndroidModuleResManager.ResourceType.NAVIGATION
import com.itsaky.androidide.templates.impl.R
import com.itsaky.androidide.templates.impl.base.createRecipe
import com.itsaky.androidide.templates.impl.base.emptyThemesAndColors
import com.itsaky.androidide.templates.impl.base.writeMainActivity
import com.itsaky.androidide.templates.impl.baseProjectImpl
import com.itsaky.androidide.templates.impl.templateAsset

fun navDrawerActivityProject() = baseProjectImpl {
  templateName = R.string.template_navigation_drawer
  thumb = R.drawable.template_blank_activity_drawer
  defaultAppModule {
    recipe = createRecipe {
      sources {
        writeMainActivity(this, ktSrc = ::navDrawerActivitySrcKt,
          javaSrc = ::navDrawerActivitySrcJava)
      }

      res {
        copyAssetsRecursively(templateAsset("navDrawer", "res"), mainResDir())

        writeXmlResource("mobile_navigation", NAVIGATION,
          source = ::navDrawerNavigationXmlSrc)

        putStringRes("navigation_drawer_open", "Open navigation drawer")
        putStringRes("navigation_drawer_close", "Close navigation drawer")
        putStringRes("nav_header_title", "AndroidIDE")
        putStringRes("nav_header_subtitle", "contact@androidide.com")
        putStringRes("nav_header_desc", "Navigation header")
        putStringRes("action_settings", "Settings")
        putStringRes("menu_home", "Home")
        putStringRes("menu_gallery", "Gallery")
        putStringRes("menu_slideshow", "Slideshow")

        emptyThemesAndColors()
      }

      if (data.language == Kotlin) {
        navDrawerActivityProjectKt()
      } else {
        navDrawerActivityProjectJava()
      }
    }
  }
}

private fun AndroidModuleTemplateBuilder.navDrawerActivityProjectJava() {
  executor.apply {
    addDependency(Dependency.AndroidX.Navigation_Ui)
    addDependency(Dependency.AndroidX.Navigation_Fragment)
    addDependency(Dependency.AndroidX.LifeCycle_LiveData)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel)

    sources {
      writeJavaSrc("${data.packageName}.ui.gallery", "GalleryFragment",
        source = ::galleryFragmentSrcJava)
      writeJavaSrc("${data.packageName}.ui.gallery", "GalleryViewModel",
        source = ::galleryModelSrcJava)

      writeJavaSrc("${data.packageName}.ui.home", "HomeFragment",
        source = ::homeFragmentSrcJava)
      writeJavaSrc("${data.packageName}.ui.home", "HomeViewModel",
        source = ::homeModelSrcJava)

      writeJavaSrc("${data.packageName}.ui.slideshow", "SlideshowFragment",
        source = ::slideshowFragmentSrcJava)
      writeJavaSrc("${data.packageName}.ui.slideshow", "SlideshowViewModel",
        source = ::slideshowModelSrcJava)
    }
  }
}

private fun AndroidModuleTemplateBuilder.navDrawerActivityProjectKt() {
  executor.apply {
    addDependency(Dependency.AndroidX.Navigation_Ui_Ktx)
    addDependency(Dependency.AndroidX.Navigation_Fragment_Ktx)
    addDependency(Dependency.AndroidX.LifeCycle_LiveData_Ktx)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel_Ktx)

    sources {
      writeKtSrc("${data.packageName}.ui.gallery", "GalleryFragment",
        source = ::galleryFragmentSrcKt)
      writeKtSrc("${data.packageName}.ui.gallery", "GalleryViewModel",
        source = ::galleryModelSrcKt)

      writeKtSrc("${data.packageName}.ui.home", "HomeFragment",
        source = ::homeFragmentSrcKt)
      writeKtSrc("${data.packageName}.ui.home", "HomeViewModel",
        source = ::homeModelSrcKt)

      writeKtSrc("${data.packageName}.ui.slideshow", "SlideshowFragment",
        source = ::slideshowFragmentSrcKt)
      writeKtSrc("${data.packageName}.ui.slideshow", "SlideshowViewModel",
        source = ::slideshowModelSrcKt)
    }
  }
}