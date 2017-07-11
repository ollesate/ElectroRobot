package olof.sjoholm.Net.Server;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;

import olof.sjoholm.Utils.Logger;

/**
 * Worker that listens for incoming connections.
 */
class ServerConnectionsWorker implements Runnable {
    private ServerSocket serverSocket;
    private ConnectionListener listener;
    private boolean isRunning;

    /**
     * Called when a new client connected.
     */
    interface ConnectionListener {

        void onNewConnection(Socket socketConnection);
    }

    ServerConnectionsWorker(ConnectionListener listener, ServerSocket serverSocket) {
        this.listener = listener;
        this.serverSocket = serverSocket;
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        Logger.d("Listening to new connections...");
        while (isRunning) {
            try {
                Socket socketConnection = serverSocket.accept(null);
                Logger.d("New connection!");
                listener.onNewConnection(socketConnection);
            } catch (GdxRuntimeException ignored) {
                // Seems to be only be caused by socket time outs here
            }
        }
    }
}
