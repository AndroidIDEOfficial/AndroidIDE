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

package com.itsaky.lsp.java.utils;

import com.itsaky.lsp.java.CompileTask;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Akash Yadav
 */
public class SynchronizedTask implements AutoCloseable {
    
    private volatile CompileTask task;
    
    public synchronized void runWithTask (Consumer<CompileTask> taskConsumer) {
        taskConsumer.accept (task);
    }
    
    public synchronized <T> T getWithTask (Function<CompileTask, T> function) {
        return function.apply (task);
    }
    
    public synchronized void setTask (CompileTask task) {
        this.task = task;
    }
    
    @Override
    public void close () {
        if (task != null) {
            task.close ();
        }
    }
}
