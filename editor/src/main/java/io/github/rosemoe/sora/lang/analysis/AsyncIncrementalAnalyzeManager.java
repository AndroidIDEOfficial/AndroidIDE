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
package io.github.rosemoe.sora.lang.analysis;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.github.rosemoe.sora.lang.styling.CodeBlock;
import io.github.rosemoe.sora.lang.styling.Span;
import io.github.rosemoe.sora.lang.styling.Spans;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.Content;
import io.github.rosemoe.sora.text.ContentReference;
import io.github.rosemoe.sora.util.IntPair;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public abstract class AsyncIncrementalAnalyzeManager<S, T> implements IncrementalAnalyzeManager<S, T> {

    private StyleReceiver receiver;
    private ContentReference ref;
    private Bundle extraArguments;
    private LooperThread thread;
    private final static int MSG_BASE = 11451400;
    private final static int MSG_INIT = MSG_BASE + 1;
    private final static int MSG_MOD = MSG_BASE + 2;
    private final static int MSG_EXIT = MSG_BASE + 3;

    @Override
    public void setReceiver(StyleReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void reset(@NonNull ContentReference content, @NonNull Bundle extraArguments) {
        this.ref = content;
        this.extraArguments = extraArguments;
        rerun();
    }

    @Override
    public void insert(CharPosition start, CharPosition end, CharSequence insertedText) {
        if (thread != null) {
            thread.handler.sendMessage(thread.handler.obtainMessage(MSG_MOD, new TextModification(IntPair.pack(start.line, start.column), IntPair.pack(end.line, end.column), insertedText)));
            sendUpdate(thread.styles);
        }
    }

    @Override
    public void delete(CharPosition start, CharPosition end, CharSequence deletedText) {
        if (thread != null) {
            thread.handler.sendMessage(thread.handler.obtainMessage(MSG_MOD, new TextModification(IntPair.pack(start.line, start.column), IntPair.pack(end.line, end.column), null)));
            sendUpdate(thread.styles);
        }
    }

    @Override
    public void rerun() {
        if (thread != null) {
            thread.callback = () -> { throw new CancelledException(); };
            if (thread.isAlive()) {
                thread.handler.sendMessage(Message.obtain(thread.handler, MSG_EXIT));
                thread.abort = true;
            }
        }
        final var text = ref.getReference().copyText();
        text.setUndoEnabled(false);
        thread = new LooperThread(() -> thread.handler.sendMessage(thread.handler.obtainMessage(MSG_INIT, text)));
        thread.start();
        sendUpdate(null);
    }

    @Override
    public void destroy() {
        if (thread != null) {
            thread.callback = () -> { throw new CancelledException(); };
            if (thread.isAlive()) {
                thread.handler.sendMessage(Message.obtain(thread.handler, MSG_EXIT));
                thread.abort = true;
            }
        }
        receiver = null;
        ref = null;
        extraArguments = null;
        thread = null;
    }

    private void sendUpdate(Styles styles) {
        final var r = receiver;
        if (r != null) {
            r.setStyles(this, styles);
        }
    }

    /**
     * Compute code blocks
     * @param text The text. can be safely accessed.
     */
    public abstract List<CodeBlock> computeBlocks(Content text);

    public Bundle getExtraArguments() {
        return extraArguments;
    }

    private class LooperThread extends Thread {

        volatile boolean abort;
        Looper looper;
        Handler handler;
        Content shadowed;

        List<LineTokenizeResult<S, T>> states = new ArrayList<>();
        Styles styles;
        LockedSpans spans;
        private Runnable callback;

        public LooperThread(Runnable callback) {
            this.callback = callback;
        }

        private void tryUpdate() {
            if (!abort)
                sendUpdate(styles);
        }

        private void initialize() {
            styles = new Styles(spans = new LockedSpans());
            S state = getInitialState();
            var mdf = spans.modify();
            for (int i = 0;i < ref.getLineCount();i++) {
                var result = tokenizeLine(ref.getLine(i), state);
                state = result.state;
                var spans = result.spans != null ? result. spans :generateSpansForLine(result);
                states.add(result.clearSpans());
                mdf.addLineAt(i, spans);
            }
            styles.blocks = computeBlocks(shadowed);
            tryUpdate();
        }

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler(looper) {

                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    try {
                        switch (msg.what) {
                            case MSG_INIT:
                                shadowed = (Content) msg.obj;
                                if (!abort) {
                                    initialize();
                                }
                                break;
                            case MSG_MOD:
                                if (!abort) {
                                    var mod = (TextModification) msg.obj;
                                    int startLine = IntPair.getFirst(mod.start);
                                    int endLine = IntPair.getFirst(mod.end);
                                    if (mod.changedText == null) {
                                        shadowed.delete(IntPair.getFirst(mod.start), IntPair.getSecond(mod.start),
                                                IntPair.getFirst(mod.end), IntPair.getSecond(mod.end));
                                        S state = startLine == 0 ? getInitialState() : states.get(startLine - 1).state;
                                        // Remove states
                                        if (endLine >= startLine + 1) {
                                            states.subList(startLine + 1, endLine + 1).clear();
                                        }
                                        var mdf = spans.modify();
                                        for (int i = startLine + 1;i <= endLine;i++) {
                                            mdf.deleteLineAt(startLine + 1);
                                        }
                                        int line = startLine;
                                        while (line < shadowed.getLineCount()){
                                            var res = tokenizeLine(shadowed.getLine(line), state);
                                            mdf.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
                                            var old = states.set(line, res.clearSpans());
                                            if (stateEquals(old.state, res.state)) {
                                                break;
                                            }
                                            state = res.state;
                                            line ++;
                                        }
                                    } else {
                                        shadowed.insert(IntPair.getFirst(mod.start), IntPair.getSecond(mod.start), mod.changedText);
                                        S state = startLine == 0 ? getInitialState() : states.get(startLine - 1).state;
                                        int line = startLine;
                                        var spans = styles.spans.modify();
                                        // Add Lines
                                        while (line <= endLine) {
                                            var res = tokenizeLine(shadowed.getLine(line), state);
                                            if (line == startLine) {
                                                spans.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
                                                states.set(line, res.clearSpans());
                                            } else {
                                                spans.addLineAt(line, res.spans != null ? res.spans : generateSpansForLine(res));
                                                states.add(line, res.clearSpans());
                                            }
                                            state = res.state;
                                            line++;
                                        }
                                        // line = end.line + 1, check whether the state equals
                                        while (line < shadowed.getLineCount()) {
                                            var res = tokenizeLine(shadowed.getLine(line), state);
                                            if (stateEquals(res.state, states.get(line).state)) {
                                                break;
                                            } else {
                                                spans.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
                                                states.set(line, res.clearSpans());
                                            }
                                            line ++;
                                        }
                                    }
                                }
                                styles.blocks = computeBlocks(shadowed);
                                tryUpdate();
                                break;
                            case MSG_EXIT:
                                looper.quit();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };

            try {
                callback.run();
                Looper.loop();
            } catch (CancelledException e) {
                //ignored
            }
        }
    }

    private static class LockedSpans implements Spans {

        private final Lock lock;
        private final List<Line> lines;

        public LockedSpans() {
            lines = new ArrayList<>(128);
            lock = new ReentrantLock();
        }

        @Override
        public void adjustOnDelete(CharPosition start, CharPosition end) {

        }

        @Override
        public void adjustOnInsert(CharPosition start, CharPosition end) {

        }

        @Override
        public Reader read() {
            return new ReaderImpl();
        }

        @Override
        public Modifier modify() {
            return new ModifierImpl();
        }

        @Override 
        public boolean supportsModify() {
            return true;
        }

        private static class Line {

            public Lock lock = new ReentrantLock();

            public List<Span> spans;

            public Line() {
                this(null);
            }

            public Line(List<Span> s) {
                spans = s;
            }

        }

        private class ReaderImpl implements Spans.Reader {

            private Line line;

            public void moveToLine(int line) {
                if (line < 0) {
                    if (this.line != null) {
                        this.line.lock.unlock();
                    }
                    this.line = null;
                } else if (line >= lines.size()) {
                    if (this.line != null) {
                        this.line.lock.unlock();
                    }
                    this.line = null;
                } else {
                    if (this.line != null) {
                        this.line.lock.unlock();
                    }
                    var locked = false;
                    try {
                        locked = lock.tryLock(1, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (locked) {
                        try {
                            var obj = lines.get(line);
                            if (obj.lock.tryLock()) {
                                this.line = obj;
                            } else {
                                this.line = null;
                            }
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        this.line = null;
                    }
                }
            }

            @Override
            public int getSpanCount() {
                return line == null ? 1 : line.spans.size();
            }

            @Override
            public Span getSpanAt(int index) {
                return line == null ? Span.obtain(0, EditorColorScheme.TEXT_NORMAL) : line.spans.get(index);
            }

            @Override
            public List<Span> getSpansOnLine(int line) {
                var spans = new ArrayList<Span>();
                var locked = false;
                try {
                    locked = lock.tryLock(1, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (locked) {
                    Line obj = null;
                    try {
                        if (line < lines.size()) {
                            obj = lines.get(line);
                        }
                    } finally {
                        lock.unlock();
                    }
                    if (obj != null && obj.lock.tryLock()) {
                        try {
                            for (var span : obj.spans) {
                                spans.add(span.copy());
                            }
                        } finally {
                            obj.lock.unlock();
                        }
                    } else {
                        spans.add(getSpanAt(0));
                    }
                } else {
                    spans.add(getSpanAt(0));
                }
                return spans;
            }
        }

        private class ModifierImpl implements Modifier {

            @Override
            public void setSpansOnLine(int line, List<Span> spans) {
                lock.lock();
                try {
                    while (lines.size() <= line) {
                        var list = new ArrayList<Span>();
                        list.add(Span.obtain(0, EditorColorScheme.TEXT_NORMAL));
                        lines.add(new Line(list));
                    }
                    lines.get(line).spans = spans;
                } finally {
                    lock.unlock();
                }
            }

            @Override
            public void addLineAt(int line, List<Span> spans) {
                lock.lock();
                try {
                    lines.add(line, new Line(spans));
                } finally {
                    lock.unlock();
                }
            }

            @Override
            public void deleteLineAt(int line) {
                lock.lock();
                try {
                    var obj = lines.get(line);
                    obj.lock.lock();
                    try {
                        lines.remove(line);
                    } finally {
                        obj.lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

    }

    private static class TextModification {

        private final long start;
        private final long end;
        /**
         * null for deletion
         */
        private final CharSequence changedText;

        TextModification(long start, long end, CharSequence text) {
            this.start = start;
            this.end = end;
            changedText = text;
        }
    }

    private static class CancelledException extends RuntimeException {}


}
