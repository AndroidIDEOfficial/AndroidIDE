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
package com.itsaky.lsp.api

import com.itsaky.androidide.utils.ILogger
import com.itsaky.lsp.api.util.ReflectiveLogListener
import java.util.*

/**
 * This class provides an efficient way to log messages.
 *
 * @author Akash Yadav
 */
abstract class LoggingTest : BaseLanguageServerTest() {
    companion object {
        private var logListener: ReflectiveLogListener? = null
        
        init {
            logListener = ReflectiveLogListener(LoggingTest::class.java)
            ILogger.addLogListener(logListener)
        }
        
        @Suppress("unused")
        @JvmStatic
        fun log(priority: Int, tag: String, message: String) {
            System.out.printf(Locale.ROOT, "%-8s%-26s%-20s", ILogger.priorityText(priority), tag, message)
            println()
        }
    }
    
    @Suppress("PropertyName")
    @JvmField
    protected val LOG = ILogger.newInstance(javaClass.simpleName)
}