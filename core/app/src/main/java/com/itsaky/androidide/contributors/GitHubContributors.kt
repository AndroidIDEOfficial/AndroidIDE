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

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author Akash Yadav
 */
interface GitHubContributorsService {

  @GET("contributors")
  fun getAllContributors(): Call<List<GitHubContributor>>
}

object GitHubContributors {

  /**
   * Get all GitHub contributors.
   */
  suspend fun getAllContributors(): List<GitHubContributor> {
    return Contributors.getAllContributors<GitHubContributorsService, GitHubContributor>(
      "${GITHUB_API_REPO_URL}/",
      GitHubContributorsService::getAllContributors
    )
  }
}

/**
 * A GitHub contributor.
 */
data class GitHubContributor(
  @SerializedName("id") override val id: Int,
  @SerializedName("login") override val username: String,
  @SerializedName("avatar_url") override val avatarUrl: String,
  @SerializedName("html_url") override val profileUrl: String
) : Contributor