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

package com.itsaky.androidide.tooling.impl.progress

import com.itsaky.androidide.tooling.api.IToolingApiClient
import com.itsaky.androidide.tooling.impl.Main
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.ProgressEvent
import org.gradle.tooling.events.ProgressListener
import org.gradle.tooling.events.StartEvent
import org.gradle.tooling.events.StatusEvent
import org.gradle.tooling.events.configuration.ProjectConfigurationFinishEvent
import org.gradle.tooling.events.configuration.ProjectConfigurationProgressEvent
import org.gradle.tooling.events.configuration.ProjectConfigurationStartEvent
import org.gradle.tooling.events.download.FileDownloadOperationDescriptor
import org.gradle.tooling.events.task.TaskFinishEvent
import org.gradle.tooling.events.task.TaskProgressEvent
import org.gradle.tooling.events.task.TaskStartEvent
import org.gradle.tooling.events.test.TestFinishEvent
import org.gradle.tooling.events.test.TestProgressEvent
import org.gradle.tooling.events.test.TestStartEvent
import org.gradle.tooling.events.transform.TransformFinishEvent
import org.gradle.tooling.events.transform.TransformProgressEvent
import org.gradle.tooling.events.transform.TransformStartEvent
import org.gradle.tooling.events.work.WorkItemFinishEvent
import org.gradle.tooling.events.work.WorkItemProgressEvent
import org.gradle.tooling.events.work.WorkItemStartEvent

/**
 * A [ProgressListener] which forwards all of its event to [IToolingApiClient].
 * @author Akash Yadav
 */
class ForwardingProgressListener : ProgressListener {

  override fun statusChanged(event: ProgressEvent?) {
    if (event == null || Main.client == null) {
      return
    }

    // File download progress event must not be sent
    if (event.descriptor is FileDownloadOperationDescriptor) {
      return
    }

    val ideEvent: com.itsaky.androidide.tooling.events.ProgressEvent =
      when (event) {
        is ProjectConfigurationProgressEvent ->
          when (event) {
            is ProjectConfigurationStartEvent -> EventTransformer.projectConfigurationStart(event)
            is ProjectConfigurationFinishEvent -> EventTransformer.projectConfigurationFinish(event)
            else -> EventTransformer.projectConfigurationProgress(event)
          }

        is TaskProgressEvent ->
          when (event) {
            is TaskStartEvent -> EventTransformer.taskStart(event)
            is TaskFinishEvent -> EventTransformer.taskFinish(event)
            else -> EventTransformer.taskProgress(event)
          }

        is TestProgressEvent ->
          when (event) {
            is TestStartEvent -> EventTransformer.testStart(event)
            is TestFinishEvent -> EventTransformer.testFinish(event)
            else -> EventTransformer.testProgress(event)
          }

        is TransformProgressEvent ->
          when (event) {
            is TransformStartEvent -> EventTransformer.transformStart(event)
            is TransformFinishEvent -> EventTransformer.transformFinish(event)
            else -> EventTransformer.transformProgress(event)
          }

        is WorkItemProgressEvent ->
          when (event) {
            is WorkItemStartEvent -> EventTransformer.workStart(event)
            is WorkItemFinishEvent -> EventTransformer.workFinish(event)
            else -> EventTransformer.workProgress(event)
          }

        is StatusEvent -> EventTransformer.statusEvent(event)
        else ->
          when (event) {
            is StartEvent -> EventTransformer.start(event)
            is FinishEvent -> EventTransformer.finish(event)
            else -> EventTransformer.progress(event)
          }
      }

    Main.client.onProgressEvent(ideEvent)
  }
}
