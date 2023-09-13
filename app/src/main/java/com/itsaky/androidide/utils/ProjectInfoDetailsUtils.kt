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

package com.itsaky.androidide.utils

import android.os.Parcelable
import com.itsaky.androidide.models.ProjectInfoDetails
import kotlinx.parcelize.Parcelize

object ProjectInfoDetailsUtils { @Parcelize
data class ProjectSortOptions(val sortBy: SortBy, val order: Order) : Parcelable {

  fun createComparator(): Comparator<ProjectInfoDetails> {
    val comparator = when (sortBy) {
      SortBy.OPEN_LAST -> compareByDescending<ProjectInfoDetails> { it.cache.lastOpened }
    }

    return if (order == Order.DESCENDING) comparator.reversed() else comparator
  }
}

  enum class SortBy { OPEN_LAST,
  }

  enum class Order { ASCENDING, DESCENDING
  }

}