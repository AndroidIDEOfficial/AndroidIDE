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
package com.itsaky.lsp.xml;

import androidx.annotation.NonNull;

import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.models.DocumentChangeEvent;
import com.itsaky.lsp.models.DocumentCloseEvent;
import com.itsaky.lsp.models.DocumentOpenEvent;
import com.itsaky.lsp.models.DocumentSaveEvent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Akash Yadav
 */
class XMLFileStore {
    
    private static final Set<Path> workspaceRoots = new HashSet<> ();
    private static final Map<Path, VersionedContent> activeDocuments = new HashMap<> ();
    
    private static final Logger LOG = Logger.instance ("XMLFileStore");
    
    static void shutdown () {
        activeDocuments.clear ();
    }
    
    public static void open (@NonNull DocumentOpenEvent event) {
        final var file = event.getOpenedFile ();
        if (!isXMLFile (file)) {
            LOG.error ("Opened file is not an XML file. Ignoring...");
            return;
        }
        
        activeDocuments.put (file, new VersionedContent (event.getText (), event.getVersion ()));
    }
    
    public static void close (@NonNull DocumentCloseEvent event) {
        final var file = event.getClosedFile ();
        if (!isXMLFile (file)) {
            LOG.error ("Closed file is not an XML file. Ignoring...");
            return;
        }
        
        activeDocuments.remove (file);
    }
    
    @SuppressWarnings ("unused")
    public static void save (DocumentSaveEvent event) {
    
    }
    
    public static void change (@NonNull DocumentChangeEvent event) {
        final var file = event.getChangedFile ();
        if (!isXMLFile (file)) {
            LOG.error ("Changed file is not an XML file. Ignoring...");
            return;
        }
        
        activeDocuments.put (file, new VersionedContent (event.getNewText (), event.getVersion ()));
    }
    
    public static Instant lastModified (final Path file) {
        if (!isXMLFile (file)) {
            return Instant.now ();
        }
    
        final var document = activeDocuments.get (file);
        if (document != null) {
            return document.modified;
        }
        
        return Instant.now ();
    }
    
    public static boolean isXMLFile (Path file) {
        return file != null && !Files.isDirectory (file) && file.getFileName ().toString ().endsWith (".xml");
    }
    
    public static void setWorkspaceRoots (Set<Path> roots) {
        workspaceRoots.clear ();
        workspaceRoots.addAll (roots);
    }
    
    static class VersionedContent {
        final String contents;
        final int version;
        final Instant modified = Instant.now ();
    
        VersionedContent (String contents, int version) {
            this.contents = contents;
            this.version = version;
        }
    }
}