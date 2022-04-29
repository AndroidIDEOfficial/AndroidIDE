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

package com.itsaky.androidide.builder

import java.io.File

/**
 * A build service provides API to initialize project, execute builds, query a build, cancel running
 * builds, etc.
 *
 * @author Akash Yadav
 */
interface BuildService {

    /**
     * Initialize the project.
     *
     * @param rootDir The root directory of the project to initialize.
     */
    fun initializeProject(rootDir: File)
    
    /**
     * Execute the given tasks.
     *
     * @param tasks The tasks to execute. If the fully qualified path of the task is not specified,
     * then it will be executed in the root project directory.
     * @see BuildService.executeProjectTasks
     */
    fun executeTasks(vararg tasks: String)

    /**
     * Execute the given tasks of the given project.
     *
     * @param projectPath The path of the project. All the tasks will be executed in this project.
     * @param tasks The tasks to execute. These may or may not be fully qualified names of the
     * project. If the task name is not fully qualified, then it will be executed in the given
     * project path. For example, if the project path is ':app' and the task is 'assembleDebug',
     * then, ':app:assembleDebug' task will be executed.
     */
    fun executeProjectTasks(projectPath: String, vararg tasks: String)
}
