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

package com.itsaky.androidide.xml.internal.versions

import com.itsaky.androidide.xml.versions.ClassInfo
import com.itsaky.androidide.xml.versions.FieldInfo
import com.itsaky.androidide.xml.versions.MethodInfo
import java.util.concurrent.ConcurrentHashMap
import org.eclipse.jdt.core.Signature

/**
 * Default implementation of [ClassInfo]
 *
 * @author Akash Yadav
 */
internal class DefaultClassInfo(name: String, since: Int, removed: Int, deprecated: Int) :
  DefaultInfo(name, since, removed, deprecated), ClassInfo {

  internal val fields = ConcurrentHashMap<String, FieldInfo>()
  internal val methods = ConcurrentHashMap<String, MutableList<MethodInfo>>()

  override fun getField(name: String): FieldInfo? {
    return fields[name]
  }

  override fun getMethod(name: String, vararg params: String): MethodInfo? {
    val methods = methods[name] ?: return null
    val paramTypes = Array(size = params.size) { "" }
    params.forEachIndexed { index, type ->
      paramTypes[index] = Signature.createTypeSignature(type.replace('.', '/'), true)
    }

    return methods.find {
      val methodParams = Signature.getParameterTypes(it.name)
      methodParams != null && paramTypes.contentDeepEquals(methodParams)
    }
  }
}
