package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;

import olof.sjoholm.Interfaces.Callback;
import olof.sjoholm.Net.Both.*;
import olof.sjoholm.Net.Envelope;

/**
 * Created by sjoholm on 02/10/16.
 */

public class Server implements ConnectionWorker.ConnectionListener {
    private ConnectionWorker connectionListener;
    private ClientManager clientManager;
    private Callback onConnectorsUpdated;
    private final ThreadWorker threadWorker;
    private OnClientStateUpdated clientStateUpdated;

    public Server(String host, int port, final OnMessageListener onMessageListener,
                  final OnClientStateUpdated clientStateUpdated) {
        this.clientStateUpdated = clientStateUpdated;
        ServerSocket socket = Gdx.net.newServerSocket(
                Net.Protocol.TCP,
                host,
                port,
                new ServerSocketHints()
        );
        connectionListener = new ConnectionWorker(this, socket);
        connectionListener.start();
        clientManager = new ClientManager(onMessageListener, new Client.OnDisconnectedListener() {
            @Override
            public void onDisconnected(Client client) {
                Server.this.onDisconnected(client);
            }
        });
        threadWorker = new ThreadWorker();
        threadWorker.start();
    }

    public void stop() {
        connectionListener.stop();
    }

    @Override
    public void onNewConnection(Client client) {
        client.sendData(new Envelope.Message("Welcome my friend. :)"));
        clientManager.addClient(client);

        clientStateUpdated.onClientConnected(client);

        if (onConnectorsUpdated != null) {
            onConnectorsUpdated.callback();
        }
    }

    private void onDisconnected(Client client) {
        clientStateUpdated.onClientDisconnected(client);

        if (onConnectorsUpdated != null) {
            onConnectorsUpdated.callback();
        }
    }

    public int getConnectedClientsSize() {
        return clientManager.getClientsSize();
    }

    public void setOnConnectorsUpdated(Callback onConnectorsUpdated) {
        this.onConnectorsUpdated = onConnectorsUpdated;
    }

    public void sendData(final Envelope envelope) {
        threadWorker.execute(new Runnable() {
            @Override
            public void run() {
                clientManager.broadcast(envelope);
            }
        });
    }
}
