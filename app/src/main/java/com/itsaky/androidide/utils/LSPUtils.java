/************************************************************************************
 * This file is part of AndroidIDE.
 *
 * Copyright (C) 2021 Akash Yadav
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/


package com.itsaky.androidide.utils;

import java.util.Comparator;
import java.util.List;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import java.util.ArrayList;

public class LSPUtils {
    
    public static final Position Position_ofZero;
    public static final Range Range_ofZero;
    
    static {
        Position_ofZero = new Position(0, 0);
        Range_ofZero = new Range(Position_ofZero, Position_ofZero);
    }
    
    public static boolean isEqual(Position p1, Position p2) {
        if(p1 == null) return false;
        if(p2 == null) return false;
        
        return p1.getLine() == p2.getLine()
            && p1.getCharacter() == p2.getCharacter();
    }
    
    public static boolean isInRange (Range range, Position position) {
        final int line = position.getLine();
        final int column = position.getCharacter();
        return isInRange(range, line, column);
    }
    
    public static boolean isInRange (Range range, int line, int column) {
        final int startLine = range.getStart().getLine();
        final int startCol = range.getStart().getCharacter();
        final int endLine = range.getEnd().getLine();
        final int endCol = range.getEnd().getCharacter();
        
        return startLine <= line && line <= endLine
        && startCol <= column && column <= endCol;
    }
    
    public static boolean isEqual(Range r1, Range r2) {
        if(r1 == null) return false;
        if(r2 == null) return false;

        return LSPUtils.isEqual(r1.getStart(), r2.getStart())
            && LSPUtils.isEqual(r1.getEnd(), r2.getEnd());
    }
    
    public static Diagnostic newInfoDiagnostic(int line, int column, int length, String message, String text) {
        final Diagnostic diag = new Diagnostic();
        diag.setCode("todo");
        diag.setMessage(message);
        diag.setRange(getSingleLineRange(line, column, length));
        diag.setSeverity(DiagnosticSeverity.Information);
        diag.setSource(text);
        return diag;
    }

    public static Diagnostic newWarningDiagnostic(int line, int column, int length, String message, String text) {
        final Diagnostic diag = new Diagnostic();
        diag.setCode("custom_warning");
        diag.setMessage(message);
        diag.setRange(getSingleLineRange(line, column, length));
        diag.setSeverity(DiagnosticSeverity.Warning);
        diag.setSource(text);
        return diag;
    }

    public static Range getSingleLineRange(int line, int column, int length) {
        final Range range = new Range();
        range.setStart(new Position(line, column));
        range.setEnd(new Position(line, column + length));
        return range;
    }
    
    public static int binarySearchStartPosition(List<Range> ranges, int line, int column) {
        int left = 0, right = ranges.size() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            final Range range = ranges.get(mid);
            final int l = range.getStart().getLine();
            final int c = range.getStart().getCharacter();
            
            // Compare lines
            if(l < line) {
                left = mid + 1;
            } else if(l > line) {
                right = mid - 1;
            } else {
                // Lines are same. Compare by columns
                if(c < column) {
                    left = mid + 1;
                } else if (c > column) {
                    right = mid - 1;
                } else {
                    // Found!
                    return mid;
                }
            }
        }
        
        return -1;
    }
    
    public static final Comparator<Range> RANGE_START_COMPARATOR = new Comparator<Range>() {

        @Override
        public int compare(Range r1, Range r2) {
            // Check if ranges are null
            if(r1 == null && r2 != null) {
                return 1;
            } else if (r1 != null && r2 == null) {
                return -1;
            } else if (r1 == null && r2 == null) {
                return 0;
            } else {
                final int r1StartLine = r1.getStart().getLine();
                final int r1StartCol = r1.getStart().getCharacter();
                final int r2StartLine = r2.getStart().getLine();
                final int r2StartCol = r2.getStart().getCharacter();
                
                // Compare by lines
                if(r1StartLine < r2StartLine) {
                    return -1;
                } else if (r1StartLine > r2StartLine) {
                    return 1;
                } else {
                    
                    // Lines are same
                    // Compare by columns
                    if(r1StartCol < r2StartCol) {
                        return -1;
                    } else if (r1StartCol > r2StartCol) {
                        return 1;
                    } else {
                        // Both ranges have same start position
                        return 0;
                    }
                }
            }
        }
    };
}
