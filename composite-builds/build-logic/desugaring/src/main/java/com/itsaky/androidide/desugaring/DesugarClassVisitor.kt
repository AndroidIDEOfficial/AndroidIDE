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

import com.android.build.api.instrumentation.ClassContext
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

/**
 * [ClassVisitor] implementation for desugaring.
 *
 * @author Akash Yadav
 */
class DesugarClassVisitor(private val params: DesugarParams,
                          private val classContext: ClassContext, api: Int,
                          classVisitor: ClassVisitor
) : ClassVisitor(api, classVisitor) {

  override fun visitMethod(access: Int, name: String?, descriptor: String?,
                           signature: String?, exceptions: Array<out String>?
  ): MethodVisitor {
    return DesugarMethodVisitor(params, classContext, api,
      super.visitMethod(access, name, descriptor, signature, exceptions))
  }
}

