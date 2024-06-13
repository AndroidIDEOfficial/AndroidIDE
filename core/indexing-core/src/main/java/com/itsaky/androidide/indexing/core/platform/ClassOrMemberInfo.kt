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

package com.itsaky.androidide.indexing.core.platform

import io.realm.annotations.RealmField
import io.realm.annotations.Required

/**
 * [ApiVersion] of a class or class members.
 *
 * @property className The fully qualified name of the class.
 * @property memberName The name of the member (optional). Must be set if the objects represents API
 * information about a member of a class. Otherwise, it could be `null`.
 * @property apiVersion The [ApiVersion] about the class or the member.
 *
 * @author Akash Yadav
 */
open class ClassOrMemberInfo : IPlatformIndexable {

  @Required
  @RealmField("className")
  var className: String? = null

  @Required
  @RealmField("memberName")
  var memberName: String? = null

  @Required
  @RealmField("apiVersion")
  var apiVersion: ApiVersion? = null

  fun isMember(): Boolean = !isClass()
  fun isClass(): Boolean = this.memberName == null
}