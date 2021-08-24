package com.itsaky.androidide.sockets;

import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.gson.Gson;
import com.itsaky.androidide.utils.Logger;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public abstract class SocketConnection extends Thread {
    
    private final String host;
    private final int port;
    protected Socket mSocket;
    protected BufferedOutputStream mWriter;
    
    public static final String LOCALHOST = "localhost";
    
    public SocketConnection(int port) {
        this(LOCALHOST, port);
    }
    
    public SocketConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    protected abstract void onConnected();
    protected abstract void onDisconnected();
    protected abstract void onFailedToConnect(Throwable exception);
    protected abstract void onResponse(String line);
    
    protected void write(Object msg) {
        writeString(GSON.toJson(msg));
    }
    
    protected void writeString(String msg) {
        new Thread(() -> {
            try {
                final String message = msg + "\n";
                mWriter.write(message.getBytes());
                mWriter.flush();
            } catch (Throwable e) {
                LOG.e("Error writing to server: " + ex(e));
            }
        }).start();
    }
    
    @Override
    public void run() {
        try {
            mSocket = tryConnect();
            onConnected();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                onResponse(line);
            }
            CloseUtils.closeIOQuietly(mWriter, mSocket);
            onDisconnected();
        } catch (Throwable e) {
            LOG.e("Failed to connect to socket", ex(e));
            onFailedToConnect(e);
        }
    }

    private Socket tryConnect() {
        Socket socket = null;
        while(socket == null || !socket.isConnected() || mWriter == null) {
            try {
                socket = new Socket(host, port);
                OutputStream os = socket.getOutputStream();
                if(os != null)
                    mWriter = new BufferedOutputStream(os);
            } catch (Throwable th){}
        }
        return socket;
    }
    
    protected String ex(Throwable th) {
        return ThrowableUtils.getFullStackTrace(th);
    }
    
    protected static final Logger LOG = Logger.instance("SocketConnection");
    protected static Gson GSON = new Gson();
}
