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
package com.itsaky.androidide.tooling.impl.sync

/**
 * A model builder builds project models when the project is initialized/synced.
 *
 * @param <P> The parameter for building the model.
 * @param <R> The type of model that is built.
 * @author Akash Yadav
 */
interface IModelBuilder<P, R> {

  /**
   * Builds the model.
   *
   * @param param Parameter for building the model.
   * @return The built model. Implementations should throw [ModelBuilderException] instead of
   * returning `null`.
   * @throws ModelBuilderException If the model could not be built.
   */
  @Throws(ModelBuilderException::class)
  fun build(param: P): R
}