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


package com.itsaky.androidide.tasks;

import java.io.File;
import java.util.List;

public interface GradleTask {
    
	public String getName();
    public String getCommands();
    public List<String> getTasks();
	public int getTaskID();
    public boolean affectsGeneratedSources();
	public boolean shouldSaveFiles();
	public boolean canOutput();
	public Type getType();
	
	public static enum Type {
		ANDROIDIDE_PLUGIN,
		BUILD,
		LINT,
		HELP,
		OTHER
    }
    
    
    public static final int TASK_SHOW_DEPENDENCIES                   = 0;
    public static final int TASK_ASSEMBLE_DEBUG                      = 1;
    public static final int TASK_ASSEMBLE_RELEASE                    = 2;
    public static final int TASK_BUILD                               = 3;
    public static final int TASK_BUNDLE                              = 4;
    public static final int TASK_CLEAN                               = 5;
    public static final int TASK_CLEAN_BUILD                         = 6;
    public static final int TASK_COMPILE_JAVA                        = 7;
    public static final int TASK_DEX                                 = 8;
    public static final int TASK_LINT                                = 9;
    public static final int TASK_LINT_DEBUG                          = 10;
    public static final int TASK_LINT_RELEASE                        = 11;
    public static final int TASK_TASKS                               = 12;
    public static final int TASK_INIT_PROJECT                        = 13;
    public static final int TASK_PROCESS_DEBUG_RESOURCES             = 14;
    public static final int DATABINDING_GEN_BASE_CLASSES_DEBUG       = 15;
    public static final int TASK_UPDATE_RESOURCE_CLASSES             = 16;
}
