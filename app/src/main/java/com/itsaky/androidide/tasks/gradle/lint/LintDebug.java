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


package com.itsaky.androidide.tasks.gradle.lint;

import com.itsaky.androidide.R;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.services.builder.IDEService;
import com.itsaky.androidide.tasks.BaseGradleTask;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LintDebug extends BaseGradleTask {
    
	@Override
	public String getName() {
		return StudioApp.getInstance().getString(R.string.lint_debug);
	}
    
    @Override
	public String getCommands() {
		return "lintDebug";
	}
    
    @Override
    public List<String> getTasks() {
        return Collections.singletonList ("lintDebug");
    }

	@Override
	public int getTaskID() {
		return IDEService.TASK_LINT_DEBUG;
	}

	@Override
	public boolean canOutput() {
		return true;
	}

	@Override
	public Type getType() {
		return Type.LINT;
	}
    
}
