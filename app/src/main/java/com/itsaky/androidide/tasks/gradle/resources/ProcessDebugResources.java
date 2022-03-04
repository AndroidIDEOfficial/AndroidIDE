/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 **************************************************************************************/

package com.itsaky.androidide.tasks.gradle.resources;

import com.itsaky.androidide.tasks.BaseGradleTask;
import com.itsaky.androidide.tasks.GradleTask;
import java.util.Arrays;
import java.util.List;

public class ProcessDebugResources extends BaseGradleTask {

  @Override
  public boolean affectsGeneratedSources() {
    return true;
  }

  @Override
  public String getName() {
    return "processDebugResources";
  }

  @Override
  public String getCommands() {
    return "processDebugResources";
  }

  @Override
  public List<String> getTasks() {
    return Arrays.asList(getCommands());
  }

  @Override
  public int getTaskID() {
    return TASK_PROCESS_DEBUG_RESOURCES;
  }

  @Override
  public boolean canOutput() {
    return true;
  }

  @Override
  public GradleTask.Type getType() {
    return Type.BUILD;
  }
}
