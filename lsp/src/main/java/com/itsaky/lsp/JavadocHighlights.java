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

public class JavadocHighlights {
	
	public final List<Range>
		
		authorTags = new ArrayList<>(),
		authorNames = new ArrayList<>(),
		
		deprecatedTags = new ArrayList<>(),
		deprecatedMessages = new ArrayList<>(),
		
		docrootTags = new ArrayList<>(),
		
		hiddenTags = new ArrayList<>(),
		hiddenMessages = new ArrayList<>(),
		
		indexTags = new ArrayList<>(),
		indexDescriptions = new ArrayList<>(),
		indexSearchTerms = new ArrayList<>(),
		
		inheritDocTags = new ArrayList<>(),
		
		linkTags = new ArrayList<>(),
		linkLabels = new ArrayList<>(),
		linkReferences = new ArrayList<>(),
		
		literalTags = new ArrayList<>(),
		literalTexts = new ArrayList<>(),
		
		paramTags = new ArrayList<>(),
		paramNames = new ArrayList<>(),
		paramDescriptions = new ArrayList<>(),
		
		providesTags = new ArrayList<>(),
		providesServiceTypes = new ArrayList<>(),
		providesDescriptions = new ArrayList<>(),
		
		returnTags = new ArrayList<>(),
		returnDescriptions = new ArrayList<>(),
		
		seeTags = new ArrayList<>(),
		seeReferences = new ArrayList<>(),
		
		serialDataTags = new ArrayList<>(),
		serialDataDescriptions = new ArrayList<>(),
		
		serialTags = new ArrayList<>(),
		
		sinceTags = new ArrayList<>(),
		sinceBodies = new ArrayList<>(),
		
		summaryTags = new ArrayList<>(),
		summaryMessages = new ArrayList<>(),
		
		throwsTags = new ArrayList<>(),
		throwsDescriptions = new ArrayList<>(),
		throwsNames = new ArrayList<>(),
		
		usesTags = new ArrayList<>(),
		usesDescriptions = new ArrayList<>(),
		usesServiceTypes = new ArrayList<>(),
		
		valueTags = new ArrayList<>(),
		valueReferences = new ArrayList<>(),
		
		versionTags = new ArrayList<>(),
		versionBodies = new ArrayList<>(),
		
		unknownTags = new ArrayList<>(),
		unknownTagContents = new ArrayList<>(),
		
		unknownInlineTags = new ArrayList<>(),
		unknownInlineTagContents = new ArrayList<>();

    public void sort(Comparator<Range> comparator) {
        sortAll(comparator, asArray());
    }
    
    public List<Range>[] asArray () {
        return new List[] {
            authorTags,
            authorNames,
            deprecatedTags,
            deprecatedMessages,
            docrootTags,
            hiddenTags,
            hiddenMessages,
            indexTags,
            indexSearchTerms,
            inheritDocTags,
            inheritDocTags,
            linkTags,
            linkLabels,
            linkReferences,
            literalTags,
            literalTexts,
            paramTags,
            paramNames,
            paramDescriptions,
            providesTags,
            providesDescriptions,
            providesServiceTypes,
            returnTags,
            returnDescriptions,
            seeTags,
            seeReferences,
            serialDataTags,
            serialDataDescriptions,
            serialTags,
            sinceTags,
            sinceBodies,
            summaryTags,
            summaryMessages,
            throwsTags,
            throwsNames,
            throwsDescriptions,
            usesTags,
            usesDescriptions,
            usesServiceTypes,
            valueTags,
            valueReferences,
            versionTags,
            versionBodies,
            unknownTags,
            unknownTagContents,
            unknownInlineTags,
            unknownInlineTagContents
        };
    }
    
    private void sortAll (Comparator<Range> comparator, List<Range>... lists) {
        for (List<Range> list : lists) {
            Collections.sort(list, comparator);
        }
    }
}
