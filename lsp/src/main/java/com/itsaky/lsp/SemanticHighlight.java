package com.itsaky.lsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.eclipse.lsp4j.Range;

public class SemanticHighlight {
	
	public String uri;
	
	public final List<Range>
        packages = new ArrayList<>(),
        enumTypes = new ArrayList<>(),
        classNames = new ArrayList<>(),
        annotationTypes = new ArrayList<>(),
        interfaces = new ArrayList<>(),
        enums = new ArrayList<>(),
        statics = new ArrayList<>(),
        fields = new ArrayList<>(),
        parameters = new ArrayList<>(),
        locals = new ArrayList<>(),
        exceptionParams = new ArrayList<>(),
        methodDeclarations = new ArrayList<>(),
        methodInvocations = new ArrayList<>(),
        constructors = new ArrayList<>(),
        staticInits = new ArrayList<>(),
        instanceInits = new ArrayList<>(),
        typeParams = new ArrayList<>(),
        resourceVariables = new ArrayList<>();
        
    public final JavadocHighlights javadocs = new JavadocHighlights();
    
    public void sort(Comparator<Range> comparator) {
        sortAll(comparator,
        
            // Semantic highlights
            packages,
            enumTypes,
            classNames,
            annotationTypes,
            interfaces,
            enums,
            statics,
            fields,
            parameters,
            locals,
            exceptionParams,
            methodDeclarations,
            methodInvocations,
            constructors,
            staticInits,
            instanceInits,
            typeParams,
            resourceVariables);
        
        javadocs.sort(comparator);
    }
    
    private void sortAll (Comparator<Range> comparator, List<Range>... lists) {
        for (List<Range> list : lists) {
            Collections.sort(list, comparator);
        }
    }
}
