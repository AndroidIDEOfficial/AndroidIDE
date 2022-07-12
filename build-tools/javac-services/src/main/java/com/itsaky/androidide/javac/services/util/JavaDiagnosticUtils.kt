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

package com.itsaky.androidide.javac.services.util

import com.sun.tools.javac.api.ClientCodeWrapper
import com.sun.tools.javac.util.JCDiagnostic
import javax.tools.Diagnostic
import javax.tools.JavaFileObject

/** @author Akash Yadav */
class JavaDiagnosticUtils {
  companion object {
    @JvmStatic
    fun asJCDiagnostic(diagnostic: Diagnostic<out JavaFileObject>): JCDiagnostic? {
      if (diagnostic is JCDiagnostic) {
        return diagnostic
      } else if (diagnostic is ClientCodeWrapper.DiagnosticSourceUnwrapper) {
        return diagnostic.d
      }

      return null
    }

    @JvmStatic
    fun asUnwrapper(
      diagnostic: Diagnostic<out JavaFileObject>
    ): ClientCodeWrapper.DiagnosticSourceUnwrapper? {
      if (diagnostic is ClientCodeWrapper.DiagnosticSourceUnwrapper) {
        return diagnostic
      } else if (diagnostic is JCDiagnostic) {
        return wrap(diagnostic)
      }

      return null
    }

    private fun wrap(diagnostic: JCDiagnostic): ClientCodeWrapper.DiagnosticSourceUnwrapper {
      val klass = ClientCodeWrapper.DiagnosticSourceUnwrapper::class.java
      val construct = klass.getDeclaredConstructor(JCDiagnostic::class.java)
      construct.isAccessible = true
      return construct.newInstance(diagnostic)
    }
  }
}
