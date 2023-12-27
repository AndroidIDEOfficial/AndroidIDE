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

package com.itsaky.androidide.contributors

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * @author Akash Yadav
 */
@RunWith(RobolectricTestRunner::class)
class ContributorsAPITest {

  @Test
  fun `test crowdin contributors deserialization`() = runBlocking {
    val translators = CrowdinTranslators.getAllTranslators()
    assertThat(translators).isNotEmpty()
  }

  @Test
  fun `test GitHub contributors exclusion`() = runBlocking {
    val contributors = GitHubContributors.getAllContributors()
    assertThat(contributors).isNotEmpty()
    assertThat(contributors.filter { it.username == "itsaky" }).isNotEmpty()
  }
}