package olof.sjoholm.Net.Server;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.utils.GdxRuntimeException;

import olof.sjoholm.Utils.Logger;
import olof.sjoholm.Net.Both.Client;

/**
 * @author sjoholm
 */
class IncomingConnectionsWorker {
    private ServerSocket serverSocket;
    private ConnectionListener listener;
    private boolean isRunning;

    IncomingConnectionsWorker(ConnectionListener listener, ServerSocket serverSocket) {
        this.listener = listener;
        this.serverSocket = serverSocket;
    }

    void start() {
        isRunning = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.d("Listening to new connections...");
                while (isRunning) {
                    try {
                        Socket newSocket = serverSocket.accept(null);
                        Logger.d("New connection!");
                        listener.onNewConnection(new Client(newSocket));
                    } catch (GdxRuntimeException e) {}
                }
            }
        });
        thread.start();
    }

    void stop() {
        isRunning = false;
    }

    interface ConnectionListener {

        void onNewConnection(Client client);
    }
}
