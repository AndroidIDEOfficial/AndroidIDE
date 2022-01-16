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
package com.itsaky.androidide.language.java;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.lsp.models.Range;
import com.itsaky.lsp.models.SemanticHighlight;

import java.util.Comparator;
import java.util.List;

public class HighlightRangeHelper {
    
    private final SemanticHighlight highlights;
    
    public static final int NOT_FOUND = -29291; // Some random integer
    
    public HighlightRangeHelper (SemanticHighlight highlights) {
        this.highlights = highlights;
    }
    
    public void sort () {
        sortHighlights (LSPUtils.RANGE_START_COMPARATOR);
    }
    
    public boolean isEnumType (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getEnumTypes (), line, column);
    }
    
    public boolean isAnnotationType (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getAnnotationTypes (), line, column);
    }
    
    public boolean isInterface (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getInterfaces (), line, column);
    }
    
    public boolean isEnum (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getEnums (), line, column);
    }
    
    public boolean isParameter (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getParameters (), line, column);
    }
    
    public boolean isExceptionParam (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getExceptionParams (), line, column);
    }
    
    public boolean isConstructor (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getConstructors (), line, column);
    }
    
    public boolean isStaticInit (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getStaticInits (), line, column);
    }
    
    public boolean isInstanceInit (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getInstanceInits (), line, column);
    }
    
    public boolean isTypeParam (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getTypeParams (), line, column);
    }
    
    public boolean isResourceVariable (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getResourceVariables (), line, column);
    }
    
    public boolean isPackageName (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getPackages (), line, column);
    }
    
    public boolean isClassName (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getClassNames (), line, column);
    }
    
    public boolean isField (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getFields (), line, column);
    }
    
    public boolean isStaticField (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getStatics (), line, column);
    }
    
    public boolean isMethodDeclaration (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getMethodDeclarations (), line, column);
    }
    
    public boolean isMethodInvocation (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getMethodInvocations (), line, column);
    }
    
    public boolean isLocal (int line, int column) {
        if (highlights == null) {
            return false;
        }
        return isInRange (highlights.getLocals (), line, column);
    }
    
    public boolean isInRange (List<Range> ranges, int line, int column) {
        if (ranges != null && ranges.size () > 0) {
            for (int i = 0; i < ranges.size (); i++) {
                final Range range = ranges.get (i);
                if (range == null) {
                    continue;
                }
                if (range.getStart ().getLine () == line && range.getStart ().getColumn () == column) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void sortHighlights (Comparator<Range> comparator) {
        final SemanticHighlight highlights = this.highlights;
    
        sortAll (comparator,
                
                // Semantic highlights
                highlights.getPackages (),
                highlights.getEnumTypes (),
                highlights.getClassNames (),
                highlights.getAnnotationTypes (),
                highlights.getInterfaces (),
                highlights.getEnums (),
                highlights.getStatics (),
                highlights.getFields (),
                highlights.getParameters (),
                highlights.getLocals (),
                highlights.getExceptionParams (),
                highlights.getMethodDeclarations (),
                highlights.getMethodInvocations (),
                highlights.getConstructors (),
                highlights.getStaticInits (),
                highlights.getInstanceInits (),
                highlights.getTypeParams (),
                highlights.getResourceVariables ());
    }
    
    @SafeVarargs
    private void sortAll (Comparator<Range> comparator, @NonNull List<Range>... lists) {
        for (List<Range> list : lists) {
            list.sort (comparator);
        }
    }
}
