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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Akash Yadav
 */
internal object Contributors {

  private val log = LoggerFactory.getLogger(Contributors::class.java)

  @JvmStatic
  suspend inline fun <reified Service, reified Model : Contributor> getAllContributors(
    baseUrl: String,
    crossinline action: (Service) -> Call<List<Model>>
  ): List<Model> {
    val retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

    val service = retrofit.create(Service::class.java)
    return withContext(Dispatchers.IO) {
      try {
        val response = action(service).execute()
        if (!response.isSuccessful) {
          log.error(
            "Failed to get contributors list [${Model::class.java.name}], request unsuccessful",
            response.errorBody()?.string() ?: "(empty error response)"
          )
          return@withContext emptyList()
        }

        return@withContext response.body() ?: run {
          log.error("Response body is null")
          emptyList()
        }
      } catch (err: Throwable) {
        err.printStackTrace()
        emptyList()
      }
    }
  }
}