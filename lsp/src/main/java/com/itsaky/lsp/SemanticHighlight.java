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
