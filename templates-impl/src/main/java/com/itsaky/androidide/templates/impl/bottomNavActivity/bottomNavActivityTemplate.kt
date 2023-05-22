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

package com.itsaky.androidide.templates.impl.bottomNavActivity

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

fun bottomNavActivityProject() = baseProjectImpl {
  templateName = R.string.template_navigation_tabs
  thumb = R.drawable.template_bottom_navigation_activity
  defaultAppModule {
    recipe = createRecipe {
      sources {
        writeMainActivity(this, ktSrc = ::bottomNavActivitySrcKt,
          javaSrc = ::bottomNavActivitySrcJava)
      }

      res {
        copyAssetsRecursively(templateAsset("bottomNav", "res"), mainResDir())

        writeXmlResource("mobile_navigation", NAVIGATION,
          source = ::bottomNavNavigationXmlSrc)

        putStringRes("title_home", "Home")
        putStringRes("title_dashboard", "Dashboard")
        putStringRes("title_notifications", "Notifications")

        emptyThemesAndColors(actionBar = true)
      }

      if (data.language == Kotlin) {
        bottomNavActivityProjectKt()
      } else {
        bottomNavActivityProjectJava()
      }
    }
  }
}

fun AndroidModuleTemplateBuilder.bottomNavActivityProjectKt() {
  executor.apply {
    addDependency(Dependency.AndroidX.Navigation_Ui_Ktx)
    addDependency(Dependency.AndroidX.Navigation_Fragment_Ktx)
    addDependency(Dependency.AndroidX.LifeCycle_LiveData_Ktx)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel_Ktx)

    sources {
      writeKtSrc("${data.packageName}.ui.dashboard", "DashboardFragment",
        source = ::bottomNavFragmentDashSrcKt)
      writeKtSrc("${data.packageName}.ui.dashboard", "DashboardViewModel",
        source = ::bottomNavModelDashSrcKt)

      writeKtSrc("${data.packageName}.ui.home", "HomeFragment",
        source = ::bottomNavFragmentHomeSrcKt)
      writeKtSrc("${data.packageName}.ui.home", "HomeViewModel",
        source = ::bottomNavModelHomeSrcKt)

      writeKtSrc("${data.packageName}.ui.notifications",
        "NotificationsFragment", source = ::bottomNavFragmentNotificationsSrcKt)
      writeKtSrc("${data.packageName}.ui.notifications",
        "NotificationsViewModel", source = ::bottomNavModelNotificationsSrcKt)

    }
  }
}

private fun AndroidModuleTemplateBuilder.bottomNavActivityProjectJava() {
  executor.apply {
    addDependency(Dependency.AndroidX.Navigation_Ui)
    addDependency(Dependency.AndroidX.Navigation_Fragment)
    addDependency(Dependency.AndroidX.LifeCycle_LiveData)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel)

    sources {
      writeJavaSrc("${data.packageName}.ui.dashboard", "DashboardFragment",
        source = ::bottomNavFragmentDashSrcJava)
      writeJavaSrc("${data.packageName}.ui.dashboard", "DashboardViewModel",
        source = ::bottomNavModelDashSrcJava)

      writeJavaSrc("${data.packageName}.ui.home", "HomeFragment",
        source = ::bottomNavFragmentHomeSrcJava)
      writeJavaSrc("${data.packageName}.ui.home", "HomeViewModel",
        source = ::bottomNavModelHomeSrcJava)

      writeJavaSrc("${data.packageName}.ui.notifications",
        "NotificationsFragment",
        source = ::bottomNavFragmentNotificationsSrcJava)
      writeJavaSrc("${data.packageName}.ui.notifications",
        "NotificationsViewModel", source = ::bottomNavModelNotificationsSrcJava)

    }
  }
}
