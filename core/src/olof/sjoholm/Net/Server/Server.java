package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Api.Request;
import olof.sjoholm.Interfaces.OnMessageReceivedListener;
import olof.sjoholm.Net.Both.ConnectionMessageWorker;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;

public class Server implements ServerConnectionsWorker.ConnectionListener, ConnectionMessageWorker.OnMessageListener {
    private final List<OnMessageReceivedListener> messageReceivedListeners = new ArrayList<OnMessageReceivedListener>();
    private final List<ConnectionMessageWorker> connectionMessageWorkers = new ArrayList<ConnectionMessageWorker>();
    private final OnClientMessageReceived onClientMessageReceived;
    private final ServerConnectionsWorker incomingConnectionsWorker;
    private final ThreadWorker threadWorker;
    private final ServerSocket serverSocket;
    private int clientCounter = 0;

    public interface OnClientMessageReceived {

        void onMessageReceived(Envelope envelope);
    }

    public Server(String host, int port, OnClientMessageReceived listener) {
        onClientMessageReceived = listener;
        serverSocket = Gdx.net.newServerSocket(
                Net.Protocol.TCP,
                host,
                port,
                new ServerSocketHints()
        );

        incomingConnectionsWorker = new ServerConnectionsWorker(this, serverSocket);
        threadWorker = new ThreadWorker();
    }

    public void start() {
        incomingConnectionsWorker.start();
        threadWorker.start();
    }

    public void stop() {
        incomingConnectionsWorker.stop();
        threadWorker.stop();
        serverSocket.dispose();
    }

    @Override
    public void onNewConnection(Socket socketConnection) {
        clientCounter++;
        ConnectionMessageWorker connection = new ConnectionMessageWorker(socketConnection, clientCounter);
        connectionMessageWorkers.add(connection);

        // Notify all we have a new connection
        dispatchMessage(new Envelope.ClientConnection(connection));
        // Send a welcome with its id
        connection.sendData(new Envelope.Welcome(connection.getId()));
        connection.setOnMessageListener(this);
        connection.setOnDisconnectedListener(new ConnectionMessageWorker.OnDisconnectedListener() {
            @Override
            public void onDisconnected(ConnectionMessageWorker connectionMessageWorker) {
                dispatchMessage(new Envelope.ClientDisconnection(connectionMessageWorker));
            }
        });
    }

    private void dispatchMessage(Envelope envelope) {
        synchronized (onClientMessageReceived) {
            onClientMessageReceived.onMessageReceived(envelope);
        }
    }

    public void broadcast(final Envelope envelope) {
        Logger.d("broadcast");
        threadWorker.execute(new Runnable() {
            @Override
            public void run() {
                for (ConnectionMessageWorker connectionMessageWorker : connectionMessageWorkers) {
                    connectionMessageWorker.sendData(envelope);
                }
            }
        });
    }

    @Override
    public void onMessage(Envelope envelope) {
        dispatchMessage(envelope);
    }

    @Override
    public Envelope onRequest(Request request) {
        // TODO: Missing implementation. Who's responsibility is this?
        return null;
    }
}
