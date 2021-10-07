/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.lemminx.extensions.contentmodel.participants.diagnostics;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.eclipse.lemminx.extensions.contentmodel.model.FilesChangedTracker;
import org.eclipse.lemminx.extensions.dtd.utils.DTDUtils;
import org.eclipse.lemminx.extensions.xsd.utils.XSDUtils;

import com.google.common.base.Objects;

/**
 * LSP XML grammar pool.
 * 
 * <p>
 * This class is a copy/paste of
 * {@link org.apache.xerces.util.XMLGrammarPoolImpl.XMLGrammarPoolImpl} from
 * Xerces adapated to use .lemminx cache.
 * </p>
 * 
 * @author Jeffrey Rodriguez, IBM
 * @author Andy Clark, IBM
 * @author Neil Graham, IBM
 * @author Pavani Mukthipudi, Sun Microsystems
 * @author Neeraj Bajaj, SUN Microsystems
 * @author Angelo ZERR
 * 
 *
 */
public class LSPXMLGrammarPool implements XMLGrammarPool {

	private static final int TABLE_SIZE = 11;

	/** Grammars. */
	private final Entry[] fGrammars;

	public LSPXMLGrammarPool() {
		this(TABLE_SIZE);
	}

	public LSPXMLGrammarPool(int initialCapacity) {
		fGrammars = new Entry[initialCapacity];
	}

	@Override
	public Grammar[] retrieveInitialGrammarSet(String grammarType) {
		// To avoid having trouble with xsi:noNamespaceSchemaLocation, we return nothing
		// because in the case of xsi:noNamespaceSchemaLocation
		// it's the first XML Schema which was registered as
		// xs:noNamespaceSchemaLocation which is used.
		return null;
	}

	@Override
	public void cacheGrammars(String grammarType, Grammar[] grammars) {
		for (int i = 0; i < grammars.length; i++) {
			putGrammar(grammars[i]);
		}
	}

	@Override
	public Grammar retrieveGrammar(XMLGrammarDescription desc) {
		return getGrammar(desc);
	}

	private void putGrammar(Grammar grammar) {
		synchronized (fGrammars) {
			XMLGrammarDescription desc = grammar.getGrammarDescription();
			int hash = hashCode(desc);
			int index = (hash & 0x7FFFFFFF) % fGrammars.length;
			for (Entry entry = fGrammars[index]; entry != null; entry = entry.next) {
				if (entry.hash == hash && equals(entry.desc, desc)) {
					entry.grammar = grammar;
					return;
				}
			}
			// create a new entry
			Entry entry = new Entry(hash, desc, grammar, fGrammars[index]);
			fGrammars[index] = entry;
		}
	}

	/**
	 * Returns the grammar associated to the specified grammar description.
	 * Currently, the root element name is used as the key for DTD grammars and the
	 * target namespace is used as the key for Schema grammars.
	 *
	 * @param desc The Grammar Description.
	 */
	private Grammar getGrammar(XMLGrammarDescription desc) {
		synchronized (fGrammars) {
			int hash = hashCode(desc);
			int index = (hash & 0x7FFFFFFF) % fGrammars.length;
			for (Entry entry = fGrammars[index]; entry != null; entry = entry.next) {
				if ((entry.hash == hash) && equals(entry.desc, desc)) {
					if (entry.isDirty()) {
						removeGrammar(entry.desc);
						return null;
					}
					return entry.grammar;
				}
			}
			return null;
		}
	}

	/**
	 * Removes the grammar associated to the specified grammar description from the
	 * grammar pool and returns the removed grammar. Currently, the root element
	 * name is used as the key for DTD grammars and the target namespace is used as
	 * the key for Schema grammars.
	 *
	 * @param desc The Grammar Description.
	 * @return The removed grammar.
	 */
	private Grammar removeGrammar(XMLGrammarDescription desc) {
		synchronized (fGrammars) {
			int hash = hashCode(desc);
			int index = (hash & 0x7FFFFFFF) % fGrammars.length;
			for (Entry entry = fGrammars[index], prev = null; entry != null; prev = entry, entry = entry.next) {
				if ((entry.hash == hash) && equals(entry.desc, desc)) {
					if (prev != null) {
						prev.next = entry.next;
					} else {
						fGrammars[index] = entry.next;
					}
					Grammar tempGrammar = entry.grammar;
					entry.grammar = null;
					return tempGrammar;
				}
			}
			return null;
		}
	}

	public void removeGrammar(String grammarURI) {
		for (Entry entry : fGrammars) {
			if (entry != null) {
				if (grammarURI.equals(entry.desc.getExpandedSystemId())) {
					removeGrammar(entry.desc);
					return;
				}
			}
		}
	}

	@Override
	public void lockPool() {
		// Do nothing
	}

	@Override
	public void unlockPool() {
		// Do nothing
	}

	@Override
	public void clear() {
		for (int i = 0; i < fGrammars.length; i++) {
			if (fGrammars[i] != null) {
				fGrammars[i].clear();
				fGrammars[i] = null;
			}
		}
	}

	/**
	 * This method checks whether two grammars are the same. Currently, we compare
	 * the root element names for DTD grammars and the target namespaces for Schema
	 * grammars. The application can override this behaviour and add its own logic.
	 *
	 * @param desc1 The grammar description
	 * @param desc2 The grammar description of the grammar to be compared to
	 * @return True if the grammars are equal, otherwise false
	 */
	public boolean equals(XMLGrammarDescription desc1, XMLGrammarDescription desc2) {
		String systemId1 = desc1.getExpandedSystemId();
		String systemId2 = desc2.getExpandedSystemId();
		if (systemId1 != null && systemId2 != null) {
			return Objects.equal(systemId1, systemId2);
		}
		return false; // desc1.equals(desc2);
	}

	/**
	 * Returns the hash code value for the given grammar description.
	 *
	 * @param desc The grammar description
	 * @return The hash code value
	 */
	public int hashCode(XMLGrammarDescription desc) {
		return desc.hashCode();
	}

	/**
	 * This class is a grammar pool entry. Each entry acts as a node in a linked
	 * list.
	 */
	protected static final class Entry {
		public int hash;
		public XMLGrammarDescription desc;
		public Grammar grammar;
		public Entry next;
		private final FilesChangedTracker tracker;

		protected Entry(int hash, XMLGrammarDescription desc, Grammar grammar, Entry next) {
			this.hash = hash;
			this.desc = desc;
			this.grammar = grammar;
			this.next = next;
			this.tracker = create(grammar);
		}

		private static FilesChangedTracker create(Grammar grammar) {
			if (grammar instanceof SchemaGrammar) {
				return XSDUtils.createFilesChangedTracker((SchemaGrammar) grammar);
			}
			if (grammar instanceof DTDGrammar) {
				return DTDUtils.createFilesChangedTracker((DTDGrammar) grammar);
			}
			return null;
		}

		public boolean isDirty() {
			return tracker != null ? tracker.isDirty() : true;
		}

		// clear this entry; useful to promote garbage collection
		// since reduces reference count of objects to be destroyed
		protected void clear() {
			desc = null;
			grammar = null;
			if (next != null) {
				next.clear();
				next = null;
			}
		}
	}

}
