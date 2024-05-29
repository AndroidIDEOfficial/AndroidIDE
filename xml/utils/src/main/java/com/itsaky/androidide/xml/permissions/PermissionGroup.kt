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

package com.itsaky.androidide.xml.permissions

/**
 * Model for a permission group entry.
 *
 * @property constant The constant value of the permission group.
 * @author Akash Yadav
 */
enum class PermissionGroup(val constant: String) {
  ACTIVITY_RECOGNITION(ManifestPermissionConstants.GROUP_ACTIVITY_RECOGNITION),
  CALENDAR(ManifestPermissionConstants.GROUP_CALENDAR),
  CALL_LOG(ManifestPermissionConstants.GROUP_CALL_LOG),
  CAMERA(ManifestPermissionConstants.GROUP_CAMERA),
  CONTACTS(ManifestPermissionConstants.GROUP_CONTACTS),
  LOCATION(ManifestPermissionConstants.GROUP_LOCATION),
  MICROPHONE(ManifestPermissionConstants.GROUP_MICROPHONE),
  NEARBY_DEVICES(ManifestPermissionConstants.GROUP_NEARBY_DEVICES),
  NOTIFICATIONS(ManifestPermissionConstants.GROUP_NOTIFICATIONS),
  PHONE(ManifestPermissionConstants.GROUP_PHONE),
  READ_MEDIA_AURAL(ManifestPermissionConstants.GROUP_READ_MEDIA_AURAL),
  READ_MEDIA_VISUAL(ManifestPermissionConstants.GROUP_READ_MEDIA_VISUAL),
  SENSORS(ManifestPermissionConstants.GROUP_SENSORS),
  SMS(ManifestPermissionConstants.GROUP_SMS),
  STORAGE(ManifestPermissionConstants.GROUP_STORAGE)
}