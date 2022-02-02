/*
 *    sora-editor - the awesome code editor for Android
 *    https://github.com/Rosemoe/sora-editor
 *    Copyright (C) 2020-2022  Rosemoe
 *
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 *
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *     USA
 *
 *     Please contact Rosemoe by email 2073412493@qq.com if you need
 *     additional information or have any questions
 */
package io.github.rosemoe.sora.event;

/**
 * Instance for unsubscribing for a receiver.
 *
 * Note that this instance can be reused during an event dispatch, so
 * it is not a valid behavior to save the instance in event receivers.
 * Always use the one given by {@link EventReceiver#onReceive(Event, Unsubscribe)}.
 */
public class Unsubscribe {

    private boolean unsubscribeFlag = false;

    /**
     * Unsubscribe the event. And current receiver will not get event again.
     * References to the receiver are also removed.
     */
    public void unsubscribe() {
        unsubscribeFlag = true;
    }

    /**
     * Checks whether unsubscribe flag is set
     */
    public boolean isUnsubscribed() {
        return unsubscribeFlag;
    }

    /**
     * Reset the flag
     */
    public void reset() {
        unsubscribeFlag = false;
    }

}
