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
import com.itsaky.androidide.utils.ILogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * @author Akash Yadav
 */
interface GitHubContributorsService {

  @GET("contributors")
  fun getAllContributors(): Call<List<GitHubContributor>>
}

object GitHubContributors {

  private val log = ILogger.newInstance("GitHubContributors")

  /**
   * Get all GitHub contributors.
   */
  suspend fun getAllContributors(): List<GitHubContributor> {
    val retrofit = Retrofit.Builder()
      .baseUrl("${GITHUB_API_REPO_URL}/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    val service = retrofit.create(GitHubContributorsService::class.java)
    val response = withContext(Dispatchers.IO) { service.getAllContributors().execute() }

    if (!response.isSuccessful) {
      log.error("Failed to get GitHub contributors list, request unsuccessful",
        response.errorBody()?.string() ?: "(empty error response)")
      return emptyList()
    }

    return response.body() ?: run {
      log.error("Response body is null")
      emptyList()
    }
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