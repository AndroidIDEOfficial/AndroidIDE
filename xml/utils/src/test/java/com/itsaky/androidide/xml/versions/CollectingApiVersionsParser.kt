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

package com.itsaky.androidide.xml.versions

/**
 * @author Akash Yadav
 */
class CollectingApiVersionsParser :
  com.itsaky.androidide.xml.internal.versions.ApiVersionsParser() {

  private val apiInfo = mutableMapOf<String, Pair<ApiVersion, MutableMap<String, ApiVersion>>>()

  override fun isDuplicateClass(name: String): Boolean {
    return apiInfo.containsKey(name)
  }

  override fun consumeClassVersionInfo(name: String, apiVersion: ApiVersion) {
    this.apiInfo[name] = apiVersion to HashMap()
  }

  override fun isDuplicateMember(className: String, memberName: String): Boolean {
    return apiInfo[className]?.second?.containsKey(memberName) == true
  }

  override fun consumeMemberVersionInfo(
    className: String,
    member: String,
    memberType: String,
    apiVersion: ApiVersion
  ) {
    val (_, members) = apiInfo[className]!!
    val existing = members.put(member, apiVersion)
    check(existing == null) {
      "Duplicate $memberType entry in class $className: $member"
    }
  }

  /**
   * Returns [ApiVersion] for the given class.
   */
  fun getClassInfo(className: String): ApiVersion? {
    return apiInfo[className]?.first
  }

  /**
   * Returns [ApiVersion] for the given class and member.
   */
  fun getMemberInfo(className: String, memberName: String): ApiVersion? {
    return apiInfo[className]?.second?.get(memberName)
  }

  /**
   * Removes and returns the [ApiVersion] for the given class and member. This also removes the [ApiVersion]
   * for the class if the all the members of the class have been removed.
   */
  fun removeMemberInfo(className: String, memberName: String): ApiVersion? {
    return apiInfo[className]?.second?.remove(memberName).also {
      if (apiInfo[className]?.second?.isEmpty() == true) {
        apiInfo.remove(className)
      }
    }
  }
}