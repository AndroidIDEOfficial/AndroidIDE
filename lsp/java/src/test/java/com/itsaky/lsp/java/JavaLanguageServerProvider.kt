package com.itsaky.lsp.java

import com.itsaky.lsp.api.ILanguageServer
import com.itsaky.lsp.api.LanguageServerProvider

class JavaLanguageServerProvider : LanguageServerProvider() {
    private val server: JavaLanguageServer = JavaLanguageServer()
    override fun getServer(): ILanguageServer {
        return server
    }
    
    companion object {
        @JvmStatic
        val INSTANCE: LanguageServerProvider = JavaLanguageServerProvider()
    }
}