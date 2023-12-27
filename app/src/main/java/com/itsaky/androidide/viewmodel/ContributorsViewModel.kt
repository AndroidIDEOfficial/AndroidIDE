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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsaky.androidide.contributors.Contributor
import com.itsaky.androidide.contributors.CrowdinTranslator
import com.itsaky.androidide.contributors.CrowdinTranslators
import com.itsaky.androidide.contributors.GitHubContributor
import com.itsaky.androidide.contributors.GitHubContributors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Akash Yadav
 */
class ContributorsViewModel : ViewModel() {

  internal val _crowdinTranslators = MutableLiveData(emptyList<CrowdinTranslator>())
  internal val _githubContributors = MutableLiveData(emptyList<GitHubContributor>())

  companion object {
    private const val CONTRIBUTORS_MAX_SIZE = 30
  }

  val crowdinTranslators: List<CrowdinTranslator>
    get() = _crowdinTranslators.value ?: emptyList()

  val githubContributors: List<GitHubContributor>
    get() = _githubContributors.value ?: emptyList()

  fun fetchCrowdinTranslators() {
    viewModelScope.launch(Dispatchers.Default) {
      val translators = CrowdinTranslators.getAllTranslators()
      withContext(Dispatchers.Main) {
        _crowdinTranslators.value = translators.trimToMaxSize()
      }
    }
  }

  fun fetchGitHubTranslators() {
    viewModelScope.launch(Dispatchers.Default) {
      val contributors = GitHubContributors.getAllContributors()
      withContext(Dispatchers.Main) {
        _githubContributors.value = contributors.trimToMaxSize()
      }
    }
  }

  fun fetchAll() {
    fetchCrowdinTranslators()
    fetchGitHubTranslators()
  }

  private fun <T : Contributor> List<T>.trimToMaxSize() : List<T> {
    return if (size > CONTRIBUTORS_MAX_SIZE) {
      subList(0, CONTRIBUTORS_MAX_SIZE)
    } else {
      this
    }
  }
}