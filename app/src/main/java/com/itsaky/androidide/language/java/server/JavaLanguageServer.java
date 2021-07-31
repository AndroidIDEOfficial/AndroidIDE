package com.itsaky.androidide.language.java.server;

import androidx.core.util.Pair;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itsaky.androidide.app.StudioApp;
import com.itsaky.androidide.models.AndroidProject;
import com.itsaky.androidide.shell.ShellServer;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.Logger;
import com.itsaky.lsp.CancelParams;
import com.itsaky.lsp.CompletionList;
import com.itsaky.lsp.DidChangeConfigurationParams;
import com.itsaky.lsp.DidChangeTextDocumentParams;
import com.itsaky.lsp.DidOpenTextDocumentParams;
import com.itsaky.lsp.InitializeParams;
import com.itsaky.lsp.JavaReportProgressParams;
import com.itsaky.lsp.JavaStartProgressParams;
import com.itsaky.lsp.LanguageClient;
import com.itsaky.lsp.Message;
import com.itsaky.lsp.PublishDiagnosticsParams;
import com.itsaky.lsp.TextDocumentPositionParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class JavaLanguageServer implements ShellServer.Callback {

    private AndroidProject project;
    private ShellServer shell;
    
    private LanguageClient client;
    private Socket server;
    private OutputStream send;
    
    private final ConcurrentHashMap<Integer, Integer> completionRequests = new ConcurrentHashMap<>();
    private final BlockingQueue<CompletionList> responseQueue = new ArrayBlockingQueue<CompletionList>(1);
    
    private static final String START_COMMAND = "java " +
    "--add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED " +
    "--add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED " +
    "--add-opens jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED " +
    "-cp $JLS_HOME/lib/jls.jar:$JLS_HOME/lib/gson.jar:$JLS_HOME/lib/protobuf.jar " +
    "org.javacs.Main";

    
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String SOCKET_HOST = "localhost";
    private static final int SOCKET_PORT = 5443;
    private static int UNIVERSION_ID = 1;
    private static final Logger logger = Logger.instance("JavaLanguageServer");
    private static final Gson gson = new Gson();
    
    public JavaLanguageServer(AndroidProject project, LanguageClient client) {
        this.project = project;
        this.client = client;
        this.shell = StudioApp.getInstance().newShell(this);
    }
    
    public void startServer() {
        this.shell.bgAppend(START_COMMAND);
        
        new Thread(() -> {
            try {
                Socket socket = connectAndGet();
                onConnectToServer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line;
                while((line = reader.readLine()) != null) {
                    final String temp = line;
                    ThreadUtils.runOnUiThread(() -> {
                        onServerOut(temp);
                    });
                }
            } catch (Throwable e) {
                logEx(e);
            }
        }).start();
    }

    private Socket connectAndGet() {
        int tryCount = 0;
        while(server == null || send == null || !server.isConnected()) {
            try {
                server = new Socket(SOCKET_HOST, SOCKET_PORT);
                send = server.getOutputStream();
            } catch (IOException e) {
                tryCount++;
                try {
                     Thread.sleep(100);
                } catch (InterruptedException e2) {}
            }
        }
        return server;
    }
    
    protected void onConnectToServer() {
        InitializeParams p = new InitializeParams();
        p.rootUri = new File(project.getProjectPath()).toURI();
        initialize(p);
        initialized();

        final List<String> cps = project.getClassPaths();
        if(!cps.contains(Environment.BOOTCLASSPATH.getAbsolutePath())) cps.add(Environment.BOOTCLASSPATH.getAbsolutePath());
        JsonObject settings = new JsonObject();
        JsonObject java = new JsonObject();
        JsonArray classpaths = new JsonArray();
        for(int i=0;i<cps.size();i++) classpaths.add(cps.get(i));
        java.add("classPath", classpaths);
        settings.add("java", java);
        DidChangeConfigurationParams p2 = new DidChangeConfigurationParams();
        p2.settings = settings;
        configurationChanged(p2);
    }
    
    public void initialize(InitializeParams p) {
        write(Method.INITIALIZE, gson.toJson(p));
    }
    
    public void initialized() {
        write(Method.INITIALIZED, "{}");
    }
    
    public void configurationChanged(DidChangeConfigurationParams p) {
        write(Method.CHANGED_CONFIGURATION, gson.toJson(p));
    }
    
    public void didOpen(DidOpenTextDocumentParams p) {
        write(Method.DID_OPEN, gson.toJson(p));
    }
    
    public void didChange(DidChangeTextDocumentParams p) {
        write(Method.DID_CHANGE, gson.toJson(p));
    }
    
    public Pair<Integer, CompletionList> completion(TextDocumentPositionParams p) {
        int id = write(Method.COMPLETION, gson.toJson(p));
        completionRequests.put(id, id);
        CompletionList result = null;
        try {
            result = responseQueue.take();
        } catch (InterruptedException e) {}
        return Pair.create(id, result);
    }
    
    public void cancelRequest(int requestId) {
        CancelParams p = new CancelParams();
        p.id = requestId;
        int id = write(Method.CANCEL_REQUEST, gson.toJson(p));
    }
    
    private int write(String method, String data) {
        final int id = UNIVERSION_ID++;
        Message msg = new Message();
        msg.id = id;
        msg.jsonrpc = "2.0";
        msg.method = method;
        msg.params = new JsonParser().parse(data);
        
        if(this.send != null) {
            final String body = gson.toJson(msg);
            final String head = String.format("Content-Length: %d\r\n\r\n", body.length());
            new Thread(() -> {
                try {
                    send.write(head.concat(body).getBytes(StandardCharsets.UTF_8));
                } catch (Throwable e) {
                    logger.e("Error writing to server: " + ThrowableUtils.getFullStackTrace(e));
                }
            }).start();
        }
        
        return id;
    }
    
    private void logEx(Throwable th) {
        logger.e(ThrowableUtils.getFullStackTrace(th));
    }

    @Override
    public void output(CharSequence charSequence) {
        logger.d(charSequence);
    }
    
    /**
     * This method is called on UI Thread, do not perform time consuming or networking tasks
     */
    private void onServerOut(String line) {
        if(line == null || line.trim().length() <= 0) return;
        if(line.contains(CONTENT_LENGTH)) line = line.substring(0, line.lastIndexOf(CONTENT_LENGTH));
        if(this.client == null) return;
        try {
            JsonObject obj = new JsonParser().parse(line.trim()).getAsJsonObject();
            if(obj.has(Key.METHOD)) {
                final String method = obj.get(Key.METHOD).getAsString();
                if(method.equals(Method.PUBLISH_DIAGNOSTICS)) {
                    PublishDiagnosticsParams params = gson.fromJson(obj.get(Key.PARAMS).toString(), PublishDiagnosticsParams.class);
                    client.publishDiagnostics(params);
                } else if(method.equals(Method.JLS_PROGRESS_START)) {
                    JavaStartProgressParams params = gson.fromJson(obj.get(Key.PARAMS).toString(), JavaStartProgressParams.class);
                    client.javaProgressStart(params);
                } if(method.equals(Method.JLS_PROGRESS_REPORT)) {
                    JavaReportProgressParams params = gson.fromJson(obj.get(Key.PARAMS).toString(), JavaReportProgressParams.class);
                    client.javaProgressReport(params);
                } if(method.equals(Method.JLS_PROGRESS_END)) {
                    client.javaProgressEnd();
                }
            } else if(obj.has(Key.ID)) {
                int id = obj.get(Key.ID).getAsInt();
                if(completionRequests.containsKey(id)) {
                    final CompletionList list = gson.fromJson(obj.get(Key.RESULT).getAsJsonObject(), CompletionList.class);
                    responseQueue.put(list);
                }
            }
        } catch (Throwable th) {
            logger.e("onServerOut", ThrowableUtils.getFullStackTrace(th));
        }
    }
    
    private class Method {
        public static final String INITIALIZE = "initialize";
        public static final String INITIALIZED = "initialized";
        public static final String SHUTDOWN = "shutdown";
        public static final String EXIT = "exit";
        
        public static final String CHANGED_CONFIGURATION = "workspace/didChangeConfiguration";
        public static final String CHANGED_WATCHED_FILES = "workspace/didChangeWatchedFiles";
        
        public static final String DID_OPEN = "textDocument/didOpen";
        public static final String DID_CLOSE = "textDocument/didClose";
        public static final String DID_CHANGE = "textDocument/didChange";
        public static final String DID_SAVE = "textDocument/didSave";
        public static final String WILL_SAVE = "textDocument/willSave";
        
        public static final String COMPLETION = "textDocument/completion";
        public static final String RESOLVE_COMPLETION = "completionItem/resolve";
        
        public static final String HOVER = "textDocument/hover";
        public static final String SIGNATURE_HELP = "textDocument/signatureHelp";
        public static final String DEFINITION = "textDocument/definition";
        
        public static final String PREPARE_RENAME = "textDocument/prepareRename";
        public static final String RENAME = "textDocument/rename";
        
        public static final String CODE_ACTION = "textDocument/codeAction";
        
        public static final String CANCEL_REQUEST = "$/cancelRequest";
        
        public static final String JLS_PROGRESS_START = "java/startProgress";
        public static final String JLS_PROGRESS_REPORT = "java/reportProgress";
        public static final String JLS_PROGRESS_END = "java/endProgress";
        
        public static final String PUBLISH_DIAGNOSTICS = "textDocument/publishDiagnostics";
        public static final String JAVA_COLORS = "java/colors";
    }
    
    private class Key {
        public static final String METHOD = "method";
        public static final String PARAMS = "params";
        public static final String ID = "id";
        public static final String RESULT = "result";
    }
}
