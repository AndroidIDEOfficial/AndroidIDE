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

package com.itsaky.androidide.testing.common.logging

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.Configurator
import ch.qos.logback.classic.spi.ConfiguratorRank
import ch.qos.logback.core.spi.ContextAwareBase
import com.google.auto.service.AutoService
import com.itsaky.androidide.logging.JvmStdErrAppender
import com.itsaky.androidide.logging.encoder.IDELogFormatEncoder

@ConfiguratorRank(ConfiguratorRank.CUSTOM_HIGH_PRIORITY)
@AutoService(Configurator::class)
@Suppress("UNUSED")
class ToolingLoggingConfigurator : ContextAwareBase(), Configurator {

  override fun configure(context: LoggerContext): Configurator.ExecutionStatus {
    addInfo("Setting up logging configuration for test runtime")

    val stdErrAppender = JvmStdErrAppender()
    stdErrAppender.encoder = IDELogFormatEncoder()
    stdErrAppender.start()

    val rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME)
    rootLogger.addAppender(stdErrAppender)

    return Configurator.ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY
  }
}