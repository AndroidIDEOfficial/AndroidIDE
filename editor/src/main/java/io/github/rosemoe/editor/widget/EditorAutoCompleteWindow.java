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
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.blankj.utilcode.util.DeviceUtils;
import com.itsaky.androidide.language.java.parser.internal.SuggestItem;
import com.itsaky.androidide.models.CompletionEdit;
import com.itsaky.androidide.models.CompletionListItem;
import com.itsaky.androidide.utils.Either;
import io.github.rosemoe.editor.databinding.LayoutCompletionWindowBinding;
import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.struct.CompletionItem;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Cursor;
import io.github.rosemoe.editor.text.TextAnalyzeResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Auto complete window for editing code quicker
 *
 * @author Rose
 */
public class EditorAutoCompleteWindow extends EditorBasePopupWindow {
    private final static String TIP = "Refreshing...";
    private final CodeEditor mEditor;
    private final GradientDrawable mBg;
    protected boolean mCancelShowUp = false;
    private int mCurrent = 0;
    private long mRequestTime;
    private String mLastPrefix;
    private AutoCompleteProvider mProvider;
    private boolean mLoading;
    private int mMaxHeight;
    private EditorCompletionAdapter mAdapter;
	private LayoutCompletionWindowBinding mBinding;
	
    /**
     * Create a panel instance for the given editor
     *
     * @param editor Target editor
     */
    public EditorAutoCompleteWindow(CodeEditor editor) {
        super(editor);
        mEditor = editor;
        mAdapter = new DefaultCompletionItemAdapter();
		mBinding = LayoutCompletionWindowBinding.inflate(LayoutInflater.from(mEditor.getContext()));
        mBinding.tip.setText(TIP);
        setContentView(mBinding.getRoot());
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadii(new float[]{6, 6, 0, 0, 6, 6, 6, 6}); // (top-left, top-right, bottom-right, bottom-left) x 2
        mBinding.getRoot().setBackgroundDrawable(gd);
        mBg = gd;
        applyColorScheme();
        mBinding.list.setDividerHeight(0);
        setLoading(true);
        mBinding.list.setOnItemClickListener((parent, view, position, id) -> {
            try {
                select(position);
            } catch (Exception e) {
                Toast.makeText(mEditor.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void setAdapter(EditorCompletionAdapter adapter) {
        mAdapter = adapter;
        if (adapter == null) {
            mAdapter = new DefaultCompletionItemAdapter();
        }
    }

    @Override
    public void show() {
        if (mCancelShowUp) {
            return;
        }
        super.show();
    }

    public Context getContext() {
        return mEditor.getContext();
    }

    public int getCurrentPosition() {
        return mCurrent;
    }

    /**
     * Set a auto completion items provider
     *
     * @param provider New provider.can not be null
     */
    public void setProvider(AutoCompleteProvider provider) {
        mProvider = provider;
    }

    /**
     * Apply colors for self
     */
    public void applyColorScheme() {
        EditorColorScheme colors = mEditor.getColorScheme();
        mBg.setStroke(1, colors.getColor(EditorColorScheme.AUTO_COMP_PANEL_CORNER));
        mBg.setColor(colors.getColor(EditorColorScheme.AUTO_COMP_PANEL_BG));
    }

    /**
     * Change layout to loading/idle
     *
     * @param state Whether loading
     */
    public void setLoading(boolean state) {
        mLoading = state;
        if (state) {
            mEditor.postDelayed(() -> {
                if (mLoading) {
                    mBinding.tip.setVisibility(View.VISIBLE);
                }
            }, 300);
        } else {
            mBinding.tip.setVisibility(View.GONE);
        }
        //mBinding.list.setVisibility((!state) ? View.VISIBLE : View.GONE);
        //update();
    }

    /**
     * Move selection down
     */
    public void moveDown() {
        if (mCurrent + 1 >= mBinding.list.getAdapter().getCount()) {
            return;
        }
        mCurrent++;
        ((EditorCompletionAdapter) mBinding.list.getAdapter()).notifyDataSetChanged();
        ensurePosition();
    }

    /**
     * Move selection up
     */
    public void moveUp() {
        if (mCurrent - 1 < 0) {
            return;
        }
        mCurrent--;
        ((EditorCompletionAdapter) mBinding.list.getAdapter()).notifyDataSetChanged();
        ensurePosition();
    }

    /**
     * Make current selection visible
     */
    private void ensurePosition() {
        mBinding.list.setSelection(mCurrent);
    }

    /**
     * Select current position
     */
    public void select() {
        select(mCurrent);
    }

    /**
     * Select the given position
     *
     * @param pos Index of auto complete item
     */
    public void select(int pos) {
        Either<SuggestItem, CompletionItem> either = ((EditorCompletionAdapter) mBinding.list.getAdapter()).getItem(pos);
		if(either != null && either.isLeft()) {
			SuggestItem item = either.getLeft();
			item.onSelectThis(mEditor);
		} else if(either != null && either.isRight()) {
			final CompletionItem item = either.getRight();
			Cursor cursor = mEditor.getCursor();
			if (!cursor.isSelected()) {
				int l = 1;
				if(mLastPrefix.endsWith("."))
					l = 0;
				else if(mLastPrefix.contains("."))
					l = mLastPrefix.substring(mLastPrefix.lastIndexOf(".") + 1, mLastPrefix.length() - 1).length();
				else l = mLastPrefix.length() - 1;
				l++;
				if(mLastPrefix.endsWith(".")) l--;
				mEditor.getText().delete(cursor.getLeftLine(), cursor.getLeftColumn() - l, cursor.getLeftLine(), cursor.getLeftColumn());
				cursor.onCommitText(item.commit);

				if (item.cursorOffset != item.commit.length()) {
					int delta = (item.commit.length() - item.cursorOffset);
					if (delta != 0) {
						int newSel = Math.max(mEditor.getCursor().getLeft() - delta, 0);
						CharPosition charPosition = mEditor.getCursor().getIndexer().getCharPosition(newSel);
						mEditor.setSelection(charPosition.line, charPosition.column);
					}
				}

				if(item instanceof CompletionListItem && ((CompletionListItem) item).hasAdditionalEdits()) {
					for(CompletionEdit edit : ((CompletionListItem) item).getAdditionalEdits()) {
						if(edit.position.column == -1) {
							int col = mEditor.getText().getColumnCount(edit.position.line);
							edit.position.column = col;
						}
						mEditor.getText().insert(edit.position.line, edit.position.column, edit.insertText);
					}
				}
			}
		}
        mEditor.postHideCompletionWindow();
    }

    /**
     * Get prefix set
     *
     * @return The previous prefix
     */
    public String getPrefix() {
        return mLastPrefix;
    }

    /**
     * Set prefix for auto complete analysis
     *
     * @param prefix The user's input code's prefix
     */
    public void setPrefix(String prefix) {
        if (mCancelShowUp) {
            return;
        }
        setLoading(true);
        mLastPrefix = prefix;
        mRequestTime = System.currentTimeMillis();
        new MatchThread(mRequestTime, prefix).start();
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
    }

    /**
     * Display result of analysis
     *
     * @param results     Items of analysis
     * @param requestTime The time that this thread starts
     */
    private void displayResults(final List<Either<SuggestItem, CompletionItem>> results, long requestTime) {
        if (mRequestTime != requestTime) {
            return;
        }
        mEditor.post(() -> {
            setLoading(false);
            if (results == null || results.isEmpty()) {
                hide();
                return;
            }
            mAdapter.attachAttributes(this, results);
            mBinding.list.setAdapter(mAdapter);
            mCurrent = 0;
            float newHeight = mEditor.getDpUnit() * 40 * results.size();
            if (isShowing()) {
                update(getWidth(), (int) Math.min(newHeight, mMaxHeight));
            }
        });
    }

    /**
     * Analysis thread
     *
     * @author Rose
     */
    private class MatchThread extends Thread {
        private final long mTime;
        private final String mPrefix;
        private final boolean mInner;
        private final TextAnalyzeResult mColors;
        private final int mLine;
        private final AutoCompleteProvider mLocalProvider = mProvider;

        public MatchThread(long requestTime, String prefix) {
            mTime = requestTime;
            mPrefix = prefix;
            mColors = mEditor.getTextAnalyzeResult();
            mLine = mEditor.getCursor().getLeftLine();
            mInner = (!mEditor.isHighlightCurrentBlock()) || (mEditor.getBlockIndex() != -1);
        }

        @Override
        public void run() {
            try {
                displayResults(mLocalProvider.getAutoCompleteItems(mPrefix, mInner, mColors, mLine), mTime);
            } catch (Exception e) {
                e.printStackTrace();
                displayResults(new ArrayList<>(), mTime);
            }
        }


    }
}

