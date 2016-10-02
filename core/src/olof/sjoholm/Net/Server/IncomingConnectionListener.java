package olof.sjoholm.Net.Server;

import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

import java.io.IOException;

import olof.sjoholm.GameWorld.Utils.Logger;

/**
 * Created by sjoholm on 02/10/16.
 */
class IncomingConnectionListener {
    private ServerSocket serverSocket;
    private ConnectionListener listener;
    private boolean isRunning;
    private final SocketHints socketHints;

    IncomingConnectionListener(ConnectionListener listener, ServerSocket serverSocket) {
        this.listener = listener;
        this.serverSocket = serverSocket;

        socketHints = new SocketHints();
        socketHints.connectTimeout = 0;
    }

    void start() {
        isRunning = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    Logger.d("Listening to new connections...");
                    Socket newSocket = serverSocket.accept(socketHints);
                    Logger.d("New connection!");
                    listener.onNewConnection(new Client(newSocket));
                    break;
                }
            }
        });
        thread.start();
    }

    void stop() {
        isRunning = false;
    }

    public interface ConnectionListener {

        void onNewConnection(Client client);

    }
}
