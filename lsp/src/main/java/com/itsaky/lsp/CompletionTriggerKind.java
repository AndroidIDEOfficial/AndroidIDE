package com.itsaky.lsp;

public class CompletionTriggerKind {
    /**
     * Completion was triggered by typing an identifier (24x7 code complete), manual invocation (e.g Ctrl+Space) or via
     * API.
     */
    public static final int Invoked = 1;

    /**
     * Completion was triggered by a trigger character specified by the `triggerCharacters` properties of the
     * `CompletionRegistrationOptions`.
     */
    public static final int TriggerCharacter = 2;

    /** Completion was re-triggered as the current completion list is incomplete. */
    public static final int TriggerForIncompleteCompletions = 3;
}
