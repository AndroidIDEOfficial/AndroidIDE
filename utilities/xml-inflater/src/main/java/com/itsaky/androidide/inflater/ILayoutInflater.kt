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

import android.view.ViewGroup
import com.itsaky.androidide.inflater.events.IInflateEventsListener
import com.itsaky.androidide.inflater.internal.LayoutInflaterImpl
import com.itsaky.androidide.lookup.Lookup.Key
import com.itsaky.androidide.projects.android.AndroidModule
import java.io.Closeable
import java.io.File

/**
 * An [ILayoutInflater] converts (inflates) XML layout to Android view which can be easily rendered.
 *
 * @author Akash Yadav
 */
interface ILayoutInflater : Closeable {

  /** The listener which listens to layout inflation events. */
  var inflationEventListener: IInflateEventsListener?

  /** The [IComponentFactory] which is used to create components of the layout inflater. */
  var componentFactory: IComponentFactory

  /** The [AndroidModule] which is used to resolve resource references. */
  var module: AndroidModule?

  /**
   * Inflate the given raw XML file.
   *
   * **NOTE** : The [startParse][com.itsaky.androidide.inflater.utils.startParse] method is called
   * if it hasn't been called yet. However, the [endParse]
   * [com.itsaky.androidide.inflater.utils.endParse] method is not called after the parse is done.
   * The caller is expected to call [close] when this inflater instance is no longer needed. The
   * [close] method calls the [endParse] [com.itsaky.androidide.inflater.utils.endParse].
   *
   * @param file The file to inflate.
   * @param parent The parent view.
   */
  fun inflate(file: File, parent: ViewGroup): List<IView>

  /**
   * Inflate the given raw XML file.
   *
   * **NOTE** : The [startParse][com.itsaky.androidide.inflater.utils.startParse] method is called
   * if it hasn't been called yet. However, the [endParse]
   * [com.itsaky.androidide.inflater.utils.endParse] method is not called after the parse is done.
   * The caller is expected to call [close] when this inflater instance is no longer needed. The
   * [close] method calls the [endParse] [com.itsaky.androidide.inflater.utils.endParse].
   *
   * @param file The file to inflate.
   * @param parent The parent view.
   */
  fun inflate(file: File, parent: IViewGroup): List<IView>

  fun closeSilently() {
    try {
      close()
    } catch (err: Throwable) {
      // ignored
    }
  }

  companion object {

    /** The [Key] that can be used to lookup the layout inflater service. */
    @JvmField val LOOKUP_KEY = Key<ILayoutInflater>()

    /**
     * Creates a new [ILayoutInflater] instance.
     *
     * @param module The [AndroidModule] that will be used to resolve resource references.
     * @param componentFactory The [IComponentFactory] that will be used to create [ILayoutInflater]
     * components.
     */
    @JvmStatic
    @JvmOverloads
    fun newInflater(
      module: AndroidModule? = null,
      componentFactory: IComponentFactory = DefaultComponentFactory()
    ): ILayoutInflater {
      return LayoutInflaterImpl().also {
        it.module = module
        it.componentFactory = componentFactory
      }
    }
  }
}
