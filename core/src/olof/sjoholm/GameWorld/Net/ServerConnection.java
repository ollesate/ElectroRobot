package olof.sjoholm.GameWorld.Net;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.Server.OnClientStateUpdated;
import olof.sjoholm.Net.Server.Server;
import olof.sjoholm.Net.ServerConstants;

/**
 * Created by sjoholm on 09/10/16.
 */

public class ServerConnection {
    private static ServerConnection singleton;
    private final Server server;
    private final List<OnClientStateUpdated> onDisconnectedListeners;

    public ServerConnection() {
        onDisconnectedListeners = new ArrayList<OnClientStateUpdated>();

        server = new Server(
                ServerConstants.HOST_NAME,
                ServerConstants.CONNECTION_PORT,
                new OnMessageListener() {
                    @Override
                    public void onMessage(Client client, Envelope envelope) {
                        onMessageReceived(client, envelope);
                    }
                }, new OnClientStateUpdated() {
            @Override
            public void onClientConnected(Client client) {
                onPlayerConnected(client);
            }

            @Override
            public void onClientDisconnected(Client client) {
                onPlayerDisconnected(client);
            }
        }
        );
    }

    private void onMessageReceived(Client client, Envelope envelope) {
        if (envelope instanceof Envelope.Message) {
            protocol(client, envelope.getContents(String.class));
        }
    }

    private void onPlayerDisconnected(Client client) {
        for (OnClientStateUpdated listener : onDisconnectedListeners) {
            if (listener != null) {
                listener.onClientDisconnected(client);
            }
        }
    }

    private void onPlayerConnected(Client client) {
        for (OnClientStateUpdated listener : onDisconnectedListeners) {
            if (listener != null) {
                listener.onClientConnected(client);
            }
        }
    }

    private void protocol(Client client, String message) {

    }

    private void addClientStateListener(OnClientStateUpdated listener) {
        onDisconnectedListeners.add(listener);
    }

    public static void startServer() {
        if (singleton == null) {
            singleton = new ServerConnection();
        }
    }

    public static void broadcast(Envelope envelope) {
        singleton.server.sendData(envelope);
    }

    public static void addOnDisconnectedListener(OnClientStateUpdated listener) {
        singleton.addClientStateListener(listener);
    }

    public static int getNrConnectedPlayers() {
        return singleton.server.getConnectedClientsSize();
    }

    public static void send(Envelope envelope) {
        singleton.server.sendData(envelope);
    }

}
