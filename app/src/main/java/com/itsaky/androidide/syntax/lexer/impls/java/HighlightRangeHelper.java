/************************************************************************************
 * This file is part of AndroidIDE.
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


package com.itsaky.androidide.syntax.lexer.impls.java;

import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.lsp.SemanticHighlight;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.lsp4j.Range;

import static io.github.rosemoe.editor.widget.EditorColorScheme.*;

public class HighlightRangeHelper {
    
    private final SemanticHighlight highlights;
    
    public static final int NOT_FOUND = -29291; // Some random integer
    
    public HighlightRangeHelper(SemanticHighlight highlights) {
        this.highlights = highlights;
    }
    
    public void sort () {
        sortHighlights(LSPUtils.RANGE_START_COMPARATOR);
    }
    
    public boolean isEnumType(int line, int column) {
        if(highlights == null || highlights.enumTypes == null) return false;
        return isInRange(highlights.enumTypes, line, column);
    }

    public boolean isAnnotationType(int line, int column) {
        if(highlights == null || highlights.annotationTypes == null) return false;
        return isInRange(highlights.annotationTypes, line, column);
    }

    public boolean isInterface(int line, int column) {
        if(highlights == null || highlights.interfaces == null) return false;
        return isInRange(highlights.interfaces, line, column);
    }

    public boolean isEnum(int line, int column) {
        if(highlights == null || highlights.enums == null) return false;
        return isInRange(highlights.enums, line, column);
    }

    public boolean isParameter(int line, int column) {
        if(highlights == null || highlights.parameters == null) return false;
        return isInRange(highlights.parameters, line, column);
    }

    public boolean isExceptionParam(int line, int column) {
        if(highlights == null || highlights.exceptionParams == null) return false;
        return isInRange(highlights.exceptionParams, line, column);
    }

    public boolean isConstructor(int line, int column) {
        if(highlights == null || highlights.constructors == null) return false;
        return isInRange(highlights.constructors, line, column);
    }

    public boolean isStaticInit(int line, int column) {
        if(highlights == null || highlights.staticInits == null) return false;
        return isInRange(highlights.staticInits, line, column);
    }

    public boolean isInstanceInit(int line, int column) {
        if(highlights == null || highlights.instanceInits == null) return false;
        return isInRange(highlights.instanceInits, line, column);
    }

    public boolean isTypeParam(int line, int column) {
        if(highlights == null || highlights.typeParams == null) return false;
        return isInRange(highlights.typeParams, line, column);
    }

    public boolean isResourceVariable(int line, int column) {
        if(highlights == null || highlights.resourceVariables == null) return false;
        return isInRange(highlights.resourceVariables, line, column);
    }

    public boolean isPackageName(int line, int column) {
        if(highlights == null || highlights.packages == null) return false;
        return isInRange(highlights.packages, line, column);
    }

    public boolean isClassName(int line, int column) {
        if(highlights == null || highlights.classNames == null) return false;
        return isInRange(highlights.classNames, line, column);
    }

    public boolean isField(int line, int column) {
        if(highlights == null || highlights.fields == null) return false;
        return isInRange(highlights.fields, line, column);
    }

    public boolean isStaticField(int line, int column) {
        if(highlights == null || highlights.statics == null) return false;
        return isInRange(highlights.statics, line, column);
    }

    public boolean isMethodDeclaration(int line, int column) {
        if(highlights == null || highlights.methodDeclarations == null) return false;
        return isInRange(highlights.methodDeclarations, line, column);
    }

    public boolean isMethodInvocation(int line, int column) {
        if(highlights == null || highlights.methodInvocations == null) return false;
        return isInRange(highlights.methodInvocations, line, column);
    }

    public boolean isLocal(int line, int column) {
        if(highlights == null || highlights.locals == null) return false;
        return isInRange(highlights.locals, line, column);
    }

    public boolean isInRange(List<Range> ranges, int line, int column) {
        if(ranges != null && ranges.size() > 0) {
            for(int i=0;i<ranges.size();i++) {
                final Range range = ranges.get(i);
                if(range == null) continue;
                if(range.getStart().getLine() == line && range.getStart().getCharacter() == column)
                    return true;
            }
        } 
        return false;
    }
    
    public void sortHighlights(Comparator<Range> comparator) {
        final SemanticHighlight h = this.highlights;
        sortAll(comparator,

                // Semantic highlights
                h.packages,
                h.enumTypes,
                h.classNames,
                h.annotationTypes,
                h.interfaces,
                h.enums,
                h.statics,
                h.fields,
                h.parameters,
                h.locals,
                h.exceptionParams,
                h.methodDeclarations,
                h.methodInvocations,
                h.constructors,
                h.staticInits,
                h.instanceInits,
                h.typeParams,
                h.resourceVariables);
    }

    private void sortAll (Comparator<Range> comparator, List<Range>... lists) {
        for (List<Range> list : lists) {
            Collections.sort(list, comparator);
        }
    }
}
