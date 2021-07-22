package com.itsaky.androidide.language.java.parser.util;

import androidx.annotation.NonNull;
import android.text.Layout;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ScrollView;

public class LineUtils {
    private boolean[] toCountLinesArray;
    private int[] realLines;

    public static int getYAtLine(ScrollView scrollView, int lineCount, int line) {
        if (lineCount == 0) return 0;
        return scrollView.getChildAt(0).getHeight() / lineCount * line;
    }

    public static int getFirstVisibleLine(@NonNull ScrollView scrollView, int childHeight,
                                          int lineCount) throws ArithmeticException {
        if (childHeight == 0) return 0;
        int line = (scrollView.getScrollY() * lineCount) / childHeight;
        if (line < 0) line = 0;
        return line;
    }

    public static int getLastVisibleLine(@NonNull ScrollView scrollView,
                                         int childHeight, int lineCount) {
        if (childHeight == 0) return 0;
        int line = (scrollView.getScrollY() * lineCount) / childHeight;
        if (line > lineCount) line = lineCount;
        return line;
    }

    /**
     * Gets the lineInfo from the index of the letter in the text
     */
    public static int getLineFromIndex(int index, int lineCount, Layout layout) {
        int line;
        int currentIndex = 0;

        for (line = 0; line < lineCount; line++) {
            currentIndex += layout.getLineEnd(line) - layout.getLineStart(line);
            if (currentIndex >= index) {
                break;
            }
        }
        return line;
    }

    public static int getStartIndexAtLine(EditText editable, int line) {
        Layout layout = editable.getLayout();
        if (layout != null) {
            return layout.getLineStart(line);
        }
        return 0;
    }

    public boolean[] getGoodLines() {
        return toCountLinesArray;
    }

    public int[] getRealLines() {
        return realLines;
    }

    public void updateHasNewLineArray(int lineCount, Layout layout, String text) {
        boolean[] hasNewLineArray = new boolean[lineCount];
        toCountLinesArray = new boolean[lineCount];
        realLines = new int[lineCount];
        if (TextUtils.isEmpty(text)) {
            toCountLinesArray[0] = false;
            realLines[0] = 0;
            return;
        }

        if (lineCount == 0) return;

        int i;

        // for every lineInfo on the edittext
        for (i = 0; i < lineCount; i++) {
            // check if this lineInfo contains "\n"
            if (layout.getLineEnd(i) == 0) {
                hasNewLineArray[i] = false;
            } else {
                hasNewLineArray[i] = text.charAt(layout.getLineEnd(i) - 1) == '\n';
            }
            // if true
            if (hasNewLineArray[i]) {
                int j = i - 1;
                while (j >= 0 && !hasNewLineArray[j]) {
                    j--;
                }
                toCountLinesArray[j + 1] = true;

            }
        }

        toCountLinesArray[lineCount - 1] = true;

        int realLine = 0;
        for (i = 0; i < toCountLinesArray.length; i++) {
            realLines[i] = realLine;
            if (toCountLinesArray[i]) {
                realLine++;
            }
        }
    }

    public int firstReadLine() {
        return realLines[0];
    }

    public int lastReadLine() {
        return realLines[realLines.length - 1];
    }

    public int fakeLineFromRealLine(int realLine) {
        int i;
        int fakeLine = 0;
        for (i = 0; i < realLines.length; i++) {
            if (realLine == realLines[i]) {
                fakeLine = i;
                break;
            }
        }
        return fakeLine;
    }
}
