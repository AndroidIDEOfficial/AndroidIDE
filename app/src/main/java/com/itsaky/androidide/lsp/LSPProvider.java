package com.itsaky.androidide.lsp;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.lsp4j.services.LanguageServer;
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
    private static final Map<String, LanguageServer> serverMap = new HashMap<>();
    
    /**
     * Maps a reference to the capabilities of a language server with its language id as a key
     */
    private static final Map<String, ServerCapabilities> capabilitiesMap = new HashMap<>();
    
    /**
     * Maps a reference to the client of a language server with its language id as a key
     */
    private static final Map<String, IDELanguageClient> clientMap = new HashMap<>();
    
    static void setLanguageServer(String languageCode, LanguageServer server) {
        serverMap.put(languageCode, server);
    }
    
    static void setServerCapabilitesForLanguage(String languageCode, ServerCapabilities capabilities) {
        capabilitiesMap.put(languageCode, capabilities);
    }
    
    static void setClientForLanguage(String languageCode, IDELanguageClient client) {
        clientMap.put(languageCode, client);
    }
    
    public static Map<String, LanguageServer> getAvailableServers() {
        return serverMap;
    }
    
    public static Map<String, ServerCapabilities> getAllServerCapabilities() {
        return capabilitiesMap;
    }
    
    public static Map<String, IDELanguageClient> getAllServerClients() {
        return clientMap;
    }
    
    public static LanguageServer getServerForLanguage(String languageCode) {
        return languageCode == null ? null : serverMap.get(languageCode);
    }
    
    public static ServerCapabilities getServerCapabilitiesForLanguage(String language) {
        return language == null ? null : capabilitiesMap.get(language);
    }
    
    public static IDELanguageClient getClientForLanguage(String language) {
        return language == null ? null : clientMap.get(language);
    }
}
