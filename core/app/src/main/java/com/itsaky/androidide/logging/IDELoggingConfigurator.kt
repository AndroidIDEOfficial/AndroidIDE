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

package com.itsaky.androidide.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ConfiguratorRank
import ch.qos.logback.core.spi.ContextAwareBase
import com.google.auto.service.AutoService
import com.itsaky.androidide.logging.encoder.IDELogFormatEncoder

/**
 * Default IDE logging configurator.
 *
 * @author Akash Yadav
 */
@ConfiguratorRank(ConfiguratorRank.CUSTOM_TOP_PRIORITY)
@AutoService(Configurator::class)
@Suppress("UNUSED")
class IDELoggingConfigurator : ContextAwareBase(), Configurator {

  override fun configure(context: LoggerContext): Configurator.ExecutionStatus {
    addInfo("Setting up logging configuration")

    val appender = LogcatAppender()
    appender.encoder = IDELogFormatEncoder()
    appender.start()

    val rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME)
    rootLogger.addAppender(appender)

    return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
  }
}