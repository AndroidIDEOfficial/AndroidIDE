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
import android.widget.Toast;
import com.itsaky.androidide.databinding.LayoutCompletionWindowBinding;
import com.itsaky.androidide.utils.JavaCharacter;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.CompletionItem;
import com.itsaky.lsp.models.Position;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.TextEdit;

import io.github.rosemoe.editor.interfaces.AutoCompleteProvider;
import io.github.rosemoe.editor.syntax.EditorColorScheme;
import io.github.rosemoe.editor.text.CharPosition;
import io.github.rosemoe.editor.text.Content;
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
    private String mLastPrefix;
    private AutoCompleteProvider mProvider;
    private boolean mLoading;
    private int mMaxHeight;
    private EditorCompletionAdapter mAdapter;
	private final LayoutCompletionWindowBinding mBinding;
	
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
        mBinding.getRoot().setBackground(gd);
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
        final CompletionItem item = ((EditorCompletionAdapter) mBinding.list.getAdapter()).getItem(pos);
        final Range range = getIdentifierRange(mEditor.getCursor().getRight(), mEditor.getText());
        
        String text = item.getInsertText() == null ? item.getLabel() : item.getInsertText();
        final boolean shiftCursor = item.getInsertText() != null && text.contains("$0");
        
		mEditor.getText().delete(
            range.getStart().getLine(),
            range.getStart().getColumn (),
            range.getEnd().getLine(),
            range.getEnd().getColumn ()
         );
        if(!text.contains("\n")) {
            mEditor.getCursor().onCommitText(text);
        } else {
            final String[] lines = text.split("\n");
            mEditor.getCursor().onCommitText(lines[0]);
            for(int i=1;i<lines.length;i++) {
                mEditor.getCursor().onCommitText("\n" + lines[i].trim());
            }
        }
        
        if(shiftCursor) {
            final int line = mEditor.getCursor().getLeftLine();
            final String lineText = mEditor.getText().getLineString(line);
            final int column = lineText.lastIndexOf("$0");
            if(column != -1){
                mEditor.setSelection(line, column);
                mEditor.getText().delete(line, column, line, column + 2);
            }
        }
        
        final List<TextEdit> edits = item.getAdditionalTextEdits();
        if(edits != null && edits.size() > 0) {
            for(int i=0;i<edits.size();i++) {
                final TextEdit edit = edits.get(i);
                if(edit == null) {
                    continue;
                }
                final Position start = edit.getRange().getStart();
                final Position end =  edit.getRange().getEnd();
                if(start.equals(end)) {
                    mEditor.getText().insert(start.getLine(), start.getColumn (), edit.getNewText());
                } else {
                    mEditor.getText().replace(start.getLine(),
                            start.getColumn (),
                            end.getLine(),
                            end.getColumn (),
                            edit.getNewText());
                }
            }
        }
        
        if(item.getCommand() != null) {
            var cmd = item.getCommand();
            if("editor.action.triggerParameterHints".equals(cmd.getCommand())) {
                
                // Trigger signature help request if included in CompletionItem
                // TODO Don't rely on CompletionItem for requesting signature help
                
                // If the insert text contains '(', automatically trigger this action
                mEditor.signatureHelp("(");
            }
        }
        
        mEditor.postHideCompletionWindow();
    }
    
    private Range getIdentifierRange(int end, Content content) {
        int start = end;
        while(start > 0 && JavaCharacter.isJavaIdentifierPart(content.charAt(start - 1))) {
            start--;
        }

        CharPosition startPos = content.getIndexer().getCharPosition(start);
        CharPosition endPos = content.getIndexer().getCharPosition(end);
        return new Range(new Position(startPos.line, startPos.column), new Position(endPos.line, endPos.column));
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
        new MatchThread(prefix).start();
    }

    public void setMaxHeight(int height) {
        mMaxHeight = height;
    }

    /**
     * Display result of analysis
     *
     * @param results     Items of analysis
     */
    public void displayResults (final List<CompletionItem> results) {
        mEditor.post(() -> {
            setLoading(false);
            mAdapter.clear();
            mAdapter.attachAttributes(this, results);
            if (results == null || results.isEmpty()) {
                if (isShowing ()) {
                    hide();
                }
                return;
            }
            mBinding.list.setAdapter(mAdapter);
            mCurrent = 0;
            float newHeight = mEditor.getDpUnit() * mAdapter.getItemHeight() * results.size();
            
            show ();
            update(getWidth(), (int) Math.min(newHeight, mMaxHeight));
        });
    }

    /**
     * Analysis thread
     *
     * @author Rose
     */
    private class MatchThread extends Thread {
        private final String mPrefix;
        private final String mFileUri;
        private final Content mContent;
        private final boolean mInner;
        private final TextAnalyzeResult mColors;
        private final int mIndex;
        private final int mLine;
        private final int mColumn;
        private final AutoCompleteProvider mLocalProvider = mProvider;

        public MatchThread (String prefix) {
            mPrefix = prefix;
            mFileUri = mEditor.getFile() != null ? mEditor.getFile().toURI().toString() : null;
            mContent = mEditor.getText();
            mColors = mEditor.getTextAnalyzeResult();
            mIndex = mEditor.getCursor().getLeft();
            mLine = mEditor.getCursor().getLeftLine();
            mColumn = mEditor.getCursor().getLeftColumn();
            mInner = (!mEditor.isHighlightCurrentBlock()) || (mEditor.getBlockIndex() != -1);
        }

        @Override
        public void run() {
            try {
                displayResults(mLocalProvider.getAutoCompleteItems(mContent, mFileUri, mPrefix, mInner, mColors, mIndex, mLine, mColumn));
            } catch (Exception e) {
                LOG.error ("Error computing completion", e);
                displayResults(new ArrayList<> ());
            }
        }
        
        private final Logger LOG = Logger.instance ("CompletionMatchThread");
    }
}

