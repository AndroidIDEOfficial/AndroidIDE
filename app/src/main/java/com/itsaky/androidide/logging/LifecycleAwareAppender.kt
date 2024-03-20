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

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import ch.qos.logback.core.Context
import com.itsaky.androidide.logging.encoder.IDELogFormatLayout

/**
 * An [AppenderBase] implementation to show the logs in the GUI.
 *
 * @author Akash Yadav
 */
class LifecycleAwareAppender @JvmOverloads constructor(
  private val requireLifecycleState: Lifecycle.State = Lifecycle.State.CREATED,
  var consumer: ((String) -> Unit)? = null
) : AppenderBase<ILoggingEvent>(), LifecycleEventObserver {

  private var currentState: Lifecycle.State? = null
  private val logLayout = IDELogFormatLayout()

  init {
    setName("LifecycleAwareAppender")
    logLayout.isOmitMessage = true
  }

  fun attachTo(lifecycleOwner: LifecycleOwner) = attachTo(lifecycleOwner.lifecycle)
  fun attachTo(lifecycle: Lifecycle) = lifecycle.addObserver(this)

  fun detachFrom(lifecycleOwner: LifecycleOwner) = detachFrom(lifecycleOwner.lifecycle)
  fun detachFrom(lifecycle: Lifecycle) = lifecycle.removeObserver(this)

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    this.currentState = when (event) {
      Lifecycle.Event.ON_ANY -> null
      else -> event.targetState
    }
  }

  override fun isStarted(): Boolean {
    return (super.isStarted()
        && currentState?.isAtLeast(this.requireLifecycleState) == true
        && consumer != null)
  }

  override fun start() {
    this.logLayout.start()
    super.start()
  }

  override fun stop() {
    super.stop()
    this.logLayout.stop()
    this.consumer = null
  }

  override fun setContext(context: Context?) {
    super.setContext(context)
    this.logLayout.context = context
  }

  override fun append(eventObject: ILoggingEvent?) {
    if (eventObject == null || !isStarted) {
      return
    }

    // When rendering the logs in the GUI, we need to ensure that the message does not span multiple lines
    // if it does, we need to prefix the message with the layout header
    val prefix = logLayout.doLayout(eventObject)
    eventObject.formattedMessage.split('\n').forEach {
      consumer?.invoke("$prefix $it")
    }
  }
}