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

private interface CrowdinTranslatorsService {

  @GET(CrowdinTranslators.CROWDIN_CONTRIBUTORS_JSON)
  fun getAllTranslators(): Call<List<CrowdinTranslator>>
}

object CrowdinTranslators {

  internal const val CROWDIN_CONTRIBUTORS_JSON = "crowdin-contributors.json"
  private val log = ILogger.newInstance("CrowdinTranslators")

  /**
   * Get all Crowdin translators.
   */
  suspend fun getAllTranslators(): List<CrowdinTranslator> {
    val retrofit = Retrofit.Builder()
      .baseUrl("${GITHUB_RAW_API_REPO_BRANCH_URL}/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    val service = retrofit.create(CrowdinTranslatorsService::class.java)
    val response = withContext(Dispatchers.IO) { service.getAllTranslators().execute() }

    if (!response.isSuccessful) {
      log.error("Failed to get Crowdin translators list, request unsuccessful",
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
 * A Crowdin translator.
 *
 * @author Akash Yadav
 */
data class CrowdinTranslator(
  @SerializedName("id") private val _id: String,
  @SerializedName("username") override val username: String,
  @SerializedName("picture") override val avatarUrl: String,
) : Contributor {

  override val id: Int
    get() = _id.toInt()

  override val profileUrl: String
    get() = "${CROWDIN_PROFILE_BASE_URL}/$username"

  companion object {

    const val CROWDIN_PROFILE_BASE_URL = "https://crowdin.com/profile"
  }
}