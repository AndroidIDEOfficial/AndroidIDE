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

package com.itsaky.androidide.services;

import androidx.annotation.NonNull;

import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.LogLine;
import com.itsaky.androidide.shell.ShellServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IDELogService {
    
    private final ShellServer shell;
    private final List<LogReceiver.LogListener> logListeners;
    
    private boolean started;
    
    public IDELogService () {
        this.shell = StudioApp.getInstance ().newShell (this::output);
        this.logListeners = new ArrayList<> ();
    }
    
    public void start () {
        if (started) {
            return;
        }
        
        this.shell.bgAppend ("logcat -v threadtime");
        started = true;
    }
    
    public boolean isStarted () {
        return started;
    }
    
    public void addLogListener (LogReceiver.LogListener listener) {
        this.logListeners.add (Objects.requireNonNull (listener));
    }
    
    public void removeLogListener (LogReceiver.LogListener listener) {
        this.logListeners.remove (Objects.requireNonNull (listener));
    }
    
    private void output (@NonNull String log) {
        final var line = LogLine.forLogString (log);
        for (final var listener : this.logListeners) {
            listener.appendLogLine (line);
        }
    }
}
