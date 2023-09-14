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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.io.File

@Dao
interface ProjectInfoDao {

  @Query("SELECT * from project_info ORDER by name ASC")
  fun getAllProjectInfo(): Flow<List<ProjectInfo>>

  @Query("SELECT * from project_info WHERE file = :file")
  fun getProjectInfo(file: File): Flow<ProjectInfo>

  @Upsert
  suspend fun upsert(projectInfo: ProjectInfo)

  @Delete
  suspend fun delete(projectInfo: ProjectInfo)
}