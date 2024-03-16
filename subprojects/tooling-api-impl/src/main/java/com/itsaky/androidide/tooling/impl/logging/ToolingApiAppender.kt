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

package com.itsaky.androidide.tooling.impl.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.api.messages.LogMessageParams
import org.slf4j.LoggerFactory

/**
 * [AppenderBase] implementation which forwards all logs to the tooling API client.
 *
 * @author Akash Yadav
 */
class ToolingApiAppender(
  private val client: IToolingApiClient
) : AppenderBase<ILoggingEvent>() {

  fun attachToRoot() {
    val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    setContext(LoggerFactory.getILoggerFactory() as LoggerContext)
    start()

    rootLogger.addAppender(this)
  }

  fun detachFromRoot() {
    stop()

    val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    rootLogger.detachAppender(this)
  }

  override fun append(eventObject: ILoggingEvent?) {
    if (eventObject == null || !isStarted) {
      return
    }

    client.logMessage(
      LogMessageParams(
        eventObject.level.levelStr[0],
        eventObject.loggerName,
        eventObject.formattedMessage
      )
    )
  }
}