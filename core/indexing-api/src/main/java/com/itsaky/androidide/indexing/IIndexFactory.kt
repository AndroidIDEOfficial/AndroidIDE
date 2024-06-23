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

package com.itsaky.androidide.indexing

import com.itsaky.androidide.utils.ServiceLoader

/**
 * A factory for creating instances of [IIndex].
 *
 * @param I The indexable type.
 * @param P The index creation params.
 * @author Akash Yadav
 */
interface IIndexFactory<I : IIndexable, P : IIndexParams> {

  /**
   * The parameters used to create the index.
   */
  var params: P?

  /**
   * Get the indexable type for the index.
   */
  fun indexableType(): Class<out I>

  /**
   * Get the parameter type for the index.
   */
  fun paramType(): Class<out P>

  /**
   * Create the index.
   *
   * @return The created [IIndex].
   */
  @Throws(NotFoundException::class)
  fun create(): IIndex<I>

  companion object {

    private val factoryCache =
      mutableMapOf<Class<out IIndexable>, IIndexFactory<out IIndexable, out IIndexParams>>()

    /**
     * Finds the index factory for the given symbol type returning `null` if not found.
     *
     * @param symTyp The symbol type.
     * @param paramTyp The parameter type.
     * @return The index factory.
     */
    fun <T : IIndexable, P : IIndexParams> findFactoryForSymType(
      symTyp: Class<out T>,
      paramTyp: Class<out P>? = null
    ) = __findFactoryForSymType(symTyp = symTyp, paramTyp = paramTyp, fail = false)

    /**
     * Get the index factory for the given symbol type failing if not found.
     *
     * @param symTyp The symbol type.
     * @param paramTyp The parameter type.
     * @return The index factory.
     */
    fun <T : IIndexable, P : IIndexParams> getFactoryForSymType(
      symTyp: Class<out T>,
      paramTyp: Class<out P>? = null
    ) = __findFactoryForSymType(symTyp = symTyp, paramTyp = paramTyp, fail = true)!!

    /**
     * Finds an index factory for the given symbol type.
     *
     * @param symTyp The symbol type.
     * @param paramTyp The parameter type.
     * @param fail Whether to fail if no factory is found.
     * @return The index factory.
     */
    @Suppress("UNCHECKED_CAST", "FunctionName")
    private fun <T : IIndexable, P : IIndexParams> __findFactoryForSymType(
      symTyp: Class<out T>,
      paramTyp: Class<out P>?,
      fail: Boolean = true
    ): IIndexFactory<T, P>? {
      var factory: IIndexFactory<T, P>? = this.factoryCache[symTyp] as IIndexFactory<T, P>?
      if (factory != null) {
        return factory
      }

      val impls = ServiceLoader.load(IIndexFactory::class.java).iterator()
      while (impls.hasNext()) {
        val impl = impls.next()
        if (symTyp == impl.indexableType()) {
          if (factory == null) {
            factory = impl as IIndexFactory<T, P>
            continue
          }

          if (!fail) {
            return null
          }

          throw MultipleFactoriesException(symTyp)
        }
      }

      if (factory != null && paramTyp != null && factory.paramType() != paramTyp) {
        if (!fail) {
          return null
        }
        throw InvalidParamTypeException(factory, paramTyp)
      }

      if (factory == null) {
        if (!fail) {
          return null
        }

        throw NotFoundException(symTyp)
      }

      val typ = factory.indexableType()

      factoryCache.put(typ, factory)?.also {
        throw IllegalStateException("Invalid cache state")
      }

      return factory
    }
  }

  /**
   * Exception thrown when an [IIndexFactory] cannot be found.
   */
  class NotFoundException(sym: Class<out IIndexable>) :
    RuntimeException("No index factory found for ${sym.name}")

  /**
   * Exception thrown when multiple [IIndexFactory]s are found for a single symbol type.
   */
  class MultipleFactoriesException(sym: Class<out IIndexable>) :
    RuntimeException("Multiple index factories found for ${sym.name}")

  /**
   * Exception thrown when the index parameters of a factory are of invalid type.
   */
  class InvalidParamTypeException(factory: IIndexFactory<*, *>, paramTyp: Class<out IIndexParams>) :
    RuntimeException("Factory ${factory.javaClass} expected parameters of type ${factory.paramType()} but an instance of $paramTyp was provided")
}