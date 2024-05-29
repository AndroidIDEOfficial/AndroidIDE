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
package com.itsaky.androidide.builder.model

import com.android.builder.model.v2.ide.SourceProvider
import com.android.builder.model.v2.ide.SourceSetContainer
import java.io.Serializable

/** @author Akash Yadav */
class DefaultSourceSetContainer : SourceSetContainer, Serializable {

  private val serialVersionUID = 1L
  @Deprecated("Contained in deviceTestSourceProviders")
  override var androidTestSourceProvider: DefaultSourceProvider? = null
  override var sourceProvider: DefaultSourceProvider = DefaultSourceProvider()
  override var testFixturesSourceProvider: DefaultSourceProvider? = null
  @Deprecated("Contained in hostTestSourceProviders")
  override var unitTestSourceProvider: DefaultSourceProvider? = null
  override val deviceTestSourceProviders: Map<String, SourceProvider> = emptyMap()
  override val hostTestSourceProviders: Map<String, SourceProvider> = emptyMap()
}
