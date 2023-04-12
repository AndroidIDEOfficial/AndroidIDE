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

/**
 * @author Akash Yadav
 */
object ProjectConfig {

  /**
   * The GitHub organization for this project.
   */
  const val GITHUB_ORGANIZATION = "AndroidIDEOfficial"

  /**
   * The GitHub repsitory for this project.
   */
  const val GITHUB_REPOSITORY = "AndroidIDE"

  /**
   * The GitHub repository URL for this project.
   */
  const val GITHUB_URL = "https://github.com/${GITHUB_ORGANIZATION}/${GITHUB_REPOSITORY}"

  /**
   * The Project website.
   */
  const val PROJECT_SITE = "https://androidide.com"

  /**
   * The GIT SCM URL.
   */
  const val SCM_GIT = "scm:git:git://github.com/${GITHUB_ORGANIZATION}/${GITHUB_REPOSITORY}.git"

  /**
   * The SSH SCM URL.
   */
  const val SCM_SSH = "scm:git:ssh://git@github.com/${GITHUB_ORGANIZATION}/${GITHUB_REPOSITORY}.git"
}