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

import com.itsaky.androidide.buildinfo.BuildInfo

const val GITHUB_API_BASE_URL = "https://api.github.com"
const val GITHUB_API_REPOS_URL = "${GITHUB_API_BASE_URL}/repos"
const val GITHUB_API_REPO_URL = "${GITHUB_API_REPOS_URL}/${BuildInfo.REPO_OWNER}/${BuildInfo.REPO_NAME}"

const val GITHUB_RAW_API_BASE_URL = "https://raw.githubusercontent.com"
const val GITHUB_RAW_API_REPO_URL = "${GITHUB_RAW_API_BASE_URL}/${BuildInfo.REPO_OWNER}/${BuildInfo.REPO_NAME}"
const val GITHUB_RAW_API_REPO_BRANCH_URL = "${GITHUB_RAW_API_REPO_URL}/dev"