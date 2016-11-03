package olof.sjoholm.GameWorld.Net;

import java.util.ArrayList;
import java.util.List;

import olof.sjoholm.Net.Both.Client;
import olof.sjoholm.Net.Both.OnMessageListener;
import olof.sjoholm.Net.Envelope;
import olof.sjoholm.Net.ServerConstants;

/**
 * Created by sjoholm on 09/10/16.
 */

public class ClientConnection {
    private static ClientConnection singleton;

    private final List<OnServerMessageListener> serverMessageListeners;
    private final Client serverConnection;

    public ClientConnection() {
        serverMessageListeners = new ArrayList<OnServerMessageListener>();

        serverConnection = new Client(
                ServerConstants.HOST_NAME,
                ServerConstants.CONNECTION_PORT
        );
        serverConnection.setOnMessageListener(new OnMessageListener() {
            @Override
            public void onMessage(Client client, Envelope envelope) {
                onMessageReceived(envelope);
            }
        });
        serverConnection.startReading();
    }

    private void onMessageReceived(Envelope envelope) {
        synchronized (serverMessageListeners) {
            for (OnServerMessageListener listener : serverMessageListeners) {
                if (listener != null) {
                    listener.onServerMessage(envelope);
                }
            }
        }
    }

    private void addListener(OnServerMessageListener listener) {
        synchronized (serverMessageListeners) {
            serverMessageListeners.add(listener);
        }
    }

    public static void addServerMessageListener(OnServerMessageListener serverListener) {
        singleton.addListener(serverListener);
    }

    private void sendMessage(Envelope envelope) {
        serverConnection.sendData(envelope);
    }

    public static void send(Envelope envelope) {
        singleton.sendMessage(envelope);
    }

    public static void startClient() {
        if (singleton == null) {
            singleton = new ClientConnection();
        }
    }

    public interface OnServerMessageListener {

        void onServerMessage(Envelope envelope);

    }
}
