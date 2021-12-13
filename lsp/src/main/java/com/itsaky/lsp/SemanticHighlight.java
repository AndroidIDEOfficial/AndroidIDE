/************************************************************************************
 * This file is part of Java Language Server (https://github.com/itsaky/java-language-server)
 * 
 * Java Language Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Java Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Java Language Server.  If not, see <https://www.gnu.org/licenses/>.
 *
**************************************************************************************/
package com.itsaky.lsp;

import java.util.ArrayList;
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
}
