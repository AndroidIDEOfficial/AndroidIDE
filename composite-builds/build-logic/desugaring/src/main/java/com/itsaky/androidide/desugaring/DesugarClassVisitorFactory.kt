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

package com.itsaky.androidide.desugaring

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import org.objectweb.asm.ClassVisitor
import org.slf4j.LoggerFactory

/**
 * [AsmClassVisitorFactory] implementation for desugaring.
 *
 * @author Akash Yadav
 */
abstract class DesugarClassVisitorFactory :
  AsmClassVisitorFactory<DesugarParams> {

  companion object {

    private val log =
      LoggerFactory.getLogger(DesugarClassVisitorFactory::class.java)
  }

  override fun createClassVisitor(classContext: ClassContext,
                                  nextClassVisitor: ClassVisitor
  ): ClassVisitor {
    val params = parameters.orNull
    if (params == null) {
      log.warn("Could not find desugaring parameters. Disabling desugaring.")
      return nextClassVisitor
    }

    return DesugarClassVisitor(params, classContext,
      instrumentationContext.apiVersion.get(), nextClassVisitor)
  }

  override fun isInstrumentable(classData: ClassData): Boolean {
    val params = parameters.orNull
    if (params == null) {
      log.warn("Could not find desugaring parameters. Disabling desugaring.")
      return false
    }

    val isEnabled = params.enabled.get().also { isEnabled ->
      log.debug("Is desugaring enabled: $isEnabled")
    }

    if (!isEnabled) {
      return false
    }

    val includedPackages = params.includedPackages.get()
    if (includedPackages.isNotEmpty()) {
      val className = classData.className
      if (!includedPackages.any { className.startsWith(it) }) {
        return false
      }
    }

    return true
  }
}