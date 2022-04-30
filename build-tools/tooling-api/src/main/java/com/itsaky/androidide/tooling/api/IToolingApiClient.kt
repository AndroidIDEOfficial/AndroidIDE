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

package com.itsaky.androidide.tooling.api

import com.itsaky.androidide.models.LogLine
import org.eclipse.lsp4j.jsonrpc.services.JsonNotification
import org.eclipse.lsp4j.jsonrpc.services.JsonSegment

/**
 * A client consumes services provided by [IToolingApiServer].
 *
 * @author Akash Yadav
 */
@JsonSegment("client")
interface IToolingApiClient {

    /**
     * Log the given log message.
     *
     * @param line The [LogLine] to log.
     */
    @JsonNotification fun logMessage(line: LogLine)

    /**
     * Log the build output received from Gradle.
     *
     * @param line The line of the build output to log.
     */
    @JsonNotification fun logOutput(line: String)
}
