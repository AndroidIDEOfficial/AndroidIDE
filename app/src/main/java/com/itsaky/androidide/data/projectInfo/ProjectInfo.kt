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

package com.itsaky.androidide.data.projectInfo

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.itsaky.androidide.models.ProjectInfoCache
import com.itsaky.androidide.utils.FileTypeConverter
import com.itsaky.androidide.utils.ProjectInfoCacheTypeConverter
import java.io.File

@Entity(tableName = "project_info")
data class ProjectInfo(
  val name: String,
  @TypeConverters(FileTypeConverter::class)
  @PrimaryKey
  val file: File,
  @TypeConverters(ProjectInfoCacheTypeConverter::class)
  val cache: ProjectInfoCache
)