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

package com.itsaky.androidide.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsaky.androidide.data.projectInfo.ProjectInfo
import com.itsaky.androidide.data.projectInfo.ProjectInfoRepository
import com.itsaky.androidide.models.ProjectInfoDetails
import com.itsaky.androidide.models.toProjectInfo
import kotlinx.coroutines.launch

class ProjectInfoViewModel(private val projectInfoRepository: ProjectInfoRepository) : ViewModel() {

  private val _projectInfoList = MutableLiveData<List<ProjectInfo>>(listOf())

  val projectInfoList: LiveData<List<ProjectInfo>>
    get() = _projectInfoList

  fun saveProjectInfo(projectInfoDetails: ProjectInfoDetails) {
    viewModelScope.launch {
      projectInfoRepository.insertProjectInfo(projectInfoDetails.toProjectInfo())
    }
  }

  fun getAllProjectInfo() {
    viewModelScope.launch {
      projectInfoRepository.getAllProjectInfoStream().collect { projectInfoList ->
        _projectInfoList.value = projectInfoList
      }
    }
  }
}