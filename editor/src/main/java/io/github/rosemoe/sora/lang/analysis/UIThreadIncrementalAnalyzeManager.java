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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.github.rosemoe.sora.lang.styling.MappedSpans;
import io.github.rosemoe.sora.lang.styling.Styles;
import io.github.rosemoe.sora.text.CharPosition;
import io.github.rosemoe.sora.text.ContentReference;

/**
 * Simple implementation of incremental analyze manager.
 * This class saves states at line endings. It is for simple token-based highlighting, and it can also
 * save tokens on lines so that they can be reused. However, no code blocks support is provided.
 *
 * Note that the analysis is done on UI thread.
 *
 */
public abstract class UIThreadIncrementalAnalyzeManager<S, T> implements IncrementalAnalyzeManager<S, T> {

    private StyleReceiver receiver;
    /**
     * This class run actions in main thread. The reference can be safely accessed.
     */
    private ContentReference ref;
    private List<LineTokenizeResult<S, T>> states = new ArrayList<>();
    private Styles sentStyles;

    @Override
    public void setReceiver(@Nullable StyleReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void reset(@NonNull ContentReference content, @NonNull Bundle extraArguments) {
        ref = content;
        rerun();
    }

    @Override
    public void insert(CharPosition start, CharPosition end, CharSequence insertedContent) {
        S state = start.line == 0 ? getInitialState() : states.get(start.line - 1).state;
        int line = start.line;
        var spans = sentStyles.spans.modify();
        while (line <= end.line) {
            var res = tokenizeLine(ref.getLine(line), state);
            spans.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
            states.set(line, res.clearSpans());
            state = res.state;
            line++;
        }
        // line = end.line + 1, check whether the state equals
        while (line < ref.getLineCount()) {
            var res = tokenizeLine(ref.getLine(line), state);
            if (stateEquals(res.state, states.get(line).state)) {
                break;
            } else {
                spans.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
                states.set(line, res.clearSpans());
            }
            line ++;
        }
        receiver.setStyles(this, sentStyles);
    }

    @Override
    public void delete(CharPosition start, CharPosition end, CharSequence deletedContent) {
        S state = start.line == 0 ? getInitialState() : states.get(start.line - 1).state;
        // Remove states
        if (end.line >= start.line + 1) {
            states.subList(start.line + 1, end.line + 1).clear();
        }
        int line = start.line;
        while (line < ref.getLineCount()){
            var res = tokenizeLine(ref.getLine(line), state);
            var old = states.set(line, res);
            var spans = sentStyles.spans.modify();
            spans.setSpansOnLine(line, res.spans != null ? res.spans : generateSpansForLine(res));
            if (stateEquals(old.state, res.state)) {
                break;
            }
            state = res.state;
            line ++;
        }
        receiver.setStyles(this, sentStyles);
    }

    @Override
    public void rerun() {
        states.clear();
        S state = getInitialState();
        var builder = new MappedSpans.Builder(ref.getLineCount());
        for (int i = 0;i < ref.getLineCount();i++) {
            var res = tokenizeLine(ref.getLine(i), state);
            state = res.state;
            var spans = res.spans != null ? res.spans : generateSpansForLine(res);
            states.add(res.clearSpans());
            for (var span : spans) {
                builder.add(i, span);
            }
        }
        sentStyles = new Styles(builder.build());
        sendUpdate();
    }

    /**
     * Send the update.
     *
     * We always use the same object, but the editor can use a HwAcceleratedRenderer
     * so that some displaying content may not be updated in the renderer.
     * So we must call this to notify editor to invalidate its drawing cache.
     */
    private void sendUpdate() {
        final var r = receiver;
        if (r != null) {
            r.setStyles(this, sentStyles);
        }
    }

    @Override
    public void destroy() {
        states = null;
        receiver = null;
        sentStyles = null;
        ref = null;
    }

}
