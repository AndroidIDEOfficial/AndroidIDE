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
package io.github.rosemoe.sora.lang.completion;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.rosemoe.sora.annotations.UnsupportedUserUsage;
import io.github.rosemoe.sora.lang.Language;

/**
 * CompletionPublisher manages completion items to be added in one completion analyzing process.
 *
 * You can only add items to the publisher, but no deletion is allowed. As you add more items, the
 * publisher will update the list in UI from time to time, which is related to your threshold
 * settings.({@link CompletionPublisher#setUpdateThreshold(int)}).
 * There will usually be some items not displayed in screen when the thread is still running. Even
 * when the actual pending item count exceeds the threshold you set, there may still be some items
 * not committed because of lock failures. You can use {@link CompletionPublisher#updateList(boolean)}
 * with forced flag to command the UI thread update the completion list, by waiting for the lock from
 * your side to release.
 * If you want to disable this feature, you may want to set it to {@link Integer#MAX_VALUE}
 *
 * You can set a comparator by {@link CompletionPublisher#setComparator(Comparator)} to sort your
 * result items, but you should not make it too complex, which will cause laggy in UI thread. It is
 * recommended that you set the comparator before all your actions.
 * Leaving the comparator null results the completion to be unsorted. They will be ordered by the order
 * you add them.
 *
 * After all you additions, you do not need to explicitly invoke {@link CompletionPublisher#updateList(boolean)}.
 * This will automatically be called by editor framework.
 *
 * Note that your actions may be interrupted because of {@link Thread#interrupted()}.
 */
public class CompletionPublisher {

    private Comparator<CompletionItem> comparator;
    private final List<CompletionItem> items;
    private final List<CompletionItem> candidates;
    private final Lock lock;
    private final Handler handler;
    private int updateThreshold;
    private boolean invalid = false;
    private final Runnable callback;
    private final int languageInterruptionLevel;

    /**
     * Default value for {@link CompletionPublisher#setUpdateThreshold(int)}
     */
    public final static int DEFAULT_UPDATE_THRESHOLD = 5;

    public CompletionPublisher(@NonNull Handler handler, @NonNull Runnable callback, int languageInterruptionLevel) {
        this.handler = handler;
        this.items = new ArrayList<>();
        this.candidates = new ArrayList<>();
        lock = new ReentrantLock(true);
        updateThreshold = DEFAULT_UPDATE_THRESHOLD;
        this.callback = callback;
        this.languageInterruptionLevel = languageInterruptionLevel;
    }

    /**
     * Checks whether there is data
     */
    public boolean hasData() {
        return items.size() + candidates.size() > 0;
    }

    /**
     * Get items currently in display
     */
    @UnsupportedUserUsage
    public List<CompletionItem> getItems() {
        return items;
    }

    /**
     * Set the max pending items in analyzing thread.
     * See class javadoc for more information.
     */
    public void setUpdateThreshold(int updateThreshold) {
        this.updateThreshold = updateThreshold;
    }

    /**
     * Set the result's comparator.
     *
     * The comparator is used when publishing the completion to user.
     */
    public void setComparator(@Nullable Comparator<CompletionItem> comparator) {
        checkCancelled();
        if (invalid) {
            return;
        }
        this.comparator = comparator;
        if (items.size() != 0) {
            handler.post(() -> {
                if (invalid) {
                    return;
                }
                if (comparator != null) {
                    Collections.sort(items, comparator);
                }
                callback.run();
            });
        }
    }

    /**
     * Add items in the completion list.
     *
     * According to your settings and the lock's state, these items may not immediately
     * be displayed to the user.
     *
     * @see CompletionPublisher#setUpdateThreshold(int)
     */
    public void addItems(Collection<CompletionItem> items) {
        checkCancelled();
        if (invalid) {
            return;
        }
        lock.lock();
        try {
            candidates.addAll(items);
        } finally {
            lock.unlock();
        }
        if (candidates.size() >= updateThreshold) {
            updateList();
        }
    }

    /**
     * Add a single item in completion list.
     *
     * According to your settings and the lock's state, this item may not immediately
     * be displayed to the user.
     *
     * @see CompletionPublisher#setUpdateThreshold(int)
     */
    public void addItem(CompletionItem item) {
        checkCancelled();
        if (invalid) {
            return;
        }
        lock.lock();
        try {
            candidates.add(item);
        } finally {
            lock.unlock();
        }
        if (candidates.size() >= updateThreshold) {
            updateList();
        }
    }

    /**
     * Try to update completion in main thread.
     *
     * If {@link Lock#tryLock()} failed, nothing will happen.
     */
    public void updateList() {
        updateList(false);
    }

    /**
     * Update completion items on main thread
     *
     * @param forced If true, the main thread will wait for the lock. Otherwise, when the lock is
     *               currently available for the thread, the update will be executed.
     */
    public void updateList(boolean forced) {
        if (invalid) {
            return;
        }
        handler.post(() -> {
            // Lock the candidate list accordingly
            if (invalid) {
                return;
            }
            var locked = false;
            if (forced) {
                lock.lock();
                locked = true;
            } else {
                locked = lock.tryLock();
            }

            if (locked) {
                try {
                    if (candidates.size() == 0) {
                        return;
                    }
                    final var comparator = this.comparator;
                    if (comparator != null) {
                        while (!candidates.isEmpty()) {
                            var candidate = candidates.remove(0);
                            // Insert the value by binary search
                            int left = 0, right = items.size();
                            var size = right;
                            while (left <= right) {
                                var mid = (left + right) / 2;
                                if (mid < 0 || mid >= size) {
                                    left = mid;
                                    break;
                                }
                                var cmp = comparator.compare(items.get(mid), candidate);
                                if (cmp < 0) {
                                    left = mid + 1;
                                } else if (cmp > 0) {
                                    right = mid - 1;
                                } else {
                                    left = mid;
                                    break;
                                }
                            }
                            left = Math.max(0, Math.min(size, left));
                            items.add(left, candidate);
                        }
                    } else {
                        items.addAll(candidates);
                        candidates.clear();
                    }
                    callback.run();
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    public void cancel() {
        invalid = true;
    }

    private void checkCancelled() {
        if (Thread.interrupted() || invalid) {
            invalid = true;
            if (languageInterruptionLevel <= Language.INTERRUPTION_LEVEL_SLIGHT) {
                throw new CompletionCancelledException();
            }
        }
    }

}
