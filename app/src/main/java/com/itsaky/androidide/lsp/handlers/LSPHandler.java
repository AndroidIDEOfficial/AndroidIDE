package com.itsaky.androidide.lsp.handlers;
import java.util.Optional;
import org.eclipse.lsp4j.InitializeResult;

/**
 * A LSPHandler handles the launch, initialization and shutdown of a Language server
 */
public interface LSPHandler {
    
    /**
     * Called to start the language server
     *
     * @param onStarted A {@link Runnable} that must be called once the server is started
     */
    void start(Runnable onStarted);
    
    /**
     * Initialize the Language server with the root path provided
     *
     * @param rootPath Path of currently opened project
     * @return An {@link Optional} of {@link InitializeResult}.
     *         May be {@link Optional#empty} if the initialization was not successful.
     */
    Optional<InitializeResult> init(String rootPath);
     
    /**
     * Called to send initialized notification to server.
     */
    void initialized();
    
    /**
     * Called to notify that the server must shutdown now.
     */
    void shutdown();
}
