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

package com.itsaky.androidide.tasks.callables;

import com.itsaky.androidide.interfaces.ProjectWriterCallback;
import com.itsaky.androidide.models.NewProjectDetails;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.utils.ProjectWriter;

import java.util.concurrent.Callable;

public class ProjectCreatorCallable implements Callable<Void> {

  private ProjectTemplate template;
  private NewProjectDetails details;
  private ProjectWriterCallback callback;

  public ProjectCreatorCallable(
      ProjectTemplate template, NewProjectDetails details, ProjectWriterCallback callback) {
    this.template = template;
    this.details = details;
    this.callback = callback;
  }

  @Override
  public Void call() throws Exception {
    ProjectWriter.write(template, details, callback);
    return null;
  }
}
