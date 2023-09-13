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

package com.itsaky.androidide.data.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itsaky.androidide.data.projectInfo.ProjectInfo
import com.itsaky.androidide.data.projectInfo.ProjectInfoDao
import com.itsaky.androidide.utils.FileTypeConverter
import com.itsaky.androidide.utils.ProjectInfoCacheTypeConverter

@Database(entities = [ProjectInfo::class], version = 1, exportSchema = false)
@TypeConverters(FileTypeConverter::class, ProjectInfoCacheTypeConverter::class)
abstract class IDEDatabase : RoomDatabase() {

  abstract fun projectInfoDao(): ProjectInfoDao

  companion object {

    @Volatile
    private var Instance: IDEDatabase? = null

    fun getDatabase(context: Context): IDEDatabase {
      return Instance ?: synchronized(this) {
        Room.databaseBuilder(context, IDEDatabase::class.java, "ide_database")
          .fallbackToDestructiveMigrationFrom()
          .build()
          .also { Instance = it }
      }
    }
    
  }
}