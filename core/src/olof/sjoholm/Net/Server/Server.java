package olof.sjoholm.Net.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.GameWorld.Net.OnMessageReceivedListener;
import olof.sjoholm.GameWorld.Net.ServerApi;
import olof.sjoholm.GameWorld.Utils.Logger;
import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Envelope;

public class Server implements IncomingConnectionsWorker.ConnectionListener, ServerApi {
    private final String host;
    private final int port;
    private IncomingConnectionsWorker incomingConnectionsWorker;
    private ClientManager clients;
    private MessageThreadWorker messageThreadWorker;
    private ServerSocket socket;
    private final List<OnMessageReceivedListener> onMessageReceivedListeners;

    public Server(String host, int port) {
        onMessageReceivedListeners = new ArrayList<OnMessageReceivedListener>();
        this.host = host;
        this.port = port;

        clients = new ClientManager();

        socket = Gdx.net.newServerSocket(
                Net.Protocol.TCP,
                host,
                port,
                new ServerSocketHints()
        );

        incomingConnectionsWorker = new IncomingConnectionsWorker(this, socket);
        messageThreadWorker = new MessageThreadWorker();
    }

    public void start() {
        incomingConnectionsWorker.start();
        messageThreadWorker.start();
    }

    public void stop() {
        incomingConnectionsWorker.stop();
    }

    @Override
    public void onNewConnection(Client client) {
        clients.addClientAndAssignId(client);
        onMessageReceived(new Envelope.ClientConnection(client), client.getId());
        // Send a welcome with its id
        client.sendData(new Envelope.Welcome(client.getId()));
        client.setOnMessageListener(new OnMessageListener() {
            @Override
            public void onMessage(Client client, Envelope envelope) {
                onMessageReceived(envelope, client.getId());
            }
        });
        client.setOnDisconnectedListener(new Client.OnDisconnectedListener() {
            @Override
            public void onDisconnected(Client client) {
                onClientDisconnected(client);
            }
        });
    }

    private void onClientDisconnected(Client client) {
        onMessageReceived(new Envelope.ClientDisconnection(client), client.getId());
    }

    private void onMessageReceived(Envelope envelope, Long id) {
        synchronized (onMessageReceivedListeners) {
            for (OnMessageReceivedListener listener : onMessageReceivedListeners) {
                listener.onMessage(envelope, id);
            }
        }
    }

    public int getConnectedClientsSize() {
        return clients.getSize();
    }

    @Override
    public void broadcast(final Envelope envelope) {
        messageThreadWorker.execute(new Runnable() {
            @Override
            public void run() {
                clients.broadcast(envelope);
            }
        });
    }

    @Override
    public void sendMessage(Envelope envelope, Long clientId) {
        // Todo implement this
    }

    @Override
    public void addOnMessageListener(OnMessageReceivedListener listener) {
        onMessageReceivedListeners.add(listener);
    }

    public List<Client> getConnectedClients() {
        return clients.getClients();
    }
}
