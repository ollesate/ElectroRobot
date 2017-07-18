package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Api.Request;
import olof.sjoholm.Net.Both.NetClient;
import olof.sjoholm.Net.Both.Envelope;
import olof.sjoholm.Utils.Logger;

public class Server implements ServerConnectionsWorker.ConnectionListener, NetClient.OnMessageListener, NetClient.OnDisconnectedListener {
    private final List<NetClient> netClients = new ArrayList<NetClient>();
    private final ServerConnectionsWorker incomingConnectionsWorker;
    private final ThreadWorker threadWorker;
    private final ServerSocket serverSocket;

    private OnClientMessageReceived onClientMessageReceived;
    private int clientCounter = 0;

    public interface OnClientMessageReceived {

        void onMessageReceived(Envelope envelope);
    }

    public Server(String host, int port) {
        serverSocket = Gdx.net.newServerSocket(
                Net.Protocol.TCP,
                host,
                port,
                new ServerSocketHints()
        );

        incomingConnectionsWorker = new ServerConnectionsWorker(this, serverSocket);
        threadWorker = new ThreadWorker();
    }

    public void setOnClientMessageReceivedListener(OnClientMessageReceived listener) {
        onClientMessageReceived = listener;
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
        Logger.d("onNewConnection " + clientCounter);
        NetClient connection;
        try {
            connection = NetClient.open(socketConnection, clientCounter);
            netClients.add(connection);

            // Notify all we have a new connection
            dispatchMessage(new Envelope.ClientConnection(connection));
            // Send a welcome with its id
            connection.setOnMessageListener(this);
            connection.setOnDisconnectedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnected(NetClient netClient) {
        netClients.remove(netClient);
        dispatchMessage(new Envelope.ClientDisconnection(netClient));
    }

    @Override
    public void onMessage(Envelope envelope) {
        dispatchMessage(envelope);
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
                for (NetClient netClient : netClients) {
                    netClient.sendData(envelope);
                }
            }
        });
    }

    @Override
    public Envelope onRequest(Request request) {
        // TODO: Missing implementation. Who's responsibility is this?
        return null;
    }
}
