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

package com.itsaky.androidide.inflater.internal.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import com.itsaky.androidide.inflater.InflateException
import com.itsaky.androidide.inflater.internal.ViewAdapterIndexImpl
import org.slf4j.LoggerFactory
import java.lang.reflect.Method

/** @author Akash Yadav */
object ViewFactory {

  private val log = LoggerFactory.getLogger(ViewFactory::class.java)

  fun createViewInstance(name: String, context: Context): View {
    val adapter = ViewAdapterIndexImpl.INSTANCE.getViewAdapter(name)
    return try {
      if (adapter != null) {
        // Check if adapter can create the view instance
        val view = adapter.onCreateView(name, context)
        if (view != null) {
          return view
        }
      }

      val klass = javaClass.classLoader!!.loadClass(name)
      val constructor = klass.getConstructor(Context::class.java)
      constructor.newInstance(context) as View
    } catch (err: Throwable) {
      log.error("Failed to create view instance for view: {}", name, err)
      throw RuntimeException(err)
    }
  }

  fun generateLayoutParams(parent: ViewGroup): LayoutParams {
    return try {
      var clazz: Class<in ViewGroup> = parent.javaClass
      var method: Method?
      while (true) {
        try {
          method = clazz.getDeclaredMethod("generateDefaultLayoutParams")
          break
        } catch (e: Throwable) {
          /* ignored */
        }

        clazz = clazz.superclass
      }
      if (method != null) {
        method.isAccessible = true
        return method.invoke(parent) as LayoutParams
      }
      log.error("Unable to create default params for view parent: {}", parent)
      LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    } catch (th: Throwable) {
      throw InflateException("Unable to create layout params for parent: $parent", th)
    }
  }
}
