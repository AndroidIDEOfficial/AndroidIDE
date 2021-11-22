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


package com.itsaky.androidide.lsp;

import com.itsaky.lsp.services.IDELanguageClient;
import com.itsaky.lsp.services.IDELanguageServer;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.lsp4j.ServerCapabilities;

/**
 * A provider that holds references to active {@link LanguageServer}s, its capabilites and its client implementation. <br>
 * To get language server for a specific language, just call {@link #getServerForLanguage(String)}
 */
public class LSPProvider {
    
    /**
     * Language code for Java language
     */
    public static final String LANGUAGE_JAVA = "java";
    
    /**
     * Language code for Kotlin language
     */
    public static final String LANGUAGE_KOTLIN = "kotlin";
    
    /**
     * Language code for XML language
     */
    public static final String LANGUAGE_XML = "xml";
    
    /**
     * Language code for Groovy language
     */
    public static final String LANGUAGE_GROOVY = "groovy";
    
    /**
     * Map a reference to active language servers with its language id as a key
     */
    private static final Map<String, IDELanguageServer> serverMap = new HashMap<>();
    
    /**
     * Maps a reference to the capabilities of a language server with its language id as a key
     */
    private static final Map<String, ServerCapabilities> capabilitiesMap = new HashMap<>();
    
    /**
     * Maps a reference to the client of a language server with its language id as a key
     */
    private static final Map<String, IDELanguageClientImpl> clientMap = new HashMap<>();
    
    public static void setLanguageServer(String languageCode, IDELanguageServer server) {
        serverMap.put(languageCode, server);
    }
    
    public static void setServerCapabilitesForLanguage(String languageCode, ServerCapabilities capabilities) {
        capabilitiesMap.put(languageCode, capabilities);
    }
    
    public static Map<String, IDELanguageServer> getAvailableServers() {
        return serverMap;
    }
    
    public static Map<String, ServerCapabilities> getAllServerCapabilities() {
        return capabilitiesMap;
    }
    
    public static IDELanguageServer getServerForLanguage(String languageCode) {
        return languageCode == null ? null : serverMap.get(languageCode);
    }
    
    public static ServerCapabilities getServerCapabilitiesForLanguage(String language) {
        return language == null ? null : capabilitiesMap.get(language);
    }
    
    public static IDELanguageClientImpl getClient() {
        return IDELanguageClientImpl.isInitialized() ? IDELanguageClientImpl.getInstance() : null;
    }
}
