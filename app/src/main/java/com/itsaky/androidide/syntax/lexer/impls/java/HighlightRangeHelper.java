package com.itsaky.androidide.syntax.lexer.impls.java;

import androidx.core.util.Pair;
import com.itsaky.androidide.utils.LSPUtils;
import com.itsaky.lsp.JavadocHighlights;
import com.itsaky.lsp.SemanticHighlight;
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
        highlights.sort(LSPUtils.RANGE_START_COMPARATOR);
    }
    
    /**
     * Check if have any Javadoc highlight starting at the provided line and column
     *
     * @param line The start line
     * @param column The start column
     * @return A pair containing the color id as first element
     *     and the found range as the second.
     */
    public Pair<Integer, Range> findJavadocColorSchemeId (int line, int column) {
        final JavadocHighlights jd = highlights.javadocs;
        
        int index = -1;
        
        if((index = containsStartPosition(jd.authorTags, line, column)) != -1) {
            return Pair.create(JD_AUTHOR_TAG, jd.authorTags.get(index));
        }
        
        if((index = containsStartPosition(jd.authorNames, line, column)) != -1) {
            return Pair.create(JD_AUTHOR_NAME, jd.authorNames.get(index));
        }
        
        if((index = containsStartPosition(jd.deprecatedTags, line, column)) != -1) {
            return Pair.create(JD_DEPRECATED_TAG, jd.deprecatedTags.get(index));
        }

        if((index = containsStartPosition(jd.deprecatedMessages, line, column)) != -1) {
            return Pair.create(JD_DEPRECATED_MESSAGE, jd.deprecatedMessages.get(index));
        }
        
        if((index = containsStartPosition(jd.docrootTags, line, column)) != -1) {
            return Pair.create(JD_DOCROOT_TAG, jd.docrootTags.get(index));
        }

        if((index = containsStartPosition(jd.hiddenTags, line, column)) != -1) {
            return Pair.create(JD_HIDDEN_TAG, jd.hiddenTags.get(index));
        }
        
        if((index = containsStartPosition(jd.hiddenMessages, line, column)) != -1) {
            return Pair.create(JD_HIDDEN_MESSAGE, jd.hiddenMessages.get(index));
        }

        if((index = containsStartPosition(jd.indexTags, line, column)) != -1) {
            return Pair.create(JD_INDEX_TAG, jd.indexTags.get(index));
        }
        
        if((index = containsStartPosition(jd.indexDescriptions, line, column)) != -1) {
            return Pair.create(JD_INDEX_DESCRIPTION, jd.indexDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.indexSearchTerms, line, column)) != -1) {
            return Pair.create(JD_INDEX_SEARCH_TERM, jd.indexSearchTerms.get(index));
        }
        
        if((index = containsStartPosition(jd.inheritDocTags, line, column)) != -1) {
            return Pair.create(JD_INHERITDOC_TAG, jd.inheritDocTags.get(index));
        }

        if((index = containsStartPosition(jd.linkTags, line, column)) != -1) {
            return Pair.create(JD_LINK_TAG, jd.linkTags.get(index));
        }

        if((index = containsStartPosition(jd.linkLabels, line, column)) != -1) {
            return Pair.create(JD_LINK_LABEL, jd.linkLabels.get(index));
        }

        if((index = containsStartPosition(jd.linkReferences, line, column)) != -1) {
            return Pair.create(JD_LINK_REFERENCE, jd.linkReferences.get(index));
        }

        if((index = containsStartPosition(jd.literalTags, line, column)) != -1) {
            return Pair.create(JD_LITERAL_TAG, jd.literalTags.get(index));
        }

        if((index = containsStartPosition(jd.literalTexts, line, column)) != -1) {
            return Pair.create(JD_LITERAL_TEXT, jd.literalTexts.get(index));
        }

        if((index = containsStartPosition(jd.paramTags, line, column)) != -1) {
            return Pair.create(JD_PARAM_TAG, jd.paramTags.get(index));
        }

        if((index = containsStartPosition(jd.paramNames, line, column)) != -1) {
            return Pair.create(JD_PARAM_NAME, jd.paramNames.get(index));
        }

        if((index = containsStartPosition(jd.paramDescriptions, line, column)) != -1) {
            return Pair.create(JD_PARAM_DESCRIPTION, jd.paramDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.providesTags, line, column)) != -1) {
            return Pair.create(JD_PROVIDES_TAG, jd.providesTags.get(index));
        }
        
        if((index = containsStartPosition(jd.providesDescriptions, line, column)) != -1) {
            return Pair.create(JD_PROVIDES_DESCRIPTION, jd.providesDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.providesServiceTypes, line, column)) != -1) {
            return Pair.create(JD_PROVIDES_SERVICETYPE, jd.providesServiceTypes.get(index));
        }

        if((index = containsStartPosition(jd.returnTags, line, column)) != -1) {
            return Pair.create(JD_RETURN_TAG, jd.returnTags.get(index));
        }

        if((index = containsStartPosition(jd.returnDescriptions, line, column)) != -1) {
            return Pair.create(JD_RETURN_DESCRIPTION, jd.returnDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.seeTags, line, column)) != -1) {
            return Pair.create(JD_SEE_TAG, jd.seeTags.get(index));
        }

        if((index = containsStartPosition(jd.seeReferences, line, column)) != -1) {
            return Pair.create(JD_SEE_REFERENCE, jd.seeReferences.get(index));
        }

        if((index = containsStartPosition(jd.serialDataTags, line, column)) != -1) {
            return Pair.create(JD_SERIALDATA_TAG, jd.serialDataTags.get(index));
        }

        if((index = containsStartPosition(jd.serialDataDescriptions, line, column)) != -1) {
            return Pair.create(JD_SERIALDATA_DESCRIPTION, jd.serialDataDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.serialTags, line, column)) != -1) {
            return Pair.create(JD_SERIAL_TAG, jd.serialTags.get(index));
        }

        if((index = containsStartPosition(jd.sinceTags, line, column)) != -1) {
            return Pair.create(JD_SINCE_TAG, jd.sinceTags.get(index));
        }
        
        if((index = containsStartPosition(jd.sinceBodies, line, column)) != -1) {
            return Pair.create(JD_SINCE_BODY, jd.sinceBodies.get(index));
        }

        if((index = containsStartPosition(jd.summaryTags, line, column)) != -1) {
            return Pair.create(JD_SUMMARY_TAG, jd.summaryTags.get(index));
        }

        if((index = containsStartPosition(jd.summaryMessages, line, column)) != -1) {
            return Pair.create(JD_SUMMARY_MESSAGE, jd.summaryMessages.get(index));
        }

        if((index = containsStartPosition(jd.throwsTags, line, column)) != -1) {
            return Pair.create(JD_THROWS_TAG, jd.throwsTags.get(index));
        }

        if((index = containsStartPosition(jd.throwsNames, line, column)) != -1) {
            return Pair.create(JD_THROWS_NAME, jd.throwsNames.get(index));
        }

        if((index = containsStartPosition(jd.throwsDescriptions, line, column)) != -1) {
            return Pair.create(JD_THROWS_DESCRIPTION, jd.throwsDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.usesTags, line, column)) != -1) {
            return Pair.create(JD_USES_TAG, jd.usesTags.get(index));
        }

        if((index = containsStartPosition(jd.usesDescriptions, line, column)) != -1) {
            return Pair.create(JD_USES_DESCRIPTION, jd.usesDescriptions.get(index));
        }

        if((index = containsStartPosition(jd.usesServiceTypes, line, column)) != -1) {
            return Pair.create(JD_USES_SERVICETYPE, jd.usesServiceTypes.get(index));
        }

        if((index = containsStartPosition(jd.valueTags, line, column)) != -1) {
            return Pair.create(JD_VALUE_TAG, jd.valueTags.get(index));
        }
        
        if((index = containsStartPosition(jd.valueReferences, line, column)) != -1) {
            return Pair.create(JD_VALUE_REFERENCE, jd.valueReferences.get(index));
        }

        if((index = containsStartPosition(jd.versionTags, line, column)) != -1) {
            return Pair.create(JD_VERSION_TAG, jd.versionTags.get(index));
        }

        if((index = containsStartPosition(jd.versionBodies, line, column)) != -1) {
            return Pair.create(JD_VERSION_BODY, jd.versionBodies.get(index));
        }
        
        if((index = containsStartPosition(jd.unknownTags, line, column)) != -1) {
            return Pair.create(JD_UNKNOWN_BLOCKTAG, jd.unknownTags.get(index));
        }

        if((index = containsStartPosition(jd.unknownTagContents, line, column)) != -1) {
            return Pair.create(JD_UNKNOWN_BLOCKTAG_CONTENT, jd.unknownTagContents.get(index));
        }

        if((index = containsStartPosition(jd.unknownInlineTags, line, column)) != -1) {
            return Pair.create(JD_UNKNOWN_INLINETAG, jd.unknownInlineTags.get(index));
        }

        if((index = containsStartPosition(jd.unknownInlineTagContents, line, column)) != -1) {
            return Pair.create(JD_UNKNOWN_INLINETAG_CONTENT, jd.unknownInlineTagContents.get(index));
        }
        
        return Pair.create(HighlightRangeHelper.NOT_FOUND, null);
    }
    
    private int containsStartPosition (List<Range> ranges, int line, int column) {
        int index = binarySearchStartPosition(ranges, line, column);
        if(index >= 0 && index < ranges.size()) {
            return index;
        }
        
        return -1;
    }

    private int binarySearchStartPosition(List<Range> ranges, int line, int column) {
        
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
}
