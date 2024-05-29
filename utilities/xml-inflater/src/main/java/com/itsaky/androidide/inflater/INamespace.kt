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

package com.itsaky.androidide.inflater

import com.android.SdkConstants.ANDROID_NS_NAME
import com.android.SdkConstants.ANDROID_URI
import com.itsaky.androidide.inflater.internal.NamespaceImpl

/**
 * An XML namespace declaration.
 *
 * @author Akash Yadav
 */
interface INamespace {

  /** The prefix of the namespace. */
  val prefix: String

  /** The namespace URI. */
  val uri: String

  companion object {
    /** The Android namespace. */
    @JvmField val ANDROID: INamespace = NamespaceImpl(ANDROID_NS_NAME, ANDROID_URI)
  }
}
