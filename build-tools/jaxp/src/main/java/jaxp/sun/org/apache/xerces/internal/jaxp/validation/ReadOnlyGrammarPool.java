/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jaxp.sun.org.apache.xerces.internal.jaxp.validation;

import jaxp.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import jaxp.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;

/**
 * <p>Filter {@link XMLGrammarPool} that exposes a
 * read-only view of the underlying pool.</p>
 *
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
final class ReadOnlyGrammarPool implements XMLGrammarPool {

    private final XMLGrammarPool core;

    public ReadOnlyGrammarPool( XMLGrammarPool pool ) {
        this.core = pool;
    }

    public void cacheGrammars(String grammarType, Grammar[] grammars) {
        // noop. don't let caching to happen
    }

    public void clear() {
        // noop. cache is read-only.
    }

    public void lockPool() {
        // noop. this pool is always read-only
    }

    public Grammar retrieveGrammar(XMLGrammarDescription desc) {
        return core.retrieveGrammar(desc);
    }

    public Grammar[] retrieveInitialGrammarSet(String grammarType) {
        return core.retrieveInitialGrammarSet(grammarType);
    }

    public void unlockPool() {
        // noop. this pool is always read-only.
    }

} // ReadOnlyGrammarPool
