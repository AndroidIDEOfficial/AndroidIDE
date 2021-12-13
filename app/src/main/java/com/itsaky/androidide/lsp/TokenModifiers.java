/************************************************************************************
 * This file is part of AndroidIDE.
 *
 *  
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


package com.itsaky.androidide.lsp;

import org.eclipse.lsp4j.*;

public enum TokenModifiers {
    
	PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	NATIVE("native"),
	DEFAULT("default"),
	STRICTFP("strictp"),
	SYNCHRONIZED("synchronized"),
	TRANSIENT("transient"),
	VOLATILE("volatile"),
	ABSTRACT(SemanticTokenModifiers.Abstract),
	STATIC(SemanticTokenModifiers.Static),
	FINAL(SemanticTokenModifiers.Readonly),
	
	DEPRECATED(SemanticTokenModifiers.Deprecated);
	
	public final int bitmask = 1 << ordinal();
	public final int invertBitmask = ~bitmask;
	
	private String name;
	
	private TokenModifiers (String name) {
		this.name = name;
	}
	
	@Override
	public String toString () {
		return name;
	}
}
