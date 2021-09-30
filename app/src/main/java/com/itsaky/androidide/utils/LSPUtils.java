package com.itsaky.androidide.utils;

import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;

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
    
    public static boolean isEqual(Range r1, Range r2) {
        if(r1 == null) return false;
        if(r2 == null) return false;

        return LSPUtils.isEqual(r1.getStart(), r2.getStart())
            && LSPUtils.isEqual(r1.getEnd(), r2.getEnd());
    }
}
