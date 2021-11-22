/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
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
package com.itsaky.androidide.shell;

import java.io.IOException;

/**
 * Provides static access to the process executor
 */
public class ProcessExecutorFactory {
    
    private static final CommonProcessExecutor executor = new CommonProcessExecutor ();
    
    public static IProcessExecutor commonExecutor () {
        return executor;
    }
    
    public static int exec (ProcessStreamsHolder holder, String ... args) throws InterruptedException, IOException {
        return executor.exec(holder, args);
    }
    
    public static int exec (ProcessStreamsHolder holder, boolean redirectErr, String ... args) throws InterruptedException, IOException {
        return executor.exec(holder, redirectErr, args);
    }
    
    public static void execAsync (ProcessStreamsHolder holder, IProcessExitListener exitListener, boolean redirectErr, String ... args) throws IOException {
        executor.execAsync(holder, exitListener, redirectErr, args);
    }
}
