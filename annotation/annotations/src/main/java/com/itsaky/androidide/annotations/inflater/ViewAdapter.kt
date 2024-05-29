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
package com.itsaky.androidide.annotations.inflater

import android.view.View
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * Annotation used to indicate that a class is an attribute adapater for the given view class.
 *
 * @property forView The view that this adpater handles.
 * @property moduleNamespace The package name of the artifact/module in which the [view][forView] is
 * declared. Set to `android` by default.
 * @author Akash Yadav
 */
@Target(CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ViewAdapter(val forView: KClass<out View>, val moduleNamespace: String = "android")
