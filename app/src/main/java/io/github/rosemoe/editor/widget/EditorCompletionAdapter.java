/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;
import org.eclipse.lsp4j.CompletionItem;

/**
 * A class to make custom adapter for auto-completion window
 * @see EditorCompletionAdapter#getItemHeight()
 * @see EditorCompletionAdapter#getView(int, View, ViewGroup, boolean)
 */
public abstract class EditorCompletionAdapter extends BaseAdapter {

    private EditorAutoCompleteWindow mWindow;
    private List<CompletionItem> mItems;

    public void clear() {
        if(mItems != null)
            mItems.clear();
    }

    /**
     * Called by {@link EditorAutoCompleteWindow} to attach some arguments
     */
    public void attachAttributes(EditorAutoCompleteWindow window, List<CompletionItem> items) {
        mWindow = window;
        mItems = items;
    }

    @Override
    public CompletionItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, position == mWindow.getCurrentPosition());
    }

    /**
     * Get context from editor
     */
    protected Context getContext() {
        return mWindow.getContext();
    }

    /**
     * Implementation of this class should provide exact height of its item
     */
    public abstract int getItemHeight();

    /**
     * @see BaseAdapter#getView(int, View, ViewGroup)
     * @param isCurrentCursorPosition Is the {@param position} currently selected
     */
    protected abstract View getView(int position, View convertView, ViewGroup parent, boolean isCurrentCursorPosition);
	
	protected String getHtmlJavadocAt(int index) {
		return "";
	}

}
