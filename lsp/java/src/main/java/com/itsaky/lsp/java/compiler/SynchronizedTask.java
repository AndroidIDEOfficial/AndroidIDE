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

package com.itsaky.lsp.java.compiler;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

// See CodeAssist's CompilerContainer
public class SynchronizedTask {

    private volatile boolean isWriting;
    private final Object lock = new Object();

    @GuardedBy("lock")
    private volatile CompileTask task;

    private final List<Thread> readerThreads = Collections.synchronizedList(new ArrayList<>());

    private void closeIfEmpty() {
        if (readerThreads.isEmpty()) {
            if (task != null) {
                task.close();
            }
        }
    }

    public void runWithTask(@NonNull Consumer<CompileTask> taskConsumer) {
        waitForWriter();
        readerThreads.add(Thread.currentThread());
        try {
            taskConsumer.accept(task);
        } finally {
            readerThreads.remove(Thread.currentThread());
        }
    }

    public <T> T getWithTask(@NonNull Function<CompileTask, T> function) {
        waitForWriter();
        readerThreads.add(Thread.currentThread());
        try {
            return function.apply(task);
        } finally {
            readerThreads.remove(Thread.currentThread());
        }
    }

    void doCompile(@NonNull Runnable run) {
        synchronized (lock) {
            assertIsNotReader();
            waitForReaders();
            try {
                isWriting = true;
                run.run();
            } finally {
                isWriting = false;
            }
        }
    }

    void setTask(CompileTask task) {
        this.task = task;
    }

    private void waitForReaders() {
        while (true) {
            if (readerThreads.isEmpty()) {
                closeIfEmpty();
                return;
            }
        }
    }

    private void waitForWriter() {
        while (true) {
            if (!isWriting) {
                return;
            }
        }
    }

    private void assertIsNotReader() {
        if (readerThreads.contains(Thread.currentThread())) {
            throw new RuntimeException("Cannot compile inside a container.");
        }
    }
}
