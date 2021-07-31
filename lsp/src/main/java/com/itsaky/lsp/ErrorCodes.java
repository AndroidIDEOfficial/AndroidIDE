package com.itsaky.lsp;

public class ErrorCodes {
    public static final int ParseError = -32700,
            InvalidRequest = -32600,
            MethodNotFound = -32601,
            InvalidParams = -32602,
            InternalError = -32603,
            serverErrorStart = -32099,
            serverErrorEnd = -32000,
            ServerNotInitialized = -32002,
            UnknownErrorCode = -32001,

            // TODO comment doesn't highlight properly
            // Defined by the protocol.
            RequestCancelled = -32800,
            ContentModified = -32801;
}
